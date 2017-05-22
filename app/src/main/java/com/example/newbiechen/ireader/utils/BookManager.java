package com.example.newbiechen.ireader.utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by newbiechen on 17-5-20.
 * 处理书籍的工具类
 */

public class BookManager{
    private static final String TAG = "BookManager";
    private String chapterName;
    private String bookId;
    private long chapterLen;
    private long position;
    private Map<String, Cache> cacheMap = new HashMap<>();
    private static volatile BookManager sInstance;

    public static BookManager getInstance(){
        if (sInstance == null){
            synchronized (BookManager.class){
                if (sInstance == null){
                    sInstance = new BookManager();
                }
            }
        }
        return sInstance;
    }

    public void openChapter(String bookId,String chapterName){
        openChapter(bookId,chapterName,0);
    }

    public void openChapter(String bookId,String chapterName,int position){
        this.bookId = bookId;
        this.chapterName = chapterName;
        this.position = position;
        createCache();
    }

    private void createCache(){
        //创建Cache
        if (!cacheMap.containsKey(chapterName)){
            Cache cache = new Cache();
            File file = getBookFile(bookId, chapterName);
            char[] array = FileUtils.getFileContent(file).toCharArray();
            WeakReference<char[]> charReference = new WeakReference<char[]>(array);
            cache.size = array.length;
            cache.data = charReference;
            cacheMap.put(chapterName, cache);

            chapterLen = cache.size;
        }
        else {
            chapterLen = cacheMap.get(chapterName).getSize();
        }
    }

    public void setPosition(long position){
        this.position = position;
    }

    public long getPosition(){
        return position;
    }


    //这种方式，会造成逻辑混乱。而且这里还有一个bug。第一个字节和最后一个字节
    public int getPrev(boolean back){
        //判断是否越界
        if (position < 0){
            position = 0;
            return -1;
        }
        int pos = (int)position;
        char result = getContent()[pos];

        if (!back) {
            position -= 1;
        }
        return result;
    }

    //下一个字节
    public int getNext(boolean back){
        //判断是否越界

        if (position >= chapterLen){
            position = chapterLen;
            return -1;
        }

        int pos = (int)position;
        char result = getContent()[pos];

        if (!back) {
            position += 1;
        }
        return result;
    }



    //获取章节的内容
    public char[] getContent() {
        if (cacheMap.size() == 0){
            return new char[1];
        }
        char[] block = cacheMap.get(chapterName).getData().get();
        if (block == null) {
            File file = getBookFile(bookId, chapterName);
            block = FileUtils.getFileContent(file).toCharArray();
            Cache cache = cacheMap.get(chapterName);
            cache.data = new WeakReference<char[]>(block);
        }
        return block;
    }

    public long getChapterLen(){
        return chapterLen;
    }

    public void clear(){
        cacheMap.clear();
    }

    /**
     * 创建存储文件
     * @param folderName
     * @param fileName
     * @return
     */
    public static File getBookFile(String folderName, String fileName){
        return FileUtils.getFile(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_TXT);
    }

    public static long getBookSize(String folderName){
        return FileUtils.getDirSize(FileUtils
                .getFolder(Constant.BOOK_CACHE_PATH + folderName));
    }

    /**
     * 根据文件名判断是否被缓存过 (因为可能数据库显示被缓存过，但是文件中却没有的情况，所以需要根据文件判断是否被缓存
     * 过)
     * @param folderName : bookId
     * @param fileName: chapterName
     * @return
     */
    public static boolean isChapterCached(String folderName, String fileName){
        File file = new File(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_TXT);
        return file.exists();
    }

    public class Cache {
        private long size;
        private WeakReference<char[]> data;

        public WeakReference<char[]> getData() {
            return data;
        }

        public void setData(WeakReference<char[]> data) {
            this.data = data;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}

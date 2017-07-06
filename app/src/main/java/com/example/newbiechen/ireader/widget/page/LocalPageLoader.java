package com.example.newbiechen.ireader.widget.page;

import android.util.Log;

import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.model.local.Void;
import com.example.newbiechen.ireader.utils.Charset;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.FileUtils;
import com.example.newbiechen.ireader.utils.IOUtils;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-7-1.
 * 问题:
 * 1. 异常处理没有做好
 * 2. 加载的性能优化有待改进
 * 3. 预加载没有做
 * 4. 模板模式用的不太好，还需要修改
 * 7. 正在解析时候的判断
 * 8. 如果本地加载下一章节的速度，太慢会发生什么事情
 */

public class LocalPageLoader extends PageLoader {
    private static final String TAG = "LocalPageLoader";
    //默认从文件中获取数据的长度
    private final static int BUFFER_SIZE = 128 * 1024;
    //没有标题的时候，每个章节的最大长度
    private final static int MAX_LENGTH_WITH_NO_CHAPTER = 10 * 1024;

    // "序(章)|前言"
    private final static Pattern mPreChapterPattern = Pattern.compile("^(\\s{0,10})((\u5e8f[\u7ae0\u8a00]?)|(\u524d\u8a00)|(\u6954\u5b50))(\\s{0,10})$", Pattern.MULTILINE);

    //正则表达式章节匹配模式
    // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
    private static final String[] CHAPTER_PATTERNS = new String[] {"^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\f\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"};

    //书本的大小
    private long mBookSize;
    //章节解析模式
    private Pattern mChapterPattern = null;
    //获取书本的文件
    private File mBookFile;
    //编码类型
    private Charset mCharset;

    private Disposable mChapterDisp = null;
    public LocalPageLoader(PageView pageView) {
        super(pageView);
        mStatus = STATUS_PARSE;
    }

    @Override
    public void openBook(CollBookBean collBookBean) {
        super.openBook(collBookBean);
        mBookFile = new File(collBookBean.get_id());
        //这里id表示本地文件的路径

        //判断是否文件存在
        if (!mBookFile.exists()) return;

        //获取文件的大小
        mBookSize = mBookFile.length();

        //文件内容为空
        if (mBookSize == 0){
            mStatus = STATUS_EMPTY;
            return;
        }

        isBookOpen = false;
        //通过RxJava异步处理分章事件
        Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                loadBook(mBookFile);
                e.onSuccess(new Void());
            }
        }).compose(RxUtils::toSimpleSingle)
                .subscribe(new SingleObserver<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mChapterDisp = d;
                    }

                    @Override
                    public void onSuccess(Void value) {
                        mChapterDisp = null;
                        //提示目录加载完成
                        if (mPageChangeListener != null){
                            mPageChangeListener.onCategoryFinish(mChapterList);
                        }
                        //打开章节，并加载当前章节
                        openChapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //数据读取错误(弄个文章解析错误的Tip,会不会好一点)
                        mStatus = STATUS_ERROR;
                        ToastUtils.show("数据解析错误");
                    }
                });
    }

    //采用的是随机读取
    private void loadBook(File bookFile) throws IOException{
        //获取文件编码
        mCharset = FileUtils.getCharset(bookFile.getAbsolutePath());
        //查找章节，分配章节
        loadChapters();
    }

    /**
     * 未完成的部分:
     * 1. 序章的添加
     * 2. 需要对分章进行大量的优化
     * 3. 存在的章节的虚拟分章效果
     * @throws IOException
     */
    private void loadChapters() throws IOException{
        List<TxtChapter> chapters = new ArrayList<>();
        //获取文件流
        RandomAccessFile bookStream = new RandomAccessFile(mBookFile, "r");
        //寻找匹配文章标题的正则表达式，判断是否存在章节名
        boolean hasChapter = checkChapterType(bookStream);
        //加载章节
        byte[] buffer = new byte[BUFFER_SIZE];
        //获取到的块起始点，在文件中的位置
        long curOffset = 0;

        //block的个数
        int blockPos = 0;
        int length;
        //获取文件中的数据到buffer，直到没有数据为止(终于知道为什么原作者每次，都需要进行一次GC)
        while ((length = bookStream.read(buffer,0,buffer.length)) > 0){
            ++blockPos;
            //如果存在Chapter
            if (hasChapter){
                //段落的起始点
                int pgStart = 0;
                //段落的结束点
                int pgEnd = 0;
                //段落的长度
                int pgLength = 0;
                //从buffer中读取一段数据
                while ((pgLength = readParagraphForward(buffer,pgStart)) != 0){
                    pgEnd += pgLength;
                    String str = new String(buffer, pgStart, pgLength,mCharset.getName());
                    //进行正则匹配
                    Matcher matcher = mChapterPattern.matcher(str);
                    //如果匹配成功
                    if (matcher.find()){
                        //创建章节
                        TxtChapter chapter = new TxtChapter();
                        chapter.title = matcher.group().trim();
                        if (chapters.size() != 0){
                            //将上一章的最后位置，设置当前章节的初始位置
                            TxtChapter lastChapter = chapters.get(chapters.size() - 1);
                            chapter.start =  lastChapter.end;
                            chapter.end = chapter.start + pgLength;
                            lastChapter.end -= 1;
                            //如果一章大小，小于30byte那么就抛弃
                            if (lastChapter.end - lastChapter.start < 30){
                                chapters.remove(lastChapter);
                            }
                        }

                        else {
                            chapter.start = 0;
                            chapter.end = chapter.start + pgLength;
                        }
                        chapters.add(chapter);
                    }
                    //如果当前段落不存在章节，那么就将该段落加入到当前章节的内容中
                    else if (chapters.size() != 0){
                        TxtChapter chapter = chapters.get(chapters.size() - 1);
                        chapter.end += pgLength;
                    }
                    //如果存在章节、前面几个段落属于章节范围内，那么就叫做序章。
                    else {
                        TxtChapter chapter = new TxtChapter();
                        chapter.title = "序章";
                        chapter.start = 0;
                        chapter.end = pgLength;
                        chapters.add(chapter);
                    }
                    pgStart = pgEnd;
                }
            }
            //进行本地虚拟分章
            else {
                //章节在buffer的偏移量
                int chapterOffset = 0;
                //当前剩余可分配的长度
                int strLength = length;
                //分章的位置
                int chapterPos = 0;

                while (strLength > 0){
                    ++chapterPos;
                    //是否长度超过一章
                    if (strLength > MAX_LENGTH_WITH_NO_CHAPTER){
                        //在buffer中一章的终止点
                        int end = length;
                        //寻找换行符作为终止点
                        for (int i=chapterOffset+MAX_LENGTH_WITH_NO_CHAPTER; i<length; ++i){
                            if (buffer[i] == Charset.BLANK){
                                end = i;
                                break;
                            }
                        }
                        TxtChapter chapter = new TxtChapter();
                        chapter.title = "第"+blockPos+"章"+"("+chapterPos+")";
                        chapter.start = curOffset + chapterOffset + 1;
                        chapter.end = curOffset + end;
                        chapters.add(chapter);
                        //减去已经被分配的长度
                        strLength = strLength - (end - chapterOffset);
                        //设置偏移的位置
                        chapterOffset = end;
                    }
                    else {
                        TxtChapter chapter = new TxtChapter();
                        chapter.title = "第"+blockPos+"章"+"("+chapterPos+")";
                        chapter.start = curOffset + chapterOffset + 1;
                        chapter.end = curOffset + length;
                        chapters.add(chapter);
                        strLength = 0;
                    }
                }
            }
            curOffset = curOffset + length;

            //当添加的block太多的时候，执行GC
            if (blockPos % 10 == 0){
                System.gc();
                System.runFinalization();
            }
        }
        mChapterList = chapters;
        IOUtils.close(bookStream);

        System.gc();
        System.runFinalization();
    }


    /**
     *
     * @param content:需要进行判断的数据
     * @param start:段落的起始位置
     * @return 返回一行的byte数
     */
    private int readParagraphForward(byte[] content,int start){
        int index=start;
        //定位到换行符,所指向的下标
        while(index < content.length){
            if (content[index] == Charset.BLANK){
                ++index;
                break;
            }
            ++index;
        }
        return index-start;
    }

    /**
     * 耗时操作，需要修改。
     */
    @Override
    protected List<TxtPage> loadPageList(int chapterPos){
        if (mChapterList == null){
            throw new IllegalArgumentException("Chapter List must not null");
        }

        TxtChapter chapter = mChapterList.get(chapterPos);
        //从文件中获取数据
        byte[] content = getChapterContent(chapter);
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(bais,mCharset.getName()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return loadPages(chapter, br);
    }

    /**
     * 从文件中提取一章的内容
     * @param chapter
     * @return
     */
    private byte[] getChapterContent(TxtChapter chapter){
        RandomAccessFile bookStream = null;
        try {
            bookStream = new RandomAccessFile(mBookFile, "r");
            bookStream.seek(chapter.start);
            int extent = (int) (chapter.end - chapter.start);
            byte[] content = new byte[extent];
            bookStream.read(content,0,extent);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(bookStream);
        }

        return new byte[0];
    }

    /**
     * 1. 检查文件中是否存在章节名
     * 2. 判断文件中使用的章节名类型的正则表达式
     * @return 是否存在章节名
     */
    private boolean checkChapterType(RandomAccessFile bookStream)throws IOException{
        //首先获取128k的数据
        byte[] buffer = new byte[BUFFER_SIZE/4];
        int length = bookStream.read(buffer,0,buffer.length);
        //进行章节匹配
        for (String str : CHAPTER_PATTERNS){
            Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(new String(buffer,0,length,mCharset.getName()));
            //如果匹配存在，那么就表示当前章节使用这种匹配方式
            if (matcher.find()){
                mChapterPattern = pattern;
                //重置指针位置
                bookStream.seek(0);
                return true;
            }
        }
        //重置指针位置
        bookStream.seek(0);
        return false;
    }


    //本地加载不会发生这个问题
    @Override
    public void chapterError() {
    }

    @Override
    protected boolean prevChapter() {
        if (mStatus == STATUS_PARSE_ERROR) return false;
        //加载上一章
        if (mCurChapterPos - 1 < 0){
            ToastUtils.show("已经没有上一章了");
            return false;
        }
        else {
            int prevChapter = mCurChapterPos - 1;
            mPageList = loadPageList(prevChapter);
            mLastChapter = mCurChapterPos;
            mCurChapterPos = prevChapter;

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }
            return true;
        }
    }

    @Override
    protected boolean nextChapter() {
        if (mStatus == STATUS_PARSE_ERROR) return false;
        //加载一章
        if (mCurChapterPos + 1 >= mChapterList.size()){
            ToastUtils.show("已经没有下一章了");
            return false;
        }
        else {
            int nextChapter = mCurChapterPos + 1;
            mPageList = loadPageList(nextChapter);
            mLastChapter = mCurChapterPos;
            mCurChapterPos = nextChapter;

            if (mPageChangeListener != null){
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }
            return true;
        }
    }

    @Override
    public void skipToChapter(int pos) {
        if (pos < 0 || pos >= mChapterList.size()) return;
        mLastChapter = mCurChapterPos;
        mCurChapterPos = pos;

        openChapter();
        if (mPageChangeListener != null){
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(listener);
        //额，写的不太优雅，之后再改
        if (mChapterList != null && mChapterList.size() != 0){
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

    /*空实现*/
    @Override
    public void setChapterList(List<BookChapterBean> bookChapters) {
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        //修改当前COllBook记录
        if (mCollBook != null){
            //表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastChapter(mChapterList.get(mCurChapterPos).getTitle());
            mCollBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            //直接更新
            BookRepository.getInstance()
                    .saveCollBook(mCollBook);
        }
    }

    @Override
    public void closeBook() {
        super.closeBook();
        if (mChapterDisp != null){
            mChapterDisp.dispose();
            mChapterDisp = null;
        }
    }
}

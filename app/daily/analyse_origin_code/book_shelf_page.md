# BookShelf页面的分析

## 看到的功能和逻辑。

1. 添加EmptyView和FooterView，根据不同的状态。显示不同。并且具有刷新功能。
2. 通过CollectManager获取书架的书籍数据类，并且该数据类可以判断是否有更新，如果有更新书籍item就会有红点
3. 书架上的书籍跟追更绑定在一起的，如果不追更了，那么就将书从书架中删除。
4. 具有长按这个功能。主要功能包括(缓存，批量管理)

## 制作流程分析
所以说如果要获取到数据就必须从CollectManager下手，如何获取到数据。
当获取到数据之后，实现RecylerView Empty 和 footer 之间的切换。
之后就是长按事件的功能部署。

(上面都是网络文本的情况)
接下来，我们再来制作本地文本的情况。
1. 本地文本的加载。  (分析完成)
2. 显示           (根据个人理解完成)
3. 长按事件。

## 分析

### 如何将文本添加到CollectManager中。CollectionManager类的原理分析。

首先我们会想到，文本的数据哪里来的。首先是男生女生选择的时候的本文，第二种是追更时候的存储。我们就先从这里下手，进行分析。

那么我们就从加载男生和女生的文本开始。

一开始我们碰到了，当网络数据加载完成，通过Acache类进行数据的缓存，代码如下(取得是BookDiscussionPresenter类下的)
```java
//生成key
String key = StringUtils.creatAcacheKey("book-discussion-list", block, "all", sort, "all", start + "", limit + "", distillate);
//进行缓存
Observable<DiscussionList> fromNetWork = bookApi.getBookDisscussionList(block, "all", sort, "all", start + "", limit + "", distillate)
        .compose(RxUtil.<DiscussionList>rxCacheListHelper(key));
```
我们可以看到，首先通过数据的作用和参数生成了一个数据存储的key。然后将Key交给RxUtil.rxCacheListHelper方法中。
这个方法的作用是网络数据获取成功的时候对数据进行存储的回调，我们进入到方法内看一下。(直接在方法内讲解)
```java
    public static <T> Observable.Transformer<T, T> rxCacheListHelper(final String key) {
        //作者喜欢使用compose()方法创建Transformer回调来处理获取网络数据以外的逻辑
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                //创建一个新的工作子线程
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");

                                        if (data == null)
                                            return;
                                        //下面的代码是通过反射获取网络中获取到的数据对象中含有的List成员变量
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            String className = field.getType().getSimpleName();
                                            // 反射获取Class为List的成员变量，并提取变量中的数据，进行缓存
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    //如果存在则进行缓存。
                                                    if (list != null && !list.isEmpty()) {
                                                    //解析重点
                                                        ACache.get(ReaderApplication.getsInstance())
                                                                .put(key, new Gson().toJson(data, clazz));
                                                        LogUtils.d("cache finish");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
```
在通过一步步分析之后，我们看到了一个类ACache，这个才是真正实现Disk文件缓存的类。那么ACache的原理是什么呢？
首先截取上面关于ACache的代码
```java
ACache.get(ReaderApplication.getsInstance())
           .put(key, new Gson().toJson(data, clazz));
```
ReaderApplication我们知道是一个Application，用来完成对全局的初始化的过程。那么首先来看Y一下ACache的get()方法
```java
    //设置默认的缓存文件，获取对应的ACache类，并添加到Map中
    public static ACache get(Context ctx) {
        return get(ctx, "data");
    }

    //配置文件名
    public static ACache get(Context ctx, String cacheName) {
        //获取缓存的文件名（路径一致）
        File f = new File(Constant.PATH_DATA, cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    //根据文件对象配置
    public static ACache get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    //设置最大缓存大小，和文件数量
    public static ACache get(Context ctx, long max_zise, int max_count) {
        File f = new File(Constant.PATH_DATA, "data");
        return get(f, max_zise, max_count);
    }

    public static ACache get(File cacheDir, long max_zise, int max_count) {
        ACache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager == null) {
            manager = new ACache(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        }
        return manager;
    }
```
我们发现ACache的get方法有很多，并且是根据参数层层递进的。我们一路分析过后，可以得出的接理论如下。
首先通过filePath创建对应的文件，这个File对象其实是一个文件夹，而不是想象中的文件。
然后，配置文件夹允许的最大容量和文件最大数。
之后在判断，是否ACache类已经创建了。当创建了一个ACache就会存储在全局的Map中。(我们可以知道，ACache其实是File的管理器)
如果没有创建则创建ACache，并且存储到Map中。

那么接下来就是看下ACache的构造方法了
```java
private ACache(File cacheDir, long max_size, int max_count) {
    if (!cacheDir.exists() && !cacheDir.mkdirs()) {
        throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
    }
    mCache = new ACacheManager(cacheDir, max_size, max_count);
}
```
构造方法中，ACache创建了一个ACacheManager对象，并且将所有的参数都交给了这个对象。说明其实ACache对象只是统一的创建
器和管理器。真正的缓存实现其实是在ACacheManager中。

总结一下上面的知识就是，ACache会根据对应的File，创建对应的ACache管理器，其真正实现类是ACacheManager。ACache管理器
主要掌管ACache的创建和缓存。接下来，我们看一下ACache的put方法。
```java
/**
 * 保存 String数据 到 缓存中
 *
 * @param key   保存的key
 * @param value 保存的String数据
 */
public void put(String key, String value) {

    //获取缓存文件夹下的缓存文件(强调，是缓存文件，而不是创建ACache时候的文件夹)
    File file = mCache.newFile(key);
    //将value写入到缓存文件中
    BufferedWriter out = null;
    try {
        out = new BufferedWriter(new FileWriter(file), 1024);
        out.write(value);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //数据存储成功后的处理
        mCache.put(file);
    }
}
```
当然数据存储成功之后，我们还需要进行一些处理，比如说缓存数据超出限制，那么就必须删除旧数据了。处理在mCache.put()中
```java
private void put(File file) {
          //当前缓存文件的数量，cacheCount是个原子类
        int curCacheCount = cacheCount.get();
        //判断当前数量是否超出限制，如果超出限制，那么就删除一部分数据。
        while (curCacheCount + 1 > countLimit) {
            long freedSize = removeNext();
            cacheSize.addAndGet(-freedSize);

            curCacheCount = cacheCount.addAndGet(-1);
        }

        cacheCount.addAndGet(1);
        //再计算缓存大小，判断是否超出缓存大小。
        long valueSize = calculateSize(file);
        long curCacheSize = cacheSize.get();
        //如果超出，则删除一部分数据
        while (curCacheSize + valueSize > sizeLimit) {
            long freedSize = removeNext();
            curCacheSize = cacheSize.addAndGet(-freedSize);
        }
        cacheSize.addAndGet(valueSize);
        //重置文件信息
        Long currentTime = System.currentTimeMillis();
        file.setLastModified(currentTime);
        lastUsageDates.put(file, currentTime); //lastUsageDates是存储每个文件对象
    }
```
所以核心的删除策略在removeNext()中。
```java
/**
 * 移除旧的文件
 */
private long removeNext() {

    if (lastUsageDates.isEmpty()) {
        return 0;
    }

    Long oldestUsage = null;
    File mostLongUsedFile = null;
    //便利文件夹下的文件，获取最久未被编辑的文件
    Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
    synchronized (lastUsageDates) {
        for (Entry<File, Long> entry : entries) {
            if (mostLongUsedFile == null) {
                mostLongUsedFile = entry.getKey();
                oldestUsage = entry.getValue();
            } else {
                Long lastValueUsage = entry.getValue();
                if (lastValueUsage < oldestUsage) {
                    oldestUsage = lastValueUsage;
                    mostLongUsedFile = entry.getKey();
                }
            }
        }
    }
    //删除这个文件
    long fileSize = calculateSize(mostLongUsedFile);
    if (mostLongUsedFile.delete()) {
        lastUsageDates.remove(mostLongUsedFile);
    }
    return fileSize;
}
```
主要的流程就是这样子，如果想详细深入可以自行看源代码。 (ACache还能处理文件文件过期的问题)接下来我们继续进行分析。

当选择男生女生之后，执行的逻辑是什么，最终是如何显示到界面上的呢？我们继续分析。

男生女生的对话框是一个PopupWindow，即GenderPopupWindow中。找到关键代码
```java
setOnDismissListener(new OnDismissListener() {
    @Override
    public void onDismiss() {
        lighton();
        EventBus.getDefault().post(new UserSexChooseFinishedEvent());
    }
});
```
表示当Dismiss的时候，调用EventBus进行传递。最终会传递到RecommendFragment中，我们看一下RecommendFragment的代码
```java
/********选择性别后，添加推荐书籍****************/
@Subscribe(threadMode = ThreadMode.MAIN)
public void UserSexChooseFinished(UserSexChooseFinishedEvent event) {
    //首次进入APP，选择性别后，获取推荐列表
    mPresenter.getRecommendList();
}
```
通过Presenter获取数据，在showRecommendList()中处理数据
```java
@Override
public void showRecommendList(List<Recommend.RecommendBooks> list) {
    mAdapter.clear();
    //显示
    mAdapter.addAll(list);
    //推荐列表默认加入收藏
    for (Recommend.RecommendBooks bean : list) {
        //TODO 此处可优化：批量加入收藏->加入前需先判断是否收藏过
        CollectionsManager.getInstance().add(bean);
    }
}
```
这里我们看到了CollectionsManager，这个管理器的作用是存储收藏的。其主要功能如下
1. 获取收藏列表
2. 存储收藏列表
3. 判断是否收藏
4. 对收藏列表进行排序

个人认为上述的方式可以使用数据库完成，但是作者使用的是文件存储，所以产生了那么多的麻烦。好了加载的分析已经完成了，那么
开始制作吧。

## 显示与数据加载的制作

首先明确，需要有哪些显示功能(数据为空的情况，存在数据的情况)
其次如何加载数据，保证点击追更切换。
再保证更新。根据需要的功能一步步制作。

### 明确功能

1. 当没有数据的时候，显示空数据，此时没有办法刷新
2. 当点击追更之后，显示数据，并且Adapter下底部有个添加书籍，并可以刷新。
3. Adapter内可以判断文章是否更新
4. 创建添加本地书籍的方法
5. 添加本地书籍。
6. 设置长按事件。

### 缓存功能原理

首先介绍关键类:
DownloadQueue:我觉得应该叫做DownloadTask，表示整本小说的缓存任务。作者叫做DownloadQueue的原因是，整本小说中
包含多个章节数据的下载。DownloadQueue中包含了整本小说需要下载的章节列表资源地址。
AsyncTask:每本小说的守护线程。可以认为是加载整本小说的线程。
Retrofit:在AsyncTask中缓存每本小说的每个章节。
CacheManager:将获取到的数据存储到本地


执行流程:
1. 创建Service来进行后台下载
2. 通过EventBus实现(将小说加入缓存队列，当某个章节缓存完成刷新通知面板，当小说整个加载完成的通知)整体的三个通知事件。
(这里有必要说明下，为什么加入缓存队列需要使用eventbus,因为之后作者需要使用空通知，执行接下来的加载任务)
3. 创建加载队列(作者简单的使用了ListArray)

加入加载队列篇:
1. 首先判断是否是空信息，如果为空则不将信息加入下载队列中。
2. 判断是否任务已经在加载队列中，如果不再则加入加载队列。
3. 判断当前是否正在执行加载，如果正在执行，则不开启任务执行。

执行篇:
1. 创建AsyncTask作为整本小说的加载线程。
2. 获取加载任务中的地址，通过for循环进行加载。首先判断是否该章节曾经加载过，其次判断网络是否出问题，再判断是否取消加载。
3. 如果都通过检验则进行章节加载，通过使用Retrofit加载数据，当加载完成时候通过CacheManager加载到本地中。
4. 因为章节加载是异步进行的，所以当执行章节加载的时候，AsyncTask使用while()进行轮询睡眠。当章节加载完成的时候，取消
睡眠。
5. 最后到加载完成，将任务移除队列，取消busy状态，并且释放空msg提醒下一个事件缓存。

作者达到的功能:
1. 显示加载进度与完成情况
2. 实现单个任务缓存。
3. 实现加载错误通知。

我想要达到的效果:
1. 能不能缓存全本小说，而不是一章一章的存储到本地。并且可以实现断点续传。(关于小说能不能全本的问题，个人技术还达不到，
所以暂时先放弃，主要问题在于全本之后的随机读取问题，全本之后暂停，再进行分章下载的问题，全本缓存失败的问题，比如说某个
章节失败了，是继续加载呢，还是全部停止呢？) 所以说全本会造成很多问题，所以暂时先不想这个问题。

但是单章下载同样有着巨大的问题，首先是文件类过于庞大，如果具有加载数据库的话，那么就更加的庞大了。
(关于这个方面的问题只能以后再想办法了)

2. 现在的想法是，点击缓存全本，下载每个章节，并加入下载列表，列表中可以实现暂停功能。列表显示正在加载的章节数。当每个
章节加载完成，会在阅读器的目录中根据颜色表示已经加载完成。小说中的缓存允许制定缓存章节数，然后根据开始章节数和末尾章节数
命名加入下载队列。这里有个问题是，当缓存全本的时候，该章节数能否加入呢？比如说已经缓存全本了，但是其中有交叉的数据。
个人认为可以这样子做，在进行下载的时候判断是否已经加载过了，如果加载过了则不进行下载。

3. 上面关于缓存的显示还是有问题，可能作者会进行多次缓存的问题。虽然没有缓存但是会显示缓存的功能。

   点击缓存的时候，进行缓存判断，判断哪些章节没有被缓存过(从数据中进行查询，这就解决问题了不是)

下载页面:
1. 首先进入下载管理页面，显示下载的书籍，书籍显示图片和正在缓存的章节。进入显示每章的进度。
2. 需要有通知栏提示下载。



如何设计下载框架:

  能够进行下载的地方:
  1. 书架页面能够缓存全本
  2. 阅读页面，缓存全本和具体章节

  下载界面的功能:
  1. 显示缓存全本和某段章节的功能
  2. 具有暂停缓存的功能
  3. 具有删除缓存功能。(只是删除记录，如果要删除数据则需要直接删除书籍)
  删除缓存的地方:
  1. 书架页面删除全本书籍，然后退出追更

实现:

1. 创建一个含有SingleExecute的Service，并可以执行Single任务。
2. 重置CollBookBean表，加入CacheStatus(未缓存，正在缓存，缓存完成) 和 CacheChapterStation(如果缓存完成，显示
完成的位置)
3. 当点击缓存的时候如果显示缓存完成，并且当前收藏有更新的情况，开启额外缓存的功能(创建缓存对象)。如果没有则提示，
当前小说已缓存过了。如果显示正在缓存则表示已经在缓存队列中。如果显示未缓存则创建缓存对象，修改缓存状态。
4. 保存任务信息到数据库中，然后向Service发送开始缓存的请求。
5. 下载列表显示缓存状态。
6. 完成缓任务的逻辑。


## 实现SingleExecute

问题一:java中具有栈的数据结构?

从AsyncTask源码中发现ArrayDeque具有这个结构，那么ArrayDeque是什么东西？

问题二:如何实现根据任务的数量实现对任务的轮询

问题三:加入cancel与暂停状态

## 实现CollBookBean判断


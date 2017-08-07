# NovelReader

仿照"任阅"的追书App。

详见原作者项目地址:[任阅](https://github.com/JustWayward/BookReader)

# 应用简介

相对于原作增加的功能与改进:

1. 重写代码逻辑，优化代码结构，降低内存使用率
2. 重写小说阅读器,本地书籍加载功能。
3. 增加书本的断点续传功能 (仅支持单任务下载)
4. 采用数据库对数据进行存储。 (原作者是采用ACache类将数据修改成文本存储)

注: 项目目前还不完善，希望各位发现 BUG 能够尽快反馈，本人一定尽快修复，谢谢支持。

# 采用了以下开源框架

* [butterknife](https://github.com/JakeWharton/butterknife)    ==>    View注入
* [rxandroid](https://github.com/ReactiveX/RxAndroid)
* [rxjava](https://github.com/ReactiveX/RxJava)
* [greenDAO](https://github.com/greenrobot/greenDAO)    ==>    数据库
* [retrofit](https://github.com/square/retrofit)  ==> 网络
* [Glide](https://github.com/bumptech/glide)    ==>    图片加载
* [retrolambda](https://github.com/orfjackal/retrolambda)    ==>    在AndroidStudio3.0以下版本支持Lambda
* [treader](https://github.com/PeachBlossom/treader)    ==>    小说阅读页面的切换动画及整体架构都来自这个项目。
* [AndroidTagGroup](https://github.com/2dxgujun/AndroidTagGroup)    ==>    标签效果
# 应用展示

![](https://github.com/newbiechen1024/NovelReader/blob/master/screenshot/reader.gif)

![](https://github.com/newbiechen1024/NovelReader/blob/master/screenshot/load_local_file.gif)

![](https://github.com/newbiechen1024/NovelReader/blob/master/screenshot/download.gif)

# 更新记录

**update 2017-08-07**

修复BUG:无法翻阅上一页，且翻阅的上一页显示的是下一页的内容问题 (感谢 le9527 的修正)

**update 2017-07-24**

实现上下滚动翻页效果，但是问题还是比较多，主要如下:
1. 上下滚动不支持音量键翻页
2. 未修复，当上下滚动的时候，发生正在加载的情况的问题。
3. 未修复当发生加载情况，又进行效果切换的问题。
4. 文字的显示没有做好，当下滑的时候，会丢失MarginHeight高度的文字
5. 类封装的比较乱，逻辑不够清晰。

**注:**更新移除了左右滑动翻页效果，但保留了代码，如果有需要的话，可以删除注释使用。

上面的问题之后会慢慢修复。还有本人只实现了功能，没有进行仔细的测试，可能BUG比较多，如果各位发现BUG希望能够及时提交issue，谢谢。

**update 2017-07-14**
* 提供阅读器预加载功能(防止阅读下一章卡顿)
* 优化阅读器文字显示，提供段落间隔。
* 重新设计了加载本地章节的算法(之前的本地加载算法太慢了)
* 优化了PageLoader类的设计
* 修复一些BUG

**update 2017-07-06**

更新(如果之前已安装过，需要卸载重新安装):
* 提供进入更新书本功能和按照阅读循序排序功能
* 提供加载阅读本地书籍功能。
* 提供加入、删除本地书籍功能。
* 修改了页面加载器的设计。
* 优化了加载章节的逻辑和数据库存取逻辑
* 优化页面的绘制逻辑

**update 2017-06-07**
* 增加搜索书籍界面
* 增加阅读界面中小说目录中选择具体章节的用户体验(如增加滑动条，保证当前目录位置为正在阅读的小说章节的位置)
* 增加阅读界面中更多设置界面(提供音量键翻页，全屏显示效果)

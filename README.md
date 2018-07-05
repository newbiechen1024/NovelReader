# NovelReader

基于"任阅"的改进追书App。

详见原作者项目地址:[任阅](https://github.com/JustWayward/BookReader)

# 应用简介

相对于原作增加的功能与改进:

1. 重写代码逻辑，优化代码结构，降低内存使用率
2. 采用 sql 数据库对数据进行存储。 (原作者是采用ACache类将数据修改成文本存储)
3. 增加书本的断点续传功能 (仅支持单任务下载)
4. 支持小说更新提示。
5. 重写小说阅读器,仿掌阅设计。提供的功能如下

   * 支持翻页动画:仿真翻页、覆盖翻页、上下滚动翻页等翻页效果。
   * 支持页面定制:亮度调节、背景调节、字体大小调节
   * 支持全屏模式(含有虚拟按键的手机)、音量键翻页
   * 支持页面进度显示、页面切换、上下章切换。
   * 支持在线章节阅读、本地书籍查找。
   * 支持本地书籍加载到页面(支持本地书籍分章、加载速度快、耗费内存少)

注: 项目目前仍有许多问题，希望各位发现 BUG 能够尽快反馈，本人一定尽快修复，谢谢支持。

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

**update 2018-03-03**

* 修复：内存泄露
* 修复：一些细节 BUG
* 优化：页面加载细节

感谢小伙伴的提交和修复。

**update 2018-02-22**

* 重构：PageLoader 类的逻辑，提高代码的可读性。
* 修复：翻页到正在加载章节，无法翻页的问题。
* 优化：对于从网络加载章节的逻辑。

注：剩下的问题会尽快修复。

**update 2018-01-04**

* 修复：加载本地书籍导致的 OOM 问题：[原因比较坑爹](https://github.com/newbiechen1024/NovelReader/issues/26)
* 修改：重新修改了智能搜索本地书籍的办法(采用 LoaderManager + Android 媒体库，不会只扫描三级目录了)

**update 2018-01-06**

* 智能搜索书籍过慢的问题(采用 eric0815 建议，只扫描三级目录)
* 提供本地章节加载缓存。
* 提升数据库索引性能(将本地路径作为 id，改成使用 MD5生成 16 位字符串的方式)
* 提供数据库更新策略，不需要卸载在安装。
* 修复，第一次安装进入会崩溃的问题。

**update 2017-11-05**

* 添加每章标题显示
* 修改每页的排版

注:由于现在 IDEA 升级到了 AS 3.0 了。没有升级的同学，可能造成编译错误。或者 lambda 表达式不能使用的问题。解决方法是：

```gradle
# lambda 错误的解决办法。
# 在项目的 build.gradle 顶部添加

apply plugin: 'me.tatarka.retrolambda'
```

**update 2017-10-24**

* 修复上下滑动，手指只能移动一小段距离的 bug。
* 修复上下滑动时，存在页面重复显示的问题。

**update 2017-10-09**

* 重写 ItemDecoration，更好的方式设置 RecyclerView 的 divider
* 修复 ReadActivity 页面的亮度调节问题。(还有一些小问题)
* 修复上下滑动卡顿问题 (感谢 zeroAngus 的修改)
* 支持 Android 6.0 动态权限设置。
* 还修改了一些小 BUG。

**update 2017-09-07**
* 修复某些机型点击中间区域无法显示菜单的问题。(如果还是无法显示菜单，请发 issue)
* 添加翻页背面遮盖效果。

# 阅读器解析

阅读器具有的功能

1. 目录的显示 (当前页，已加载，未加载)
2. 夜间模式
3. 亮度调试(自动、自己设置)
4. 字体大小
5. 阅读的背景颜色
6. 范围点击弹出菜单
7. 翻页的动画效果
8. 文本的显示效果(根据字的大小，行间距显示文本)
9. 文本的来源(网络加载，还是存储，还有网络加载问题，是保留数据还是不保留数据，网络阅读的架构问题)
10. StatusBar的变化
11. 底部battery和时间、百分比的显示

作者是如何实现的

主要类:

ReadActivity:主要实现了显示切换还有一系列的点击事件。设置功能(夜间模式，亮度、目录、
字体大小的设置，文本的来源，StatusBar的变化)

BaseReadView:主要实现了加载文本上次的位置、判断点击事件(左边上一页，右边下一页，中间
弹出菜单并且判断是否相应翻页事件)、绘制文本页面的接口。总结而言实现的功能是对于点击事件
的判断并回调、开放翻页效果动画的接口、阅读位置的跳转与存储。之后所有的动画效果都是继承了
这个类。

PageFactory:其实BaserReadView还有的作用就是作为PageFactory的代理。PageFactory是
绘制的具体实现，绘制的内容有背景、字体大小、每页显示的文字、电池、时间、获取小说的段落等功能。

SettingManager:保存与恢复用户的阅读状态(性别、夜间模式、上一次阅读的节点、屏幕亮度、是否使用音量键翻阅等)

ThemeManager:设置阅读的背景的。

基本的了解完成了，我们现在来找难点在哪里，如何一步步的实现。

个人想法:

1. 参考掌阅还有UC阅读


## 实现阶段

1. 首先需要有一个可以显示含有菜单的界面，并实现展示目录的功能+设置版面的外形。

在设置之前，我们遇到的一个问题在于数据存储的设计(目录是否是永久保存的，会在退出的时候删除)。现在需要思考架构上的问题。
就是文章目录的问题，到底如何存储。

   1. 由于本身架构和能力的限制，当阅读的时候加载目录，并且默认缓存5章。当退出时候提示加入书架，并且提示不加入不保存进度

   2. 当书籍加入收藏的时候需要保存阅读的进度，还有章节的目录。这个应该由哪一方存储。

首先制作整体布局的问题，我需要的功能。 (大致布局框架完成)

### 阶段一

具体实现:

1. 实现菜单栏的显示和消失。 (完成)
2. 实现Toolbar顶部栏的显示和消失
3. 实现侧滑栏显示目录。(思考开始阅读和收藏下的不同状态 收藏保存目录保存状态。未收藏加载目录和状态)

沉浸式导航栏:在制作之前需要弄明白的几个问题

1. 如何将头部设置成半透明
2. 在半透明状态下，如何将布局不伸到StatusBar上
3. 如何设置隐藏StatusBar和NavigationBar，并且点击不会恢复，只有显示菜单的时候才恢复
4. 重新设置界面的StatusBar的问题。(1. 不设置StatusBar 2. 设置StatusBar 3. )

### 阶段二

具体实现:
1. 查看别人是如何构建阅读器，需要哪些类，每个类的作用是什么？
2. 自己如何组织如何实现。

根据treader作者代码的总结:

主要类分析:

PageWidget:原理是接收Bitmap作为展示，内部处理点击事件，最后通过AnimatorProvider处理翻页动画的类。对Bitmap进行处理
显示并绘制。在PageWidget中通过Scroller不断调用AnimatorProvider绘制翻页动画

AnimatorProvider:处理翻页动画接口类。真正的实现类是SimuationAnimator(仿真)、SlideAnimator(滑动)等。其真正的原理
是根据PageWidget传过来的具体点，然后对Bitmap进行转换而成。

PageFactory:制作Page的Factory。即创建显示在PageWidget中的Bitmap。

下面分析主要类的开发接口，以及做的具体的功能:

PageWidget:

开放的方法:

setCurrentPage(): 设置当前页面显示的Bitmap

setNextPage():设置下一个页面显示的bitmap

setBgColor():设置背景颜色 (不是被Bitmap覆盖了么，有什么用- -)

setPageMode():设置页面的切换模式

onTouchListener: 比如说是否存在下一页和上一页的判断接口。是否点击到中间的接口

整个的核心原理是TouchEvent事件的逻辑处理(详见PageWidget的onTouchEvent()事件)

这里的PageWidget还有AnimatorProvider都是照搬原作者的，我只开发PageFactory逻辑。

## 进度执行

1. 可以将head变成Toolbar，减小视图  (完成)
2. 可以将Setting移出来，定义为一个Dialog，减少ReadActivity的视图逻辑。(完成)
3. 重新修改SystemBar的弹出方式。(完成)
4. 实现屏幕的变化 (基本完成)
5. 实现PageFactory

PageFactory需要实现的东西:

1. 实现章节的显示(如何确保获取章节，然后根据视图获取每个page显示的行数量)
2. 实现电池的现实
3. 实现百分比的显示

## 如何实现PageFactory

1. 首先定义下载。首先从CollBook中查看当前进度，然后获取具体章节，再判断当前章节及其后五章是否已经下载，如果存在没有加载
就从网络中加载。之后将章节内容传递给PageFactory,PageFactory获取具体进度，并将数据进行分割，显示。每进行翻页的时候，
判断是否存在next如果不存在，则向ReadActivity告急，ReadActivity再从文本中导入数据给PageFactory,PageFactory再
进行切割。

顺序就是 ReadActivity传值给PageFactory->PageFactory切割并显示，当next章显示完了，再向ReadActivity进行请求。
ReadActivity在从数据库中加载文本(文本过程中下一章显示正在加载状态)。如果文本存在

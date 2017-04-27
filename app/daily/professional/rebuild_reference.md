# 代码重构

## 第一次(4月24日)

1. 成员变量、方法、命名问题 (完成一部分)
2. 方法的排版问题 (完成一部分)
3. Fragment采用名字调用的问题 (完成)
4. 小说分类的命名问题
5. Presenter与View中MVP连接过于复杂的问题，添加一些网络交互的基础View和Presenter
6. 添加数据库，模仿作者进行数据库与网络的连接
7. RefreshLayout的重构问题，痛点与作用。
8. Decoration的完美解决方案
9. 重构Gradle，将版本号放在gradle.properties
10. 重构Repository重新定义RxJava2的使用

以后的想法
8. 内存优化、LeakCache、单元测试

## 第一次重构进度与好的想法
App.java RxBus.java SplashActivity.java MainActivity,CommunityFragment

### 出现的问题

1. 项目中多次出现使用到ViewPager + TabLayout + Fragment的情况。该如何创建Activity进行复用

2. 如何正确的使用MVP，达到复用的效果。 (参考google的demo)

### 解决问题

1. 已解决

2. 如何对于命名的重构(完成，最终选择采用枚举)

#### 复用问题
1. 首先创建通用的Activity和layout。
2. 找出不同的地方。主要集中与Fragment与Fragment对应的标题上。那么如何解决这些问题呢？或者说如何做到Fragment与其名字同步。

如何命名:
1. 创建枚举类，包含title,icon,具体类的创建。 缺点，无法传递具体参数。因为我们需要采用Fragment.newInstance传递参数。
如果某天发现你的Fragment需要传递参数了，那么只能重新编写。因为Fragment会在内部被创建。传递参数只能通过其他的方式。

优点:将所有的参数统一起来，调用的时候可以很清晰的调用。并且对扩展开发。枚举本身就能充当一种类型。
缺点:无法给Fragment传递参数。

使用场景:在需要返回单个Fragment的时候，并且保证Fragment不会接收任何参数的情况下推荐使用。

2. 采用Constant的方式:将其作为键值对的形式存储在Constant中。
这种方式的优点:可能会比enum轻量级一点。并且调用方式比enum方便。
缺点:无法回收。
基本上不能在Fragment上使用，因为Fragment成为静态的是无法接受的，并且Fragment也无法创建。

3. 直接创建Fragment，在Fragment中设置Fragment的Title或者在strings.xml中设置。这样至少不容易出错。但是不符合开闭原则。


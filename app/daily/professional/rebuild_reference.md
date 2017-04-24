# 代码重构

## 第一次(4月24日)

1. 成员变量、方法、命名问题
2. 方法的排版问题
3. Fragment采用名字调用的问题
4. 小说分类的命名问题
5. Presenter与View中MVP连接过于复杂的问题，添加一些网络交互的基础View和Presenter
6. 添加数据库，模仿作者进行数据库与网络的连接
7. RefreshLayout的重构问题，痛点与作用。
8. Decoration的完美解决方案

以后的想法
8. 内存优化、LeakCache、单元测试

## 第一次重构进度与好的想法
App.java RxBus.java SplashActivity.java

### 出现的问题

1. 项目中多次出现使用到ViewPager + TabLayout + Fragment的情况。该如何创建Activity进行复用

### 解决问题

#### 复用问题
1. 首先创建通用的Activity和layout。
2. 找出不同的地方。主要集中与Fragment与Fragment对应的标题上。那么如何解决这些问题呢？或者说如何做到Fragment与其名字同步。
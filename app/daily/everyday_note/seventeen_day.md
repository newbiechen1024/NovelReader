# 任务

1. 修改Comment的命名
2. 完成CommentDetail (保证数据重启，数据存储)
3.

## 问题

1. 如何获取Class的内部类

2. 定义的Presenter由于双向绑定导致无法解耦合。。

3. 如何制作RatingBar。


## 实现


### 如何制作RatingBar

个人想法:
1. 继承android自带的RatingBar然后修改layout-list就完成了
2. 自定义RatingBar

源码分析:
1. 看了一波源代码，完全看不懂- -，到底怎么办。看来我还是没有达到这种境界。之后需要自己
分析一下遇到问题如何从源代码中找解决办法。并且分析源代码是如何实现的

自定义RatingBar:

原理:
1. 选择图片中宽、高最小的，根据star的数量分割宽度，然后选择每个空间中宽高最小的。
2. 从中选择最小的宽高作为图片的半径。然后通过中点绘制，达到显示并且默认居中于每块空间。
3. 可能有一种就是图片占满了空间，所以还需要能够自定义的大小，用来设置图片在每块空间的间距。
4. 由于是默认居中的，所以设置padding之后是无效的。(或者以后可以加上去，现在先不想padding的问题)

可以输入的参数:
1. starNum:等级的个数
2. canRate:是否可设置
3. initNum:初始化显示点击
4. unRateRes:未选中时候的图片
5. rateRes:已选中时候的图片
6. 图片的大小是否可以设置:个人认为不可以。。

实现:
1. 首先获取Drawable
2. 设定Drawable的大小

使用详情:
1. 根据Drawable 的大小 和 控件的大小决定整个Rating的宽高。然后设定每个room的大小。
2.默认图片居中显示



## 总结一下问题

1. DefaultItemDecoration无法显示的问题

2. ScrollRefreshLayout和RefreshLayout配置的问题

3. WholeAdapter兼容GridLayoutManager的问题

4.

## 未完成的事情

1. -> 数据存储的问题
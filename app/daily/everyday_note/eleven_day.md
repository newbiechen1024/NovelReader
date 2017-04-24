# 问题

1. RecyclerView 在ScrollView中设置成android:layout_height="wrap_content"导致没有大小的问题
2. 获取到Drawable的时候，如何设置Drawable的宽高。Drawable.setBounds的作用

# 解决方式

1. 原来是我个人犯的一个小错误，没有添加addView()。。。不关RecyclerView什么事情。

# 发现的东西

1. GridLayout是继承了LinearLayout的，所以说，我在自定义itemDecoration的时候，不能使用manger instanceof LinearLayoutManager 进行区分。

# ItemDrawable

1. 哪个方法先执行
2. Drawable.setBounds的作用
3. 为什么无法显示绘制信息
4. 作者的制作方式
5. 之后去研究一下Decoration的问题，现在不相关了，心态爆炸了

# 接下来的任务

1. 搜索 + 书籍详情 + 阅读器 + 下载

2. 数据缓存 + 单元测试 + 内存泄露 + 依赖注入

## 搜索狂的制作

1. 顶部的搜索条是什么东西 -> ActionMode -> SearchView
2. 大家都在搜:个人感觉是一个StaggerLayout
3. 搜索历史:需要保证数据的存储，与删除。个人想法是搜索成功后，添加到历史记录中，然后返回的时候再重新调用数据查找
4. 每当输入完成的时候都会调用Api，查找数据，并刷新，调用PopupWindow

## 上面最不熟悉的就是SearchView的使用

SearchView分为两种，一种是Android自带的ActionView。这个的优势是不需要自己创建。但是缺点是没有开放的样式。
所以一般都是采用自定义的View的方式来设置的。
为了简便起见，个人准备采用ActionView的方式


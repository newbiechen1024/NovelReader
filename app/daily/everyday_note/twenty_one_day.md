# 任务
1. 完成书籍详情页面。 (完成了一大半) (还缺少追更、阅读的点击事件，TagGroupView的制作)

2. 补全以前的错误。 (懒得做 - -)

3. 代码的复用。  (懒得做)

4. 解析阅读器。

# 明天的任务

1. 完成书籍详情页面的展示

## 关于错误补全

1. ScrollRecyclerView -> 修复 ScrollRefreshLayout  (完成)

2. 完成Tag的检索功能 (完成)

3. 完成Toolbar的上滑功能 (完成)

4. WholeAdapter兼容GridLayoutManager的问题 就是在使用的时候需要 (半完成)

5. RatingBar的点击功能 (我没有想到一个问题，就是头部和尾部都会居中，应该是只有中间部分的room才会居中) (懒得做)

6. 自定义的TextView (懒得做)

## 复用问题

1. 思考BaseAdapter是否需要继承RelativeLayout，还有View中使用ButterKnife有什么更好的删除方式。  (懒得做)

2. 逻辑复用(RecyclerView 中 setUpAdapter的 showError 、 complete 部分的复用) (懒得做)

## ScrollRefreshLayout的逻辑修复

需要重新修改布局，但是改变布局有两种方式。一种是创建view_tip并添加，另一种是修改layout_scroll_refresh。最后选择了
创建view_tip的方式。为什么选择view_tip，因为view_tip可以减少一层层级调用，这就在以后的修改中很方便了。

其实RefreshLayout是学到了如何制作复合控件，其他都不太重要。

## Tag的检索功能

首先是实现分类列表的HorizonTag。

遇到的主要问题:
1. 如何让Adapter拥有控制全局的能力，但是同时保持单个item的独立即。即外部的Adapter能够调用item内部的控件。

思考:
我原先的方法是将item独立出来，封装了各自的点击事件，然后将view内部的布局通过创建View来自定义，所以造成了这个问题。
这种方式对于RecyclerView中包含了多个不同的item而言，可读性更高。对于Item被其他RecyclerView复用而言这种方式也不错。
但是这种方法的缺点在于，Adapter无法直接访问Item了，并且每次需要继承一个View和IAdapter，当然这个是可以修改复用的。
而且每次需要使用到一个RelativeLayout进行封装也不是一个明知的选择。那么到底如何选择使用呢？

## 解决方法

1. IAdapter写一个复用类，代替ViewHolder。 相对于ViewHolder可以将onBindView()方法拿出来，之后这个ViewHolder就
能够被复用了。如果是使用ViewHolder的话，就必须被当成内部类，之后必须在Adapter中处理逻辑。那么逻辑不能能够被复用了。
所以我选择IAdapter

2. 那么独立出来之后，Adapter如何控制内部的所有事件呢？

首先Adapter需要获取所有内部的点击事件，这一点我们在onCreate中setOnClickListener()方法可以很好的办到。我们如何实现
TextView的颜色变红呢？ 这点如果是使用ViewHolder很容易办到，因为View可以获取到Adapter的全局变量，但是在IAdapter中
就很难办到，因为View获取不到全局变量。逻辑是当进行点击事件的时候，修改Adapter的选中状态，从而改变颜色。
第一个想法就是IAdapter能否获取状态，当然设置个setXX()方法。那么有没有更高效的方法，那就是当点击的时候自动切换颜色。
这种方式就必须修改代码了0 0。 得到的知识，有时候必须重写的就要重写，不要怕麻烦- - (其实使用ViewPager也可以0 0)

学到的东西，对于Adapter的理解和优化。思考如何改进Adapter。并且对于Adapter的全局响应有个概念。

现在解决筛选的问题，点击后显示红色。开放每个Item的点击事件。

第一个解决办法：
1. 就是根据当前已经写好的代码改写，通过使用层层的接口，开放点击。由于层级比较深最方便的办法就是使用RxBus。
(上面这种方式还不如重写)
2. 第二种方法,改变ExpandListView，在其中嵌套一个GridView。(感觉上似乎可以行的通)

3. 通过使用GridLayoutManager改写。

感觉第二个和第三个方法可行。如何决定使用哪一种方式呢？

感觉第三种方法会更好，但是第二种方法会更加的方便一点。 (那么就先采用第二种方法先试一下)

## Toolbar的上滑功能。

原理:使用Coordaret 然后 通过设置Behavior让顶部上滑。


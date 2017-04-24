# 含有弹性效果的ScrollView

## 思路
1. 截取点击事件，那么是截取dispatchEvent还是截取onInterpretEvent呢
2. 当判断滑动到顶端的时候，ScrollView是如何判断是滑动到底端还是顶端的，据我所知ScrollView内部的滑动并不是采用scrollTo的方式。
3. 当滑动到两个极端的时候，通过使用阻尼值*手动滑动得到距离，然后通过scrollBy进行滑动。当松手的时候使用Scroller进行复原。

## 我的问题

1. 继承一个类，是重写dispatchEvent还是onInterpretEvent好。
2. ScrollView的滑动效果的原理是什么，如何知道滑动到顶部还是底部。

##　作者实现的方法

我以为是采用scroll的方式移动内部View的位置的。但是从作者的方式看，ScrollView其实就是采用的是移动scroll来进行滑动的。
所以作者采用的另一种方式，直接改变contentView的layout的位置，从而达成效果。

## 回答问题

1. 关于重写dispatchEvent还是onInterpretEvent哪个好，首先需要考虑到的问题是,父类dispatchEvent方法是否有一些重要的逻辑，比如说对点击事件做出一些处理，如果你需要这些处理的时候，那么就重写onInterpret。
如果不需要或者父类没有实现是什么重要逻辑的情况下，接下来就是考虑如果父类获取到了onTouchEvent方法时候，你的逻辑能不能执行，我们知道如果父类使用了onTouchEvent方法的时候，不会调用onInterpretEvent(这点才是最重要的)
所以说，如果我重写的时候重写onInterpret方法的时候，会造成必须到底部的时候，重新点击滑动才能发生阻尼效果。

2. 因为之后看作者的答案的时候，才知道ScrollView内部采用的就是scrollBy的原理，所以说通过使用getHeight+getScrollY()与contentView进行比较就知道是否滑动到顶部或者底部了。

## 实现。

1. 获取contentView。然后通过layout设置contentView的originRect
2. 重写dispatchEvent方法。 编写onMove时候的

## 制作中的问题

1. ScrollView的滑动原理
2. ScrollView.getHeight()的高度指的是content的高度，还是指ScrollView的高度

## 总结

1. 被作者的实现套进去了，没有自己理解一遍。还有在滑动冲突的时候，没有考虑到各种情况，这也是我的问题。
2. 我有很多问题没有考虑清楚，就直接制作，导致最后都需要去看一下作者的实现。

### 整体流程总结

1. 第一步了解原理:当达到顶端或者低端的时候，通过改变ContentView的layout形成滑动，最后通过一个自动恢复的方式进行滑动恢复。

2. 首先我们需要获取到ContentView，获取ContentView的初始值等之后回复的时候使用。

3. 判断是否达到顶端或者底端，并设计什么时候可以滑动(到达顶端并且是下滑，到达低端并且是上滑)，最后up的时候，个人采用Scroller进行回复。
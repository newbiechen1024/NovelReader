# 第四天

## 昨日拖欠的任务

无

## 任务分配

1. 制作SelectionLayout (完成)
2. 制作CommunityActivity的雏形 (未完成)
3. 制作可刷新的RecyclerView (未完成)
4. 制作网络框架，实现展示雏形 (未完成)

## 遇到的问题

1. 选择器的制作

## 一些低级错误

1. 如何在代码上设置Animation动画
2. 关于如何创建Selection这个问题。

### 设置Animation动画

```java
Animation anim = AnimationUtils.loadAnim(context,xxx);
imageView.startAnimation(anim);
```

### 创建SelectionLayout

遇到了一个非常低级的问题，就是不知道需要创建SelectorItem。居
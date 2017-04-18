# 沉浸式效果制作

参考:

1. http://blog.csdn.net/lmj623565791/article/details/48649563
2. http://blog.csdn.net/guolin_blog/article/details/51763825

## 什么叫做沉浸式
这个可以参考:[郭霖——真正的沉浸式](http://blog.csdn.net/guolin_blog/article/details/51763825)

从郭神的文章中我们知道了，真正的沉浸式指的是一下两点:

  1. StatusBar和NavigationBar都会被隐藏
  2. 并且当App被点击的时候，并不会再次显示出来。

这个才是真正沉浸式的定义，而我们说的是实现透明栏。那么哪些地方可以给我们实现透明栏效果呢，当然就是StatusBar和NavigationBar了。

## 如何实现透明栏效果

从郭神的文章中说到了，透明栏效果是在5.x以后被加上去的，所以在这之前的是无法使用沉浸式导航栏的。那么现在我们来了解一个如何现实一个透明的StatusBar



### 5.x以上透明的StatusBar的实现

如果我们不是需要设置成透明导航栏，而是只是想通过代码改变沉浸式的颜色的话那么就使用
```java
activity.getWindow().setStatusBarColor(statusColor);
```

透明的StatusBar:
```java
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
//必须在LOLLIPOP以上才能使用
if (Build.VERSION.SDK_INT >= 21) {
    View decorView = getWindow().getDecorView();
    //配置为全屏，稳定
    int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    //确认配置
    decorView.setSystemUiVisibility(option);
    //将StatusBar变成透明
    getWindow().setStatusBarColor(Color.TRANSPARENT);
}
ActionBar actionBar = getSupportActionBar();
actionBar.hide();
```
但是这种方式会有一个问题，就是整个布局的大小会变大。如果之前的Toolbar大小被定死了的话，就可能会出现Toolbar的一部分被显示在StatusBar后面的情况。如何解决这个问题呢？
就是在可能上移的地方加上
```
android:fitsSystemWindows=true;
```
即
```xml
<android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/colorPrimary"
    android:minHeight="?attr/actionBarSize"
    android:fitsSystemWindows="true">
</android.support.v7.widget.Toolbar>
```


### 5.x以上透明的NavigationBar的实现
```java
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
if (Build.VERSION.SDK_INT >= 21) {
    View decorView = getWindow().getDecorView();
    int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    decorView.setSystemUiVisibility(option);
    getWindow().setNavigationBarColor(Color.TRANSPARENT);
    getWindow().setStatusBarColor(Color.TRANSPARENT);
}
ActionBar actionBar = getSupportActionBar();
actionBar.hide();
```

### 4.4以上StatusBar的实现

鸿神交给新的方法，能够在4.4以上设置半透明的导航栏。
当然就是详见:[](http://blog.csdn.net/lmj623565791/article/details/48649563)

推荐使用鸿阳的方法。
# 详解Dialog

我们从原理上弄懂什么是dialog。我们以前说过，其实Dialog跟Activity的原理差不多。都存在一个DecorView,然后通过onCreate()方法的setContent()来添加我们的子View。他们的不同之处有以下几点
1. Activity的Window配置的参数与Dialog不一样。
2. Activity在onResume()方法中将Window交给WindowManager。而Dialog是通过show()方法。

Dialog因为其特性，Android默认提供给我们了一些默认的样式。
第一种最熟悉的就是AlertDialog。
最终的效果是这样的:
![](xxx)
第二种就是我们重写Dialog获取到的DecorView的样式。最终的效果都是这样
![](http://img.blog.csdn.net/20160322220623719?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
我们发现只是内容变了，但是布局都没有改变。那么我们怎么改变这些布局呢？
首先我们得知道，我们通过一般方式改变不了的样式，都是Window的样式。那么最终就是如何更改Window的样式了。关于样式我们知道，如果我们需要制作通用的样式，那么只要从styles.xml中搜索android给我们留下的相应的属性进行修改就行了
```xml
<style name="CommonDialog" parent="android:Theme.Dialog">
    <item name="android:windowNoTitle">true</item>
    <!-- 无边框效果 -->
    <item name="android:windowFrame">@null</item>
    <item name="android:windowIsFloating">true</item>
    <item name="android:windowBackground">@color/transparent</item>

    <item name="android:windowEnterAnimation">@anim/slide_left_in</item>
    <item name="android:windowExitAnimation">@anim/slide_right_out</item>
</style>
```
本人查找了半天没有发现窗口的宽高的设置，所以就只能使用代码了

```java
WindowManager.LayoutParams lp = getWindow().getAttributes();
lp.width = WindowManager.LayoutParams.MATCH_PARENT;
lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
```
这里注意，如果我们想将Dialog设置成宽度占满屏幕，必须还要加上
```xml
<item name="android:windowBackground">@color/transparent</item>
```
其原因是:[Dialog无法占满横屏的原因](http://blog.csdn.net/u011183394/article/details/51445202)

总结:
1. Dialog的原理。(Dialog是由Window组成的，并且包含DecorView)
2. 当通用的样式无效的时候，如何通过使用styles.xml和Window来设置样式。
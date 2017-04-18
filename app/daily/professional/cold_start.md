# 如何实现SplashActivity的冷启动

## 原理

### 什么是冷启动什么是热启动

**冷启动:**当启动应用时，后台没有该应用的进程，这时系统会重新创建一个新的进程分配给该应用，这个启动方式就是冷启动。

特点:冷启动因为系统会重新创建一个新的进程分配给它，所以会先创建和初始化application类，再创建和初始化MainActivity类（包括一系列的测量、布局、绘制），最后显示在界面上。

**热启动:**当启动应用时，后台已有该应用的进程（例：按back键、home键，应用虽然会退出，但是该应用的进程是依然会保留在后台，可进入任务列表查看），所以在已有进程的情况下，这种启动会从已有的进程中来启动应用，这个方式叫热启动。

### 冷启动为什么会造成感觉卡顿的情况
首先我们要知道当打开一个Activity的时候发生了什么，在一个Activity打开时，如果该Activity所属的Application还没有启动，那么系统会为这个Activity创建一个进程（每创建一个进程都会调用一次Application，所以Application的onCreate()方法可能会被调用多次），在进程的创建和初始化中，势必会消耗一些时间，在这个时间里，WindowManager会先加载APP里的主题样式里的窗口背景（windowBackground）作为预览元素，然后才去真正的加载布局，如果这个时间过长，而默认的背景又是黑色或者白色，这样会给用户造成一种错觉，这个APP很卡，很不流畅，自然也影响了用户体验。

另一种情况是在2.0以上的gradle版本会使用instant run，instant run 会让我们快速部署带代码。因此带来的副作用就是会白屏一段时间。这个只是在debug版本下的情况，当使用release就不会发生这种情况了。

### 如何解决问题

第一种方式:我们刚才分析了一波冷启动的过程其中看到这样一段话
```
在这个时间里，WindowManager会先加载APP里的主题样式里的窗口背景（windowBackground）作为预览元素
```
所以说其中一种解决办法就是设置一个theme并且在theme中的
```xml
<item name="windowBackground">logo_path</item>
```

第二种方式:如果你没有logo，那么就可以将background设置为透明的方式。这样就看不出来似乎你的App造成了卡顿

**注意:**这种问题，会造成另一种问题就是，我们设置的background的图片一直存在于内存中无法释放。

## 使用

第一种实现方式:
1. 我们在styles.xml的style中，这样设置
```xml
<style name="SplashTheme" parent="AppTheme">
  <item name="windowBackground">logo_path</item>
</style>
```
当然我们也可以将logo_path换成
```xml
  <item name="windowBackground">@color/transparent</item>
```

2. 添加到AndroidManifest.xml中
```xml
<activity android:name=".ui.activity.SplashActivity"
    android:theme="@style/SplashTheme">
</activity>
```
或者在Activity中这样使用
```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    setTheme(R.style.SplashTheme);
    super.onCreate(savedInstanceState);
    setContentView(getContentId());
}
```

第二种实现方式:
在styles.xml中添加
```xml
<item name="android:windowIsTranslucent">true</item>
<item name="android:windowNoTitle">true</item>
```
但是这样会造成Activity切换动画无效的问题。所以不推荐这种方法。
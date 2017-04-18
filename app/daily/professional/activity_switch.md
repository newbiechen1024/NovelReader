# Activity之间切换的动画

## 切换动画的多种方式

第一、第二种方式都是使用overridePendingTransition();这个方法。其实现是这样的
```java
Intent intent = new Intent(this,MainActivity.class);
startActivity(intent);
overridePendingTransition(R.anim.enter,R.anim.exit);
```
第一参数表示:下一个Activity进入的动画。
第二个参数表示:当前退出的动画。

这个方法必须在startActivity()之后调用。因为这是当我们准备跳转到另外一个界面的动画。

第二种方式是这样的
```java
public class MainActivity extends BaseActivity{

  //...略
  @Override
  public void finish(){
       super.finish();
       overridePendingTransition(R.anim.enter,R.anim.exit);
  }
}
```
必须在，当前Activity结束的时候调用。两个参数的的意思跟上面的相同。都是指的是下一个要显示的Activity的进入和当前Activity的退出。

这个两种方式都必须在特定的时候调用很麻烦，如果我们想做一个通用的进入和退出，岂不是每次都要在特定的地方写一遍。那么有没有好的办法呢？
其实这些动画的都是交给Window实现的，那么当我们没有设置Activity进出的动画的时候，为什么会有动画呢？那当然是Android默认帮我们实现了。一想到Android默认帮我们实现的效果，那么就一定能够在style中找到重写的属性。我们就可以在styles.xml中靠提示猜测一下是哪个属性。最终我们得到下面的结果。
```xml
<item name="android:windowEnterAnimation">@anim/slide_right_in</item>
<item name="android:windowExitAnimation">@anim/slide_left_out</item>
```
这样就可以直接设置为通用的了。

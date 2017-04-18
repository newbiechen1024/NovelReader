# 如何修改自带的menu样式

一下几点是需要修改的

1. overflow图标的颜色
2. Menu显示的PopupMenu如何在图标下面
3. 如何设置overflow栏的背景颜色
4. 如何在overflow栏内添加图片
5. 设置overflow栏内的分割线

```xml
 <style name="Theme.ToolBar.Menu" parent="ThemeOverlay.AppCompat.Dark.ActionBar">
    <!-- 设置-弹窗的背景-颜色-->
    <item name="android:colorBackground">@color/nb.theme.background</item>
    <!-- 字体的颜色-->
    <item name="android:textColor">@color/nb.text.default</item>
    <!-- 字体大小-->
    <item name="android:textSize">16sp</item>
    <!-- 用于替换菜单的三个小白点，换成自已的图片-->
    <item name="actionOverflowButtonStyle">@style/ActionButton.Overflow.Menu</item>
    <!--用于控制menu弹出的位置，位于toolbar的下面，而不是上面-->
    <item name="actionOverflowMenuStyle">@style/OverflowMenuStyle</item>
    <!-- 用于修改item默认的分割线-->
    <item name="android:dropDownListViewStyle">@style/dropDownStyle</item>
    <item name="dropDownListViewStyle">@style/dropDownStyle</item>
</style>

<style name="ActionButton.Overflow.Menu" parent="android:style/Widget.Holo.Light.ActionButton.Overflow">
    <item name="android:src">@drawable/ic_menu_overflow</item>
</style>

<style name="OverflowMenuStyle" parent="Widget.AppCompat.Light.PopupMenu.Overflow">
    <item name="overlapAnchor">false</item>
</style>

<!--用于修改popmenu的分割线，和点击背景-->
<style name="dropDownStyle" parent="android:style/Widget.Holo.ListView.DropDown">
    <item name="android:listSelector">@drawable/btn_common_touch</item>
    <item name="android:divider">@color/nb.divider.common</item>
    <item name="android:dividerHeight">@dimen/nb.divider.line</item>
</style>
```

我们有没有想过，menu的原理，到底怎么查询如何修改这些样式？
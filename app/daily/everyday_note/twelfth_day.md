# 第十二天

任务:
完成重构，参照rebuild_reference。

1. 重构Gradle，将版本号放在gradle.properties()(完成)
2. 重构Repository重新定义RxJava2的使用。将Observer改成SingleObserver。SingleObserver更加轻量级一点。(完成)
3. 重构MVP接口，太多数据冗余和重复数据了。比如说重复的持有相互的引用调用，相互取消RxJava的订阅事件。(完成)
4. 重构数据接口，需要使用太多重复的代码，并且不利于添加数据(添加数据库)
5. 重构命名方式 (子布局第一个为父布局的缩写) (完成)
6. 重构方法(完成)
7. 设置逻辑(继承的类，设置onSaveBundle()方法，Layout的改写,数据存储,Presenter的交互)

## 问题

1. Dao如何跟Json共同使用


2. 文件的命名规则

## 明天的任务

一、重构ScrollRefreshLayout。
按照以下几点来:
  1. 能够使用attr.xml自定义的参数。

  2. 不设置为一个抽象类，而是设置成一个普通的RefreshLayout，并只能接收到一个View

  3. 重置ScrollRefreshRecyclerView

二、为WholeAdapter添加支持GridLayout的功能。




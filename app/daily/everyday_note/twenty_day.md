# 任务

1. 代码的重构
2. 排行榜书籍 (完成)
3. 别人家的排行榜 (完成)
4. 分类中的类别详情 (完成)

5. 完成主题书单的筛选(大boss)

## 遇到的问题

1. 排行榜下滑，会自动缩小的问题。
2. ExpandableListView 二级菜单无法点击的问题

## 解决问题

1. 不知道如何解决。感觉Expandable本身没有问题，而是自定的ReboundScrollView有问题
2. 需要重写Adapter中的isChildSelected()方法，返回true。才可以点击

## 代码重构

重构的主要部分如下
1. BaseTagActivity的重构  (完成)
2. 关于使用refresh_layout和scroll_refresh_layout的重构 (完成)
3. detail代码的重构  (对使用refresh_layout和scroll_refresh_layout的代码重构)
(想了半天，发现这些东西可以复用但是，又觉得没有必要)

4. 重构基于Presenter的Fragment的重构 (感觉没有必要。。就setUpAdapter()的重复代码特别多，其他只是意思重复，但是都不可以改)

## 排行榜的详情

展示数据的详细步骤
1. 分析数据接口，完成接口 (完成)
2. 创建BaseActivity  ()
3. 创建Fragment ()
4. 进行数据交互

## 明天的任务

1. 完成书籍详情页面。
2. 补全以前的错误。
3. 代码的复用。
4. 解析阅读器。
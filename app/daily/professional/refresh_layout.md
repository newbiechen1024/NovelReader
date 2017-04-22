# 制作RefreshLayout

## 分析

功能:
1. 加载动画View
2. 错误点击的View

开放的方法:
1. showLoading()
2. showError()
3. setOnErrorListener()
4. loadFinish()
## 制作

1. 原理是使用FrameLayout嵌套这三个(Load,Content,Error)，然后通过逻辑
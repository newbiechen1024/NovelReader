# 已发现的BUG

## ReadActivity

1. 点击切换章节，章节正在加载的时候。切换到其他章节会造成，一直现实正在加载的问题。 (需要对状态进行重构)
2. 采用直接对章节进行页面分割的方式，放弃采用一页一页读取加载的方式。(需要考虑章节内容的问题)

## BookShelfFragment

1. 书架上书籍显示的顺序问题

## 排行榜的BookDetail页

1. 追书人数显示有问题，一直是0

## 分类页面的BookDetail页

1. BookDetail中存在的追书人数有问题，有时存在有时候不存在。

2. 退出崩溃 (不知道)

3. 弱网环境无法加载数据的问题

## BookSortActivity页面

无法加载更多 (完成)

# 功能性bug

1. 修复RatingBar的图片显示并且提供点击事件

2. ItemDecoration有时候无法展示divider
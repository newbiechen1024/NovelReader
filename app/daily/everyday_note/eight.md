```java
    //book--block
    @StringDef({
            Block.COMMENT,
            Block.ORIGIN,
            Block.GIRL
    }
    )
    @Retention(RetentionPolicy.SOURCE)
    public @interface Block{
        String COMMENT = "ramble";
        String ORIGIN = "original";
        String GIRL = "girl";
    }
```
这段代码的作用和原理

2. 嵌套命名的问题

到底如何进行命名转换才算好

3. 问题:动画效果不显示的问题

4. 问题:Activity退出的时候，会重新启动上一个Activity

5. 任务:时间显示的问题 （思路:通过update与本地时间差）

   1. 一小时以内显示具体分钟

   2. 几小时内显示具体时间

   3. 一天内显示，昨天

   4. 之后显示具体月份

6. 数据库的缓存问题
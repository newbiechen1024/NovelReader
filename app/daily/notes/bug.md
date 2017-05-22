# 遇到未解决的bug

1. ReadPresenter -> loadCategory() 没有对目录加载错误的情况做出逻辑处理。
   (第34行，并使用todo标记)

2. 关于Adapter的框架写的有问题，需要改进 (完成)

3. 在BookManager中的150行，有编码转换的问题。采用Channel是不是更好呢？

4. 无法对本机导入的文本进行处理(多数据导入内存的问题)




## 入参解析->MP

- 常规. 属性名==表字段名(驼峰-下划线)
- 范围字段. `nickName_ge`, `nickName_lt`, ...
- 排序字段. `字段名_order_升序_顺序`, 如: `nickName_order_desc_1`

@MpField 注解只用定义 ew 组名, 就可以了.
优先使用注解定义的变量名.






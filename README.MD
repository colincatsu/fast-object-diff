# Fast-object-diff

我们经常要打一些日志记录,然而手动记录是比价困难的,
也是不方便的,非常需要一个对比java相同对象内不同值的方法
Fast-object-diff就这样诞生.

1. 支持基本对象 和 Collection集合
2. 日期可按照自定义格式输出
3. 注解方法输出日志,配置简单
4. 忽略不需要的值
5. 提供基础方法 generateDiff 和扩展方法
6. 集合中可根据某个key进行对应diff记录,方便乱序中使用diff对比某个Object

使用方法很简单:
```java
    public class BeanA {
    
        //使用注解标记列
        @DiffLog(name = "测试a")
        private String a;
    
        //忽略这个
        @DiffLog(name = "测试b", ignore = true)
        private String b;
        
        //集合递归支持
        @DiffLog(name = "BList集合")
        private List<BeanB> bList;
    
        //日期格式转换
        @DiffLog(name = "开始时间",dateFormat = "yyyy-dd-MM hh:mm:ss")
        private Date start;
    
        @DiffLog(name = "价格")
        private BigDecimal price;
    }

```
```java
    public class BeanB {

        @DiffLogKey(name = "订单编号")//标记集合中对应的key,根据这个key来比对输出
        @DiffLog(name = "主键")
        private Long id ;

        @DiffLog(name = "机场")
        private String name;
    }

```


```java
            BeanB a1b = new BeanB(1L,"北京");
            BeanB a1b3 = new BeanB(3L,"3");
            BeanB a1b2 = new BeanB(2L,"1");
    
            ArrayList<BeanB> list = new ArrayList<>();
            list.add(a1b);
            list.add(a1b3);
            list.add(a1b2);
            BeanA a1 = new BeanA("1","1",list);
            a1.setStart(new Date());
            //        a1.setPrice(new BigDecimal("10.23"));
    
    
            BeanB a2b = new BeanB(1L,"上海");
            BeanB a2b2 = new BeanB(2L,"2");
    
            ArrayList<BeanB> list2 = new ArrayList<>();
            list2.add(a2b);
            list2.add(a2b2);
            final BeanA a2 = new BeanA("2","2",list2);
            a2.setPrice(new BigDecimal("50.852236"));
            List<DiffWapper> diffWappers = AbstractObjectDiff.generateDiff(a1, a2);


```

结果Json格式:
```javascript
[{"diffValue":{"newValue":"2","oldValue":"1"},"logName":"测试a","op":"CHANGE","path":"/a"},{"diffValue":{"newValue":"上海","oldValue":"北京"},"logName":"BList集合.订单编号[1].机场","op":"CHANGE","path":"/bList/1/name"},{"diffValue":{"newValue":"2","oldValue":"1"},"logName":"BList集合.订单编号[2].机场","op":"CHANGE","path":"/bList/2/name"},{"diffValue":{"newValue":null,"oldValue":"[主键]=3 ,[机场]=3 "},"logName":"BList集合.订单编号[3]","op":"REMOVE","path":"/bList/3"},{"diffValue":{"newValue":null,"oldValue":"2019-09-09 02:16:30"},"logName":"开始时间","op":"REMOVE","path":"/start"},{"diffValue":{"newValue":50.852236,"oldValue":null},"logName":"价格","op":"ADD","path":"/price"}]
```

也可以直接输出中文日志格式:
```java
  String s = ChineseObjectDiff.genChineseDiffStr(a1, a2);

```
结果直接输出:
```java
「测试a」由[1]修改为[2]
「BList集合.订单编号[1].机场」由[北京]修改为[上海]
「BList集合.订单编号[2].机场」由[1]修改为[2]
「BList集合.订单编号[3]」由[[主键]=3 ,[机场]=3 ]被删除
「开始时间」由[2019-09-09 02:01:54]被删除
「价格」被添加成[50.852236]
```

也可以使用AbstractObjectDiff+DiffWapper扩展自己的格式输出.
```java
public class ChineseObjectDiff extends AbstractObjectDiff {
    @Override
    protected String genDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

}

```
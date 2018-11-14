## 知识点
- text字段建索引时指定为not_analyzed，才能用term query
- type=text，默认会设置两个field，一个是field本身，比如articleID，就是分词的；
    还有一个的话，就是field.keyword，articleID.keyword，默认不分词，会最多保留256个字符
- 每个filter条件都会对应一个bitset；遍历每个过滤条件对应的bitset，优先从最稀疏的开始搜索。

- bool：must，must_not，should，组合多个过滤条件；（2）bool可以嵌套
- terms多值搜索，相当于SQL中的in语句
- 控制搜索结果精准度：and operator，minimum_should_match
- multi-value搜索 -> bool + term
- best fields策略 :某一个field中匹配到了尽可能多的关键词;dis_max:直接取多个query中，分数最高的那一个query的分数即可;
    使用tie_breaker将其他query的分数也考虑进去
- multi_match  查询为能在多个字段上反复执行相同查询提供了一种便捷方式

> multi_match 多匹配查询的类型有多种，其中的三种恰巧与 了解我们的数据 中介绍的三个场景对应，
    即： best_fields 、 most_fields 和 cross_fields （最佳字段、多数字段、跨字段）。
best-fields策略，主要是说将某一个field匹配尽可能多的关键词的doc优先返回回来
most-fields策略，主要是说尽可能返回更多field匹配到某个关键词的doc，优先返回回来    
## 灵活使用and关键字
GET /forum/article/_search
{
    "query": {
        "match": {
            "title": {
		"query": "java elasticsearch",
		"operator": "and"
   	    }
        }
    }
}

GET /forum/article/_search
{
  "query": {
    "match": {
      "title": {
        "query": "java elasticsearch spark hadoop",
        "minimum_should_match": "75%"
      }
    }
  }
}
## term filter来搜索数据
- constant_score
不关心 TF/IDF ， 只想知道一个词是否在某个字段中出现过。如果出现，则记 1 分，不出现记 0 分。
```sh

GET /forum/article/_search
{
    "query" : {
        "constant_score" : {
          "boost": 1.1, 
            "filter" : {
                "term" : { 
                    "userID" : 1
                }
            }
        }
    }
}
```

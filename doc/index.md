

- 测试分词器

```json

GET _analyze?pretty
{
  "analyzer": "ik_max_word",
  "text": "公交八条防盗暗语"
}

GET _analyze?pretty
{
  "analyzer": "ik_smart",
  "text": "公交八条防盗暗语"
}


```

- 建立索引(配置 mapping,自定义分词器)

```sh

PUT yimilan_index_cp
{
  "settings": {
    "analysis": {
      "analyzer": {
        "split_analyzer": {
          "type":      "custom", 
          "tokenizer": "split_tokenizer",
          "char_filter": [
            "html_strip"
          ],
          "filter": [
            "lowercase"
          ]
        }
      },
      "tokenizer": {
        "split_tokenizer": {
          "type": "pattern",
          "pattern": ","
        }
      }
    }
  }
  , "mappings": {
     "school": { 
      "properties": { 
        "name":    { "type": "text","analyzer": "ik_smart"  }, 
        "address": { "type": "text"  ,"analyzer": "ik_max_word"},
        "tags": { "type": "text"  ,"analyzer": "split_analyzer"}
      }
    }
  }
}

```

- 测试分词

```sh

POST yimilan_index_cp/_analyze
{
  "analyzer": "split_analyzer",
  "text": "默认,bbb"
}
```


- match查询 
```sh

GET yimilan_index_cp/_search
{
  "query": {
    "match": {
      "name": "方庄第一幼儿园"
    }
  }
}
```

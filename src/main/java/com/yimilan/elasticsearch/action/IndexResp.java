package com.yimilan.elasticsearch.action;
import lombok.Data;

@Data
public class IndexResp {
    String index;
    String type;
    String id;
    long version;
    long seqNo;
    String result;
}

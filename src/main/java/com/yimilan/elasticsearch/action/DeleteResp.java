package com.yimilan.elasticsearch.action;

import lombok.Builder;
import lombok.Data;

@Data
public class DeleteResp {
    String index;
    String type;
    String id;
    long version;
    String result;
}

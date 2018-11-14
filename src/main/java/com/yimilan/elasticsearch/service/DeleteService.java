package com.yimilan.elasticsearch.service;

import com.yimilan.elasticsearch.action.DeleteResp;
import com.yimilan.elasticsearch.index.query.Query;

/**
 * Delete API
 */
public interface DeleteService {

    /**
     * 基于文档ID删除
     * @param indice
     * @param type
     * @param docId
     * @return
     */
    public DeleteResp delete(String indice, String type, String docId);

    /**
     * Delete By Query API
     * @param indice
     * @param type
     * @param query
     * @return 删除的数量
     */
    public long delete(String indice, String type, Query query);
}

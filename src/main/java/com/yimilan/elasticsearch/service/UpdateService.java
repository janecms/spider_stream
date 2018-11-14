package com.yimilan.elasticsearch.service;

import com.yimilan.elasticsearch.action.UpdateResp;

import java.util.concurrent.ExecutionException;

/**
 *
 */
public interface UpdateService {
    /**
     * 替换更新
     * @param index
     * @param type
     * @param docId
     * @param docData
     */
    public UpdateResp update(String index, String type, String docId, String docData);

    /**
     * 合并更新
     * @param index
     * @param type
     * @param docId
     * @param particularDoc
     */
    public UpdateResp merge(String index,String type,String docId,String particularDoc) throws ExecutionException, InterruptedException;
}

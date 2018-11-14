package com.stream.es.service.impl;
import com.stream.spider.dao.ESClient;
import com.yimilan.elasticsearch.action.UpdateResp;
import com.yimilan.elasticsearch.service.UpdateService;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
@Service
public class ElasticUpdateService implements UpdateService {
    @Autowired
    private ESClient client;

    @Override
    public UpdateResp update(String index, String type, String docId, String docData) {
        UpdateResponse updateResponse = client.getClient().prepareUpdate(index, type, docId)
                .setDoc(docData)
                .get();
        UpdateResp resp = toResp(updateResponse);
        return resp;
    }

    private UpdateResp toResp(UpdateResponse updateResponse) {
        UpdateResp resp = new UpdateResp();
        resp.setId(updateResponse.getId());
        resp.setIndex(updateResponse.getIndex());
        resp.setType(updateResponse.getType());
        resp.setResult(updateResponse.getResult().toString());
        resp.setSeqNo(updateResponse.getSeqNo());
        resp.setVersion(updateResponse.getVersion());
        return resp;
    }

    @Override
    public UpdateResp merge(String index, String type, String docId, String particularDoc) throws ExecutionException, InterruptedException {
        UpdateRequest updateRequest = new UpdateRequest(index, type, docId).doc(particularDoc, XContentType.JSON);
        UpdateResponse updateResponse = client.getClient().update(updateRequest).get();
        return toResp(updateResponse);
    }
}

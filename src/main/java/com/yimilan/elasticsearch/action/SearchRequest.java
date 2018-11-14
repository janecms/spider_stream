package com.yimilan.elasticsearch.action;
import com.yimilan.elasticsearch.index.query.Query;
public class SearchRequest  extends ActionRequest{
    String searchType;
    /**
     * 索引
     */
    String indice;
    /**
     * 类型
     */
    String type;

    Query query;
    Query postFilter;

    String from;
    String size;

    public SearchRequest(String indice, String type) {
        this.indice = indice;
        this.type = type;
    }

    public SearchRequest() {
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getPostFilter() {
        return postFilter;
    }

    public void setPostFilter(Query postFilter) {
        this.postFilter = postFilter;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

package com.yimilan.elasticsearch.index.query;

/**
 * @see org.elasticsearch.index.query.ExistsQueryBuilder
 */
public class MatchQuery implements Query{
    private  String fieldName;
    private  Object value;
    public MatchQuery() {

    }

    public MatchQuery(String fieldName, Object value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

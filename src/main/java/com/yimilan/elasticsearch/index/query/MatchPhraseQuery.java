package com.yimilan.elasticsearch.index.query;
/**
 * NAME = "match_phrase"
 * @see org.elasticsearch.index.query.MatchPhraseQueryBuilder
 */
public class MatchPhraseQuery implements Query{
    private  String fieldName;

    private  Object value;

    public MatchPhraseQuery() {
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

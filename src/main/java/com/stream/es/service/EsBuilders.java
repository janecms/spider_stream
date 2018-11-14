package com.stream.es.service;

import com.yimilan.elasticsearch.index.query.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

public class EsBuilders {


    /**
     * 请求对象转换为es builder
     *
     * @param query
     * @return
     */
    public static QueryBuilder toQueryBuilder(Query query) {
        QueryBuilder esBuilder = null;
        if (query instanceof MatchQuery) {
            MatchQuery matchQuery = (MatchQuery) query;
            esBuilder = QueryBuilders.matchQuery(matchQuery.getFieldName(), matchQuery.getValue());
        } else if (query instanceof MatchPhraseQuery) {
            MatchPhraseQuery matchPhraseQuery = (MatchPhraseQuery) query;
            esBuilder = QueryBuilders.matchPhraseQuery(matchPhraseQuery.getFieldName(), matchPhraseQuery.getValue());
        }else if(query instanceof TermQuery){
            TermQuery termQuery =(TermQuery)query;
            esBuilder = QueryBuilders.termQuery(termQuery.getFieldName(), termQuery.getValue());
        } else if (query instanceof BoolQuery) {
            BoolQuery boolQuery = (BoolQuery) query;
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.minimumShouldMatch(boolQuery.getMinimumShouldMatch());
            final List<Query> mustClauses = boolQuery.getMustClauses();
            final List<Query> shouldClauses = boolQuery.getShouldClauses();
            final List<Query> mustNotClauses = boolQuery.getMustNotClauses();
            final List<Query> filterClauses = boolQuery.getFilterClauses();
            for (Query must : mustClauses) {
                final QueryBuilder queryBuilder = toQueryBuilder(must);
                boolQueryBuilder.must(queryBuilder);
            }
            for (Query should : shouldClauses) {
                final QueryBuilder queryBuilder = toQueryBuilder(should);
                boolQueryBuilder.should(queryBuilder);
            }
            for (Query mustNot : mustNotClauses) {
                final QueryBuilder queryBuilder = toQueryBuilder(mustNot);
                boolQueryBuilder.mustNot(queryBuilder);
            }
            for (Query filter : filterClauses) {
                final QueryBuilder queryBuilder = toQueryBuilder(filter);
                boolQueryBuilder.filter(queryBuilder);
            }
            esBuilder = boolQueryBuilder;
        }
        return esBuilder;
    }

}

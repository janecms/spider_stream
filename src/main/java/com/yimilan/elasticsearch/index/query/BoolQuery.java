package com.yimilan.elasticsearch.index.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @org.elasticsearch.index.query.BoolQueryBuilder
 */
public class BoolQuery implements Query{
    private final List<Query> mustClauses = new ArrayList<>();

    private final List<Query> mustNotClauses = new ArrayList<>();

    private final List<Query> filterClauses = new ArrayList<>();

    private final List<Query> shouldClauses = new ArrayList<>();

    private String minimumShouldMatch;

    public List<Query> getMustClauses() {
        return mustClauses;
    }

    public List<Query> getMustNotClauses() {
        return mustNotClauses;
    }

    public List<Query> getFilterClauses() {
        return filterClauses;
    }

    public List<Query> getShouldClauses() {
        return shouldClauses;
    }

    public String getMinimumShouldMatch() {
        return minimumShouldMatch;
    }

    public void setMinimumShouldMatch(String minimumShouldMatch) {
        this.minimumShouldMatch = minimumShouldMatch;
    }
    public void addFilterClause(Query query){
        this.filterClauses.add(query);
    }
    public void addMustClause(Query query){
        this.mustClauses.add(query);
    }
    public void addMustNotClause(Query query){
        this.mustNotClauses.add(query);
    }
    public void addShouldClause(Query query){
        this.shouldClauses.add(query);
    }
}

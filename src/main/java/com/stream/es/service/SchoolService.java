package com.stream.es.service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Sets;
import com.stream.es.mapper.SchoolMapper;
import com.stream.es.model.DuHit;
import com.stream.es.model.Hit;
import com.stream.es.model.School;
import com.stream.spider.dao.ESClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * 学校库索引
 */
@Slf4j
@Service
public class SchoolService {
    private static final String INDEX_NAME = "yimilan_index_cp";
    private static final String TYPE_NAME = "school";
    @Autowired
    ESClient client;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    JedisCluster jedisCluster;
    /**
     * 创建学校
     *
     * @param school
     * @return
     */
    public boolean index(School school) {
        String source = JSON.toJSONString(school);
        IndexResponse response = client.getClient().prepareIndex(INDEX_NAME, TYPE_NAME, school.getId())
                .setSource(source, XContentType.JSON).get();
        return response.getResult() != DocWriteResponse.Result.NOT_FOUND;
    }

    private List<String> tags = new ArrayList<>();

    public SchoolService() {
        tags.add("市直属");
        tags.add("县直属");
        tags.add("村级");
        tags.add("重点");
        tags.add("非重点");
        tags.add("留守儿童");
        tags.add("CRM");
        tags.add("新用户");
        tags.add("潜在客户");
    }

    //无意义
    public void customTag(School school) {
        int count = RandomUtil.getRandom().nextInt(5);
        Set<String> tagSet = Sets.newHashSet();
        for (int i = 0; i < count; i++) {
            final int tagIdx = RandomUtil.getRandom().nextInt(tags.size());
            tagSet.add(tags.get(tagIdx));
        }
        school.setTags(tagSet);
    }

    /**
     * 批量插入
     *
     * @param schoolList
     * @return true:存在失败
     */
    public boolean bulk(List<School> schoolList) {
        BulkRequestBuilder bulkRequest = client.getClient().prepareBulk();
        for (School school : schoolList) {
            customTag(school);
            String source = JSON.toJSONString(school);
            IndexRequestBuilder indexRequestBuilder = client.getClient().prepareIndex(INDEX_NAME, TYPE_NAME, school.getId()).setSource(source, XContentType.JSON);
            bulkRequest.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        return bulkResponse.hasFailures();
    }

    /**
     * @param id
     * @return
     */
    public School get(String id) {
        GetResponse response = client.getClient().prepareGet(INDEX_NAME, TYPE_NAME, id).get();
        String json = response.getSourceAsString();
        return JSON.parseObject(json, School.class);
    }

    /**
     * 删除文档
     *
     * @param docId
     * @return
     */
    public DocWriteResponse.Result delete(String docId) {
        DeleteResponse deleteResponse = client.getClient().prepareDelete(INDEX_NAME, TYPE_NAME, docId).get();
        return deleteResponse.getResult();
    }

    public void update(String docId, Map<String, String> fields) throws IOException {
        XContentBuilder xcb = jsonBuilder().startObject();
        UpdateRequestBuilder updateRequestBuilder = client.getClient().prepareUpdate("ttl", "doc", "1");
        fields.keySet().stream().forEach(key -> {
            try {
                xcb.field(key, fields.get(key));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        UpdateResponse updateResponse = updateRequestBuilder.setDoc(xcb.endObject()).get();
        updateResponse.status();
    }

    /**
     * 查询
     *
     * @param page
     * @param school
     * @return
     */
    public Hit<School> query(Integer page, Integer pageSize, School school) {

//        QueryBuilders.termsQuery()
        final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (CollectionUtils.isNotEmpty(school.getTags())) {
            boolQuery.must(QueryBuilders.termsQuery("tags", school.getTags().toArray()));
        }
        if (StringUtils.isNotBlank(school.getName())) {
            boolQuery.must(QueryBuilders.matchQuery("name", school.getName()));
        }
        SearchResponse searchResponse = client.getClient().prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setFrom(page).setSize(pageSize).
                setQuery(boolQuery).get();
        SearchHits hits = searchResponse.getHits();
        List<School> schools = new ArrayList<>();
        hits.iterator().forEachRemaining(hit -> {
            String sas = hit.getSourceAsString();
            final School result = JSON.parseObject(sas, School.class);
            final List<DuHit> duHitList = this.duSchool(result, 0.2f);
            result.setDupex(duHitList.size()-1);
            schools.add(result);
        });
        Hit<School> hit = new Hit<School>();
        hit.setHints(schools);
        hit.setTotal(hits.totalHits);
        return hit;
    }

    /**
     * 根据名称匹配
     *
     * @param school
     */
    public List<DuHit> duSchool(School school, float threshold) {
        final Long cityId = school.getCityId();
        final Long provinceId = school.getProvinceId();
        final Long districtId = school.getDistrictId();
        final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //省市区 三级完全一样
        final BoolQueryBuilder addrQb = QueryBuilders.boolQuery();
        addrQb.must(QueryBuilders.termQuery("districtId", districtId));
        addrQb.must(QueryBuilders.termQuery("cityId", cityId));
        addrQb.must(QueryBuilders.termQuery("provinceId", provinceId));
        boolQuery.filter(addrQb);

        final BoolQueryBuilder queryBuilder = boolQuery.must(QueryBuilders.multiMatchQuery(school.getName(), "name", "address"));
        final ScoreSortBuilder order = SortBuilders.scoreSort().order(SortOrder.DESC);
        SearchResponse searchResponse = client.getClient().prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setFrom(0).setSize(10)
                .setQuery(queryBuilder).addSort(order).get();
        final SearchHits hits = searchResponse.getHits();
        final SearchHit[] hitsArr = hits.getHits();
        List<DuHit> duHitList = new ArrayList<>();
        final float maxScore = hits.getMaxScore();
        for (SearchHit hit : hitsArr) {
            final DuHit<Object> duHit = DuHit.builder().data(JSON.parseObject(hit.getSourceAsString(), School.class)).score(hit.getScore()).build();
            if (hit.getId().equals(school.getId())) {
                log.info("本学校 score:{}", hit.getScore());
            }
            float v = (maxScore - hit.getScore()) / hit.getScore();//越小越匹配
            if (v > threshold) {
                return duHitList;
            }
            log.info("searchHit:{} --{}", hit.getSourceAsString(), hit.getScore());
            duHitList.add(duHit);
        }
        return duHitList;
    }

    /**
     * 重建索引
     */
    public void reindex() {
        IPage<School> page = new Page();
        School school = new School();
        school.setCityId(null);
        school.setDistrictId(null);
        school.setProvinceId(null);
        QueryWrapper<School> queryWrapper = new QueryWrapper<>(school);
        page.setSize(1000);
        IPage<School> schoolPage = this.schoolMapper.selectPage(page, queryWrapper);
        log.info("保存学校索引 pageNum:{} :page:{}", page.getCurrent(), page.getPages());
        this.bulk(schoolPage.getRecords());
        ForkJoinPool.commonPool().invoke(new ReinexAction(1, page.getPages()));
    }

    public void schoolName() {
        IPage<School> page = new Page();
        School school = new School();
        school.setCityId(null);
        school.setDistrictId(null);
        school.setProvinceId(null);
        QueryWrapper<School> queryWrapper = new QueryWrapper<>(school);
        page.setSize(1000);
        IPage<School> schoolPage = this.schoolMapper.selectPage(page, queryWrapper);
        log.info("保存学校索引 pageNum:{} :page:{}", page.getCurrent(), page.getPages());
        this.bulk(schoolPage.getRecords());
        List<String> result = ForkJoinPool.commonPool().invoke(new NameAction(1, page.getPages()));
        System.out.println(result.size());
    }

    class NameAction extends RecursiveTask<List<String>>{
        private static final int THRESHOLD = 10;
        long beginPage;
        long endPage;
        long size = 1000;

        public NameAction(long beginPage, long endPage) {
            this.beginPage = beginPage;
            this.endPage = endPage;
        }

        @Override
        protected List<String> compute() {
            List<String> result = new ArrayList();
            if (endPage - beginPage > THRESHOLD) {
                List<NameAction> dividedTasks = new ArrayList<>();
                NameAction s1 = new NameAction(beginPage, beginPage + (endPage - beginPage) / 2);
                NameAction s2 = new NameAction(beginPage + (endPage - beginPage) / 2 + 1, endPage);
                dividedTasks.add(s1);
                dividedTasks.add(s2);
                invokeAll(dividedTasks);
            } else {
                IPage<School> page = new Page();
                School school = new School();
                school.setCityId(null);
                school.setDistrictId(null);
                school.setProvinceId(null);
                QueryWrapper<School> queryWrapper = new QueryWrapper<>(school);
                page.setSize(size);
                for (long i = beginPage; i < endPage; i++) {
                    page.setCurrent(i);
                    log.info("保存学校索引 page:{}", page.getCurrent());
                    IPage<School> schoolPage = schoolMapper.selectPage(page, queryWrapper);
                   for (School po :schoolPage.getRecords()){
                       String str = po.getName();
                       String[] tokens = str.split("省|市|县|区");
                       if (tokens.length>1){
                           String lastToken = tokens[tokens.length - 1];
                           String[]  last = lastToken.split("镇|乡");
                           if (last.length>1){
                               System.out.println(str+":解析结果:"+last[0]);
                               jedisCluster.sadd("zhen_xian",last[0]);
                               result.add(last[0]);
                           }
                       }
                   }
                }

            }

            return result;
        }
    }

    class ReinexAction extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 10;
        long beginPage;
        long endPage;
        long size = 1000;

        public ReinexAction(long beginPage, long endPage) {
            this.beginPage = beginPage;
            this.endPage = endPage;
        }

        @Override
        public Integer compute() {
            if (endPage - beginPage > THRESHOLD) {
                List<ReinexAction> dividedTasks = new ArrayList<>();
                ReinexAction s1 = new ReinexAction(beginPage, beginPage + (endPage - beginPage) / 2);
                ReinexAction s2 = new ReinexAction(beginPage + (endPage - beginPage) / 2 + 1, endPage);
                dividedTasks.add(s1);
                dividedTasks.add(s2);
                invokeAll(dividedTasks);
            } else {
                IPage<School> page = new Page();
                School school = new School();
                school.setCityId(null);
                school.setDistrictId(null);
                school.setProvinceId(null);
                QueryWrapper<School> queryWrapper = new QueryWrapper<>(school);
                page.setSize(size);
                for (long i = beginPage; i < endPage; i++) {
                    page.setCurrent(i);
                    log.info("保存学校索引 page:{}", page.getCurrent());
                    IPage<School> schoolPage = schoolMapper.selectPage(page, queryWrapper);
                    bulk(schoolPage.getRecords());
                }
            }
            return 1;
        }
    }



    public List<String> getTags() {
        return tags;
    }
}

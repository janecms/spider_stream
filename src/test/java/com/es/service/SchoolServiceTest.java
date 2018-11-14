package com.es.service;

import com.stream.SpiderStreamApplication;
import com.alibaba.fastjson.JSON;
import com.stream.es.model.Hit;
import com.stream.es.model.School;
import com.stream.es.service.SchoolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpiderStreamApplication.class)
public class SchoolServiceTest {
    @Autowired
    SchoolService schoolService;

    @Test
    public void index() {
        String json="     {\n" +
                "        \"province\":\"北京市\",\n" +
                "        \"isHuaYue\":0,\n" +
                "        \"provinceId\":\"1\",\n" +
                "        \"city\":\"北京市\",\n" +
                "        \"isCooperation\":0,\n" +
                "        \"district\":\"西城区\",\n" +
                "        \"name\":\"北京市第三幼儿园\",\n" +
                "        \"id\":\"157\",\n" +
                "        \"districtId\":\"34\",\n" +
                "        \"latLon\":\"39.92998577808,116.39564503788\",\n" +
                "        \"cityId\":\"5001\",\n" +
                "        \"_version_\":1616357466073726976\n" +
                "     }";
        School school = JSON.parseObject(json,School.class);
        boolean flag = schoolService.index(school);
        assertTrue(flag);
    }

    @Test
    public void bulk() {
        String json="     {\n" +
                "        \"province\":\"北京市\",\n" +
                "        \"isHuaYue\":0,\n" +
                "        \"provinceId\":\"1\",\n" +
                "        \"city\":\"北京市\",\n" +
                "        \"isCooperation\":0,\n" +
                "        \"district\":\"西城区\",\n" +
                "        \"name\":\"北京市第三幼儿园1\",\n" +
                "        \"id\":\"1570\",\n" +
                "        \"districtId\":\"34\",\n" +
                "        \"latLon\":\"39.92998577808,116.39564503788\",\n" +
                "        \"cityId\":\"5001\",\n" +
                "        \"_version_\":1616357466073726976\n" +
                "     }";
        School school = JSON.parseObject(json,School.class);
        boolean flag = schoolService.bulk(Arrays.asList(school));
        assertTrue(false);
    }
    @Test
    public void query(){
        School school = new School();
        school.setCity("北京市");
        Hit query = schoolService.query(1,10,school);
        assertTrue(query.getTotal()>1);
    }

    @Test
    public void dux(){
        final School school = schoolService.get("1397");
        schoolService.duSchool(school,0.2f);
    }


    @Test
    public void reindex(){
        schoolService.reindex();
    }

    @Test
    public void name(){
        schoolService.schoolName();
    }
}
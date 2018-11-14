package com.stream.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stream.SpiderStreamApplication;
import com.stream.es.model.School;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpiderStreamApplication.class)
public class SchoolMapperTest {
    @Autowired
    SchoolMapper schoolMapper;

    @Autowired
    JedisCluster jedisCluster;
    @Test
    public void select(){
        IPage<School> page = new Page();
        School school = new School();
        school.setCityId(null);
        school.setDistrictId(null);
        school.setProvinceId(null);
        QueryWrapper<School> queryWrapper = new QueryWrapper<>(school);
        page= schoolMapper.selectPage(page,queryWrapper);
        assertNotNull(page.getRecords());
    }
@Test
    public void test() throws IOException {
    Set<String> zhen_xian1 = jedisCluster.smembers("zhen_xian");
    File file = new File("zhen_xian.txt");
    if (!file.exists()){
        file.createNewFile();
    }
    System.out.println(file.getAbsolutePath());
    FileOutputStream fos = new FileOutputStream(file);
    System.out.println(zhen_xian1.size());
    for (String name:zhen_xian1){
        if (StringUtils.length(name)>1){
            IOUtils.write(name,fos);
        }
    }
    IOUtils.closeQuietly(fos);
    }
}
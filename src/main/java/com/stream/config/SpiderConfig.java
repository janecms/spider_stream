package com.stream.config;

import com.alibaba.fastjson.support.spring.FastJsonpResponseBodyAdvice;
import com.stream.spider.dao.CommonWebpageDAO;
import com.stream.spider.dao.CommonWebpagePipeline;
import com.stream.spider.dao.SpiderInfoDAO;
import com.stream.spider.gather.async.TaskManager;
import com.stream.spider.gather.commons.CasperjsDownloader;
import com.stream.spider.gather.commons.CommonSpider;
import com.stream.spider.gather.commons.ContentLengthLimitHttpClientDownloader;
import com.stream.spider.utils.HANLPExtractor;
import com.stream.spider.utils.StaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.net.BindException;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/11/4/004.
 */
@Configuration
public class SpiderConfig {
    @Autowired
    StaticValue staticValue;
    @Autowired
    TaskManager taskManager;
    @Autowired
    CommonWebpageDAO commonWebpageDAO;
    @Autowired
    SpiderInfoDAO spiderInfoDAO;
    @Autowired
    CommonWebpagePipeline commonWebpagePipeline;
    @Autowired
    ContentLengthLimitHttpClientDownloader contentLengthLimitHttpClientDownloader;
    @Autowired
    HANLPExtractor hanlpExtractor;
    @Autowired
    CasperjsDownloader casperjsDownloader;

    @Bean("commonSpider")
    CommonSpider newCommonSpider() {
        try {
            CommonSpider commonSpider = new CommonSpider(taskManager, staticValue);
            commonSpider.setCommonWebpageDAO(commonWebpageDAO);
            commonSpider.setSpiderInfoDAO(spiderInfoDAO);
            commonSpider.setCommonWebpagePipeline(commonWebpagePipeline);
            commonSpider.setContentLengthLimitHttpClientDownloader(contentLengthLimitHttpClientDownloader);
            commonSpider.setKeywordsExtractor(hanlpExtractor);
            commonSpider.setSummaryExtractor(hanlpExtractor);
            commonSpider.setNamedEntitiesExtractor(hanlpExtractor);
            commonSpider.setCasperjsDownloader(casperjsDownloader);
            commonSpider.setPipelineList(Arrays.asList(commonWebpagePipeline));
            return commonSpider;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BindException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {
        return new SchedulerFactoryBean();
    }

    @Bean
    public FastJsonpResponseBodyAdvice fastJson() {
        return new FastJsonpResponseBodyAdvice("callback", "jsonp");
    }
}

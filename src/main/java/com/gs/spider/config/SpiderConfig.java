package com.gs.spider.config;

import com.gs.spider.dao.CommonWebpageDAO;
import com.gs.spider.dao.CommonWebpagePipeline;
import com.gs.spider.dao.SpiderInfoDAO;
import com.gs.spider.gather.async.TaskManager;
import com.gs.spider.gather.commons.CasperjsDownloader;
import com.gs.spider.gather.commons.CommonSpider;
import com.gs.spider.gather.commons.ContentLengthLimitHttpClientDownloader;
import com.gs.spider.utils.HANLPExtractor;
import com.gs.spider.utils.StaticValue;
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
    public SchedulerFactoryBean scheduler(){
        return new SchedulerFactoryBean();
    }

}

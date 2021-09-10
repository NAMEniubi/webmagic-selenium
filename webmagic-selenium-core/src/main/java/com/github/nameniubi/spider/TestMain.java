package com.github.nameniubi.spider;

import cn.hutool.log.StaticLog;
import com.github.nameniubi.spider.pipeline.FeijingItemApiPipeline;
import com.github.nameniubi.spider.processors.FeijingItemApiProcessor;
import com.github.nameniubi.spider.selenium.TestQueueScheduler;
import us.codecraft.webmagic.Spider;
import com.github.nameniubi.spider.selenium.SeleniumDownloader;
import java.util.Properties;

/**
 * @Description:
 * @Author: Yangf
 * @Create: 2021-09-10 14:31
 */

public class TestMain {
    private static String URL = "https://v1.feijing88.com/doc/list?productId=29&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6ImNzVmx2WnlkUDJqa05iRkFyeE1vdTNhbjJTZEpEdEpRMFdXaWhRTkdxeHFHWkRGbUdCMlZVajNaUFFyM1hLUTgiLCJpYXQiOjE2MzA1NjE3MzJ9.i8sofa-g8twHi-rj3NeJzfIqXG_zlvNivwCZGNaQa2o";

    public static void main(String[] args) {
        StaticLog.info("爬虫启动。。。。");
        String chromeDriverPath = "D:\\tool\\chromedriver.exe";
        String chromePath = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrone.exe";

        Properties properties = new Properties();
        properties.setProperty("webdriver.chrome.driver", chromeDriverPath);
        properties.setProperty("webdriver.chrome.path", chromePath);

        FeijingItemApiProcessor feijingItemApiProcessor = new FeijingItemApiProcessor();
        //FeijingItemApiProcessor feijingItemApiProcessor = new FeijingItemApiProcessor();
        Spider spider = Spider.create(feijingItemApiProcessor).thread(2)
                .addUrl(URL)
                .setScheduler(new TestQueueScheduler())
                .addPipeline(new FeijingItemApiPipeline())
                .setDownloader(new SeleniumDownloader(properties).operWebDriver(feijingItemApiProcessor).setSleepTime(2));
        spider.run();
    }
}
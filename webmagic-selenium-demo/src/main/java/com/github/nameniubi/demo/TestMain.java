package com.github.nameniubi.demo;

import cn.hutool.log.StaticLog;
import com.github.nameniubi.spider.cofing.DriverType;
import com.github.nameniubi.spider.cofing.SeleniumProperties;
import com.github.nameniubi.spider.selenium.TestQueueScheduler;
import us.codecraft.webmagic.Spider;
import com.github.nameniubi.spider.selenium.SeleniumDownloader;

import com.github.nameniubi.demo.processors.FeijingItemApiProcessor;
import com.github.nameniubi.demo.pipeline.FeijingItemApiPipeline;
/**
 * @Description:
 * @Author: Yangf
 * @Create: 2021-09-10 14:31
 */

public class TestMain {
//    private static String URL = "https://v1.feijing88.com/doc/list?productId=29&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6ImNzVmx2WnlkUDJqa05iRkFyeE1vdTNhbjJTZEpEdEpRMFdXaWhRTkdxeHFHWkRGbUdCMlZVajNaUFFyM1hLUTgiLCJpYXQiOjE2MzA1NjE3MzJ9.i8sofa-g8twHi-rj3NeJzfIqXG_zlvNivwCZGNaQa2o";
    private static String URL = "https://view.lixingyong.com/category/Illustration";

    public static void main(String[] args) {
        StaticLog.info("爬虫启动。。。。");
        SeleniumProperties seleniumProperties = new SeleniumProperties(){{
            setDriver(DriverType.Chrome);
//            setDriverPath("D:\\develop\\project\\PyCharm\\chromedriver.exe");
//            setAppPath("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
            setDriverPath("D:\\tool\\chromedriver.exe");
            setAppPath("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrone.exe");
            setHeadless(false);
        }};
        FeijingItemApiProcessor feijingItemApiProcessor = new FeijingItemApiProcessor();
        Spider spider = Spider.create(feijingItemApiProcessor).thread(2)
                .addUrl(URL)
                .setScheduler(new TestQueueScheduler())
                .addPipeline(new FeijingItemApiPipeline())
                .setDownloader(new SeleniumDownloader(seleniumProperties).operWebDriver(feijingItemApiProcessor).setSleepTime(2));
        spider.run();
    }
}
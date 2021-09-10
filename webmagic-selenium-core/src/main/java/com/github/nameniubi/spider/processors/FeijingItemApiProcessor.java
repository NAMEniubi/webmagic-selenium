package com.github.nameniubi.spider.processors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @Description: 飞鲸单页api调用
 * @Author: Yangf
 * @Create: 2021-09-08 18:12
 */

public class FeijingItemApiProcessor implements PageProcessor, BiConsumer<WebDriver,Page> {


    private Site site;
    private List<List<Object>> splitList = null;
    //生成md5对返回框内容进行比对
    private String md5 = null;

    private int index = 0;

    @Override
    public void process(Page page) {

        if (index < splitList.size() - 1){
            String currUrl = page.getUrl().get();
            page.addTargetRequest(currUrl);
        }
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me().setDomain("feijing88.com").setSleepTime(1000);
        }
        return site;
    }

    @Override
    public void accept(WebDriver webDriver, Page page) {
        RemoteWebDriver driver = (RemoteWebDriver) webDriver;
        btnXmlToJson(driver);
        checkPageContent(driver,0,"球员资料");
        saveJson(driver,page);
    }


    /**
     * 获取json接口数据 保存
     * @param driver
     * @param page
     */
    private void saveJson(RemoteWebDriver driver,Page page){
        WebElement runBtn = driver.findElementByClassName("btn-run");
        WebElement result = driver.findElementByClassName("result-data");
        WebElement container = driver.findElementByXPath("//*[@id=\"__layout\"]/div/div[2]/div[2]/div[4]");
        WebElement input = container.findElement(new By.ByTagName("input"));
        if (null == splitList){
            String header = driver.findElementByClassName("document-header").getText().trim();
            StaticLog.info("当前在 {},开始请求数据。" , header);
            input.clear();
            input.sendKeys("http://interface.feijing88.com/football/player.aspx?cmd=teamlist");
            runBtn.click();
            while (StrUtil.isBlank(result.getText())){};
            JSONObject json = JSONUtil.parseObj(result.getText());
            JSONArray array = json.getJSONArray("teamIDList");
            StaticLog.info("以获取到ID ：{} 条",array.size());
            splitList = CollUtil.split(array, 8);

        }else {
            input.clear();
            String idStr = CollUtil.join(splitList.get(index), ",");
            input.sendKeys("http://interface.feijing88.com/football/player.aspx?teamId="+idStr);
            runBtn.click();
            //MD5相同说明返回框内容没有变化
            while ( StrUtil.isBlank(result.getText()) || SecureUtil.md5().digestHex(result.getText()).equals(md5)) {}
            JSONObject resJson = null;
            try {
                resJson = JSONUtil.parseObj(result.getText());
            }catch (JSONException e){
                StaticLog.error("json序列化错误 : {}" ,result.getText());
                return;
            }
            page.putField("FootBallPlayer",resJson.getJSONArray("playerList"));
            StaticLog.info("当前完成进度： {} / {}" ,index,splitList.size());
            index++;
        }
    }
    /**
     * 确保文档格式为json
     * @param webDriver
     */
    private void btnXmlToJson(WebDriver webDriver){
        WebElement btn = webDriver.findElement(new By.ByClassName("param-btn"));
        if (!btn.getText().contains("JSON")){
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
            btn.click();
            ThreadUtil.sleep(1,TimeUnit.SECONDS);
            List<WebElement> paramItem = webDriver.findElements(new By.ByClassName("param-item"));
            for (WebElement e : paramItem) {
                if (e.getText().contains("JSON")){
                    e.click();
                    break;
                }
            }
        }
    }

    /**
    *@description:
    *@param  navIndex 第几个导航
    *@param  pageName 文档名称
    *@return
    *@author: yangf
    *@date: 2021/9/10 10:45
    */
    private void checkPageContent(RemoteWebDriver driver,Integer navIndex,String pageName){
        String header = driver.findElementByClassName("document-header").getText().trim();
        if (!header.contains(pageName)){
            //切换到球员资料页面
            driver.findElementsByClassName("product-name").get(navIndex).click();
            List<WebElement> elementList = driver.findElementsByXPath("//span[@class='inline-block']");
            for (WebElement element : elementList) {
                if (element.getText().contains("球员资料")){
                    element.click();
                    break;
                }
            }
        }
    }
}
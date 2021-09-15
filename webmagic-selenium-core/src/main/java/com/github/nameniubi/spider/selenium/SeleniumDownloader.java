package com.github.nameniubi.spider.selenium;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.github.nameniubi.spider.cofing.DriverType;
import com.github.nameniubi.spider.cofing.SeleniumProperties;
import com.sun.javafx.fxml.PropertyNotFoundException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
*@description: 集成webmagic和selenium
 * 使用Selenium调用浏览器进行渲染。目前仅支持chrome。<br>
*@return
*@author: yangf
*@date: 2021/9/9 10:54
*/
public class SeleniumDownloader implements Downloader, Closeable {

    private volatile WebDriverPool webDriverPool;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int sleepTime = 0;

    private int poolSize = 1;

    protected SeleniumProperties properties ;

    private BiConsumer consumerFun;


    /**
    *@description: 下载器初始化，参数配置检查
    *@param
    *@return SeleniumDownloader
    *@author: yangf
    *@date: 2021/9/9 11:24
    */
    public SeleniumDownloader( final SeleniumProperties properties) {
        if (null == properties){
            throw new PropertyNotFoundException("properties配置为空");
        }
        this.properties = properties;
        if (DriverType.Chrome.compareTo(properties.getDriver()) == 0){
            if (StrUtil.isBlank(properties.getDriverPath())){
                throw new PropertyNotFoundException("未配置chrome驱动,需要下载chrome驱动并配置,下载地址： http://npm.taobao.org/mirrors/chromedriver/");
            }
            if (properties.getDriverPath().startsWith("http")){

            }else if (StrUtil.isBlank(properties.getAppPath())){
                throw new PropertyNotFoundException("未配置chrome程序路径, 列：C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
            }else {
                System.setProperty("webdriver.chrome.driver", properties.getDriverPath());
            }
        }else {
            throw new PropertyNotFoundException("目前只支持chrome");
        }
    }

    /**
    *@description: 增强方法，在页面渲染完整后，调用Processor之前执行此方法。
     * 可在此方法内完成按钮点击、表单提交 等操作，来改变页面渲染结果。
     * 注意：执行次函数时 page对象中没有任何参数；
    *@param
    *@return
    *@author: yangf
    *@date: 2021/9/9 14:15
    */
    public SeleniumDownloader operWebDriver(BiConsumer<WebDriver,Page> consumer){
        consumerFun = consumer;
        return this;
    }


    public SeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    /**
    *@description: 使用Selenium加载页面
    *@param  [request, task]
    *@return us.codecraft.webmagic.Page
    *@author: yangf
    *@date: 2021/9/9 12:04
    */
    public Page download(Request request, Task task) {
        checkInit();
        WebDriver webDriver;
        try {
            webDriver = webDriverPool.get(properties);
        } catch (InterruptedException e) {
            logger.warn("interrupted", e);
            return null;
        }
        if (!request.getUrl().equals(webDriver.getCurrentUrl())){
            logger.info("downloading page " + request.getUrl());
            webDriver.get(request.getUrl());
            ThreadUtil.sleep(sleepTime, TimeUnit.SECONDS);
        }
        Page page = new Page();
        if (null != consumerFun){
            consumerFun.accept(webDriver,page);
        }
        WebDriver.Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies()
                    .entrySet()) {
                Cookie cookie = new Cookie(cookieEntry.getKey(),
                        cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }
//        WebElement webElement = webDriver.findElement(By.xpath("/html"));
//        String content = webElement.getAttribute("outerHTML");
        String content = webDriver.getPageSource();
        page.setRawText(content);
        page.setHtml(new Html(content, request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        webDriverPool.returnToPool(webDriver);
        return page;
    }

    private void checkInit() {
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(poolSize);
            }
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() throws IOException {
        webDriverPool.closeAll();
    }
}
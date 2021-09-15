package com.github.nameniubi.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.StaticLog;
import com.github.nameniubi.spider.cofing.DriverType;
import com.github.nameniubi.spider.cofing.SeleniumProperties;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Yangf
 * @Create: 2021-09-15 13:04
 */
public class SeleniumSpider {
    public static void main(String[] args) {
        StaticLog.info("爬虫启动。。。。");
        SeleniumProperties seleniumProperties = new SeleniumProperties(){{
            setDriver(DriverType.Chrome);
            setDriverPath("D:\\tool\\chromedriver.exe");
            setAppPath("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrone.exe");
            setHeadless(true);
        }};

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(seleniumProperties.getAppPath());
        if (seleniumProperties.isHeadless()){
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
        }
        System.setProperty("webdriver.chrome.driver", seleniumProperties.getDriverPath());

        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().window().setSize(new Dimension(1920,1080));
        chromeDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        imageSpider(chromeDriver);
        chromeDriver.close();
    }
    static int totle = 0;
    static int i = 0;
    private static List<String> list = new ArrayList<>();
    private static LinkedList<String> categoryList = new LinkedList<String>(){
        {
            add("其他");
        }
    };

    private String category ;
    public static void imageSpider(ChromeDriver driver){
        //driver.get("https://view.lixingyong.com/category/Illustration");
        driver.get("https://view.lixingyong.com/category/movie");
        List<WebElement> box = driver.findElementsByClassName("list-item-image");
        if (box.size()< 1){
            StaticLog.error("box为空");
            return;
        }
        for (String categoryName : categoryList) {
            driver.findElementByClassName("icon-stack").click();
            String attribute = driver.findElementByLinkText("categoryName").getAttribute("href");
        }

//        totle = box.size();
//        box.get(0).click();
//        imageSpiderNext(driver);
//        list.forEach(System.out::println);
        //FileUtil.appendUtf8Lines()



    }

    public static void imageSpiderNext(ChromeDriver driver){
        List<WebElement> elements = driver.findElementsByClassName("viewer-src");
        list.add(elements.get(0).getAttribute("src"));
        WebElement next = driver.findElementByClassName("icon-uniE8A9");
        if (null != next) {
            System.out.println(i++ +" / " +totle);
            try {
                next.click();
                imageSpiderNext(driver);
            }catch (ElementNotInteractableException e){
                System.err.println("结束");
            }

        }
    }

}
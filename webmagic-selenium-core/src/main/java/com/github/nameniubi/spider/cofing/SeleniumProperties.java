package com.github.nameniubi.spider.cofing;

public class SeleniumProperties {

    /**
     * 驱动类型
     */
    private DriverType driver;
    /**
     * 驱动程序路径
     */
    private String driverPath;
    /**
     * 执行程序路径
     */
    private String appPath;
    /**
     * 开启无界面模式
     */
    private boolean isHeadless;

    public SeleniumProperties() {
        this.driver = DriverType.Chrome;
    }

    public DriverType getDriver() {
        return driver;
    }

    public void setDriver(DriverType driver) {
        this.driver = driver;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setDriverPath(String driverPath) {
        this.driverPath = driverPath;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }


    public boolean isHeadless() {
        return isHeadless;
    }

    public void setHeadless(boolean headless) {
        isHeadless = headless;
    }
}

# webmagic-selenium
由于webmagic无法满足一些特定的场景需求而诞生此项目，与selenium进行集成
目前只支持Chrome浏览器，其他浏览器还在实现中

- 在selenium下载器中增加了operWebDriver方法，可在页面加载后直接操作WebDriver
- 增加了无界面运行模式的配置

demo模块演示了一个文档数据的爬取配置
将coren模块install 可直接运行demo
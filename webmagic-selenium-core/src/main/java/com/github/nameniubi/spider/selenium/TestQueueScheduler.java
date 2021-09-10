package com.github.nameniubi.spider.selenium;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;
import java.util.LinkedList;


/**
 * @Description: 避免URL去重，临时实现的调度器
 * @Author: Yangf
 * @Create: 2021-09-09 19:31
 */
public class TestQueueScheduler implements Scheduler {
    LinkedList<Request> list = new LinkedList<>();
    @Override
    public void push(Request request, Task task) {

        list.push(request);
    }

    @Override
    public Request poll(Task task) {
        return list.poll();
    }
}
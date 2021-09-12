package com.github.nameniubi.demo.pipeline;


import cn.hutool.log.StaticLog;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import java.util.List;

/**
 * @Description: 足球队员数据处理器
 * @Author: Yangf
 * @Create: 2021-09-09 16:31
 */

public class FeijingItemApiPipeline implements Pipeline {



    @Override
    public void process(ResultItems resultItems, Task task) {
        if (null != resultItems.get( "FootBallPlayer" )){
            List<Object> playerList = resultItems.get("FootBallPlayer");
            StaticLog.info("Pipeline 收到数据：{} 条",playerList.size());
            //数据入库
            //footBallPlayerService.insertBatchAsync(playerList);
        }
    }
}
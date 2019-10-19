package com.zhuodewen.www.controller;

import com.zhuodewen.www.domain.Goods;
import com.zhuodewen.www.service.GoodsService;
import com.zhuodewen.www.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品的controller
 */
@Controller
@RequestMapping("goods")
public class GoodController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RabbitMqService rabbitMqService;


    @RequestMapping(value = "selectById",method = RequestMethod.GET)
    @ResponseBody
    public Goods selectById(int id){
        return goodsService.selectById(id);
    }

    /**
     * 测试rabbitMq--消息发送者
     * @param id
     * @return
     */
    @RequestMapping(value = "selectByRabbitMq",method = RequestMethod.GET)
    @ResponseBody
    public String selectByRabbitMq(int id){
        Message build=MessageBuilder.withPayload(id).build();
        Boolean b=rabbitMqService.sendMessage().send(build);       //接口接收须是SubscribableChannel
        return b.toString();
    }

}

package com.zhuodewen.www.controller;

import com.zhuodewen.www.domain.Goods;
import com.zhuodewen.www.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * 订单的controller
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 测试使用ribbon调用
     * @param id
     * @return
     */
    @RequestMapping(value = "selectById",method = RequestMethod.GET)
    @ResponseBody
    public Goods selectById(int id){
        return restTemplate.getForObject("http://eureka-good/goods/selectById?id="+id,Goods.class);
    }

    /**
     * 测试使用feign调用(在接口内)
     * @param id
     * @return
     */
    @RequestMapping(value = "selectById2",method = RequestMethod.GET)
    @ResponseBody
    public Goods selectById2(int id){
        return orderService.selectById2(id);
    }

    /**
     * rabbitMq--消息消费者
     */
    //@StreamListener(Sink.INPUT)
	@StreamListener("rabbit")
	public void receiveMq(int id) {
		orderService.selectById2(id);
        System.out.println("RabbitMq测试成功=======================================================");
    }

}

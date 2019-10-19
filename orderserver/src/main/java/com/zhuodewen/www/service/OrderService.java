package com.zhuodewen.www.service;

import com.zhuodewen.www.domain.Goods;
import com.zhuodewen.www.service.impl.HystrixServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单的service接口(测试feign调用)
 */

@FeignClient(name="eureka-good",fallback = HystrixServiceImpl.class)    //name--对应提供者的应用名,fallback--熔断后对应的实现类(必须实现该接口)
//熔断器测试(方式二:配合Feign使用,在feign对应接口上的@FeignClient注解上,添加属性fallback,指定熔断时对应的实现类--消费端)
public interface OrderService {

    //feign--对应提供者controller接口
    @RequestMapping(value = "/goods/selectById", method = RequestMethod.GET)
    public Goods selectById2(@RequestParam(value = "id")int  id);

}

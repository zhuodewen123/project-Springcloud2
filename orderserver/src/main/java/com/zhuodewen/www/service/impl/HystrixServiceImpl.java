package com.zhuodewen.www.service.impl;

import com.zhuodewen.www.domain.Goods;
import com.zhuodewen.www.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * 定义orderService接口熔断后所调用的类及方法(对应名称的方法,就是熔断后所调用的方法)
 * 该类必须实现feign接口
 */
@Service
public class HystrixServiceImpl implements OrderService {

    public Goods selectById2(int id) {
        Goods good=new Goods();
        good.setGoodName("配合Feign的FeignClient的fallback属性,进行熔断");
        return good;
    }
}

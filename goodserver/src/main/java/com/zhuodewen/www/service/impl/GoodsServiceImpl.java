package com.zhuodewen.www.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhuodewen.www.domain.Goods;
import com.zhuodewen.www.mapper.GoodsMapper;
import com.zhuodewen.www.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品服务的实现类
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    //@HystrixCommand(fallbackMethod = "fault")       //熔断器测试(方式一:在"提供者"具体的业务方法上,使用@HystrixCommand(fallbackMethod = "xxx")注解进行熔断)
    public Goods selectById(int  id) {
        //int b=1/0;                                    //模拟异常--模拟调用接口失败
        Goods good=goodsMapper.selectByPrimaryKey(id);
        return good;
    }

    /**
     * 熔断器--测试调用失败后的备选方法
     * @param id
     * @return
     */
    public Goods fault(int id){
        Goods good=new Goods();
        good.setGoodName("使用@HystrixCommand熔断");
        return good;
    }


}

package com.zhuodewen.www.controller;

import com.zhuodewen.www.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {

    /*@Autowired
    private RestTemplate restTemplate;*/

    /*@RequestMapping(value = "selectAll",method = RequestMethod.GET)
    public List<Goods> selectAll(){

        return restTemplate.getForObject("http://GOODS-SERVICE/selectAll",Goods.class);
    }*/

}

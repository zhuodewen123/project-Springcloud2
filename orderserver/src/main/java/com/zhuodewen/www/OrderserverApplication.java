package com.zhuodewen.www;

import com.zhuodewen.www.service.RabbitMqService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Import(CommonApplication.class)
@PropertySource("classpath:application-order.properties")	//配置文件需改名,因为和引入的common模块冲突了
@EnableEurekaClient											//Eureka的消费端(SpringCloud消费者)
@MapperScan("com.zhuodewen.www.mapper") 					//mapper扫描器
@EnableFeignClients											//开启feign(调用)
@EnableHystrix                                              //熔断器
@EnableBinding(value={RabbitMqService.class})				//rabbitMq
public class OrderserverApplication {

	/**
	 * ribbon消费端,需要注入RestTemplate对象并开启负载均衡
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * rabbitMq--消息消费者
	 */
	//@StreamListener(Sink.INPUT)
	/*@StreamListener("rabbit")
	public void receiveMq(int id) {
		orderService.selectById2(id);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(OrderserverApplication.class, args);
	}

}

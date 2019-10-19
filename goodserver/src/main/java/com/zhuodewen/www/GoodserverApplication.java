package com.zhuodewen.www;

import com.zhuodewen.www.service.RabbitMqService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import(CommonApplication.class)
@PropertySource("classpath:application-good.properties")//配置文件需改名,因为和引入的common模块冲突了
@EnableDiscoveryClient                                  //Zookeeper的消费端(SpringCloud生产者)
@MapperScan("com.zhuodewen.www.mapper") 				//mapper扫描器
@EnableHystrix											//熔断器
@EnableTransactionManagement							//事务管理器
@EnableBinding(value= {RabbitMqService.class})			//开启RabbitMQ,注明接口类
public class GoodserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodserverApplication.class, args);
	}

}

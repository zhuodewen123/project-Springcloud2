package com.zhuodewen.www;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import(CommonApplication.class)
@PropertySource("classpath:application-member.properties") //配置文件需改名,因为和引入的common模块冲突了
@EnableEurekaClient                                        //Eureka的消费端(SpringCloud生产者)
@EnableHystrix                                             //熔断器
@MapperScan("com.zhuodewen.www.mapper") 				   //mapper扫描器
@EnableTransactionManagement                               //事务管理器
public class MemberserverApplication {

	/**
	 * gateway网关配置(配置方式二:代码配置,还有一种是properties/yml配置)
	 * @param builder
	 * @return
	 */
	/*@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/qq*//**")
						.and().uri("http://www.qq.com/"))
				.build();
	}*/

	public static void main(String[] args) {
		SpringApplication.run(MemberserverApplication.class, args);
	}

}

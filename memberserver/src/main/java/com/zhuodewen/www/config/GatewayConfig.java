package com.zhuodewen.www.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gateway网关配置(配置方式二:代码配置,还有一种是properties/yml配置)
 * @return
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                //第一个
                .route(r -> r.path("/qq/**")
                        .and().uri("http://www.qq.com/")
                )

                //第二个(以此类推)
                .route(r -> r.path("/xx/**")
                        .and().uri("http://www.qxueyou.com/pc/")
                )

                //最后才是build
                .build();
    }
}

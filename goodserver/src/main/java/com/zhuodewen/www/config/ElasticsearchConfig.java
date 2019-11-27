package com.zhuodewen.www.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import java.util.ArrayList;

/**
 * Elasticsearch客户端工具类
 */
public class ElasticsearchConfig {

    private static String hosts = "127.0.0.1";              // 集群地址，多个用,隔开
    private static int port = 9200;                         // 使用的端口号
    private static String schema = "http";                  // 使用的协议

    private static ArrayList<HttpHost> hostList = null;
    private static int connectTimeOut = 1000;               // 连接超时时间
    private static int socketTimeOut = 30000;               // socket连接超时时间
    private static int connectionRequestTimeOut = 500;      // 获取连接的超时时间
    private static int maxConnectNum = 100;                 // 最大连接数
    private static int maxConnectPerRoute = 100;            // 最大路由连接数

    private static RestClientBuilder builder;
    private static RestHighLevelClient restHighLevelClient; //RestHighLevelClient对象,单例模式

    /**
     * 静态代码块,初始化ES集群的ip
     */
    static {
        hostList = new ArrayList<>();
        String[] hostStrs = hosts.split(",");
        for (String host : hostStrs) {
            hostList.add(new HttpHost(host, port, schema));
        }
        builder = RestClient.builder(hostList.toArray(new HttpHost[0]));

        // 异步httpclient连接延时配置
        setConnectTimeOutConfig(builder);

        // 异步httpclient连接数配置
        setConnectTimeOutConfig(builder);

        restHighLevelClient= new RestHighLevelClient(builder);
    }

    /**
     * 对外提供获取RestHighLevelClient对象的方法(单例模式)
     * @return
     */
    public static RestHighLevelClient getHighLevelClient(){
        return restHighLevelClient;
    }

    // 异步httpclient连接延时配置
    public static void setConnectTimeOutConfig(RestClientBuilder builder){
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });
    }

    // 主要关于异步httpclient的连接数配置
    public static void setMutiConnectConfig(RestClientBuilder builder){
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            }
        });
    }


}
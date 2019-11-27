package com.zhuodewen.www.config;

import com.zhuodewen.www.domain.Student;
import org.elasticsearch.action.get.GetResponse;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * 自定义线程(第三种线程实现方式--实现Callable<V>,该方式有返回值,V为返回类型)
 */
public class MyThread3 implements Callable<GetResponse> {

    //线程的共享数据(重点)
    private Student  student;

    private String index;

    //自定义线程的构造器
    public MyThread3(Student student,String index) {
        this.student=student;   //传入的数据
        this.index=index;
    }

    //自定义线程的操作方法
    public GetResponse call() {
        GetResponse getResponse=null;
        synchronized (this) {
            try {
                // 业务:获取Elasticsearch记录(主要业务)
                getResponse=ElasticsearchUtil.get(index, index, student.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getResponse;
    }
}


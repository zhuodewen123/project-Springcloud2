package com.zhuodewen.www.config;

import com.zhuodewen.www.domain.Student;
import java.io.IOException;

/**
 * 自定义线程(第一种线程实现方式--继承Thread)
 */
public class MyThread extends Thread {

    //线程的共享数据(重点)
    private Student  student;

    private String INDEX="student";

    //自定义线程的构造器
    public MyThread(Student student,String name, int pro) {
        super(name);            //设置线程的名称
        setPriority(pro);       //设置线程的优先级
        this.student=student;   //传入的数据
    }

    //自定义线程的操作方法
    public void run() {
        synchronized (this) {
            try {
                // 业务:增加Elasticsearch记录(主要业务)
                ElasticsearchUtil.add(INDEX, INDEX, student.getId(),student);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


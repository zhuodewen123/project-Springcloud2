package com.zhuodewen.www.thread;

import com.zhuodewen.www.domain.Student;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池测试类
 */
public class ThreadPoolExecutorTest {

    private static final int MAX_PRIORITY=10;                                               //高优先级
    private static final int NORM_PRIORITY=5;                                               //普通优先级
    private static final int MIN_PRIORITY =1;                                               //低优先级
    private static Student student=new Student(10);                                    //共享对象(内含共享数据)

    public static void main(String[] args) throws InterruptedException {
        //创建线程池
        ExecutorService executorService= Executors.newFixedThreadPool(10);         //最多可运行线程数
        MyThread myThread = new MyThread(student,"线程A",NORM_PRIORITY);	            //创建自定义线程类的对象1
        MyThread myThread2 = new MyThread(student,"线程B",NORM_PRIORITY);	            //创建自定义线程类的对象2
        for(int i =0;i<10;i++){
            //为线程池分配任务
            executorService.submit(myThread);	                                            //传入自定义线程类的对象1
            executorService.submit(myThread2);	                                            //传入自定义线程类的对象1
            System.out.println("执行线程:"+(i+1)+",age="+student.getAge());
            student.decrement();
            Thread.currentThread().sleep(1000);
        }
        //关闭线程池
        executorService.shutdown();
    }
}


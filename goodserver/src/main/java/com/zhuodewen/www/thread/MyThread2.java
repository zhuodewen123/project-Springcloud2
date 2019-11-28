package com.zhuodewen.www.thread;

import com.zhuodewen.www.domain.Student;

/**
 * 自定义线程(第二种线程实现方式--实现Runnable)
 */
public class MyThread2 implements Runnable {

    //线程的共享数据(重点)
    private Student  student;

    private String INDEX="student";

    //自定义线程的构造器
    public MyThread2(Student student) {
        this.student=student;   //共享数据
    }

    //自定义线程的操作方法
    public void run() {
        synchronized (this) {
            try {
                //业务:测试共享数据/共享对象
                for(int i =0;i<5;i++){
                    System.out.println("age="+student.getAge());
                    student.decrement();                                        //操作共享数据,age--
                    Thread.currentThread().sleep(500);
                }
                System.out.println("共享对象="+student.getAge()+"=====================================");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


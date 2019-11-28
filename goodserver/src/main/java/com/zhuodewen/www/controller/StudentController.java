package com.zhuodewen.www.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuodewen.www.config.ElasticsearchUtil;
import com.zhuodewen.www.thread.MyThread;
import com.zhuodewen.www.thread.MyThread2;
import com.zhuodewen.www.thread.MyThread3;
import com.zhuodewen.www.domain.Student;
import com.zhuodewen.www.util.JSONResult;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("student")
public class StudentController {

    private static final int MAX_PRIORITY=10;                                               //线程优先级--高
    private static final int NORM_PRIORITY=5;                                               //线程优先级--普通
    private static final int MIN_PRIORITY =1;                                               //线程优先级--低

    /**
     * Elasticsearch添加记录
     * @param index
     * @param student
     */
    @RequestMapping("addES")
    @ResponseBody
    public JSONResult addES(String index, Student student){
        JSONResult js=new JSONResult();
        try {
            // 判断是否存在索引
            if (!ElasticsearchUtil.existsIndex(index)) {
                // 不存在则创建索引
                ElasticsearchUtil.createIndex(index);
            }

            // 判断是否存在记录
            if (!ElasticsearchUtil.exists(index, index,student.getId())) {
                // 不存在增加记录
                ElasticsearchUtil.add(index, index, student.getId(),student);
                js.setMsg("SUCCESS");
            }
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * Elasticsearch删除记录(根据id删除)
     * @param index
     * @param id        ES中该记录的id
     */
    @RequestMapping("deleteES")
    @ResponseBody
    public JSONResult deleteES(String index,Long  id){
        JSONResult js=new JSONResult();
        try {
            // 删除记录
            ElasticsearchUtil.delete(index, index, id);
            js.setMsg("SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * Elasticsearch更新记录(根据id进行更新)
     * @param index
     * @param student   ES中该记录对象(包含id)
     */
    @RequestMapping("updateES")
    @ResponseBody
    public JSONResult updateES(String index,Student student){
        JSONResult js=new JSONResult();
        try {
            // 更新记录
            ElasticsearchUtil.update(index, index, student.getId(),student);
            js.setMsg("SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * Elasticsearch获取记录(根据id获取)
     * @param index
     * @param id        ES中该记录的id
     */
    @RequestMapping("getES")
    @ResponseBody
    public JSONResult getES(String index,Long id){
        JSONResult js=new JSONResult();
        try {
            // 获取记录
            GetResponse getResponse=ElasticsearchUtil.get(index, index, id);
            js.setMsg("SUCCESS");
            //js.setResult(JSON.toJSONString(getResponse.getSource()));     //getSource()方法获取数据--json字符串
            js.setResult(JSON.toJSON(getResponse.getSource()));             //getSource()方法获取数据--json对象
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * Elasticsearch搜索记录
     * @param index
     * @param keyword   搜索关键字
     */
    @RequestMapping("searchES")
    @ResponseBody
    public JSONResult searchES(String index,String keyword){
        JSONResult js=new JSONResult();
        List<Student> list=new ArrayList<>();
        try {
            // 获取记录
            SearchHit[] searchHits=ElasticsearchUtil.search(index, index, keyword);
            for (SearchHit hit : searchHits) {
                //1)将response返回的每一个json对象转为map
                Map map=hit.getSourceAsMap();
                //2)利用fastJson将Map转成实体对象
                Student stu=JSONObject.parseObject(JSONObject.toJSONString(map), Student.class);
                //3)将获取到实体对象存入list集合中
                list.add(stu);
            }
            js.setMsg("SUCCESS");
            js.setResult(list);
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    //Elasticsearch--批量新增/批量删除/批量更新



    /**
     * 增加ES数据
     * 测试多线程处理高并发业务(一"次"业务操作,使用一个线程)
     */
    @RequestMapping("addEsByThread")
    @ResponseBody
    public JSONResult addEsByThread(Student student){
        JSONResult js=new JSONResult();
        try {
            MyThread myThread = new MyThread(student,"线程A",NORM_PRIORITY);	            //创建自定义线程类的对象(业务放在线程内)
            //启动线程(线程run()内含有业务逻辑)
            myThread.start();
            js.setMsg("SUCCESS");
        } catch(Exception e){
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * 测试多线程共享数据(线程池)
     */
    @RequestMapping("testShardDataByThread")
    @ResponseBody
    public JSONResult testShardDataByThread(Student student){                                   //Student对象中的age作为线程的共享数据(测试)
        JSONResult js=new JSONResult();
        try {
            //创建线程池
            ExecutorService executorService= Executors.newFixedThreadPool(10);         //最多可运行线程数(固定线程数)
            MyThread2 myThread2 = new MyThread2(student);	                                    //创建自定义线程类的对象(业务放在线程内)
            Thread thread = new Thread(myThread2);

            //为线程池分配线程任务
            for(int i =0;i<3;i++){
                executorService.submit(thread);	                                                //传入自定义线程类的对象(共享数据时,需要使用同一个线程对象myThread)
            }
            //关闭线程池
            executorService.shutdown();
            js.setMsg("SUCCESS");
        } catch(Exception e){
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }

    /**
     * 获取ES数据
     * 测试多线程处理高并发业务(一"次"业务操作,使用一个线程)
     */
    @RequestMapping("getEsByThread")
    @ResponseBody
    public JSONResult getEsByThread(String index,Student student){
        JSONResult js=new JSONResult();
        try {
            //启动线程(线程run()内含有业务逻辑)
            Callable<GetResponse> callable = new MyThread3(student,index); 	//多态创建Callable实例
            GetResponse getResponse=callable.call();                        //call()启动线程并获取返回值

            js.setMsg("SUCCESS");
            //js.setResult(JSON.toJSONString(getResponse.getSource()));     //getSource()方法获取数据--json字符串
            js.setResult(JSON.toJSON(getResponse.getSource()));             //getSource()方法获取数据--json对象
        } catch(Exception e){
            e.printStackTrace();
            js.setMsg("FALSE");
        }
        return js;
    }


}

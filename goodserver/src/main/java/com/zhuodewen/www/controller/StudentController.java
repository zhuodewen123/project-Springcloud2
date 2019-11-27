package com.zhuodewen.www.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuodewen.www.config.ElasticsearchUtil;
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

@Controller
@RequestMapping("student")
public class StudentController {

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
            js.setResult(JSON.toJSONString(getResponse.getSource()));       //getSource()方法获取数据
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

    //批量新增/删除/更新...
}

package com.zhuodewen.www.config;

import com.alibaba.fastjson.JSON;
import com.zhuodewen.www.domain.Student;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import java.io.IOException;
import java.util.List;

public class ElasticsearchUtil {

    private static RestHighLevelClient restHighLevelClient=ElasticsearchConfig.getHighLevelClient();

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public static boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("existIndex: " + exists);
        return exists;
    }

    /**
     * 创建索引
     * @param index
     * @throws IOException
     */
    public static void createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request,RequestOptions.DEFAULT);
        System.out.println("createIndex: " + JSON.toJSONString(createIndexResponse));
    }

    /**
     * 判断记录是都存在
     * @param index
     * @param type
     * @param id
     * @return
     * @throws IOException
     */
    public static boolean exists(String index, String type, Long id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id+"");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("existRecord: " + exists);
        return exists;
    }

    /**
     * 增加记录
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public static void add(String index, String type, Long id,Object obj) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index, type, id+"");
        indexRequest.source(JSON.toJSONString(obj), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("add: " + JSON.toJSONString(indexResponse));
    }

    /**
     * 删除记录
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public static void delete(String index, String type, Long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id.toString());
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("delete: " + JSON.toJSONString(response));
    }

    /**
     * 更新记录信息
     * @param index
     * @param type
     * @param id
     * @param obj
     * @throws IOException
     */
    public static void update(String index, String type, Long id, Object obj) throws IOException {
        UpdateRequest request = new UpdateRequest(index, type, id+"");
        request.doc(JSON.toJSONString(obj), XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println("update: " + JSON.toJSONString(updateResponse));
    }

    /**
     * 获取记录信息
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public static GetResponse get(String index, String type, Long id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id.toString());
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println("get: " + JSON.toJSONString(getResponse.getSource()));
        return getResponse;
    }

    /**
     * 搜索
     * @param index
     * @param type
     * @param name
     * @throws IOException
     */
    public static SearchHit[] search(String index, String type, String name) throws IOException {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.matchQuery("name", name));       // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        // boolBuilder.must(QueryBuilders.matchQuery("id", tests.getId().toString()));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(100);                                            // 获取记录数，默认10
        sourceBuilder.fetchSource();                                        // 获取全部字段
        //sourceBuilder.fetchSource(new String[] { "id", "name" }, new String[] {}); // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("search: " + JSON.toJSONString(response));
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println("search -> " + hit.getSourceAsString());
        }
        return searchHits;
    }

    /**
     * 批量增加学生
     * @throws IOException
     */
    public static void bulkAdd(String index,List<Object> list) throws IOException {
        // 批量增加
        BulkRequest bulkAddRequest = new BulkRequest();
        Student stu=new Student();
        for (int i = 0; i < list.size(); i++) {
            stu= (Student) list.get(i);
            IndexRequest indexRequest = new IndexRequest(index, index, stu.getId().toString());
            indexRequest.source(JSON.toJSONString(stu), XContentType.JSON);
            bulkAddRequest.add(indexRequest);
        }
        BulkResponse bulkAddResponse = restHighLevelClient.bulk(bulkAddRequest, RequestOptions.DEFAULT);
        System.out.println("bulkAdd: " + JSON.toJSONString(bulkAddResponse));
        search(index, index, "this");
    }

    /**
     * 批量更新学生
     * @throws IOException
     */
    public static void bulkUpdate(String index,List<Object> list) throws IOException {
        // 批量更新
        BulkRequest bulkUpdateRequest = new BulkRequest();
        Student stu=new Student();
        for (int i = 0; i < list.size(); i++) {
            stu = (Student) list.get(i);
            UpdateRequest updateRequest = new UpdateRequest(index, index, stu.getId().toString());
            updateRequest.doc(JSON.toJSONString(stu), XContentType.JSON);
            bulkUpdateRequest.add(updateRequest);
        }
        BulkResponse bulkUpdateResponse = restHighLevelClient.bulk(bulkUpdateRequest, RequestOptions.DEFAULT);
        System.out.println("bulkUpdate: " + JSON.toJSONString(bulkUpdateResponse));
        search(index, index, "updated");
    }

    /**
     * 批量删除学生(Long类型的id集合)
     * @throws IOException
     */
    public static void bulkDelete(String index,List<Long> list) throws IOException {
        // 批量删除
        BulkRequest bulkDeleteRequest = new BulkRequest();
        for (int i = 0; i < list.size(); i++) {
            DeleteRequest deleteRequest = new DeleteRequest(index, index, list.get(i).toString());
            bulkDeleteRequest.add(deleteRequest);
        }
        BulkResponse bulkDeleteResponse = restHighLevelClient.bulk(bulkDeleteRequest, RequestOptions.DEFAULT);
        System.out.println("bulkDelete: " + JSON.toJSONString(bulkDeleteResponse));
        search(index, index, "this");
    }
}

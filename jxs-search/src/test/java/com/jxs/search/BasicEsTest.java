package com.jxs.search;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicEsTest {

    @Autowired
    private RestHighLevelClient client;


    /**
     * @throws IOException 创建索引
     *                     PUT /users/_doc/1
     *                     {
     *                     “userName”;"zhangsan",
     *                     "password":"1234",
     *                     "age":12,
     *                     "createTime":1614735524148
     *                     }
     */
    @Test
    public void putDataTest() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("1234");
        user.setCreateTime(new Date());
        user.setAge(12);
        request.source(JSONObject.toJSONString(user), XContentType.JSON);
        //设置超时时间
        request.timeout(TimeValue.timeValueSeconds(1));
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
        client.close();

    }


    /**
     * @throws IOException 根据索引 +id 查询数据
     */
    @Test
    public void getDataTest() throws IOException {

        GetRequest request = new GetRequest(
                "users",
                "1");
        //设置查询那些字段，当为空时，时查询所有字段
        //String[] includes = new String[]{"userName", "password"};
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);
        //同步
        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        System.out.println("同步数据：\n" + getResponse);
        //异步
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse getResponse) {
                System.out.println("异步成功数据：\n" + getResponse);

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步失败数据：\n" + getResponse);

            }
        };
        client.getAsync(request, RequestOptions.DEFAULT, listener);
        client.close();

    }


    /**
     * @throws IOException 如果 document 存在则返回true
     *                     不存在则返回false
     */
    @Test
    public void existTest() throws IOException {

        GetRequest getRequest = new GetRequest(
                "users",
                "1");
        //只要返回true/false 为提高效率 所以禁止抓取 source数据
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("同步成功数据:\n" + exists);

        ActionListener<Boolean> listener = new ActionListener<Boolean>() {
            @Override
            public void onResponse(Boolean exists) {
                System.out.println("异步成功数据：\n" + exists);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步成功数据：\n" + e);
            }
        };
        client.existsAsync(getRequest, RequestOptions.DEFAULT, listener);

        //禁止抓取source后返回的结果：{"_index":"users","_type":"_doc","_id":"2","found":false}
        //{"_index":"users","_type":"_doc","_id":"1","_version":2,"_seq_no":1,"_primary_term":1,"found":true}
        GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println("抓取到的数据是:\n" + documentFields);


    }

    /**
     * @throws IOException
     * 删除数据
     */
    @Test
    public void deleteTest() throws IOException {

        DeleteRequest request = new DeleteRequest(
                "users",
                "1");
        DeleteResponse deleteResponse = client.delete(
                request, RequestOptions.DEFAULT);
        System.out.println("同步成功数据:\n" + deleteResponse);

        ActionListener<DeleteResponse> listener = new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse asynDeleteResponse) {
                System.out.println("异步成功数据:\n" + asynDeleteResponse);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步失败数据:\n" + e);
            }
        };
        client.deleteAsync(request, RequestOptions.DEFAULT, listener);
    }


    /**
     * @throws IOException
     * 存在就更新
     * 不存在则鑫泽docAsUpsert
     */
    @Test
    public void updateTest() throws IOException {

        UpdateRequest request = new UpdateRequest(
                "users",
                "2");
        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("1234");
        user.setCreateTime(new Date());
        user.setAge(12);
        //重试次数
        request.retryOnConflict(3);
        //该方法id 不存在时则新增操作,存在时则更新操作
        request.docAsUpsert(true);
        request.doc(JSONObject.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = client.update(
                request, RequestOptions.DEFAULT);
        System.out.println("同步成功数据:\n" + updateResponse);
        String index = updateResponse.getIndex();
        String id = updateResponse.getId();
        long version = updateResponse.getVersion();
        if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("同步成功-新增数据:\n" + updateResponse);

        } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("同步成功-更新数据:\n" + updateResponse);
        } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
            System.out.println("同步成功-删除数据:\n" + updateResponse);
        } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
            System.out.println("同步成功-nood数据:\n" + updateResponse);
        }

        ActionListener<UpdateResponse> listener  = new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse asynUpdateResponse) {
                System.out.println("异步成功数据:\n" + asynUpdateResponse);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步失败数据:\n" + e);
            }
        };
        client.updateAsync(request, RequestOptions.DEFAULT, listener);
    }




    @Data
    class User {

        private String userName;

        private String password;

        private int age;

        private Date createTime;

    }

}

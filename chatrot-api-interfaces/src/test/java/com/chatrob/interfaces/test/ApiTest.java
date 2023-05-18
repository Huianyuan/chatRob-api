package com.chatrob.interfaces.test;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Base64;

/**
 * @author :lhy
 * @description :测试接口，包括获取问题和回答问题，chatGPT接口调用
 * @date :2023/05/18 下午 04:12
 */
public class ApiTest {


    /**
     * 获取未回答问题列表
     */
    @Test
    public void query_unanswered_questions() throws IOException {

        //创建连接请求
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //请求URL（这里是知识星球连接）
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/48411118851818/topics?scope=unanswered_questions" +
                "&count=20");
        //增加头信息 cookie和文本类型
        get.addHeader("cookie", "知识星球个人cookie信息");
        get.addHeader("Content-Type", "application/json;charset=utf8");
        //执行get请求获得请求信息
        CloseableHttpResponse response = httpClient.execute(get);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }


    /**
     * 测试回答问题
     */
    @Test
    public void answer() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/412884248251548/answer");
        post.addHeader("cookie", "知识星球个人cookie信息");
        post.addHeader("Content=Type", "application/json;charset=utf8");

        String paramJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"自己去百度！\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"silenced\": false\n" +
                "  }\n" +
                "}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    /*
     * ChatGPT接口调用测试
     */
    @Test
    public void test_chatGPT() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //ChatGPT注册账号后官方文档可以查阅
        HttpPost post = new HttpPost("https://api.openai.com/v1/completions");
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 自行申请 https://beta.openai.com/overview");

        String paramJson = "{\"model\": \"text-davinci-003\", \"prompt\": \"帮我写一个java冒泡排序\", \"temperature\": 0, \"max_tokens\": 1024}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

}

package com.qa.restclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestClient {

    final static Logger Log = Logger.getLogger(RestClient.class);

    /**
     * @param url
     * @return 返回响应对象
     * @throws ClassCastException
     * @throws IOException
     */
    //1. Get 请求方法
    public CloseableHttpResponse get(String url) throws ClientProtocolException, IOException {

        //创建一个可关闭的HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建一个HttpGet的请求对象
        HttpGet httpGet = new HttpGet(url);
        //执行请求，相当于postman上点击发送按钮，然后赋值给HttpResponse对象接受
        Log.info("开始发送get请求...");
        CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
        Log.info("发送请求成功！开始得到响应对象。");
        return httpResponse;
    }


    /**
     * 带请求头信息的get方法
     *
     * @param url
     * @param headermap,键值对形式
     * @return 返回响应对象
     * @throws ClientProtocolException
     * @throws IOException
     */
    public CloseableHttpResponse get(String url, HashMap<String, String> headermap) throws ClientProtocolException, IOException {
        //创建一个可关闭的HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建一个Get请求
        HttpGet httpGet = new HttpGet(url);
        Map<String, String> headers = new HashMap<String, String>();
        //加载请求头到httpGet对象
        headers.forEach((k, v) -> {
            httpGet.addHeader(k, v);
        });
        CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
        Log.info("开始发送带有请求头的http请求");
        return httpResponse;

    }

    /**
     * 封装post方法
     *
     * @param url
     * @param entityString,设置请求json参数
     * @param headermap,带请求头
     * @return 返回响应对象
     * @throws ClientProtocolException
     * @throws IOException
     */

    public CloseableHttpResponse post(String url, String entityString, HashMap<String, String> headermap) throws ClientProtocolException, IOException {

        //创建一个可关闭的HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(url);
        //设置payload
        httppost.setEntity(new StringEntity(entityString));

        //加载请求头到httppost对象
        Map<String, String> headers = new HashMap<String, String>();
        headers.forEach((k, v) -> {
            httppost.addHeader(k, v);
        });
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);
        Log.info("开始发送post请求");
        return httpResponse;
    }

    /**
     * 封装put请求方法，参数和post一样
     *
     * @param url
     * @param entityString,设置请求json参数
     * @param headermap,带请求头
     * @return 返回响应对象
     * @throws ClientProtocolException
     * @throws IOException
     */

    public CloseableHttpResponse put(String url, String entityString, HashMap<String, String> headermap) throws ClientProtocolException, IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut httpput = new HttpPut(url);
        httpput.setEntity(new StringEntity(entityString));
        Map<String, String> headers = new HashMap<String, String>();
        headers.forEach((k, v) -> {
            httpput.addHeader(k, v);
        });
        //发送put请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpput);
        return httpResponse;
    }


    /**
     * 封装delete请求方法，参数和get方法一样
     *
     * @param url,接口url完整地址
     * @throws ClientProtocolException
     * @throws IOException
     * @return,返回一个response对象，方便进行得到状态码和json解析动作
     */
    public CloseableHttpResponse delete(String url) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpdel = new HttpDelete(url);

        //发送delete请求
        CloseableHttpResponse httpResponse = httpclient.execute(httpdel);
        return httpResponse;
    }

    /**
     * 获取响应码状态，常用来和TestBase中定义的状态码常亮去测试断言使用
     *
     * @param response
     * @return 返回int类型状态码
     */
    public int getStatusCode(CloseableHttpResponse response) {

        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("解析，得到响应状态码：" + statusCode);
        return statusCode;
    }

    /**
     * @param
     * @return 接下来，一般会继续调用TestUtil类下的json解析方法得到某一个json对象的值
     * @throws ParseException
     * @throws IOException
     */
    public JSONObject getResponseJson(CloseableHttpResponse response) throws ParseException, IOException {
        Log.info("得到响应对象的String格式");
        String responseString = EntityUtils.toString(response.getEntity(), "Utf-8");
        JSONObject responseJson = JSON.parseObject(responseString);
        Log.info("返回响应内容的JSON格式");
        return responseJson;
    }
}



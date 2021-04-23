package com.smartcity.naolifang.common.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static String hikivisionPlatformUrl = "";

    public static String doGet(String url, Map<String, Object> paramMap) {
        url += "?";
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            url  += entry.getKey() + "=" + entry.getValue() + "&";
        }

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 获取结果流并转换成结果
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(result.toString());
        return result.toString();
    }

    public static String doPost(String url, Map<String, Object> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            JSONObject param = new JSONObject(paramMap);
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public static byte[] downImageToByte(String path) throws IOException {
        byte[] data = null;
        URL url = null;
        InputStream input = null;
        try{
            url = new URL(path);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            input = httpUrl.getInputStream();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numBytesRead = 0;
        while ((numBytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, numBytesRead);
        }
        data = output.toByteArray();
        output.close();
        input.close();

        return data;
    }

    public static String postToHikvisionPlatform(String api, Map<String, Object> paramMap) {
        try {
            if (StringUtils.isBlank(hikivisionPlatformUrl)) {
                Properties props = new Properties();
                InputStream inputStream= HttpUtil.class.getClassLoader().getResourceAsStream("application-prod.properties");
                props.load(inputStream);
                hikivisionPlatformUrl = props.getProperty("hikivision.platform.url");
            }
            ArtemisConfig.host = hikivisionPlatformUrl;
            ArtemisConfig.appKey = "26930432";
            ArtemisConfig.appSecret = "ZbBuQUbPytNgIktNtBoF";
            String ARTEMIS_PATH = "/artemis";
            String previewURLsApi = ARTEMIS_PATH + api;
            String contentType = "application/json";
            Map<String, String> path = new HashMap<String, String>(2) {
                {
                    put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
                }
            };

            JSONObject jsonBody = new JSONObject(paramMap);
            String body = jsonBody.toJSONString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);
            System.out.println(result);

            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
//        List<String> indexCodes = new ArrayList<>();
//        indexCodes.add("c13b45b0d2fc436cb9592145986e047f");
//        indexCodes.add("6898cf8b8e66496e9a7d769baca1f881");
//        indexCodes.add("f5edfb13d8324b9f8c365b85ca8311c2");
//        indexCodes.add("0f1fc19516ea4482aea97be2f87cb78b");
//        indexCodes.add("46b85baffbe147f185f6f8e606ae442f");
//        paramMap.put("indexCodes", indexCodes);
//        paramMap.put("personId", "test123");
//        paramMap.put("personName", "测试人员123");
//        paramMap.put("certificateType", 111);
//        paramMap.put("certificateNo", "440682199510201364");
//        paramMap.put("gender", 1);
//        paramMap.put("orgIndexCode", "root000000");
//        paramMap.put("resourceType", "person");
//        paramMap.put("pageNo", 1);
//        paramMap.put("pageSize", 20);
        paramMap.put("resourceType", "door");
        paramMap.put("pageNo", 1);
        paramMap.put("pageSize", 20);
        postToHikvisionPlatform("/api/irds/v2/deviceResource/resources", paramMap);

//        byte[] data = downImageToByte("http://192.168.8.123:2020/image/avatar/1-348ae2aa-7c3a-48ba-8b03-3a05b6951d32.png");
//        Base64.Encoder encoder = Base64.getEncoder();
//        String base64Str = encoder.encodeToString(data);
//
//        String isoStartTime = DateTimeUtil.stringToIso8601("2021-04-01 15:00:00");
//        String isoEndTime = DateTimeUtil.stringToIso8601("2021-04-02 15:00:00");
//        paramMap.put("startTime", isoStartTime);
//        paramMap.put("endTime", isoEndTime);
//        paramMap.put("facePicBinaryData", base64Str);
//        paramMap.put("minSimilarity", 80);
//        String resultStr = HttpUtil.postToHikvisionPlatform("/api/frs/v1/application/captureSearch", paramMap);

    }
}

package org.moboxlab.MoBoxFrp.Web;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.moboxlab.MoBoxFrp.BasicInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class WebBasic {
    public static String getRemoteIP(HttpExchange exchange) {
        return exchange.getRemoteAddress().getHostString();
    }

    public static JSONObject loadRequestData(HttpExchange request) throws Exception{
        InputStream inputStream = request.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder text = new StringBuilder();
        String read;
        while ((read = reader.readLine())!=null) text.append(read);
        return JSONObject.parseObject(text.toString());
    }

    public static void initBasicResponse(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "*");
        headers.add("Access-Control-Max-Age", "864000");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
    }

    public static void completeResponse(HttpExchange exchange, JSONObject responseData, JSONObject data){
        BasicInfo.sendDebug(responseData.toString());
        OutputStreamWriter oStreamWriter = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8);
        try {
            exchange.sendResponseHeaders(200,0);
            oStreamWriter.append(responseData.toString());
        } catch (Exception e) {
            BasicInfo.logger.sendWarn("API在返回响应时发生错误，原因："+e.getMessage()+"，数据如下：["+data.toString()+"]["+ responseData +"]");
        } finally {
            try {
                oStreamWriter.close();
                exchange.close();
            } catch (IOException e) {
                BasicInfo.logger.sendWarn("关闭API的输出流时发生错误，原因："+e.getMessage());
            }
        }
    }
}

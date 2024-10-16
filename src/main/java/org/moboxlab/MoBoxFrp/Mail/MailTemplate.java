package org.moboxlab.MoBoxFrp.Mail;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrp.BasicInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MailTemplate {
    public static JSONObject getTemplate(String name) {
        try {
            JSONObject data = MailMain.mailTemplate.getJSONObject(name);
            File file = new File("./MoBoxFrp/mail/"+data.getString("content"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String read;
            while ((read = reader.readLine()) != null) {
                builder.append(read);
            }
            data.put("content",builder.toString());
            return data;
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            return null;
        }
    }
}

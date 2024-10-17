package org.moboxlab.MoBoxFrp.Mail;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrp.BasicInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MailCode {

    //邮箱-验证码
    private static final Map<String,String> mailCodeMap = new HashMap<>();
    //邮箱-可用时间
    private static final Map<String,Long> codeAvailableMap = new HashMap<>();

    //生成验证码
    private static final Random random = new Random();
    public static String getCode(String mail) {
        String code = String.valueOf(random.nextInt(800000)+100000);
        mailCodeMap.put(mail,code);
        codeAvailableMap.put(mail,System.currentTimeMillis()+ BasicInfo.config.getInteger("mailCodeTime")*60*1000L);
        return code;
    }

    //验证验证码
    public static JSONObject verifyCode(String mail, String code) {
        JSONObject result = new JSONObject();
        //短信功能开启校验
        if (!BasicInfo.config.getBoolean("enableMail")) {
            result.put("success",false);
            result.put("message","邮件功能未开启！");
            return result;
        }
        //是否存在手机号校验
        if (!mailCodeMap.containsKey(mail)) {
            result.put("success",false);
            result.put("message","缓存中不存在对应邮箱数据！");
            return result;
        }
        //验证码校验
        if (!mailCodeMap.get(mail).equals(code)) {
            result.put("success",false);
            result.put("message","验证码有误！");
            return result;
        }
        //时间校验
        if (codeAvailableMap.get(mail) < System.currentTimeMillis()) {
            result.put("success",false);
            result.put("message","验证码已超时！");
            return result;
        }
        //正确，返回数据
        result.put("success",true);
        result.put("message","验证码正确！");
        return result;
    }
}

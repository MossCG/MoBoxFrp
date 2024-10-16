package org.moboxlab.MoBoxFrp.SMS;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import org.moboxlab.MoBoxFrp.BasicInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.alibaba.fastjson.JSON.toJSONString;

public class SMSMain {
    public static JSONObject sendSMS(String phone) {
        JSONObject result = new JSONObject();
        try {
            if (!BasicInfo.config.getBoolean("enableSMS")) {
                result.put("success",false);
                result.put("message","短信功能未开启！");
                return result;
            }
            if (phone.length() != 11) {
                result.put("success",false);
                result.put("message","非+86 11位手机号！");
                return result;
            }
            BasicInfo.logger.sendInfo("正在向"+phone+"发送短信！");
            if (!SMSCoolDown.checkCoolDown(phone)) {
                result.put("success",false);
                result.put("message","未到短信冷却时间！");
                return result;
            }
            Client client = SMSClient.getClient();
            JSONObject params = new JSONObject();
            String code = getCode(phone);
            params.put("code",code);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(BasicInfo.config.getString("SMSSignName"))
                    .setTemplateCode(BasicInfo.config.getString("SMSTemplateCode"))
                    .setTemplateParam(params.toString());
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            BasicInfo.sendDebug(toJSONString(sendSmsResponse));
            BasicInfo.logger.sendInfo("向"+phone+"发送短信成功！");
            result.put("success",true);
            result.put("message","短信发送成功！");
            return result;
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            BasicInfo.logger.sendWarn("向"+phone+"发送短信失败！");
            result.put("success",false);
            result.put("message","短信发送失败，内部异常！");
            return result;
        }
    }

    //手机号-验证码
    public static Map<String,String> phoneCodeMap = new HashMap<>();
    //手机号-可用时间
    public static Map<String,Long> codeAvailableMap = new HashMap<>();

    //生成验证码
    public static Random random = new Random();
    public static String getCode(String phone) {
        String code = String.valueOf(random.nextInt(800000)+100000);
        phoneCodeMap.put(phone,code);
        codeAvailableMap.put(phone,System.currentTimeMillis()+BasicInfo.config.getInteger("SMSCodeTime")*60*1000L);
        return code;
    }

    //验证验证码
    public static JSONObject verifyCode(String phone,String code) {
        JSONObject result = new JSONObject();
        if (!BasicInfo.config.getBoolean("enableSMS")) {
            result.put("success",false);
            result.put("message","短信功能未开启！");
            return result;
        }
        if (!phoneCodeMap.containsKey(phone)) {
            result.put("success",false);
            result.put("message","缓存中不存在对应手机号数据！");
            return result;
        }
        if (!phoneCodeMap.get(phone).equals(code)) {
            result.put("success",false);
            result.put("message","验证码有误！");
            return result;
        }
        if (codeAvailableMap.get(phone) < System.currentTimeMillis()) {
            result.put("success",false);
            result.put("message","验证码已超时！");
            return result;
        }
        result.put("success",true);
        result.put("message","验证码正确！");
        return result;
    }
}

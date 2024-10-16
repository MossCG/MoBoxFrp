package org.moboxlab.MoBoxFrp.Mail;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrp.BasicInfo;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.Mail.MailManager;
import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.mossmc.mosscg.MossLib.Object.ObjectMail;
import org.mossmc.mosscg.MossLib.Object.ObjectMailInfo;

import java.io.File;

public class MailMain {
    public static ObjectConfig mailTemplate;

    //邮件模块初始化
    public static void initMail() {
        if (!BasicInfo.config.getBoolean("enableMail")) return;
        //文件初始化及校验
        mailTemplate = ConfigManager.getConfigObject("./MoBoxFrp", "mail.json", "mail.json");
        File file = new File("./MoBoxFrp/mail");
        if (!file.exists()) {
            BasicInfo.logger.sendInfo("正在保存邮件初始模板！");
            FileCheck.checkDirExist("./MoBoxFrp/mail");
            FileCheck.checkFileExist("./MoBoxFrp/mail/verifyCode.txt","mail/verifyCode.txt");
        }
        //构建邮件模块基本信息
        ObjectMailInfo mailInfo = new ObjectMailInfo();
        mailInfo.mailAccount = BasicInfo.config.getString("mailAccount");
        mailInfo.mailPassword = BasicInfo.config.getString("mailPassword");
        mailInfo.mailSMTPHost = BasicInfo.config.getString("mailSMTPHost");
        mailInfo.mailSMTPPort = BasicInfo.config.getString("mailSMTPPort");
        MailManager.initMail(mailInfo,BasicInfo.logger,false);
    }

    //邮件发送
    public static JSONObject sendMail(ObjectMail mail) {
        JSONObject result = new JSONObject();
        try {
            //邮件开启校验
            if (!BasicInfo.config.getBoolean("enableMail")) {
                result.put("success",false);
                result.put("message","发送失败，邮件功能未开启！");
                return result;
            }
            MailManager.sendMail(mail);
            result.put("success",true);
            result.put("message","发送成功！");
            return result;
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
            result.put("success",false);
            result.put("message","发送失败，内部错误！");
            return result;
        }
    }
}

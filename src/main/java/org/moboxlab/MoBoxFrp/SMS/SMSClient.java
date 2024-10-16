package org.moboxlab.MoBoxFrp.SMS;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.moboxlab.MoBoxFrp.BasicInfo;

public class SMSClient {
    public static Client getClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(BasicInfo.config.getString("AliyunAccessKeyID"))
                .setAccessKeySecret(BasicInfo.config.getString("AliyunAccessKeySecret"));
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }
}

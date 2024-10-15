package org.moboxlab.MoBoxFrp.Info;

import org.moboxlab.MoBoxFrp.BasicInfo;

import static org.moboxlab.MoBoxFrp.BasicInfo.logger;

public class InfoStart {
    public static void sendStartMessage() {
        //基础信息输出
        logger.sendInfo("欢迎使用MoBoxFrp~这里是主控哦~");
        logger.sendInfo("软件版本：" + BasicInfo.version + " " + BasicInfo.versionType);
        logger.sendInfo("软件作者：" + BasicInfo.author);
        logger.sendInfo("感谢以下贡献者：");
        logger.sendInfo(BasicInfo.contributor);

        //版本提示
        switch (BasicInfo.versionType) {
            case "Stable":
                logger.sendInfo("这是稳定版本~可以安心使用哦~");
                break;
            case "Beta":
                logger.sendWarn("这是测试版本哦~");
                logger.sendWarn("如果出问题了请及时反馈以下Github链接哦：");
                logger.sendWarn("https://github.com/MossCG/MoBoxFrp");
                break;
            default:
                logger.sendWarn("未知的版本类型！是不是太久没更新啦~");
                break;
        }
    }
}

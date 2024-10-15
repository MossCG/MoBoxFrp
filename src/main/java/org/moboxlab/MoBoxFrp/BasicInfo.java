package org.moboxlab.MoBoxFrp;

import org.mossmc.mosscg.MossLib.Object.ObjectConfig;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

public class BasicInfo {
    //版本信息，请勿修改
    public static String version = "V0.0.0.0.0000";
    public static String versionType = "Beta";

    //作者信息，请勿修改
    public static String author = "墨守MossCG";

    //贡献者信息，如果你做出了涉及代码的pr且代码量不少于100lines，请自行添加你的ID到此处
    public static String contributor = "";

    //MossLib框架功能模块
    public static ObjectLogger logger;
    public static ObjectConfig config;

    //Debug信息输出
    public static boolean debug = false;
    public static void sendDebug(String message) {
        logger.sendAPI(message,debug);
    }
}

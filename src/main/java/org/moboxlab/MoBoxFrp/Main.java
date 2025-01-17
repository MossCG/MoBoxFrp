package org.moboxlab.MoBoxFrp;

import org.moboxlab.MoBoxFrp.Command.CommandDebug;
import org.moboxlab.MoBoxFrp.Command.CommandExit;
import org.moboxlab.MoBoxFrp.Database.DatabaseMain;
import org.moboxlab.MoBoxFrp.Info.InfoStart;
import org.moboxlab.MoBoxFrp.Mail.MailMain;
import org.moboxlab.MoBoxFrp.User.UserPermission;
import org.moboxlab.MoBoxFrp.Web.WebMain;
import org.mossmc.mosscg.MossLib.Command.CommandManager;
import org.mossmc.mosscg.MossLib.Config.ConfigManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.File.FileDependency;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

public class Main {
    public static void main(String[] args) {
        //计时
        long startTime = System.currentTimeMillis();

        //日志模块初始化
        FileCheck.checkDirExist("./MoBoxFrp");
        BasicInfo.logger = new ObjectLogger("./MoBoxFrp/logs");
        //外部依赖初始化（不包含MossLib）
        FileDependency.loadDependencyDir("./MoBoxFrp/dependency","dependency");
        //启动消息组
        InfoStart.sendStartMessage();
        //配置文件初始化
        BasicInfo.logger.sendInfo("正在读取配置文件......");
        BasicInfo.config = ConfigManager.getConfigObject("./MoBoxFrp", "config.yml", "config.yml");

        //数据库模块初始化
        BasicInfo.logger.sendInfo("正在连接至数据库......");
        DatabaseMain.updateConnection(true);

        //用户模块初始化
        BasicInfo.logger.sendInfo("正在初始化用户模块......");
        UserPermission.initLevel();

        //邮箱模块初始化
        BasicInfo.logger.sendInfo("正在初始化邮箱模块......");
        MailMain.initMail();

        //WebAPI模块初始化
        BasicInfo.logger.sendInfo("正在初始化WebAPI模块......");
        WebMain.initWebAPI();

        //命令行初始化
        CommandManager.initCommand(BasicInfo.logger,true);
        CommandManager.registerCommand(new CommandExit());
        CommandManager.registerCommand(new CommandDebug());

        //计时
        long completeTime = System.currentTimeMillis();
        BasicInfo.logger.sendInfo("启动完成！耗时："+(completeTime-startTime)+"毫秒！");
    }
}

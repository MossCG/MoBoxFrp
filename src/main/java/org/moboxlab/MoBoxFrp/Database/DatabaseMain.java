package org.moboxlab.MoBoxFrp.Database;

import org.moboxlab.MoBoxFrp.BasicInfo;
import org.mossmc.mosscg.MossLib.Mysql.MysqlManager;
import org.mossmc.mosscg.MossLib.Object.ObjectMysqlInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectSQLiteInfo;
import org.mossmc.mosscg.MossLib.SQLite.SQLiteManager;

import java.sql.Connection;

public class DatabaseMain {
    private static ObjectMysqlInfo mysqlInfo;
    private static ObjectSQLiteInfo sqliteInfo;

    private static long lastCheck = 0;
    private static long lastRefresh = 0;

    public static Connection getConnection() {
        checkDatabase();
        if (BasicInfo.config.getBoolean("enableMySQL")) {
            return BasicInfo.mysqlManager.getConnection();
        } else {
            return BasicInfo.sqLiteManager.getConnection();
        }
    }


    public static void updateConnection(boolean displayLog) {
        //构建信息
        buildDatabaseInfo();
        //清除旧链接（延迟10s关闭）
        if (BasicInfo.mysqlManager!= null) {
            MysqlManager mysqlManager = BasicInfo.mysqlManager;
            Thread thread = new Thread(() -> timerClose(mysqlManager));
            thread.start();
        }
        if (BasicInfo.sqLiteManager!= null) {
            SQLiteManager sqLiteManager = BasicInfo.sqLiteManager;
            Thread thread = new Thread(() -> timerClose(sqLiteManager));
            thread.start();
        }
        //构建新链接
        if (BasicInfo.config.getBoolean("enableMySQL")) {
            BasicInfo.mysqlManager = new MysqlManager();
            BasicInfo.mysqlManager.initMysql(mysqlInfo,BasicInfo.logger,displayLog);
            BasicInfo.sqLiteManager = null;
        } else {
            BasicInfo.sqLiteManager = new SQLiteManager();
            BasicInfo.sqLiteManager.initSQLite(sqliteInfo,BasicInfo.logger,displayLog);
            BasicInfo.mysqlManager = null;
        }
    }

    //刷新链接缓存避免OOM
    private static synchronized void checkDatabase() {
        //连接检查CD，避免每次都检查
        if ((System.currentTimeMillis()-lastCheck) < BasicInfo.config.getInteger("databaseCheckCD")*1000L) return;
        try {
            //检查连接是否可用
            boolean isClosed,isValid;
            if (BasicInfo.config.getBoolean("enableMySQL")) {
                isClosed = BasicInfo.mysqlManager.getConnection().isClosed();
                isValid = BasicInfo.mysqlManager.getConnection().isValid(1);
            } else {
                isClosed = BasicInfo.sqLiteManager.getConnection().isClosed();
                isValid = BasicInfo.sqLiteManager.getConnection().isValid(1);
            }
            //不可用则刷新连接
            if (isClosed || !isValid) {
                updateConnection(false);
                lastRefresh = System.currentTimeMillis();
            }
            //更新检查时间
            lastCheck = System.currentTimeMillis();
            //可用则检查是否需要定时刷新
            if ((System.currentTimeMillis()-lastRefresh) >= BasicInfo.config.getInteger("databaseRefreshCD")*1000L) {
                updateConnection(false);
                lastRefresh = System.currentTimeMillis();
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }

    //构建MySQL和SQLite的基本信息结构
    private static void buildDatabaseInfo() {
        mysqlInfo = new ObjectMysqlInfo();
        mysqlInfo.address = BasicInfo.config.getString("databaseAddress");
        mysqlInfo.port = BasicInfo.config.getString("databasePort");
        mysqlInfo.database = BasicInfo.config.getString("databaseDatabase");
        mysqlInfo.username = BasicInfo.config.getString("databaseUser");
        mysqlInfo.password = BasicInfo.config.getString("databasePassword");
        mysqlInfo.poolSize = BasicInfo.config.getInteger("databasePoolSize");
        sqliteInfo = new ObjectSQLiteInfo();
        sqliteInfo.poolSize = BasicInfo.config.getInteger("databasePoolSize");
        sqliteInfo.filePath = "./MoBoxFrp/data.db";
    }

    //延迟关闭Mysql的子进程
    private static void timerClose(MysqlManager mysql) {
        try {
            Thread.sleep(10000L);
            mysql.close();
            BasicInfo.logger.sendInfo("已关闭失效MySQL链接！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
    //延迟关闭SQLite的子进程
    private static void timerClose(SQLiteManager sqlite) {
        try {
            Thread.sleep(10000L);
            sqlite.close();
            BasicInfo.logger.sendInfo("已关闭失效SQLite链接！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
}

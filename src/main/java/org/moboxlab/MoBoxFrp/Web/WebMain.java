package org.moboxlab.MoBoxFrp.Web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.moboxlab.MoBoxFrp.BasicInfo;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.Executors;

public class WebMain {
    //Http Https可选可切换
    private static HttpServer httpPublicServer;
    private static HttpsServer httpsPublicServer;

    //初始化方法
    public static void initWebAPI() {
        //初始化端口显示
        int port = BasicInfo.config.getInteger("webPort");
        BasicInfo.logger.sendInfo("正在加载WebAPI，端口："+port);
        //Http Https选择
        if (BasicInfo.config.getBoolean("webHttps")) {
            loadHttps(port);
        } else {
            loadHttp(port);
        }
        //完成信息
        BasicInfo.logger.sendInfo("WebAPI加载完成，端口："+port);
    }

    //停止方法
    public static void stopWebAPI() {
        if (httpPublicServer != null) {
            httpPublicServer.stop(0);
            httpPublicServer = null;
        }
        if (httpsPublicServer != null) {
            httpsPublicServer.stop(0);
            httpsPublicServer = null;
        }
        BasicInfo.logger.sendInfo("WebAPI服务已关闭！");
    }

    //Http服务器初始化
    private static void loadHttp(int port) {
        try {
            InetSocketAddress address = new InetSocketAddress(port);
            httpPublicServer = HttpServerProvider.provider().createHttpServer(address,0);
            httpPublicServer.createContext("/API", new WebHandler());
            httpPublicServer.setExecutor(Executors.newCachedThreadPool());
            httpPublicServer.start();
            BasicInfo.logger.sendInfo("Http服务初始化完成！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }

    //Https服务器初始化
    private static void loadHttps(int port) {
        try {
            //基础Https服务器初始化
            InetSocketAddress address = new InetSocketAddress(port);
            httpsPublicServer = HttpsServer.create(address,0);
            httpsPublicServer.createContext("/API", new WebHandler());
            httpsPublicServer.setExecutor(Executors.newCachedThreadPool());

            //构建Https证书
            KeyStore ks = KeyStore.getInstance("JKS");
            String password = BasicInfo.config.getString("webHttpsAuth");
            ks.load(Files.newInputStream(Paths.get("./MoBoxFrp/web.jks")), password.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            //Https服务配置
            httpsPublicServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        SSLContext SSL_Context = getSSLContext();
                        SSLEngine SSL_Engine = SSL_Context.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(SSL_Engine.getEnabledCipherSuites());
                        params.setProtocols(SSL_Engine.getEnabledProtocols());
                        SSLParameters SSL_Parameters = SSL_Context.getSupportedSSLParameters();
                        params.setSSLParameters(SSL_Parameters);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            httpsPublicServer.start();
            BasicInfo.logger.sendInfo("Https服务初始化完成！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
    }
}

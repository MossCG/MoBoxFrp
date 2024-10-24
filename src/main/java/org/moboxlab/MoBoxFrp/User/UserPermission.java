package org.moboxlab.MoBoxFrp.User;

import org.moboxlab.MoBoxFrp.BasicInfo;

import java.util.HashMap;
import java.util.Map;

public class UserPermission {
    //权限组Map
    private static final Map<String,Integer> permissionLevelMap = new HashMap<>();

    //注册权限组（插件可调接口）
    public static void registerLevel(String name,int level) {
        permissionLevelMap.put(name,level);
        BasicInfo.logger.sendInfo("已注册用户组："+name+"，权限等级为"+level);
    }

    //初始化权限组（初始化调用一次即可）
    public static void initLevel() {
        registerLevel("admin",999);
        registerLevel("staff",800);
        registerLevel("provider",600);
        registerLevel("default",200);
        registerLevel("banned",100);
    }

    //根据名称获取权限组
    public static int getLevel(String name) {
        if (permissionLevelMap.containsKey(name)) {
            return permissionLevelMap.get(name);
        }
        return 0;
    }
}

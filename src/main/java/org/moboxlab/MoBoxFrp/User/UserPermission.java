package org.moboxlab.MoBoxFrp.User;

import org.moboxlab.MoBoxFrp.BasicInfo;

import java.util.HashMap;
import java.util.Map;

public class UserPermission {
    private static final Map<String,Integer> permissionLevelMap = new HashMap<>();

    public static void registerLevel(String name,int level) {
        permissionLevelMap.put(name,level);
        BasicInfo.logger.sendInfo("已注册用户组："+name+"，权限等级为"+level);
    }

    public static void initLevel() {
        registerLevel("admin",999);
        registerLevel("staff",800);
        registerLevel("provider",600);
        registerLevel("default",200);
        registerLevel("banned",100);
    }

    public static int getLevel(String name) {
        if (permissionLevelMap.containsKey(name)) {
            return permissionLevelMap.get(name);
        }
        return 0;
    }
}

package org.moboxlab.MoBoxFrp.User;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserCache {
    //缓存Map
    private static final Map<String, JSONObject> UIDDataMap = new HashMap<>();
    private static final Map<String, Long> UIDTimeMap = new HashMap<>();
    //缓存有效时间（秒）
    private static final int cacheTime = 30;
    //用户数据缓存加速
    public static JSONObject getUIDCache(String UID) {
        if (UIDDataMap.containsKey(UID)) {
            if (UIDTimeMap.get(UID) < System.currentTimeMillis()) {
                removeUIDCache(UID);
                return null;
            }
            return UIDDataMap.get(UID);
        }
        return null;
    }

    //更新缓存，查询用户数据时自动缓存
    public static void updateUIDCache(String UID,JSONObject data) {
        if (UIDDataMap.containsKey(UID)) {
            UIDDataMap.replace(UID,data);
            UIDTimeMap.replace(UID,System.currentTimeMillis()+1000L*cacheTime);
        } else {
            UIDDataMap.put(UID,data);
            UIDTimeMap.put(UID,System.currentTimeMillis()+1000L*cacheTime);
        }
    }

    //清除缓存，对于一些信息更新类API可用
    public static void removeUIDCache(String UID) {
        UIDDataMap.remove(UID);
        UIDTimeMap.remove(UID);
    }
}

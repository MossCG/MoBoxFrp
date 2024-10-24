package org.moboxlab.MoBoxFrp.User;

import com.alibaba.fastjson.JSONObject;
import org.moboxlab.MoBoxFrp.BasicInfo;
import org.moboxlab.MoBoxFrp.Database.DatabaseMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserData {
    //带缓存读数据
    public static JSONObject getUserData(String UID) {
        JSONObject data = UserCache.getUIDCache(UID);
        if (data == null) {
            data = getDatabaseData("UID",UID);
        }
        return data;
    }

    //直接读数据库数据
    public static JSONObject getDatabaseData(String key, String value) {
        try {
            Connection connection = DatabaseMain.getConnection();
            String sql = "select * from user where "+key+"=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,value);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                JSONObject data = new JSONObject();
                data.put("UID",set.getString("UID"));
                data.put("permission",set.getString("permission"));
                data.put("username",set.getString("username"));
                data.put("password",set.getString("password"));
                data.put("email",set.getString("email"));
                data.put("phone",set.getString("phone"));
                data.put("qq",set.getString("qq"));
                UserCache.updateUIDCache(set.getString("UID"),data);
                return data;
            }
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }
        return null;
    }
}

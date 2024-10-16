package org.moboxlab.MoBoxFrp.SMS;

import org.moboxlab.MoBoxFrp.BasicInfo;

import java.util.HashMap;
import java.util.Map;

public class SMSCoolDown {
    private static final Map<String,Long> coolDownMap = new HashMap<>();

    public static synchronized boolean checkCoolDown(String phone) {
        long next = System.currentTimeMillis()+BasicInfo.config.getInteger("SMSCoolDown")*1000L;
        if (!coolDownMap.containsKey(phone)) {
            coolDownMap.put(phone,next);
            return true;
        }
        //系统时间比限制时间晚，才可以发
        if (System.currentTimeMillis()>coolDownMap.get(phone)) {
            coolDownMap.replace(phone,next);
            return true;
        } else {
            BasicInfo.logger.sendInfo(phone+"未到冷却时间！本次不予发送！");
            return false;
        }
    }
}

package org.moboxlab.MoBoxFrp.Command;

import org.moboxlab.MoBoxFrp.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandDebug extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("debug");
        prefixList.add("api");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        if (BasicInfo.debug) {
            BasicInfo.debug = false;
            BasicInfo.logger.sendInfo("已关闭debug信息！");
        } else {
            BasicInfo.debug = true;
            BasicInfo.logger.sendInfo("已开启debug信息！");
        }
        return true;
    }
}

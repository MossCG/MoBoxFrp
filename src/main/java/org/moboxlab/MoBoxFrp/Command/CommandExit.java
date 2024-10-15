package org.moboxlab.MoBoxFrp.Command;

import org.mossmc.mosscg.MossLib.Object.ObjectCommand;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;

import java.util.ArrayList;
import java.util.List;

public class CommandExit extends ObjectCommand {
    @Override
    public List<String> prefix() {
        List<String> prefixList = new ArrayList<>();
        prefixList.add("exit");
        prefixList.add("quit");
        return prefixList;
    }

    @Override
    public boolean execute(String[] args, ObjectLogger logger) {
        System.exit(0);
        return true;
    }
}

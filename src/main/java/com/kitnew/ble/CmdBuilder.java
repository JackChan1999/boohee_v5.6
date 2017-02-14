package com.kitnew.ble;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class CmdBuilder {
    CmdBuilder() {
    }

    static byte[] buildCmd(int cmd, int protocolType, int... others) {
        int i;
        int cnt = others.length + 4;
        byte[] cmds = new byte[(others.length + 4)];
        cmds[0] = (byte) cmd;
        cmds[1] = (byte) cnt;
        cmds[2] = (byte) protocolType;
        for (i = 0; i < others.length; i++) {
            cmds[i + 3] = (byte) others[i];
        }
        int checkIndex = cmds.length - 1;
        for (i = 0; i < checkIndex; i++) {
            cmds[checkIndex] = (byte) (cmds[checkIndex] + cmds[i]);
        }
        return cmds;
    }

    static byte[] buildOverCmd(int protocolType, int dataType) {
        return buildCmd(31, protocolType, dataType);
    }

    static byte[] builderTimeCmd(int protocolType) throws ParseException {
        return buildCmd(32, protocolType, builderTimeData());
    }

    static int[] builderTimeData() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        long differ = (dateFormat.parse(dateFormat.format(new Date())).getTime() - dateFormat
                .parse("2000-01-01").getTime()) / 1000;
        int[] timeData = new int[4];
        for (int i = 0; i < timeData.length; i++) {
            timeData[i] = (int) (differ >> (i * 8));
        }
        return timeData;
    }
}

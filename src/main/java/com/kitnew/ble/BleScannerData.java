package com.kitnew.ble;

import java.util.ArrayList;
import java.util.List;

public class BleScannerData {
    List<BleScannerDataItem> items = new ArrayList();
    byte[] scanRecords;

    public BleScannerData(byte[] scanRecords) {
        this.scanRecords = scanRecords;
        int index = 0;
        while (index < scanRecords.length) {
            byte length = scanRecords[index];
            if (length != (byte) 0) {
                byte type = scanRecords[index + 1];
                byte[] content = new byte[(length - 1)];
                System.arraycopy(scanRecords, index + 2, content, 0, content.length);
                BleScannerDataItem item = new BleScannerDataItem();
                item.length = length;
                item.type = type;
                item.content = content;
                this.items.add(item);
                index += length + 1;
            } else {
                return;
            }
        }
    }

    public BleScannerDataItem getByType(byte type) {
        for (int i = 0; i < this.items.size(); i++) {
            if (((BleScannerDataItem) this.items.get(i)).type == type) {
                return (BleScannerDataItem) this.items.get(i);
            }
        }
        return null;
    }
}

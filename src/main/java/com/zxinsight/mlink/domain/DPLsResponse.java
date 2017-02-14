package com.zxinsight.mlink.domain;

import com.zxinsight.common.b.a;

import java.util.ArrayList;
import java.util.List;

public class DPLsResponse extends a {
    private Data data;

    public class DPLsData {
        public String dp;
        public String gmk;
        public String k;
    }

    public class Data {
        public  DPLsData       ddl;
        private List<DPLsData> dpls;
        public  String         yyb;

        public void setDPLs(List<DPLsData> list) {
            this.dpls = list;
        }

        public List<DPLsData> getDPLs() {
            return this.dpls != null ? this.dpls : new ArrayList();
        }
    }

    public Data getData() {
        return this.data != null ? this.data : new Data();
    }

    public void setData(Data data) {
        this.data = data;
    }
}

package com.alibaba.fastjson.support.odps;

import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;
import com.tencent.tinker.android.dx.instruction.Opcodes;

public class CodecCheck extends UDF {

    public static class A {
        private int id;
        private String name;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String evaluate() throws Exception {
        A a = new A();
        a.setId(Opcodes.NEG_INT);
        a.setName("xxx");
        JSON.parseObject(JSON.toJSONString(a), A.class);
        return "ok";
    }
}

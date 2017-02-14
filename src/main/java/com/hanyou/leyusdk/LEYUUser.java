package com.hanyou.leyusdk;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LEYUUser {
    public static final int          RELATION_ACTION_ADD    = 1;
    public static final int          RELATION_ACTION_DELETE = 0;
    public static final int          RELATION_TYPE_BOTH     = 1;
    public static final int          RELATION_TYPE_FANS_HIM = 2;
    public static final int          RELATION_TYPE_FANS_ME  = 4;
    public static final int          RELATION_TYPE_NULL     = 3;
    private static      String       host                   = "http://m.miao.cn";
    private             String       access_token           = "";
    private             String       mem_headpic            = "";
    private             String       mem_name               = "游客";
    private             int          mem_sex                = 0;
    private             String       mem_username           = "";
    private             List<Member> members                = new ArrayList();
    private             int          uid                    = 0;
    private             int          userType               = 0;

    class Member {
        private String memHeadPic;
        private int    memId;
        private String memMobile;
        private String memName;
        private int    memUid;

        Member() {
        }

        public int getMemId() {
            return this.memId;
        }

        public void setMemId(int memId) {
            this.memId = memId;
        }

        public int getMemUid() {
            return this.memUid;
        }

        public void setMemUid(int memUid) {
            this.memUid = memUid;
        }

        public String getMemName() {
            return this.memName;
        }

        public void setMemName(String memName) {
            this.memName = memName;
        }

        public String getMemHeadPic() {
            return this.memHeadPic;
        }

        public void setMemHeadPic(String memHeadPic) {
            this.memHeadPic = memHeadPic;
        }

        public String getMemMobile() {
            return this.memMobile;
        }

        public void setMemMobile(String memMobile) {
            this.memMobile = memMobile;
        }
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getmem_name() {
        return this.mem_name;
    }

    public void setmem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getmem_username() {
        return this.mem_username;
    }

    public void setmem_username(String mem_username) {
        this.mem_username = mem_username;
    }

    public String getaccess_token() {
        return this.access_token;
    }

    public void setaccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getmem_sex() {
        return this.mem_sex;
    }

    public void setmem_sex(int mem_sex) {
        this.mem_sex = mem_sex;
    }

    public String getmem_headpic() {
        return this.mem_headpic;
    }

    public void setmem_headpic(String mem_headpic) {
        this.mem_headpic = mem_headpic;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public static LEYUUser fromJSONString(String jsonString) throws JSONException {
        String headPic;
        LEYUUser user = new LEYUUser();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("memArray");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject memJson = jsonArray.getJSONObject(i);
            user.getClass();
            Member member = new Member();
            headPic = memJson.getString("fs_mem_headpic");
            if (!headPic.startsWith(host)) {
                headPic = host + headPic;
            }
            member.setMemHeadPic(headPic);
            member.setMemId(memJson.optInt("fs_mem_id", 0));
            member.setMemMobile(memJson.getString("fs_mem_mobile"));
            member.setMemName(memJson.optString("fs_name", "游客"));
            member.setMemUid(memJson.optInt("fs_mem_userid", 0));
            user.members.add(member);
        }
        user.setaccess_token(jsonObject.getString("access_token"));
        headPic = jsonObject.getString("uheadpic");
        if (!headPic.startsWith(host)) {
            headPic = host + headPic;
        }
        user.setmem_headpic(headPic);
        user.setmem_name(jsonObject.optString(SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME,
                "游客"));
        user.setmem_sex(jsonObject.optInt("usex", 0));
        user.setmem_username(jsonObject.getString(SocializeProtocolConstants
                .PROTOCOL_KEY_USER_NAME2));
        user.setUid(jsonObject.optInt(SocializeProtocolConstants.PROTOCOL_KEY_UID, 0));
        user.setUserType(jsonObject.optInt("utype", 0));
        return user;
    }

    public String toJSONString() {
        Map<String, Object> map = new HashMap();
        JSONArray jsonArray = new JSONArray();
        for (Member member : this.members) {
            map.put("fs_mem_id", Integer.valueOf(member.getMemId()));
            map.put("fs_mem_userid", Integer.valueOf(member.getMemUid()));
            map.put("fs_name", member.getMemName());
            map.put("fs_mem_mobile", member.getMemMobile());
            map.put("fs_mem_headpic", member.getMemHeadPic());
            jsonArray.put(new JSONObject(map));
            map.clear();
        }
        map.put("memArray", jsonArray);
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(this.uid));
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME, this.mem_name);
        map.put("access_token", this.access_token);
        map.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_NAME2, this.mem_username);
        map.put("uheadpic", this.mem_headpic);
        map.put("usex", Integer.valueOf(this.mem_sex));
        map.put("utype", Integer.valueOf(this.userType));
        return new JSONObject(map).toString();
    }
}

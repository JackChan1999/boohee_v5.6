package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.Platform;
import com.mob.tools.FakeActivity;
import com.tencent.open.SocialConstants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.share.domain.BMPlatform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FollowerListFakeActivity extends FakeActivity {
    protected Platform platform;

    public static class FollowersResult {
        public boolean hasNextPage = false;
        public ArrayList<Following> list;
    }

    public static class Following {
        public String atName;
        public boolean checked;
        public String description;
        public String icon;
        public String screenName;
        public String uid;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Platform getPlatform() {
        return this.platform;
    }

    public boolean isRadioMode(String platformName) {
        return "FacebookMessenger".equals(platformName);
    }

    public void setResultForChecked(ArrayList<String> checked) {
        HashMap<String, Object> res = new HashMap();
        res.put("selected", checked);
        res.put("platform", this.platform);
        setResult(res);
    }

    public static FollowersResult parseFollowers(String platformName, HashMap<String, Object> res, HashMap<String, Boolean> uidMap) {
        if (res == null || res.size() <= 0) {
            return null;
        }
        boolean hasNext = false;
        ArrayList<Following> data = new ArrayList();
        Iterator it;
        HashMap<String, Object> user;
        String uid;
        Following following;
        if (BMPlatform.NAME_SINAWEIBO.equals(platformName)) {
            it = ((ArrayList) res.get("users")).iterator();
            while (it.hasNext()) {
                user = (HashMap) it.next();
                uid = String.valueOf(user.get("id"));
                if (!uidMap.containsKey(uid)) {
                    following = new Following();
                    following.uid = uid;
                    following.screenName = String.valueOf(user.get("name"));
                    following.description = String.valueOf(user.get("description"));
                    following.icon = String.valueOf(user.get(SocializeProtocolConstants.PROTOCOL_KEY_FRIENDS_ICON));
                    following.atName = following.screenName;
                    uidMap.put(following.uid, Boolean.valueOf(true));
                    data.add(following);
                }
            }
            hasNext = ((Integer) res.get("total_number")).intValue() > uidMap.size();
        } else if (BMPlatform.NAME_TENCENTWEIBO.equals(platformName)) {
            hasNext = ((Integer) res.get("hasnext")).intValue() == 0;
            it = ((ArrayList) res.get("info")).iterator();
            while (it.hasNext()) {
                HashMap<String, Object> info = (HashMap) it.next();
                uid = String.valueOf(info.get("name"));
                if (!uidMap.containsKey(uid)) {
                    following = new Following();
                    following.screenName = String.valueOf(info.get("nick"));
                    following.uid = uid;
                    following.atName = uid;
                    Iterator it2 = ((ArrayList) info.get("tweet")).iterator();
                    if (it2.hasNext()) {
                        following.description = String.valueOf(((HashMap) it2.next()).get("text"));
                    }
                    following.icon = String.valueOf(info.get("head")) + "/100";
                    uidMap.put(following.uid, Boolean.valueOf(true));
                    data.add(following);
                }
            }
        } else if ("Facebook".equals(platformName)) {
            it = ((ArrayList) res.get("data")).iterator();
            while (it.hasNext()) {
                HashMap<String, Object> d = (HashMap) it.next();
                uid = String.valueOf(d.get("id"));
                if (!uidMap.containsKey(uid)) {
                    following = new Following();
                    following.uid = uid;
                    following.atName = "[" + uid + "]";
                    following.screenName = String.valueOf(d.get("name"));
                    HashMap<String, Object> picture = (HashMap) d.get(SocialConstants.PARAM_AVATAR_URI);
                    if (picture != null) {
                        following.icon = String.valueOf(((HashMap) picture.get("data")).get("url"));
                    }
                    uidMap.put(following.uid, Boolean.valueOf(true));
                    data.add(following);
                }
            }
            hasNext = ((HashMap) res.get("paging")).containsKey("next");
        } else if ("Twitter".equals(platformName)) {
            it = ((ArrayList) res.get("users")).iterator();
            while (it.hasNext()) {
                user = (HashMap) it.next();
                uid = String.valueOf(user.get("screen_name"));
                if (!uidMap.containsKey(uid)) {
                    following = new Following();
                    following.uid = uid;
                    following.atName = uid;
                    following.screenName = String.valueOf(user.get("name"));
                    following.description = String.valueOf(user.get("description"));
                    following.icon = String.valueOf(user.get(SocializeProtocolConstants.PROTOCOL_KEY_FRIENDS_ICON));
                    uidMap.put(following.uid, Boolean.valueOf(true));
                    data.add(following);
                }
            }
        } else if ("FacebookMessenger".equals(platformName)) {
            it = ((ArrayList) res.get("users")).iterator();
            while (it.hasNext()) {
                user = (HashMap) it.next();
                String userAddr = String.valueOf(user.get("jid"));
                if (!uidMap.containsKey(userAddr)) {
                    following = new Following();
                    following.uid = userAddr;
                    following.atName = userAddr;
                    following.screenName = String.valueOf(user.get("name"));
                    uidMap.put(following.uid, Boolean.valueOf(true));
                    data.add(following);
                }
            }
            hasNext = false;
        }
        FollowersResult ret = new FollowersResult();
        ret.list = data;
        ret.hasNextPage = hasNext;
        return ret;
    }
}

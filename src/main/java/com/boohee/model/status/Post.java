package com.boohee.model.status;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class Post {
    public static final String IMAGE_TYPE  = "image";
    public static final String SPORT_TYPE  = "sport";
    public static final String WEIGHT_TYPE = "weight";
    public AttachMent         attachments;
    public String             body;
    public int                comment_count;
    public ArrayList<Comment> comments;
    public String             created_at;
    public String             current_reposted_user;
    public boolean            cut;
    public boolean            disabled;
    public int                envious_count;
    public boolean            favorite;
    public String             feedback;
    public int                id;
    @SerializedName("private")
    public boolean            isPrivate;
    public boolean            isTop;
    public String             original_post_user;
    public boolean            own;
    public ArrayList<Photo>   photos;
    public boolean            repost;
    public boolean            reposted;
    public int                reposted_count;
    public String             type;
    public StatusUser         user;

    public static Post parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (Post) new Gson().fromJson(object.toString(), Post.class);
    }

    @NonNull
    public static ArrayList<Post> parsePosts(String str) {
        try {
            return (ArrayList) new Gson().fromJson(str, new TypeToken<ArrayList<Post>>() {
            }.getType());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public static ArrayList<Post> removeDisablePost(List<Post> posts) {
        if (posts == null || posts.size() <= 0) {
            return null;
        }
        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            if (((Post) iterator.next()).disabled) {
                iterator.remove();
            }
        }
        return (ArrayList) posts;
    }
}

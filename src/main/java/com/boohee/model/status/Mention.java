package com.boohee.model.status;

public class Mention {
    public static final String COMMENT = "comment";
    public static final String POST    = "post";
    public Comment comment;
    public Post    post;
    public String  type;

    public int postId() {
        if (POST.equals(this.type)) {
            return this.post.id;
        }
        return this.comment.post.id;
    }

    public String nickname() {
        if (POST.equals(this.type)) {
            return this.post.user.nickname;
        }
        if ("comment".equals(this.type)) {
            return this.comment.user.nickname;
        }
        return null;
    }

    public String body() {
        if (POST.equals(this.type)) {
            return this.post.body;
        }
        return this.comment.body;
    }

    public String originBody(int user_id) {
        if (!"comment".equals(this.type)) {
            return null;
        }
        if (this.comment.post.user.id == user_id) {
            return "我说：\n" + this.comment.post.body;
        }
        return this.comment.post.user.nickname + "说：\n" + this.comment.post.body;
    }
}

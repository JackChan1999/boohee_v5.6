package com.boohee.api;

import android.content.Context;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;

public class FavoriteApi {
    public static final String ARTICLE_ADD_FAVORITE    = "/api/v1/favorite_articles";
    public static final String ARTICLE_CHECK           = "/api/v1/favorite_articles/check";
    public static final String ARTICLE_DELETE_FAVORITE = "/api/v1/favorite_articles/%d";
    public static final String ARTICLE_GET_FAVORITE    =
            "/api/v1/favorite_articles?page=%1$d&per_page=%2$d";
    public static final String POST_ADD_FAVORITE       = "/api/v1/posts/%d/favorite";
    public static final String POST_DELETE_FAVORITE    = "/api/v1/posts/%d/unfavorite";

    public static void getFavoriteArticle(int page, JsonCallback jsonCallback, Context context) {
        getFavoriteArticle(page, 20, jsonCallback, context);
    }

    public static void getFavoriteArticle(int page, int perPage, JsonCallback jsonCallback,
                                          Context context) {
        BooheeClient.build("status").get(String.format(ARTICLE_GET_FAVORITE, new Object[]{Integer
                .valueOf(page), Integer.valueOf(perPage)}), jsonCallback, context);
    }

    public static void addFavoriteArticle(JsonParams jsonParams, JsonCallback jsonCallback,
                                          Context context) {
        BooheeClient.build("status").post(ARTICLE_ADD_FAVORITE, jsonParams, jsonCallback, context);
    }

    public static void deleteFavoriteArticle(int articleId, JsonCallback jsonCallback, Context
            context) {
        BooheeClient.build("status").delete(String.format(ARTICLE_DELETE_FAVORITE, new
                Object[]{Integer.valueOf(articleId)}), null, jsonCallback, context);
    }

    public static void checkFavoriteArticle(String url, JsonParams params, JsonCallback
            jsonCallback, Context context) {
        BooheeClient.build("status").post(String.format(ARTICLE_CHECK, new Object[]{url}),
                params, jsonCallback, context);
    }

    public static void addFavoritePost(int postId, JsonCallback jsonCallback, Context context) {
        BooheeClient.build("status").post(String.format(POST_ADD_FAVORITE, new Object[]{Integer
                .valueOf(postId)}), null, jsonCallback, context);
    }

    public static void deleteFavoritePost(int postId, JsonCallback jsonCallback, Context context) {
        BooheeClient.build("status").post(String.format(POST_DELETE_FAVORITE, new
                Object[]{Integer.valueOf(postId)}), null, jsonCallback, context);
    }
}

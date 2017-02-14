package com.boohee.api;

import android.content.Context;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;

public class FoodApi {
    public static final String ADD_CUSTOM_MENUS             = "/fb/v1/custom_menus";
    public static final String COLLECTION_DELETE_BATCH      =
            "/v2/favorite_foods/batch_delete?food_ids=%s";
    public static final String COLLECTION_LIST              = "/v2/favorite_foods?page=%d";
    public static final String CUSTOM_COOK_COUNT            = "/fb/v1/custom_menus/count";
    public static final String CUSTOM_DELETE_BATCH          =
            "/v2/custom_foods/batch_delete?ids=%s";
    public static final String CUSTOM_FOOD_COUNT            = "/v2/custom_foods/count";
    public static final String DELETE_CUSTOM_ACTIVITIES     = "/v2/custom_activities/%1$d";
    public static final String DELETE_CUSTOM_COOK           =
            "/fb/v1/custom_menus/batch_delete?ids=%s";
    public static final String DELETE_CUSTOM_FOODS          = "/v2/custom_foods/%1$d";
    public static final String DELETE_FAVORITE              =
            "/v1/users/favorite_food_records/%1$s";
    public static final String FOOD_FAVORITE_COUNT          = "/v2/favorite_foods/count";
    public static final String GET_ACTIVITIES_SEARCH        =
            "/v2/activities/search?q=%1$s&page=%2$s";
    public static final String GET_CUSTOM_ACTIVITIES        = "/v2/custom_activities?page=%1$s";
    public static final String GET_CUSTOM_COOK_DETAIL       = "/fb/v1/custom_menus/%1$d";
    public static final String GET_CUSTOM_FOODS             = "/v2/custom_foods?page=%1$s";
    public static final String GET_CUSTOM_MENUS             = "/fb/v1/custom_menus?page=%1$s";
    public static final String GET_FAVORITE_FOODS           =
            "/v1/users/favorite_food_records?page=%1$d";
    public static final String GET_FOODS_MATERIAL_SEARCH    =
            "/fb/v1/foods/search?q=%1$s&page=%2$s&source=core";
    public static final String GET_FOODS_RECIPE             = "/fb/v1/foods/%1$s/recipe";
    public static final String GET_FOODS_SEARCH             =
            "/fb/v1/foods/search?q=%1$s&page=%2$s";
    public static final String GET_FOOD_DETAIL              = "/fb/v1/foods/%1$s";
    public static final String GET_FOOD_HOT                 = "/v2/ifoods/hot?page=%1$d";
    public static final String GET_FOOD_SHOW_WITH_LIGHT     = "/v2/ifoods/%1$s/show_with_light";
    public static final String GET_FOOD_WITH_BARCODE        = "/fb/v1/foods/barcode?barcode=%s";
    public static final String GET_IS_FAVORITE              =
            "/v1/users/favorite_food_records/%1$s/whether_liked";
    public static final String GET_SUCCESS_UPLOAD_FOOD_LIST = "/fb/v1/food_drafts/verified?page=%d";
    public static final String GET_WHOLE_FOODS_RECIPE       = "/fb/v1/foods/%1$s/recipe/whole";
    public static final String HOST_IMAGE                   = "http://s.boohee.cn";
    public static final String POST_CUSTOM_ACTIVITIES       = "/v2/custom_activities";
    public static final String POST_CUSTOM_FOODS            = "/v2/custom_foods";
    public static final String POST_FAVORITE                = "/v1/users/favorite_food_records";
    public static final String UPLOAD_FOOD                  = "/fb/v1/food_drafts";
    public static final String UPLOAD_FOOD_COUNT            = "/fb/v1/food_drafts/count";
    public static final String UPLOAD_FOOD_LIST             = "/fb/v1/food_drafts?page=%d";

    public static void getSuccessUploadFoodList(int page, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_SUCCESS_UPLOAD_FOOD_LIST, new
                Object[]{Integer.valueOf(page)}), callback, context);
    }

    public static void getFoodsMaterialSearch(Context context, String q, int page, JsonCallback
            callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_FOODS_MATERIAL_SEARCH, new
                Object[]{q, Integer.valueOf(page)}), callback, context);
    }

    public static void getCustomCookCount(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(CUSTOM_COOK_COUNT, callback, context);
    }

    public static void deleteCustomCook(Context context, String id, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).delete(String.format(DELETE_CUSTOM_COOK, new
                Object[]{id}), null, callback, context);
    }

    public static void getCustomCookDetail(Context context, int id, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_CUSTOM_COOK_DETAIL, new
                Object[]{Integer.valueOf(id)}), callback, context);
    }

    public static void getCustomMenus(Context context, int page, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_CUSTOM_MENUS, new
                Object[]{Integer.valueOf(page)}), callback, context);
    }

    public static void addCustomMenus(JsonParams jsonParams, Context context, JsonCallback
            callback) {
        BooheeClient.build(BooheeClient.FOOD).post(ADD_CUSTOM_MENUS, jsonParams, callback, context);
    }

    public static void getFoodWithBarcode(String code, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_FOOD_WITH_BARCODE, new
                Object[]{code}), callback, context);
    }

    public static void uploadFood(JsonParams jsonParams, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).post(UPLOAD_FOOD, jsonParams, callback, context);
    }

    public static void deleteCustomFood(String foodIds, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").delete(String.format(CUSTOM_DELETE_BATCH, new
                Object[]{foodIds}), null, callback, context);
    }

    public static void getUploadFoodList(int page, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(UPLOAD_FOOD_LIST, new
                Object[]{Integer.valueOf(page)}), callback, context);
    }

    public static void deleteCollectionFood(String foodIds, Context context, JsonCallback
            callback) {
        BooheeClient.build("ifood").delete(String.format(COLLECTION_DELETE_BATCH, new
                Object[]{foodIds}), null, callback, context);
    }

    public static void getCollectionFoodList(int page, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(COLLECTION_LIST, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void getUploadFoodCount(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(UPLOAD_FOOD_COUNT, callback, context);
    }

    public static void getFoodFavoriteCount(Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(FOOD_FAVORITE_COUNT, callback, context);
    }

    public static void getCustomFoodCount(Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(CUSTOM_FOOD_COUNT, callback, context);
    }

    public static void getFavoriteFoods(int page, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_FAVORITE_FOODS, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void addFavorite(String code, Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("code", code);
        BooheeClient.build("ifood").post(POST_FAVORITE, jsonParams, callback, context);
    }

    public static void deleteFavorite(String code, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").delete(String.format(DELETE_FAVORITE, new Object[]{code}),
                null, callback, context);
    }

    public static void isFavorite(String code, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_IS_FAVORITE, new Object[]{code}),
                callback, context);
    }

    public static void getFoodHot(int page, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_FOOD_HOT, new Object[]{Integer.valueOf
                (page)}), callback, context);
    }

    public static void getFoodShowWithLight(String code, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_FOOD_SHOW_WITH_LIGHT, new
                Object[]{code}), callback, context);
    }

    public static void getActivitiesSearch(Context context, String q, int page, JsonCallback
            callback) {
        BooheeClient.build("ifood").get(String.format(GET_ACTIVITIES_SEARCH, new Object[]{q,
                Integer.valueOf(page)}), callback, context);
    }

    public static void getFoodsSearch(Context context, String q, int page, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_FOODS_SEARCH, new Object[]{q,
                Integer.valueOf(page)}), callback, context);
    }

    public static void getCustomFoods(Context context, int page, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_CUSTOM_FOODS, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void createCustomFood(JsonParams jsonParams, Context context, JsonCallback
            callback) {
        BooheeClient.build("ifood").post(POST_CUSTOM_FOODS, jsonParams, callback, context);
    }

    public static void deleteCustomFood(int id, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").delete(String.format(DELETE_CUSTOM_FOODS, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void getCustomActivities(Context context, int page, JsonCallback callback) {
        BooheeClient.build("ifood").get(String.format(GET_CUSTOM_ACTIVITIES, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void createCustomActivities(JsonParams jsonParams, Context context,
                                              JsonCallback callback) {
        BooheeClient.build("ifood").post(POST_CUSTOM_ACTIVITIES, jsonParams, callback, context);
    }

    public static void deleteCustomActivities(int id, Context context, JsonCallback callback) {
        BooheeClient.build("ifood").delete(String.format(DELETE_CUSTOM_ACTIVITIES, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void getFoodsRecipe(String code, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_FOODS_RECIPE, new
                Object[]{code}), null, callback, context);
    }

    public static void getWholeFoodsRecipe(String code, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_WHOLE_FOODS_RECIPE, new Object[]{code}), null, callback, context);
    }

    public static void getFoodDetail(String code, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.FOOD).get(String.format(GET_FOOD_DETAIL, new Object[]{code}), null, callback, context);
    }
}

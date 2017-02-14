package cn.sharesdk.framework;

import android.graphics.Bitmap;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import java.util.HashMap;

public abstract class d {
    protected static final String ADDRESS = "address";
    protected static final String AUTHOR = "author";
    protected static final String COMMENT = "comment";
    protected static final String CONTENT_TYPE = "contentType";
    protected static final String CUSTOM_FLAG = "customFlag";
    protected static final String EXECUTE_URL = "executeUrl";
    protected static final String EXT_INFO = "extInfo";
    protected static final String FILE_PATH = "filePath";
    protected static final String GROPU_ID = "gropuId";
    protected static final String HIDDEN = "hidden";
    protected static final String IMAGE_ARRAY = "imageArray";
    protected static final String IMAGE_DATA = "imageData";
    protected static final String IMAGE_PATH = "imagePath";
    protected static final String IMAGE_URL = "imageUrl";
    protected static final String INSTALL_URL = "installUrl";
    protected static final String IS_FAMILY = "isFamily";
    protected static final String IS_FRIEND = "isFriend";
    protected static final String IS_PUBLIC = "isPublic";
    protected static final String IS_SHARE_TENCENT_WEIBO = "isShareTencentWeibo";
    protected static final String LATITUDE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String MUSIC_URL = "musicUrl";
    protected static final String NOTEBOOK = "notebook";
    protected static final String SAFETY_LEVEL = "safetyLevel";
    protected static final String SCENCE = "scene";
    protected static final String SHARE_TYPE = "shareType";
    protected static final String SITE = "site";
    protected static final String SITE_URL = "siteUrl";
    protected static final String STACK = "stack";
    protected static final String TAGS = "tags";
    protected static final String TEXT = "text";
    protected static final String TITLE = "title";
    protected static final String TITLE_URL = "titleUrl";
    protected static final String URL = "url";
    protected static final String VENUE_DESCRIPTION = "venueDescription";
    protected static final String VENUE_NAME = "venueName";
    private HashMap<String, Object> params;

    public d() {
        this.params = new HashMap();
    }

    public d(String str) {
        this(new Hashon().fromJson(str));
    }

    public d(HashMap<String, Object> hashMap) {
        this();
        if (hashMap != null) {
            this.params.putAll(hashMap);
        }
    }

    public <T> T get(String str, Class<T> cls) {
        Object obj = this.params.get(str);
        return obj != null ? cls.cast(obj) : (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) ? cls.cast(new Byte((byte) 0)) : (Short.class.equals(cls) || Short.TYPE.equals(cls)) ? cls.cast(new Short((short) 0)) : (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) ? cls.cast(new Integer(0)) : (Long.class.equals(cls) || Long.TYPE.equals(cls)) ? cls.cast(new Long(0)) : (Float.class.equals(cls) || Float.TYPE.equals(cls)) ? cls.cast(new Float(0.0f)) : (Double.class.equals(cls) || Double.TYPE.equals(cls)) ? cls.cast(new Double(0.0d)) : (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) ? cls.cast(Boolean.valueOf(false)) : null;
    }

    public String getAddress() {
        return (String) get(ADDRESS, String.class);
    }

    public String getAuthor() {
        return (String) get(AUTHOR, String.class);
    }

    public String getComment() {
        return (String) get("comment", String.class);
    }

    public int getContentType() {
        return ((Integer) get(CONTENT_TYPE, Integer.class)).intValue();
    }

    public String[] getCustomFlag() {
        return (String[]) get(CUSTOM_FLAG, String[].class);
    }

    public String getExecuteUrl() {
        return (String) get(EXECUTE_URL, String.class);
    }

    public String getExtInfo() {
        return (String) get(EXT_INFO, String.class);
    }

    public String getFilePath() {
        return (String) get(FILE_PATH, String.class);
    }

    public String getGroupId() {
        return (String) get(GROPU_ID, String.class);
    }

    public int getHidden() {
        return ((Integer) get(HIDDEN, Integer.class)).intValue();
    }

    public String[] getImageArray() {
        return (String[]) get(IMAGE_ARRAY, String[].class);
    }

    public Bitmap getImageData() {
        return (Bitmap) get(IMAGE_DATA, Bitmap.class);
    }

    public String getImagePath() {
        return (String) get(IMAGE_PATH, String.class);
    }

    public String getImageUrl() {
        return (String) get("imageUrl", String.class);
    }

    public String getInstallUrl() {
        return (String) get(INSTALL_URL, String.class);
    }

    public float getLatitude() {
        return ((Float) get("latitude", Float.class)).floatValue();
    }

    public float getLongitude() {
        return ((Float) get("longitude", Float.class)).floatValue();
    }

    public String getMusicUrl() {
        return (String) get(MUSIC_URL, String.class);
    }

    public String getNotebook() {
        return (String) get(NOTEBOOK, String.class);
    }

    public int getSafetyLevel() {
        return ((Integer) get(SAFETY_LEVEL, Integer.class)).intValue();
    }

    public int getScence() {
        return ((Integer) get(SCENCE, Integer.class)).intValue();
    }

    public int getShareType() {
        return ((Integer) get(SHARE_TYPE, Integer.class)).intValue();
    }

    public String getSite() {
        return (String) get("site", String.class);
    }

    public String getSiteUrl() {
        return (String) get(SITE_URL, String.class);
    }

    public String getStack() {
        return (String) get(STACK, String.class);
    }

    public String[] getTags() {
        return (String[]) get("tags", String[].class);
    }

    public String getText() {
        return (String) get("text", String.class);
    }

    public String getTitle() {
        return (String) get("title", String.class);
    }

    public String getTitleUrl() {
        return (String) get(TITLE_URL, String.class);
    }

    public String getUrl() {
        return (String) get("url", String.class);
    }

    public String getVenueDescription() {
        return (String) get(VENUE_DESCRIPTION, String.class);
    }

    public String getVenueName() {
        return (String) get(VENUE_NAME, String.class);
    }

    public boolean isFamily() {
        return ((Boolean) get(IS_FAMILY, Boolean.class)).booleanValue();
    }

    public boolean isFriend() {
        return ((Boolean) get(IS_FRIEND, Boolean.class)).booleanValue();
    }

    public boolean isPublic() {
        return ((Boolean) get(IS_PUBLIC, Boolean.class)).booleanValue();
    }

    public boolean isShareTencentWeibo() {
        return ((Boolean) get(IS_SHARE_TENCENT_WEIBO, Boolean.class)).booleanValue();
    }

    public void set(String str, Object obj) {
        this.params.put(str, obj);
    }

    public void setAddress(String str) {
        set(ADDRESS, str);
    }

    public void setAuthor(String str) {
        set(AUTHOR, str);
    }

    public void setComment(String str) {
        set("comment", str);
    }

    public void setContentType(int i) {
        set(CONTENT_TYPE, Integer.valueOf(i));
    }

    public void setCustomFlag(String[] strArr) {
        set(CUSTOM_FLAG, strArr);
    }

    public void setExecuteUrl() {
        set(EXECUTE_URL, String.class);
    }

    public void setExtInfo(String str) {
        set(EXT_INFO, str);
    }

    public void setFamily(boolean z) {
        set(IS_FAMILY, Boolean.valueOf(z));
    }

    public void setFilePath(String str) {
        set(FILE_PATH, str);
    }

    public void setFriend(boolean z) {
        set(IS_FRIEND, Boolean.valueOf(z));
    }

    public void setGroupId(String str) {
        set(GROPU_ID, str);
    }

    public void setHidden(int i) {
        set(HIDDEN, Integer.valueOf(i));
    }

    public void setImageArray(String[] strArr) {
        set(IMAGE_ARRAY, strArr);
    }

    public void setImageData(Bitmap bitmap) {
        set(IMAGE_DATA, bitmap);
    }

    public void setImagePath(String str) {
        set(IMAGE_PATH, str);
    }

    public void setImageUrl(String str) {
        set("imageUrl", str);
    }

    public void setInstallUrl() {
        set(INSTALL_URL, String.class);
    }

    public void setLatitude(float f) {
        set("latitude", Float.valueOf(f));
    }

    public void setLongitude(float f) {
        set("longitude", Float.valueOf(f));
    }

    public void setMusicUrl(String str) {
        set(MUSIC_URL, str);
    }

    public void setNotebook(String str) {
        set(NOTEBOOK, str);
    }

    public void setPublic(boolean z) {
        set(IS_PUBLIC, Boolean.valueOf(z));
    }

    public void setSafetyLevel(int i) {
        set(SAFETY_LEVEL, Integer.valueOf(i));
    }

    public void setScence(int i) {
        set(SCENCE, Integer.valueOf(i));
    }

    public void setShareTencentWeibo(boolean z) {
        set(IS_SHARE_TENCENT_WEIBO, Boolean.valueOf(z));
    }

    public void setShareType(int i) {
        set(SHARE_TYPE, Integer.valueOf(i));
    }

    public void setSite(String str) {
        set("site", str);
    }

    public void setSiteUrl(String str) {
        set(SITE_URL, str);
    }

    public void setStack(String str) {
        set(STACK, str);
    }

    public void setTags(String[] strArr) {
        set("tags", strArr);
    }

    public void setText(String str) {
        set("text", str);
    }

    public void setTitle(String str) {
        set("title", str);
    }

    public void setTitleUrl(String str) {
        set(TITLE_URL, str);
    }

    public void setUrl(String str) {
        set("url", str);
    }

    public void setVenueDescription(String str) {
        set(VENUE_DESCRIPTION, str);
    }

    public void setVenueName(String str) {
        set(VENUE_NAME, str);
    }

    public HashMap<String, Object> toMap() {
        return this.params == null ? new HashMap() : this.params;
    }

    public String toString() {
        try {
            return new Hashon().fromHashMap(this.params);
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }
}

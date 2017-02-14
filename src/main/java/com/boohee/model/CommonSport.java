package com.boohee.model;

import java.io.Serializable;

public class CommonSport extends ModelBase implements Serializable {
    private static final long serialVersionUID = 1619383214413559798L;
    public int    group_id;
    public float  mets;
    public String name;
    public String photo_url;
    public int    score;
    public int    sport_id;
    public int    status;
    public String updated_at;

    public CommonSport(String name, int group_id, int sport_id, float mets, int score, String
            photo_url, int status, String updated_at) {
        this.name = name;
        this.group_id = group_id;
        this.sport_id = sport_id;
        this.mets = mets;
        this.score = score;
        this.photo_url = photo_url;
        this.status = status;
        this.updated_at = updated_at;
    }

    public int calcCalory(float weight) {
        return Math.round((float) ((1.34d * ((double) (this.mets - 1.0f))) * ((double) weight)));
    }

    public int calcCalory(float weight, int duration) {
        return Math.round((float) ((((1.34d * ((double) (this.mets - 1.0f))) * ((double) weight))
                * ((double) duration)) / 60.0d));
    }
}

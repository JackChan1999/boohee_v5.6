package com.boohee.model.status;

import com.boohee.model.ModelBase;

public class MemberShip extends ModelBase {
    public static String CREATOR = "creator";
    public String     created_at;
    public String     role;
    public String     status;
    public StatusUser user;
}

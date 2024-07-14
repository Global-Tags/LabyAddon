package com.rappytv.globaltags.api;

public class ResponseBody {

    // For success
    public String tag;
    public String position;
    public String icon;
    public String[] roles;
    public String message;
    public Ban ban;

    // For errors
    public String error;

    // Other
    public String version;
    public String code;

    public static class Ban {
        public boolean active;
        public String reason;
        public boolean appealable;
    }
}

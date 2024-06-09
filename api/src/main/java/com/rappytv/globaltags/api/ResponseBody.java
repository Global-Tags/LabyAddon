package com.rappytv.globaltags.api;

public class ResponseBody {

    // For success
    public String tag;
    public String position;
    public String icon;
    public boolean admin;
    public String message;
    public Ban ban;

    // For errors
    public String error;

    // Version
    public String version;

    public static class Ban {
        public boolean active;
        public String reason;
    }
}

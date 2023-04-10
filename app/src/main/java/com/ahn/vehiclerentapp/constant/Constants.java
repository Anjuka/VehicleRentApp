package com.ahn.vehiclerentapp.constant;

import java.util.HashMap;

public class Constants {

    public static final String SHARE_PREFERENCE_TAG = "TravelerPreference";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String KEY_MESSAGE = "message";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAsq1WF9w:APA91bHSH5d1WrapUqiNZKJwtj41oPhPBqYzckSkrcREsB_NC2ZM7hyfTMhCyOmxH9kbTNpnz3I3tuPlbE2jCESTe24I7YfyLN4wQnlRRujTVoARVSpgdyLtkzcSMZn7weIs2xiqKNb7"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }
}

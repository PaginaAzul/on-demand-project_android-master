package com.pagin.azul.Firebase.app;

/**
 *
 * Created by siddiqui on 18/07/2018
 * Copyright to Mobulous Technology Pvt. Ltd.
 */

public class Config {
    //global topic receive app wide push notifications
    public static final String TOPIC_GLOBAL="global";
    //broadcast receiver intent filter
    public static final String REGISTRATION_COMPLETE="registrationComplete";
    public static final String PUSH_NOTIFICATION="pushNotificaton";

    //id to handle the notification in the notificaiton trau
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE=101;

    public static final String SHARED_PREF = "ah_firebase";

}

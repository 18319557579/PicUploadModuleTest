package com.example.uploadpic;

import android.app.Application;

public class AppGlobal {
    private AppGlobal(){}

    private volatile static Application instance;

    public static Application getInstance() {
        if (instance == null) {
            synchronized (AppGlobal.class) {
                if (instance == null) {
                    try {
                        instance = (Application)(Class.forName("android.app.ActivityThread")
                                .getMethod("currentApplication")
                                .invoke(null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
}

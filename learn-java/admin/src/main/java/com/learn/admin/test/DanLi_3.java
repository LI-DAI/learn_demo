package com.learn.admin.test;

public class DanLi_3 {

    private static final DanLi_3 instance = new DanLi_3();

    private DanLi_3(){ }

    public static synchronized DanLi_3 getInstance(){
        return instance;
    }
}

package com.learn.admin.test;

public class DanLi_1 {

    private static volatile DanLi_1 instance = null;

    private DanLi_1(){}

    public synchronized DanLi_1 getInstance(){
        if(instance== null){
            return new DanLi_1();
        }
        return instance;
    }
}

package com.learn.admin.test;

public class DanLi_2 {

    private DanLi_2(){}

    private static class children {
        private static final DanLi_2 instance = new DanLi_2();
    }

    public static DanLi_2 getInstance(){
        return children.instance;
    }
}

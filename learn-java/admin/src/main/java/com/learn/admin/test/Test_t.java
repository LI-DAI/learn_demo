package com.learn.admin.test;

public class Test_t {

    public static void main(String[] args) {
        String str = "fdaf";

    }


}

class People{
    String name;
    public People(){
        System.out.println(1);
    }
    public People(String name){
        System.out.println(2);
        this.name= name;
    }
}

class Child extends People{
    People father;
    public Child(String name){
        System.out.println(3);
        this.name = name;
        father = new People(name + "F");
    }
    public Child(){
        System.out.println(4);
    }

}
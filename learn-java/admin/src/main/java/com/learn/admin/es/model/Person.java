package com.learn.admin.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LD
 * @date 2021/5/21 14:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    
    private String id;

    private String name;

    private Integer age;

    public Person(Integer age) {
        this.age = age;
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

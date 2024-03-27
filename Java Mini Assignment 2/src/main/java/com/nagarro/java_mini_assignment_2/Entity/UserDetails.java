package com.nagarro.java_mini_assignment_2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {


    private String name;
    private String gender;
    private String nationality;
    private int age;


    public UserDetails(String name, String gender, String nationality, int age) {
        this.name =  name;
        this.gender = gender;
        this.nationality = nationality;
        this.age = age;
    }

    public UserDetails() {

    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", age=" + age +
                '}';
    }

    // Constructor, getters, and setters
}

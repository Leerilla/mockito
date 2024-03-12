package com.ohgiraffers.mockito.section01;

public class Bank {
    private String name;

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void test(){
        System.out.println("test");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String depositRequest(String name){
        return name+"입금 요청이 있습니다.";

    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                '}';
    }
}

package com.ohgiraffers.mockito.section01;

public class Customer {

    private String name;
    private int age;
    private AccountInformation accountInformation;
    private Bank bank;


    public Customer() {
    }

    public Customer(String name, int age, AccountInformation accountInformation, Bank bank) {
        this.name = name;
        this.age = age;
        this.accountInformation = accountInformation;
        this.bank = bank;
    }

    public String payments(int amount){
        int result = this.accountInformation.getBalance() - amount;

        if(result < 0){
            return "결제가 실패하였습니다.";
        }else{
            this.accountInformation.setBalance(result);
        }
        return "결제가 성공하였습니다.";
    }


    public String deposit(int money){
        int result = accountInformation.deposit(money, this.name);
        return money + "가 입금되어 총 : " + result +"가 되었습니다.";
    }

    public String accontInfo(){
        String result = bank.getName();
        result += " " + accountInformation.getAccount();
        result += " "+ accountInformation.getBalance();

        return result;
    }

    public void exception(){
        try{
            accountInformation.testException();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AccountInformation getAccountInformation() {
        return accountInformation;
    }

    public void setAccountInformation(AccountInformation accountInformation) {
        this.accountInformation = accountInformation;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", accountInformation=" + accountInformation +
                '}';
    }
}

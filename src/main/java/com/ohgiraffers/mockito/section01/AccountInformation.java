package com.ohgiraffers.mockito.section01;

public class AccountInformation {
    private Bank bank;
    private String account;
    private Integer balance;

    public AccountInformation() {
    }

    public AccountInformation(String account, Integer balance, Bank bank) {
        this.account = account;
        this.balance = balance;
        this.bank = bank;
    }

    public void testException() {
        throw new RuntimeException();
    }

    public int deposit(int money, String name){
        if(money < 0){
            throw new IllegalArgumentException("입금 금액은 0원보다 작을 수 없습니다.");
        }
        System.out.println(bank.depositRequest(name));
        this.balance += money;

        return this.balance;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "AccountInformation{" +
                "account='" + account + '\'' +
                ", balance=" + balance +
                '}';
    }
}

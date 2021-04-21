package ru.dinz;

public class Token {
    public int getToken(Account account) {
        return account.hashCode();
    }
}

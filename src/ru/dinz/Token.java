package ru.dinz;

import java.util.Objects;

public class Token {
    private final Integer TOKEN;
    private Account account;

    public Token(Account account) {
        this.account = account;
        TOKEN = generateToken();
    }

    private int generateToken() {
        return account.hashCode();
    }

    public int getToken(Account account) {
        return TOKEN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(TOKEN, token.TOKEN) &&
                Objects.equals(account, token.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TOKEN, account);
    }
}

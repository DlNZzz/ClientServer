package ru.dinz.version2;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Token implements Comparable<Token>, Serializable {
    private Integer token;
    private Account account;
    private static int priority = 9999;

    public Token(Account account) {
        this.account = account;
        token = generateToken();
    }

    private int generateToken() {
        return account.hashCode();
    }

    public int getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(this.token, token.token) &&
                Objects.equals(account, token.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, account);
    }

    @Override
    public int compareTo(Token o) {
        Map<Token, Integer> mapDataBase = Queue.getMap();
        int priority1 = --priority, priority2 = --priority;
        if (mapDataBase.containsKey(this)) {
            priority1 = mapDataBase.get(this);
        }
        if (mapDataBase.containsKey(o)) {
            priority2 = mapDataBase.get(o);
        }
        return priority1 - priority2;
    }
}

/*
@Override
    public int compareTo(Token o) {
        Map<Token, Integer> mapDataBase = Queue.getMap();
        int priority1 = -1, priority2 = -1;
        if (mapDataBase.containsKey(this)) {
            mapDataBase.get(this);
        }
        for (Map.Entry<Token, Integer> entry : mapDataBase.entrySet()) {
            Token token = entry.getKey();
            if (token.equals(o)) {
                priority2 = entry.getValue();
            }
        }
        return priority2 - priority1;
    }
 */
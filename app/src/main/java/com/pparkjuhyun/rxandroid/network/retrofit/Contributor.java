package com.pparkjuhyun.rxandroid.network.retrofit;

public class Contributor {
    String login;
    String url;
    int id;

    @Override
    public String toString() {
        return "login : " + login + "id : " + id + "url : " + url;
    }
}

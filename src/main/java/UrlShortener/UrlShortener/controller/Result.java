package UrlShortener.UrlShortener.controller;

import lombok.Data;

@Data
class Result <T>{
    private T data;
    public Result(T data) {
        this.data = data;
    }
}

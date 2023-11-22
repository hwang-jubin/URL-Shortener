package UrlShortener.UrlShortener.util;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlGenerator {

    @Value("${server.domain}")
    private String domain;

    public String generator(String encodedUrl){
        return "http://"+this.domain+":8080/"+"shorten/"+encodedUrl;
    }

}

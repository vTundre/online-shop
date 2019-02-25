package app.web;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

public class WebHelper {

    public List<String> getTokenFromCookies(Cookie[] cookies){
        List<String> tokens = new ArrayList<>();
        for (Cookie cookie: cookies){
            if (cookie.getName().equals("user-token")){
                tokens.add(cookie.getValue());
            }
        }
        return tokens;
    }
}

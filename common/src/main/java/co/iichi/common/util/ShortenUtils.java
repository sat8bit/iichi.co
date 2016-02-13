package co.iichi.common.util;

import java.io.UnsupportedEncodingException;

public class ShortenUtils {
    public static String nickname(String nickname) {
        if (nickname == null) {
            return "";
        }

        try {

            if (nickname.getBytes("UTF-8").length < 10) {
                return nickname;
            }

            return nickname.substring(0, 4) + "...";

        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

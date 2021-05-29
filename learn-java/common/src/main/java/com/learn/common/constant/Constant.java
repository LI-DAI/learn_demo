package com.learn.common.constant;

/**
 * @author LD
 * @date 2021/5/16 10:01
 */
public interface Constant {

    String REGEX_EMAIL = "^\\s*?(.+)@(.+?)\\s*$";
    String REGEX_PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    String UTF_8 = "UTF-8";
    String AUTHENTICATION = "Token";
    String REFRESH_PREFIX = "refresh_";

    String ACCESS_TOKEN = "access_token";
    String REFRESH_TOKEN = "refresh_token";

    String ANON_CACHE_KEY = "anonymousCache";

    String AUTHORITY_CACHE_PREFIX = "authorities_";

    static String getAuthoritiesCacheName(String username) {
        return AUTHORITY_CACHE_PREFIX + username;
    }
}

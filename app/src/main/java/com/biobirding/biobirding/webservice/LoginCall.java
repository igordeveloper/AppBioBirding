package com.biobirding.biobirding.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.InvalidKeyException;

public class Login extends RequestCall {

    public JSONObject check(String nickname, String password) throws IOException, JSONException, InvalidKeyException {
        super.setRoute("/user/validate");
        super.setHttpURLConnection();
        super.setConRequestProperty("nickname", nickname);
        super.setConRequestProperty("password", password);
        super.setParameters("");
        return super.Response();
    }
}

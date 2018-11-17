package com.biobirding.biobirding.webservice;

import com.biobirding.biobirding.entity.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class LoginCall extends RequestCall {

    public User validate(String nickname, String password) throws IOException, JSONException, InterruptedException{

        super.setRoute("/user/validate");
        super.setHttpURLConnection();
        super.setConRequestProperty("nickname", nickname);
        super.setConRequestProperty("password", password);
        super.setParameters("");
        JSONObject json = super.Response();

        if(json.has("notFound") && json.getString("notFound").equals("true")){
            return null;
        }

        User user = new User();

        if(json.has("userInfo")){
            JSONObject userInfo = json.getJSONObject("userInfo");
            user.setEmail(userInfo.getString("email"));
            user.setNickname(userInfo.getString("nickname"));
            user.setRg(userInfo.getString("rg"));
            user.setAccessLevel(Integer.parseInt(userInfo.getString("accessLevel")));
        }

        return user;
    }

    public Boolean recoverPassword(String email) throws IOException, JSONException, InterruptedException{

        super.setRoute("/user/recoverPassword");
        super.setHttpURLConnection();
        super.setConRequestProperty("email", email);
        super.setParameters("");
        JSONObject json = super.Response();
        return json.has("status") && json.getString("status").equals("true");

    }

    public Boolean updatePassword(User user, String newPassword) throws IOException, JSONException, InterruptedException{

        super.setRoute("/user/updatePassword");
        super.setHttpURLConnection();
        super.setConRequestProperty("nickname", user.getNickname());
        super.setConRequestProperty("password", user.getPassword());
        super.setConRequestProperty("newPassword", newPassword);
        super.setParameters("rg="+user.getRg());
        JSONObject json = super.Response();

        return json.getString("status").equals("true");

    }
}

package com.biobirding.biobirding.webservice;

import android.util.Log;

import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class UserCall extends RequestCall {

    public Boolean insert(User user) throws InterruptedException, IOException, JSONException{

        super.setRoute("/user/insert");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+user.getRg()+
                "&fullName="+user.getFullName()+
                "&email="+user.getEmail()+
                "&nickname="+user.getNickname()+
                "&password="+user.getPassword()+
                "&crBio="+user.getCrBio()+
                "&accessLevel="+user.getAccessLevel());
        JSONObject json = super.Response();

        return json.getString("status").equals("true");
    }


    public Boolean update(User user) throws InterruptedException, IOException, JSONException{

        super.setRoute("/user/update");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg="+user.getRg()+
                "&fullName="+user.getFullName()+
                "&crBio="+user.getCrBio()+
                "&accessLevel="+user.getAccessLevel()+
                "&enabled="+user.getEnabled());
        JSONObject json = super.Response();
        return json.getString("status").equals("true");
    }

    public User select(User selectUser) throws InterruptedException, IOException, JSONException{

        User user = new User();

        super.setRoute("/user/select");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("rg=" + selectUser.getRg());
        JSONObject json = super.Response();

        JSONObject fields = json.getJSONObject("user");
        user.setFullName(fields.getString("fullName"));
        user.setRg(fields.getString("rg"));
        user.setNickname(fields.getString("nickname"));
        user.setEmail(fields.getString("email"));
        user.setCrBio(fields.getString("crBio"));
        user.setAccessLevel(Integer.parseInt(fields.getString("accessLevel")));
        user.setEnabled(Boolean.parseBoolean(fields.getString("enabled")));
        return user;
    }




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

    public ArrayList<User> selectAll() throws IOException, JSONException, InterruptedException {

        super.setRoute("/user/selectAll");
        super.setHttpURLConnection();
        super.setConRequestProperty();
        super.setParameters("");

        JSONObject json = super.Response();

        ArrayList<User> listUsers = new ArrayList<>();
        JSONArray userCall = json.getJSONArray("users");
        for(int i = 0; i < userCall.length(); i++){
            JSONObject finalObject = userCall.getJSONObject(i);
            User user = new User();
            user.setFullName(finalObject.getString("fullName"));
            user.setRg(finalObject.getString("rg"));
            user.setAccessLevel(Integer.parseInt(finalObject.getString("accessLevel")));
            listUsers.add(user);
        }

        return listUsers;
    }

}

package webservice;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class Login extends Call {

    public JSONObject check(String nickname, String password) throws IOException, JSONException {
        super.setRoute("/user/validate");
        super.setHttpURLConnection();
        super.setConRequestProperty("nickname", nickname);
        super.setConRequestProperty("password", password);
        super.setParameters("dasda");
        return super.Response();
    }
}

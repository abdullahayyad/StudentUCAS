package ps.edu.ucas.portal.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ayyad on 6/3/17.
 */

public class Authentication {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("expires_in")
    private String expiresIN;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("as:client_id")
    private String clientID;

    @SerializedName("userName")
    private String userName;

    @SerializedName(".issued")
    private String issued;

    @SerializedName(".expires")
    private String expires;


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }


    @Override
    public String toString() {
        return "Authentication{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIN='" + expiresIN + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", clientID='" + clientID + '\'' +
                ", userName='" + userName + '\'' +
                ", issued='" + issued + '\'' +
                ", expires='" + expires + '\'' +
                '}';
    }
}

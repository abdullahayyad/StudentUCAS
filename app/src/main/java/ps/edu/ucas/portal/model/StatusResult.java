package ps.edu.ucas.portal.model;

import com.google.gson.annotations.SerializedName;

public class StatusResult {
    //for authentication
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("error")
    private String error;


    //for operation
    @SerializedName("code")
    private double code = 401.0;
    @SerializedName("message")
    private String message;




    public String getError() {
        return error;
    }

    public String getMsg() {
        return error;
    }

    public codeErrorType getCodeError() {
        return codeErrorType.fromString(code);
    }





    public enum codeErrorType{
        OTHER(401.4),
        INVALID_OR_EXPIRED(401.2),
        DENIED(401.1),
        UNKNOWING(401.3),
        MESSAGE_ERROR(401.0);

        private double error;

        codeErrorType(double error) {
            this.error = error;
        }

        public double getError() {
            return error;
        }

        public static codeErrorType fromString(double code) {
            for (codeErrorType r : codeErrorType.values()) {
                if (r.getError() == code) {
                    return r;
                }
            }
            return OTHER;
        }

    }


    @Override
    public String toString() {
        return "StatusResult{" +
                "errorDescription='" + errorDescription + '\'' +
                ", error='" + error + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

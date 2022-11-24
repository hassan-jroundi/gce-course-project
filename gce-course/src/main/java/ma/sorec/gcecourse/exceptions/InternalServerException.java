package ma.sorec.gcecourse.exceptions;

public class InternalServerException extends Exception {

    String code;
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InternalServerException() {

    }

    public InternalServerException(String code, String message) {
        this.code = code;
        this.message = message;
    }


}

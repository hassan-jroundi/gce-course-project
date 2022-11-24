package ma.sorec.gcecourse.exceptions;

public class ObjectNotFoundException extends RuntimeException {

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

    public ObjectNotFoundException() {

    }

    public ObjectNotFoundException(String code, String message) {
        this.code = code;
        this.message = message;
    }


}

package sample.talentbridge.global.response;

import lombok.Data;

@Data
public class ResponseMessage {

    private StatusEnum status;
    private String message;
    private Object data;

    public ResponseMessage(StatusEnum status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

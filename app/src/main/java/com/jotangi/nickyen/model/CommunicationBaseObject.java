package com.jotangi.nickyen.model;

public class CommunicationBaseObject {

    /**
     * status : true
     * code : 0x0200
     * responseMessage : Login success!
     * status : false
     * code : 0x0201
     * responseMessage : ID or password is wrong!
     * status : false
     * code : 0x0202
     * responseMessage : Exception error!
     * status : false
     * code : 0x0203
     * responseMessage : API parameter is required!
     * status : false
     * code : 0x0204
     * responseMessage : SQL fail!
     */

    private String status;
    private String code;
    private String responseMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}

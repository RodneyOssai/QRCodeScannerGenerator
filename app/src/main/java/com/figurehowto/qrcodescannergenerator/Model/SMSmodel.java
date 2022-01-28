package com.figurehowto.qrcodescannergenerator.Model;

public class SMSmodel {
    private String phoneNumber,smsBody;

    public SMSmodel() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }
}

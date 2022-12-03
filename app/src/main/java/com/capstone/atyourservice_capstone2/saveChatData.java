package com.capstone.atyourservice_capstone2;

public class saveChatData {

    public String sender_uid,dateNow,msgs;

    public saveChatData(){

    }

    public saveChatData(String sender_uid, String dateNow,String msgs){

        this.sender_uid = sender_uid;
        this.dateNow = dateNow;
        this.msgs = msgs;

        return;
    }

}

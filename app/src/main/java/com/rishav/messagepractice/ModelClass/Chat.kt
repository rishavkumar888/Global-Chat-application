package com.rishav.messagepractice.ModelClass

class Chat {
    private var sender:String=""
    private var receiver:String=""
    private var message:String=""
    private var messageid:String=""
    private var isseen:Boolean=false

    constructor()
    constructor(sender: String, receiver: String, message: String, messageid: String, isseen: Boolean) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.messageid = messageid
        this.isseen = isseen
    }

    fun getsender():String{
        return sender
    }
    fun setsender(sender:String){
        this.sender=sender
    }
    fun getreceiver():String{
        return receiver
    }
    fun setreceiver(receiver:String){
        this.receiver=receiver
    }
    fun getmessage():String{
        return message
    }
    fun setmessage(msg:String){
        this.message=msg
    }
    fun getmessageid():String{
        return messageid
    }
    fun setmessageid(id:String){
        this.messageid=id
    }
    fun getisseen():Boolean{
        return isseen
    }
    fun setisseen(isseen:Boolean){
        this.isseen=isseen
    }
}
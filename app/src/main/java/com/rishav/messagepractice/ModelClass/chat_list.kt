package com.rishav.messagepractice.ModelClass

class chat_list {

    private var id:String?=""

    constructor()
    constructor(id: String) {
        this.id = id
    }

    fun getid():String{
        return id!!
    }
    fun setid(id:String)
    {
        this.id=id
    }
}
package com.rishav.messagepractice.ModelClass

class Users {
    private var uid:String=""
    private var username:String=""
    private var search:String=""
    private var profile:String=""
    private var cover:String=""
    private var facebook:String=""
    private var instagram:String=""
    private var website:String=""
    private var status:String=""
    private var description:String=""

    constructor()
    constructor(
        uid: String,
        username: String,
        search: String,
        profile: String,
        cover: String,
        facebook: String,
        instagram: String,
        website: String,
        status:String,
        description:String
    ) {
        this.uid = uid
        this.username = username
        this.search = search
        this.profile = profile
        this.cover = cover
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
        this.status=status
    }

    fun getuid():String{
        return uid
    }
    fun setuid(id:String){
        this.uid=id
    }
    fun getusername():String{
        return username
    }
    fun setusername(user:String){
        this.username=user
    }
    fun getsearch():String{
        return search
    }
    fun setsearch(se:String){
        this.search=se
    }
    fun getprofile():String{
        return profile
    }
    fun setprofile(pro:String){
        this.profile=pro
    }
    fun getcover():String{
        return cover
    }
    fun setcover(cover:String){
        this.cover=cover
    }
    fun getfacebook():String{
        return facebook
    }
    fun setfacebook(fb:String){
        this.facebook=fb
    }
    fun getinstagram():String{
        return instagram
    }
    fun setinstagram(insta:String){
        this.instagram=insta
    }
    fun getwebsite():String{
        return website
    }
    fun setwebsite(wb:String){
        this.website=wb
    }
    fun getstatus():String{
        return status
    }
    fun setstatus(sta:String){
        this.status=sta
    }
    fun getdescription():String{
        return description
    }
    fun setdescription(sta:String){
        this.description=sta
    }
}
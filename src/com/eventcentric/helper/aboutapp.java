package com.eventcentric.helper;

public class aboutapp {
 
    String picurl;
    String title, subtitle, url;


    public aboutapp(String pic, String tit, String subt, String _url) {
   
        this.picurl = pic;
        this.title = tit; this.subtitle = subt; this.url = _url;
    }

    public aboutapp() {
    	   
  
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String nm) {
    	this.picurl = nm;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String nm) {
    	this.title = nm;
    }
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String nm) {
    	this.subtitle = nm;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String nm) {
    	this.url = nm;
    }
}

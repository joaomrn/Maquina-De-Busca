package com.maquinadebusca.app.model;

import java.util.LinkedList;
import java.util.List;

public class UrlsSemente {
  private List<String> urls = new LinkedList ();    

    public UrlsSemente() {
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }  
}

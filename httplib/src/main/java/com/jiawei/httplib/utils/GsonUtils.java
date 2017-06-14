package com.jiawei.httplib.utils;


import com.google.gson.Gson;

public class GsonUtils {

  private static Gson gson = new Gson();

  public static Gson get(){
    return gson;
  }

}

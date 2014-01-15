package com.xstrikers.ganmaquv2.model;

/**
 * Created by LB on 14-1-15.
 */
public class User {

  private int _id;
  private String name;
  private String password;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }
}

package com.seungwoodev.project2;

public class LoginResult {
    private Integer code;
    private String name;
    private String email;   //SerializedName

    public Integer getCode() {return code;}
    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
}

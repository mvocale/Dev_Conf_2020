package com.redhat.example.weather;


import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@SessionScoped
//@RequestScoped
@Named(value = "selectedCountry")
public class SelectedCountry implements Serializable {

    private String code = "en";


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}

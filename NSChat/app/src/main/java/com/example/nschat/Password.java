package com.example.nschat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class Password {
    private String password;
    private ImageView errorPassword;
    private ImageView checkPassword;
    private TextView textPassword;
    public Password(String password, ImageView errorPass, ImageView checkPass, TextView textPassword) {
        this.password = password;
        this.errorPassword=errorPass;
        this.checkPassword=checkPass;
        this.textPassword=textPassword;
    }
    public boolean isValid(){
        if((this.password.length()<8)||(!this.password.matches(".*[0-9].*"))||(!this.password.matches(".*[a-zA-Z].*"))){
            errorPassword.setVisibility(View.VISIBLE);
            checkPassword.setVisibility(View.GONE);
            this.textPassword.setVisibility(View.VISIBLE);
            return false;
        }
        errorPassword.setVisibility(View.GONE);
        checkPassword.setVisibility(View.VISIBLE);
        this.textPassword.setVisibility(View.INVISIBLE);
        return true;
    }
}

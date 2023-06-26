package com.example.nschat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class ConfirmPassword {

    private String password;
    private String confirmPassword;
    private ImageView errorConfirm;
    private ImageView checkConfirm;
    private TextView textConfirm;

    public ConfirmPassword(String password, String confirmPassword, ImageView errorConfirm, ImageView checkConfirm, TextView textConfirm) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.errorConfirm=errorConfirm;
        this.checkConfirm=checkConfirm;
        this.textConfirm=textConfirm;
    }
    public boolean isValid(){
        if((this.confirmPassword.length()<8)||(!this.confirmPassword.matches(".*[0-9].*"))||(!this.confirmPassword.matches(".*[a-zA-Z].*"))||(!this.confirmPassword.equals(this.password))){
            errorConfirm.setVisibility(View.VISIBLE);
            checkConfirm.setVisibility(View.GONE);
            this.textConfirm.setVisibility(View.VISIBLE);
            return false;
        }
        errorConfirm.setVisibility(View.GONE);
        checkConfirm.setVisibility(View.VISIBLE);
        this.textConfirm.setVisibility(View.INVISIBLE);
        return true;
    }
}

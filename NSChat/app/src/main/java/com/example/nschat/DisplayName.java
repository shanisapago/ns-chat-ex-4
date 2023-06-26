package com.example.nschat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
public class DisplayName {
    private String displayName;
    ImageView errorDisplayName;
    ImageView checkDisplayName;
    EditText displayText;
    TextView textDisplayName;

    public DisplayName(EditText display, String displayName, ImageView errorDisplay, ImageView checkDisplay, TextView textDisplayName) {
        this.displayName = displayName;
        this.errorDisplayName=errorDisplay;
        this.checkDisplayName=checkDisplay;
        this.displayText=display;
        this.textDisplayName=textDisplayName;
    }

    public boolean isValid(){
        if(!this.displayName.matches("[a-zA-Z]+")){
            this.errorDisplayName.setVisibility(View.VISIBLE);
            this.checkDisplayName.setVisibility(View.GONE);
            this.displayText.setText("");
            this.textDisplayName.setVisibility(View.VISIBLE);
            return false;
        }
        this.errorDisplayName.setVisibility(View.GONE);
        this.checkDisplayName.setVisibility(View.VISIBLE);
        this.textDisplayName.setVisibility(View.INVISIBLE);
        return true;
    }
}

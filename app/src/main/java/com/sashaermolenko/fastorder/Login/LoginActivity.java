package com.sashaermolenko.fastorder.Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sashaermolenko.fastorder.R;

public class LoginActivity extends AppCompatActivity {

    private String title;
    private Button btn;
    private Intent intent;
    private TextInputLayout input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        title = "Authentication";
        setTitle(title);

        TextView forFont = (TextView) findViewById(R.id.logIn);
        forFont.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));

        btn = (Button) findViewById(R.id.btn_login);
        input = null;
        //todo

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, PassActivity.class);
                //PassActivity.phone = input.getText(); TODO
                startActivity(intent);
                finish();
            }
        });
    }

}

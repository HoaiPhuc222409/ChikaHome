package com.example.chikaapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.model.Token;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView logo, text;
    private final String SharedReferencesFile = "sharedReferencesFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        initialize();

    }

    private void initialize() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                changeLanguage("vi");
                String token = SharedPreferencesUtils.loadToken(getBaseContext());
                if (token.equals("")) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                } else startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
}

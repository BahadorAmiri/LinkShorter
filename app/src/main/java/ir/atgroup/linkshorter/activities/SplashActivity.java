package ir.atgroup.linkshorter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.atgroup.linkshorter.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSplashAnimations();
        goToMainPage();
    }

    private void goToMainPage() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 3500);
    }

    private void startSplashAnimations() {

        AppCompatTextView textView2 = findViewById(R.id.textView2);
        AppCompatTextView textView3 = findViewById(R.id.textView3);

        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textView2.startAnimation(fade_in);
        textView3.startAnimation(fade_in);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
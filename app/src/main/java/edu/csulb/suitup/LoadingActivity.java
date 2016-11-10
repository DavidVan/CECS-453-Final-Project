package edu.csulb.suitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by Mark on 11/9/2016.
 */

public class LoadingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Thread loadingScreen = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.setDuration(500);

                    // Start animating the image
                    final ImageView splash = (ImageView) findViewById(R.id.load_circle);
                    splash.startAnimation(anim);
                    sleep(2000);

                } catch (Exception e) {
                    System.out.println();
                } finally {
                    Intent i = new Intent(LoadingActivity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        loadingScreen.start();

    }
}

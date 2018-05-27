package com.deemons.androidserialport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.deemons.androidserialport.R;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mDisposable = Observable.timer(1, TimeUnit.SECONDS)
            .subscribe(this::jumpToMain,Throwable::printStackTrace);
    }

    private void jumpToMain(Long aLong) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}

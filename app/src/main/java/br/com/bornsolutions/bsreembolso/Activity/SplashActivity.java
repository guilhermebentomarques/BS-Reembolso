package br.com.bornsolutions.bsreembolso.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.bornsolutions.bsreembolso.Activity.LoginActivity;

/**
 * Created by guilherme on 15/04/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}

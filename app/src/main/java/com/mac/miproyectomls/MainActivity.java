package com.mac.miproyectomls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main); // Este es el layout de tu Splash Screen

            // Esperar 3 segundos antes de redirigir
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Redirige a HomeActivity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Termina MainActivity para que no regrese a ella al presionar atrás
                }
            }, 3000); // 3 segundos de Splash
        }
    }
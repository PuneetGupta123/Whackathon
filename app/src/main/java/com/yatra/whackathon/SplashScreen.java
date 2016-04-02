package com.yatra.whackathon;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import com.yatra.whackathon.questions.DateDateQuestionActivity;
import com.yatra.whackathon.questions.NameQuestionActivity;
import com.yatra.whackathon.questions.ToCityQuestionActivity;

public class SplashScreen extends AppCompatActivity {

    static public TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.getDefault());
                    // TODO : Download if language not availaible
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to initialize text to speech engine", Toast.LENGTH_LONG).show();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("SplashScreen", "This is thread");
                    Thread.sleep(500);
                    textToSpeech.speak("Hi, Welcome to Yatra dot com", TextToSpeech.QUEUE_ADD, null, null);
                    while(textToSpeech.isSpeaking()) {
                        Thread.sleep(10);
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent  = new Intent(SplashScreen.this, NameQuestionActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        }).start();


    }

}

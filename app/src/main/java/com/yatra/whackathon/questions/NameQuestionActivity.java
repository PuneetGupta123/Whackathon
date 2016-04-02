package com.yatra.whackathon.questions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yatra.whackathon.R;
import com.yatra.whackathon.SplashScreen;
import com.yatra.whackathon.VoiceRecognitionActivity;

import java.util.ArrayList;

public class NameQuestionActivity extends AppCompatActivity {

    private final int NAME_QUESTION = 1;
    private final int YES_NO_QUESTION = 2;
    private final String nameQuestion="What is your name? Please Speak Now";
    EditText name;
    ImageButton nextQuestion;
    private final String yesNoQuestion="Is it correct? Yes or No?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_question);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //set9eSupportActionBar(toolbar);
        name=(EditText)findViewById(R.id.name);
        nextQuestion=(ImageButton)findViewById(R.id.next_question);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    startTextToSpeech(nameQuestion);
                    while (SplashScreen.textToSpeech.isSpeaking()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(500);
                    startSpeechToText(NAME_QUESTION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfPerson = name.getText().toString();
                if(nameOfPerson.length()>4)
                {
                    Intent intent = new Intent(NameQuestionActivity.this, FromCityQuestionActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void startSpeechToText(final int code) {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                "Speak something...");

        try {
//            startActivityForResult(intent, code);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    VoiceRecognitionActivity activity = new VoiceRecognitionActivity(NameQuestionActivity.this, new VoiceRecognitionActivity.VoiceRecognitionResponse() {
                        @Override
                        public void onError(int errorCode, String errorMessage) {
                            Log.e("NameQuestionActivity", errorMessage);
                            if(NAME_QUESTION == code){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                            startTextToSpeech(nameQuestion);
                                            while (SplashScreen.textToSpeech.isSpeaking()) {
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Thread.sleep(500);
                                            startSpeechToText(NAME_QUESTION);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            }
                        }

                        @Override
                        public void onResult(String result) {
                            switch (code) {
                                case NAME_QUESTION: {
                                    if (null != result) {
//                                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                        String text = result;
                                        name.setText(text);
                                        //txtOutput.setText(text);
                                        String spelling = "";
                                        for (int i = 0; i < text.length(); i++) {
                                            spelling = spelling + text.charAt(i)+" ";
                                        }
                                        String reply = "Hi " + text + " I am spelling your name and that is " + spelling + ". " + yesNoQuestion;
                                        startTextToSpeech(reply);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    while (SplashScreen.textToSpeech.isSpeaking()) {
                                                        Thread.sleep(10);
                                                    }
                                                    Thread.sleep(1000);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                startSpeechToText(YES_NO_QUESTION);
                                            }
                                        }).start();
                                    }
                                    break;
                                }
                                case YES_NO_QUESTION: {
                                    if (null != result) {
                                        //ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                        String text = result;
                                        if (new String("yes").equalsIgnoreCase(text)) {
                                            Intent intent = new Intent(NameQuestionActivity.this, FromCityQuestionActivity.class);
                                            finish();
                                            startActivity(intent);
                                        } else if (new String("no").equalsIgnoreCase(text)) {
                                            Intent intent = new Intent(NameQuestionActivity.this, NameQuestionActivity.class);
                                            finish();
                                            startActivity(intent);
                                        } else {
//                                            startTextToSpeech("Please tell Yes or No");
//                                            startSpeechToText(YES_NO_QUESTION);
                                            startTextToSpeech("Please tell Yes or No");
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        while (SplashScreen.textToSpeech.isSpeaking()) {
                                                            Thread.sleep(10);
                                                        }
                                                        Thread.sleep(100);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    startSpeechToText(YES_NO_QUESTION);
                                                }
                                            }).start();
                                        }
                                        break;

                                    }
                                }
                            }
                        }
                    });
                    activity.show();
                }
            });

        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startTextToSpeech(String question){
        SplashScreen.textToSpeech.speak(question,TextToSpeech.QUEUE_ADD,null,null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NAME_QUESTION: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    //txtOutput.setText(text);
                    String spelling = "";
                    for (int i = 0; i < text.length(); i++) {
                        spelling = spelling + text.charAt(i)+" ";
                    }
                    String reply = "Hi " + text + " I am spelling your name and that is " + spelling + ". " + yesNoQuestion;
                    startTextToSpeech(reply);
                    startSpeechToText(YES_NO_QUESTION);
                }
                break;
            }
            case YES_NO_QUESTION: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    if (new String("yes").equalsIgnoreCase(text)) {
                        //TODO: Intent
                    } else if (new String("no").equalsIgnoreCase(text)) {
                        Intent intent = new Intent(NameQuestionActivity.this, NameQuestionActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        startTextToSpeech("Please tell Yes or No");
                        startSpeechToText(YES_NO_QUESTION);
                    }
                    break;

                }
            }
        }
    }
}

package com.yatra.whackathon.questions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yatra.whackathon.R;
import com.yatra.whackathon.SplashScreen;
import com.yatra.whackathon.Util;
import com.yatra.whackathon.VoiceRecognitionActivity;

import java.text.DateFormatSymbols;

public class DateMonthQuestionActivity extends AppCompatActivity {

    private final int DATE_MONTH = 1;
    private final int YES_NO_QUESTION = 2;
    private final String introduction="In which month you want to fly? Enter your month";
    //private final String nameQuestion="What is your name? Please Speak Now";
    EditText name;
    ImageButton nextQuestion;
    private final String yesNoQuestion="Is it correct? Yes or No?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_month_question);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //set9eSupportActionBar(toolbar);
        name=(EditText)findViewById(R.id.month);
        nextQuestion=(ImageButton)findViewById(R.id.next_question);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    startTextToSpeech(introduction);
                    while (SplashScreen.textToSpeech.isSpeaking()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(500);
                    startSpeechToText(DATE_MONTH);
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
                    //TODO:check city
                    Intent intent = new Intent(DateMonthQuestionActivity.this, DateYearQuestionActivity.class);
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
                    VoiceRecognitionActivity activity = new VoiceRecognitionActivity(DateMonthQuestionActivity.this, new VoiceRecognitionActivity.VoiceRecognitionResponse() {
                        @Override
                        public void onError(int errorCode, String errorMessage) {
                            Log.e("NameQuestionActivity", errorMessage);
                            if(DATE_MONTH == code){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                            startTextToSpeech(introduction);
                                            while (SplashScreen.textToSpeech.isSpeaking()) {
                                                try {
                                                    Thread.sleep(10);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Thread.sleep(500);
                                            startSpeechToText(DATE_MONTH);
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
                                case DATE_MONTH: {
                                    if (null != result) {
//                                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                        String month = result;
                                        int monthNumber = Util.getMonthNumber(month);
                                        name.setText(month);
                                        //txtOutput.setText(text);
//                                        String spelling = "";
//                                        for (int i = 0; i < fromCity.length(); i++) {
//                                            spelling = spelling + fromCity.charAt(i)+" ";
//                                        }
                                        String reply = "Month you selected is " + DateFormatSymbols.getInstance().getMonths()[monthNumber] + ". " + yesNoQuestion;
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
                                            Intent intent = new Intent(DateMonthQuestionActivity.this, DateYearQuestionActivity.class);
                                            //TODO:verify city
                                            finish();
                                            startActivity(intent);
                                        } else if (new String("no").equalsIgnoreCase(text)) {
                                            Intent intent = new Intent(DateMonthQuestionActivity.this, DateMonthQuestionActivity.class);
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
        SplashScreen.textToSpeech.speak(question, TextToSpeech.QUEUE_ADD,null,null);
    }



}

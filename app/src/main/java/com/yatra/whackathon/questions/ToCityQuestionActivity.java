package com.yatra.whackathon.questions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yatra.whackathon.MyVolley;
import com.yatra.whackathon.R;
import com.yatra.whackathon.SplashScreen;
import com.yatra.whackathon.VoiceRecognitionActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ToCityQuestionActivity extends AppCompatActivity {

    private final int TO_CITY = 1;
    String fromCity;
    private final int YES_NO_QUESTION = 2;
    private final String introduction="Awesome, Thank you. Enter your destination city name";
    //private final String nameQuestion="What is your name? Please Speak Now";
    EditText name;
    ImageButton nextQuestion;
    String TAG="ToCityQA";
    private final String yesNoQuestion="Is it correct? Yes or No?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_city_question);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //set9eSupportActionBar(toolbar);
        name=(EditText)findViewById(R.id.city);
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
                    startSpeechToText(TO_CITY);
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
                    Intent intent = new Intent(ToCityQuestionActivity.this, DateDateQuestionActivity.class);
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
                    VoiceRecognitionActivity activity = new VoiceRecognitionActivity(ToCityQuestionActivity.this, new VoiceRecognitionActivity.VoiceRecognitionResponse() {
                        @Override
                        public void onError(int errorCode, String errorMessage) {
                            Log.e("NameQuestionActivity", errorMessage);
                            if(TO_CITY == code){
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
                                            startSpeechToText(TO_CITY);
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
                                case TO_CITY: {
                                    if (null != result) {
//                                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                        fromCity= result;
                                        name.setText(fromCity);
                                        //txtOutput.setText(text);
                                        String spelling = "";
                                        for (int i = 0; i < fromCity.length(); i++) {
                                            spelling = spelling + fromCity.charAt(i)+" ";
                                        }
                                        String reply = "I am spelling the name of city  and that is " + spelling + ". " + yesNoQuestion;
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
                                            Intent intent = new Intent(ToCityQuestionActivity.this, DateDateQuestionActivity.class);
                                            //TODO:verify city
                                            finish();
                                            startActivity(intent);
                                        } else if (new String("no").equalsIgnoreCase(text)) {
                                            Intent intent = new Intent(ToCityQuestionActivity.this, ToCityQuestionActivity.class);
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

    public void authenticateCity(String city)
    {
        MyVolley.init(getApplicationContext());
        RequestQueue queue = MyVolley.getRequestQueue();
        StringRequest myReq = new StringRequest( Request.Method.GET, "http://10.42.0.88:5000/tocity/"+city.toLowerCase()
                , reqSuccessListener(), reqErrorListener()) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(25000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(myReq);
    }

    private Response.Listener<String> reqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Intent intent = new Intent(FromCityQuestionActivity.this, ToCityQuestionActivity.class);
//                finish();
//                startActivity(intent);
                Log.d(TAG,response+" volley success");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONObject airport = jsonObject.getJSONObject("airport");
                        String name = airport.getString("to_airportname");
                        SplashScreen.textToSpeech.speak("You airport is :" + name, TextToSpeech.QUEUE_ADD, null, null);
                        while(SplashScreen.textToSpeech.isSpeaking()) {
                            Thread.sleep(10);
                        }
                        Intent intent = new Intent(ToCityQuestionActivity.this,DateDateQuestionActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        SplashScreen.textToSpeech.speak("No Airport Infomrrmation about this city, Please tell other city.", TextToSpeech.QUEUE_ADD, null, null);
                        while(SplashScreen.textToSpeech.isSpeaking()) {
                            Thread.sleep(10);
                        }
                        Intent intent = new Intent(ToCityQuestionActivity.this,ToCityQuestionActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener reqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"in volley error");
                Log.d(TAG, error.toString());
                Log.d(TAG,error+" volley error");
                SplashScreen.textToSpeech.speak("Error Contacting the server", TextToSpeech.QUEUE_ADD, null, null);
                while(SplashScreen.textToSpeech.isSpeaking()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(ToCityQuestionActivity.this,ToCityQuestionActivity.class);
                finish();
                startActivity(intent);
            }
        };
    }




}

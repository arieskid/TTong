package com.example.view;

import java.util.HashMap;
import java.util.Locale;

import com.example.ttong.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by inhuh on 15. 6. 23..
 */

public class TtsView extends Activity implements TextToSpeech.OnInitListener, View.OnClickListener {

    //create TextToSpeech native object
    private TextToSpeech tts;
    private Button btnSpeak;
    private Button btnStore;
    private EditText txtText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set application layout
        setContentView(R.layout.view_tts);
        //Initialize the tts object
        tts = new TextToSpeech(this, this);
        //Refer 'Speak' button
        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        //Refer 'Store' button
        btnStore = (Button) findViewById(R.id.btnStore);
        //Refer 'Text' control
        txtText = (EditText) findViewById(R.id.txtText);
        //Handle onClick event for button 'Speak'
        btnSpeak.setOnClickListener(this);
        btnStore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSpeak :
                //speak out
                speakOut();
                //store it to a .m4a file
                HashMap<String, String> myHashRender = new HashMap();
                String destFileName = Environment.getExternalStorageDirectory()	+"/test111.m4a";
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, txtText.getText().toString());
                tts.synthesizeToFile(txtText.getText().toString(), myHashRender, destFileName);
                break;
            case R.id.btnStore :
                //System.out.println("Store Button clicked!");
                //HashMap<String, String> myHashRender = new HashMap();
                //String destFileName = "/sdcard/Sounds/test111.wav";
                //myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, txtText.getText().toString());
                //tts.synthesizeToFile(txtText.getText().toString(), myHashRender, destFileName);
                break;
        }
    }

    public void onInit(int status) {
        // TODO Auto-generated method stub
        //TTS is successfully initialized
        if (status == TextToSpeech.SUCCESS) {
            //Setting speech language
            int result = tts.setLanguage(Locale.KOREA);
            //If your device doesn't support language you set above
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //Cook simple toast message with message
                Toast.makeText(this, "Language not support", Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
            //Enable the button - It was disabled in main.xml (Go back and Check it)
            else {
                btnSpeak.setEnabled(true);
            }
            //TTS is not initialized properly
        } else {
            Toast.makeText(this, "TTS Initilization Failed", Toast.LENGTH_LONG).show();
            Log.e("TTS", "Initilization Failed");
        }
    }

    private void speakOut() {
        //Get the text typed
        String text = txtText.getText().toString();
        //If no text is typed, tts will read out 'You haven't typed text'
        //else it reads out the text you typed
        if (text.length() == 0) {
            tts.setPitch((float)0.5);
            tts.setSpeechRate((float)0.01);
            tts.speak("하고 싶은 말을 입력해주세요.", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}



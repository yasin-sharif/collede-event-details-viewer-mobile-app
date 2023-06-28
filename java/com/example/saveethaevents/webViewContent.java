package com.example.saveethaevents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class webViewContent extends AppCompatActivity {
    @Override
    public void onCreate(Bundle sis){
        super.onCreate(sis);
        setContentView(R.layout.activity_webview);
        WebView wv=findViewById(R.id.webViewContent);
        Intent intent=getIntent();
        Log.i("event link",intent.getStringExtra("link"));
        wv.loadUrl(intent.getStringExtra("link"));
    }
}

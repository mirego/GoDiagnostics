package com.mirego.godiagnostics.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mirego.godiagnostics.DiagnosticsActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button action = (Button) findViewById(R.id.action_button);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagnosticsActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.mufeed.demosmsmessage;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecurityReceiver extends AppCompatActivity {

    private TextView tvMessageSecurity;
    private TextView tvUserNumber;
//    MediaPlayer soundAlarm = MediaPlayer.create(SecurityReceiver.this,R.raw.sound_alarm);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_receiver);

        tvMessageSecurity = (TextView) findViewById(R.id.tvMessageSecurity);
        tvUserNumber = (TextView) findViewById(R.id.tvUserNumber);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");



        if (message != null)
        {
            String[] parts = message.split(": ");
            String phone = parts[0];
            String body = parts[1];

            tvUserNumber.setText(phone);
            tvMessageSecurity.setText(body);
        }



//        soundAlarm.setLooping(true);
//        soundAlarm.start();

        Button btnMute = (Button)findViewById(R.id.btnMute);
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                soundAlarm.stop();
            }
        });


    }



}

package com.mufeed.demosmsmessage;
import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edTextMessage, edPhoneNumber;
    private Button btnSendMessage;
//    private IntentFilter intentFilter;
    private TextView inText;

    TelephonyManager telephonyManager;
    private String mPhoneNumber = "nothing";
    private Intent serviceIntent;
    public static Location location;
    private static final int ALL_PERMISSION_REQUEST = 104;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent innerIntent) {

//            inText.setText(intent.getStringExtra("mm")+"hello");
            inText.setText(getIntent().getExtras().getString("message"));

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        serviceIntent = new Intent(this, SMS_Service.class);
        startService(serviceIntent);

        inText = (TextView) findViewById(R.id.textMsg);

        btnSendMessage = (Button) findViewById(R.id.btnSend);
        edTextMessage = (EditText) findViewById(R.id.edtextMessage);
        edPhoneNumber = (EditText) findViewById(R.id.edPhoneNumber);

//        final MediaPlayer soundAlarm = MediaPlayer.create(this,R.raw.sound_alarm);
//        soundAlarm.start();
//        soundAlarm.setLooping(true);

        permissionDefinition();


        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myMessage = edTextMessage.getText().toString();
                String phoneNumber = edPhoneNumber.getText().toString();
                sendMessage(phoneNumber, myMessage);
            }
        });

    }


    private void sendMessage(String phoneNumber, String myMessage) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {

            if (location != null) {
                Log.e("Result-Latitude", "" + location.getLatitude());
                Log.e("Result-Longitude", "" + location.getLongitude());

            }

            String SENT = "Message Sent";
            String DELIVERED = "Message Delivered";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
            PendingIntent deliveredIP = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, myMessage, sentPI, deliveredIP);
        } else {
            permissionDefinition();
        }

    }


    private void permissionDefinition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestRuntimePermission();
        } else {
            telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = telephonyManager.getLine1Number();

            MyLocationListener listener = new MyLocationListener();
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // intent to filter for SMS messages received
        }
    }


    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
        }, ALL_PERMISSION_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionDefinition();
                }
                break;
        }

    }


    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            MainActivity.location = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}

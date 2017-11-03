package com.mufeed.demosmsmessage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //get message passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages;
        String str = "";

        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus != null ? pdus.length : 0];

            for (int i = 0; i < messages.length; i++)
            {
                //phone number
                messages[i] =SmsMessage.createFromPdu((byte[]) (pdus != null ? pdus[i] : null));
                str += messages[i].getOriginatingAddress();
                str += ": ";
                //message body
                str += messages[i].getMessageBody();
                str += " \n ";
            }


            Intent i = new Intent(context, SecurityReceiver.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // duplicate the activity why?
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("message", str);

            Log.i("hello","Duplicate");

            //send a broadcast intent to update the SMS received in TextView
            Intent broadcastIntent = new Intent("com.mufeed.demosmsmessage");
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message",str);
            context.sendBroadcast(broadcastIntent);

            context.startActivity(i);

        }
    }
}

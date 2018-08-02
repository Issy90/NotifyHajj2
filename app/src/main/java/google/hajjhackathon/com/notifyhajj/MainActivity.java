package google.hajjhackathon.com.notifyhajj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Log;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import external.App42GCMController;
import external.App42GCMService;

public class MainActivity extends AppCompatActivity implements App42GCMController.App42GCMListener {

    private static final String GoogleProjectNo = "4086368785";
    private TextView responseTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseTv = ((TextView) findViewById(R.id.textview1));
        App42API.initialize(
                this,
                "fa46016a9a8c1c1a5d5ebeca1ad93e3de83966a4ec764c65d99bb0ae2152f52d",
                "962641b81ec047d070ecc683e118e250e913dd2d59371f3da323cccc99fb44c3");
        App42Log.setDebug(true);
        App42API.setLoggedInUser("isra.ib@outlook.com") ;
    }

    public void onStart() {
        super.onStart();
        if (App42GCMController.isPlayServiceAvailable(this)) {
            App42GCMController.getRegistrationId(MainActivity.this, GoogleProjectNo, this);
        } else {
            Log.i("App42PushNotification",
                    "No valid Google Play Services APK found.");
        }
    }

    /*
     * * This method is called when a Activty is stop disable all the events if
     * occuring (non-Javadoc)
     *
     * @see android.app.Activity#onStop()
     */
    public void onStop() {
        super.onStop();

    }

    /*
     * This method is called when a Activty is finished or user press the back
     * button (non-Javadoc)
     *
     * @override method of superclass
     *
     * @see android.app.Activity#onDestroy()
     */
    public void onDestroy() {
        super.onDestroy();

    }


    /*
     * called when activity is paused
     *
     * @override method of superclass (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    public void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    /*
     * called when activity is resume
     *
     * @override method of superclass (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    public void onResume() {
        super.onResume();
        String message = getIntent().getStringExtra(
                App42GCMService.ExtraMessage);
        if (message != null)
            Log.d("MainActivity-onResume", "Message Recieved :" + message);
        IntentFilter filter = new IntentFilter(
                App42GCMService.DisplayMessageAction);
        filter.setPriority(2);
        registerReceiver(mBroadcastReceiver, filter);
    }

    final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent
                    .getStringExtra(App42GCMService.ExtraMessage);
            Log.e("BR", "Message Recieved :" + message);
            responseTv.setText(message);

        }
    };


    @Override
    public void onError(String errorMsg) {
        // TODO Auto-generated method stub
        responseTv.setText("Error -" + errorMsg);
    }

    @Override
    public void onGCMRegistrationId(String gcmRegId) {
        // TODO Auto-generated method stub
       // responseTv.setText("Registration Id on GCM--" + gcmRegId);
        App42GCMController.storeRegistrationId(this, gcmRegId);
        if(!App42GCMController.isApp42Registerd(MainActivity.this))
            App42GCMController.registerOnApp42(App42API.getLoggedInUser(), gcmRegId, this);
    }

    @Override
    public void onApp42Response(final String responseMessage) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseTv.setText(responseMessage);
            }
        });
    }

    @Override
    public void onRegisterApp42(final String responseMessage) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseTv.setText(responseMessage);
                App42GCMController.storeApp42Success(MainActivity.this);
            }
        });
    }
}

package orzu.org;


import com.google.android.gms.analytics.CampaignTrackingReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Tracker extends BroadcastReceiver {

    // The name of the referrer string broadcast by Google Play Store.
    private static final String PLAY_STORE_REFERRER_KEY = "referrer";

    @Override
    public void onReceive(Context context, Intent intent) {

        String referrer = intent.getStringExtra(PLAY_STORE_REFERRER_KEY);

        // Do something with the referrer.
        Common.referrer = referrer;
        // When you're done, pass the intent to the Google Analytics Campaign Tracking Receiver.
        Log.wtf("sadasd",referrer);
        new CampaignTrackingReceiver().onReceive(context, intent);

    }
}
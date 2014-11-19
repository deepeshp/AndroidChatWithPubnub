package com.pubnub.examples.pubnubExample10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, PubnubService.class);
        context.startService(intent);
		
	}


}

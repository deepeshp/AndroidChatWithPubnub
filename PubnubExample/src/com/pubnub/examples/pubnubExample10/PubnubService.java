package com.pubnub.examples.pubnubExample10;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class PubnubService extends Service {
	
	String channel = "akosha_channel";
	PowerManager.WakeLock wl = null;
	
    String PUBLISH_KEY = "pub-c-ca7745c0-28e4-454e-b5ba-41b2a65c9906";
    String SUBSCRIBE_KEY = "sub-c-9108564a-6a2a-11e4-915f-02ee2ddab7fe";
    String CIPHER_KEY = "";
    String SECRET_KEY = "sec-c-ZTUzNzU3YzctODYwZS00MGYyLTk1OGMtZDI2OTdmNDcwMjQ0";
    String ORIGIN = "pubsub";
    String AUTH_KEY;
    String UUID;
    Boolean SSL = false;
	
    Pubnub pubnub;
    
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String pnMsg = msg.obj.toString();

            final Toast toast = Toast.makeText(getApplicationContext(), pnMsg, Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 200);

        }
    };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
    private void notifyUser(Object message) {

        Message msg = handler.obtainMessage();

        try {
            final String obj = (String) message;
            msg.obj = obj;
            handler.sendMessage(msg);
          //  Log.i("Received msg : ", obj.toString());
            Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@Override
	public void onCreate() {
		super.onCreate();
		init();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (wl != null) {
            wl.acquire();
           }
        try {
            pubnub.subscribe("akosha_channel", new Callback() {
                @Override
                public void connectCallback(String channel,
                                            Object message) {
                	 notifyUser("CONNECT on channel:" + channel);
                }

                @Override
                public void disconnectCallback(String channel,
                                               Object message) {
                	notifyUser("DISCONNECT on channel:" + channel);
                }

                @Override
                public void reconnectCallback(String channel,
                                              Object message) {
                	 notifyUser("RECONNECT on channel:" + channel);
                }

                @Override
                public void successCallback(String channel,
                                            Object message) {
                	notifyUser(channel + " " + message.toString());
                    //datasrc.add(message.toString());
                    //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, datasrc);
                //    refresh(datasrc);
                    
                }

                @Override
                public void errorCallback(String channel,
                                          PubnubError error) {
                	notifyUser(channel + " " + error.toString());
                }
            });

        } catch (Exception e) {

        }
		
	}
	
	public void init(){

        pubnub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY,
                SECRET_KEY,
                CIPHER_KEY,
                SSL
        );
        pubnub.setCacheBusting(false);
        pubnub.setOrigin(ORIGIN);
        pubnub.setAuthKey(AUTH_KEY);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		  if (wl != null) {
	            wl.release();
	            wl = null;
	        }
	}

	
}

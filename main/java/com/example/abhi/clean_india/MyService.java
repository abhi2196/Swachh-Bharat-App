package com.example.abhi.clean_india;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Abhi on 21-Jan-16.
 */
public class MyService extends Service {
    Handler handler;
    IBinder mBinder;
    private SessionManager session;
    public MyService() {
    }


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://52.89.16.102:8080");
        } catch (URISyntaxException e) {}
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        handler = new Handler();
        session = new SessionManager(this);
        super.onCreate();
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        mSocket.connect();
        mSocket.emit("storeInfo",session.user() );
        mSocket.on("alert", onNewMessage);

        return 1;
    }

    void add(String a)
    {
        //Toast.makeText(this, "Get data"+a, Toast.LENGTH_LONG).show();
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    add(data);
                }
            });
        }
    };


    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

}

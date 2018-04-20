package com.example.tosch.controldevelopers.serverevents;

import java.net.URISyntaxException;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;

public abstract class WebSocketListener implements ServerListener{
    private static final String TAG = WebSocketListener.class.getSimpleName();

    private static Socket socket;

    public WebSocketListener(String uri) {
        try {
            socket = IO.socket(uri);
        } catch (URISyntaxException e) {
            throw new ServerException(e);
        }
        socket.on("add", a -> onAdd(parseStatus(a)));
        socket.on("remove", a -> onRemove(parseStatus(a)));
        socket.on("disconnect", a -> onOff());
        socket.on("update", a -> onUpdate(parseStatus(a)));
        socket.on("connect", a -> onConnect());
    }

    @Override
    public void connect() {
        socket.connect();
    }

    @Override
    public void disconnect() {
        socket.disconnect();
    }

    @Override
    public void off(){
        socket.emit("disconnect");
    }

    @Override
    public void add(String s) {
        socket.emit("add", s);
    }

    @Override
    public void remove(String s) {
        socket.emit("remove", s);
    }


    private String parseStatus(Object[] args) {
        JSONObject json = (JSONObject) args[0];
        Log.d(TAG, json.toString());
        try {
            return json.getString("array");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}


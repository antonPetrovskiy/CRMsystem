package com.example.tosch.controldevelopers.serverevents;

public interface ServerListener {
    void connect();

    void disconnect();

    void off();

    void add(String s);

    void remove(String s);

    void onConnect();

    void onOff();

    void onAdd(String status);

    void onRemove(String status);

    void onUpdate(String s);

}

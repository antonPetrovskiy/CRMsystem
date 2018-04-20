package com.example.tosch.controldevelopers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tosch.controldevelopers.serverevents.ServerListener;
import com.example.tosch.controldevelopers.serverevents.ServerStatus;
import com.example.tosch.controldevelopers.serverevents.WebSocketListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private Button buttonConnect;
    FileWorker fw;
    private WifiManager wifiManager;

    private Button toList;
    private Button toProfile;
    private Spinner wfid;
    private EditText pass;
    private boolean isConnected = false;

    private ServerListener serverListener = new WebSocketListener("http://192.168.137.1") {


        @Override
        public void onConnect() {
            isConnected = true;
        }

        @Override
        public void onOff() {
            isConnected = false;
            serverListener.disconnect();
        }

        @Override
        public void onAdd(String status) {
            fw.writeMac(status);
        }

        @Override
        public void onRemove(String status) {
            fw.removeText(status, fw.getMACFile());
        }

        @Override
        public void onUpdate(String s) {
            fw.updateFile(s);
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        fw = new FileWorker();

        init();
        buttons();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        createSpinner();

        if(fw.readRank()!= null && fw.readRank().equals("admin")){
            toList.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onResume() {
        if(fw.readRank()!= null && fw.readRank().equals("admin")){
            toList.setVisibility(View.VISIBLE);
        }else{
            toList.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        fw.writeViews(!buttonConnect.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDestroy(){
        disconnectWIFI();
        fw.removeFile(fw.getMACFile());
        fw.writeFile("off");
        super.onDestroy();
    }

    public void init(){
        buttonConnect = findViewById(R.id.wifiConnectButton);
        toList = findViewById(R.id.listButton);
        toProfile = findViewById(R.id.profileButton);
        wfid = findViewById(R.id.idText);
        pass = findViewById(R.id.passText);

        toList.setVisibility(View.INVISIBLE);
    }

    private void buttons(){
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected){
                    disconnectWIFI();
                    fw.writeFile("off");
                    //isConnected = false;
                    buttonConnect.setText("Connect");
                    //wfid.setAlpha(0);
                    //pass.setAlpha(0);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You are disconnected", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    connectWIFI();
                    fw.writeFile("on");
                    //isConnected = true;
                    buttonConnect.setText("Disconnect");
                    //wfid.setAlpha(0);
                    //pass.setAlpha(0);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You are connected", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void connectWIFI(){
        String networkSSID = "Connectify-me";
        String networkPass = "p-000363";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        conf.preSharedKey = "\""+ networkPass +"\"";

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
        while(!hasConnection(getApplicationContext())){

        }
        serverListener.connect();

        while(!isConnected){

        }
        serverListener.add(getName());
    }

    public void disconnectWIFI(){
        serverListener.remove(getName());
        fw.removeFile(fw.getMACFile());

        serverListener.off();
        //wifiManager.disconnect();
    }

    public void createSpinner(){
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        List<String> list1 = new LinkedList<>();
        for(WifiConfiguration l:list){
            list1.add(l.toString());
        }
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wfid.setAdapter(adapter);
        // заголовок
        wfid.setPrompt("Title");
        // выделяем элемент
        wfid.setSelection(2);
        // устанавливаем обработчик нажатия
        wfid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public String getName() {
        return fw.readName();
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

}

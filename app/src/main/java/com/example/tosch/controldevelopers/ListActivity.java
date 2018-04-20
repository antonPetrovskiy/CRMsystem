package com.example.tosch.controldevelopers;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tosch.controldevelopers.serverevents.ServerListener;
import com.example.tosch.controldevelopers.serverevents.WebSocketListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<String> nameList;
    ListView listView;
    ArrayAdapter<String> adapter;
    FileWorker fw;
    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
        createList();
        refreshList();
    }

    public void init(){
        listView = findViewById(R.id.listView);
        refreshButton = findViewById(R.id.refreshButton);
        nameList = new ArrayList<>();
        fw = new FileWorker();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
            }
        });
    }

    public void createList(){

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(adapter);

    }

    public void refreshList(){
        nameList = fw.getFileArray(fw.getMACFile());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,nameList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}

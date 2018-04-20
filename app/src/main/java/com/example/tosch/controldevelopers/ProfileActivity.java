package com.example.tosch.controldevelopers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    FileWorker fw;
    ImageView portrait;
    ImageView statusOn;
    ImageView statusOff;
    TextView nameView;

    Spinner positionSpinner;

    String[] data = {"developer", "designer", "human resources", "admin"};
    boolean logined = false;
    String name;
    public static final int REQ_CODE_PICK_PHOTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        fw = new FileWorker();

        initSpinnerPositions();
        refreshStatus();
        refreshName();
        refreshPosition();

    }

    public void init(){
        statusOff = findViewById(R.id.statusOffImage);
        statusOn = findViewById(R.id.statusOnImage);
        portrait = findViewById(R.id.imageProfile);
        nameView = findViewById(R.id.nameView);
        positionSpinner = findViewById(R.id.spinnerPosition);

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "select_audio_file_title"), REQ_CODE_PICK_PHOTO);
            }
        });

        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                alert.setMessage("Set name");

                final EditText input = new EditText(ProfileActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        nameView.setText(value);
                        fw.writeName(value);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

            }
        });


    }

    @Override
    protected void onResume() {
        refreshStatus();
        refreshName();
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri u = data.getData();
                portrait.setImageURI(u);
            }
        }
    }

    public void initSpinnerPositions(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        positionSpinner.setAdapter(adapter);
        // заголовок
        positionSpinner.setPrompt("Title");
        // устанавливаем обработчик нажатия
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position == 3){
                    if(fw.readRank().equals("admin"))
                        return;

                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                    alert.setMessage("Enter password");

                    final EditText input = new EditText(ProfileActivity.this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!input.getText().toString().equals("") && input.getText().toString().equals("12345")){
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Entered as admin", Toast.LENGTH_SHORT);
                                toast.show();
                                logined = true;
                                fw.writeRank("admin");
                            }else{
                                positionSpinner.setSelection(0);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Wrong password!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();
                }else{
                    logined = false;
                    fw.writeRank(data[position]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void refreshStatus(){
        if(fw.getStatus()){
            statusOn.setVisibility(ImageView.VISIBLE);
            statusOff.setVisibility(ImageView.INVISIBLE);
        }else{
            statusOn.setVisibility(ImageView.INVISIBLE);
            statusOff.setVisibility(ImageView.VISIBLE);
        }
    }

    public void refreshName(){

        nameView.setText(fw.readName());
    }

    public void refreshPosition(){
        String s = fw.readRank();
        int n = 0;
        for(int i = 0; i < data.length; i++){
            if(s!= null && s.equals(data[i]))
                n = i;
        }
        positionSpinner.setSelection(n);
    }
}

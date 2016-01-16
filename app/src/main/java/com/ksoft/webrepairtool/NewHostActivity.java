package com.ksoft.webrepairtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewHostActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_host);
        et1= (EditText)findViewById(R.id.editText);
        et2= (EditText)findViewById(R.id.editText2);
        et3= (EditText)findViewById(R.id.editText3);
    }

    public void saveHost(View view){

        int a = 1+1;

        DBHandler dbhandler = new DBHandler(this,null,null,1);

        String host = et1.getText().toString();
        String username = et2.getText().toString();
        String password = et3.getText().toString();

        ConnectionRecord connection = new ConnectionRecord(host,username,password);

        dbhandler.addConnection(connection);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}

package com.ksoft.webrepairtool.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ksoft.webrepairtool.Beans.ConnectionRecord;
import com.ksoft.webrepairtool.DBHandlers.FTPConnectionDBHandler;
import com.ksoft.webrepairtool.R;

public class NewFTPHostActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;

    String updateId;
    FTPConnectionDBHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_host);
        et1= (EditText)findViewById(R.id.editText);
        et2= (EditText)findViewById(R.id.editText2);
        et3= (EditText)findViewById(R.id.editText3);

        dbhandler = new FTPConnectionDBHandler(this,null,null,1);

        Intent intent = getIntent();
        updateId = intent.getStringExtra("updateId");

        if (updateId!=null) {
            ConnectionRecord cr = dbhandler.selectConnectionById(updateId);

            et1.setText(cr.getHost());
            et2.setText(cr.getUserName());
            et3.setText(cr.getPassword());
        }
    }

    public void saveHost(View view){

        String host = et1.getText().toString();
        String username = et2.getText().toString();
        String password = et3.getText().toString();


        ConnectionRecord connection = new ConnectionRecord(updateId,host,username,password);

        if (updateId==null)
            dbhandler.addConnection(connection);
        else
            dbhandler.updateConnection(connection);

        Intent intent = new Intent(this, ListFTPConnectionsActivity.class);
        startActivity(intent);
    }



}

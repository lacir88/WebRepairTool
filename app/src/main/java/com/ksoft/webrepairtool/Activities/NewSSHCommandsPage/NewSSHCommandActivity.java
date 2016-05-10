package com.ksoft.webrepairtool.Activities.NewSSHCommandsPage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ksoft.webrepairtool.Beans.SSHCommand;
import com.ksoft.webrepairtool.DBHandlers.SSHDBHandler;
import com.ksoft.webrepairtool.Activities.ListSSHCommandsActivity;
import com.ksoft.webrepairtool.R;

public class NewSSHCommandActivity extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;

    SSHDBHandler dbhandler;

    String updateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New SSH command");
        setContentView(R.layout.activity_new_sshcommand);

        dbhandler = new SSHDBHandler(this,null,null,1);

        Intent intent = getIntent();
        updateId = intent.getStringExtra("updateId");

        et1= (EditText)findViewById(R.id.editTextHostName);
        et2= (EditText)findViewById(R.id.editTextUserName);
        et3= (EditText)findViewById(R.id.editTextPassword);
        et4= (EditText)findViewById(R.id.editTextCommandName);
        et5= (EditText)findViewById(R.id.editTextCommandString);

        if (updateId!=null) {
            SSHCommand sshCommand = dbhandler.selectCommandById(updateId);

            et1.setText(sshCommand.getHost());
            et2.setText(sshCommand.getUserName());
            et3.setText(sshCommand.getPassword());
            et4.setText(sshCommand.getCommandName());
            et5.setText(sshCommand.getCommandString());
        }
    }

    public void onSave(View view){

        String host = et1.getText().toString();
        String username = et2.getText().toString();
        String password = et3.getText().toString();
        String commandName = et4.getText().toString();
        String commandString = et5.getText().toString();

        SSHCommand sshc = new SSHCommand(updateId,host,username,password,commandName,commandString);

        if (updateId==null)
            dbhandler.addCommand(sshc);
        else
            dbhandler.updateCommand(sshc);

        Intent intent = new Intent(this, ListSSHCommandsActivity.class);
        startActivity(intent);
    }
}

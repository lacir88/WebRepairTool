package com.ksoft.webrepairtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SSHCommandsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Object[] stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sshcommands);
    }

    protected void onResume() {
        super.onResume();
        List<SSHCommand> SSHCommandList = listSSHCommands();
        stringArray = SSHCommandList.toArray();

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView3 = (ListView) findViewById(R.id.listView3);
        listView3.setAdapter(adapter);

        listView3.setOnItemClickListener(this);

    }

    public void newSSHCommand(View view) {
        Intent intent = new Intent(this, NewSSHCommandActivity.class);
        startActivity(intent);
    }

    public List<SSHCommand> listSSHCommands () {
        SSHDBHandler sshDbhandler = new SSHDBHandler(this, null, null, 1);

        return sshDbhandler.findAllCommands();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String item = ((TextView) view).getText().toString();

        /*Intent intent = new Intent(this, SSHCommandExecuteActivity.class);
        intent.putExtra("host", ((SSHCommand)stringArray[position]).getHost());
        intent.putExtra("username", ((SSHCommand) stringArray[position]).getUserName());
        intent.putExtra("password", ((SSHCommand)stringArray[position]).getPassword());
        intent.putExtra("command", ((SSHCommand)stringArray[position]).getCommandString());

        startActivity(intent);*/

    }
}

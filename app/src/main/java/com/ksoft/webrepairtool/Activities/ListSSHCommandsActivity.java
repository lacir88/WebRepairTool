package com.ksoft.webrepairtool.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ksoft.webrepairtool.Beans.SSHCommand;
import com.ksoft.webrepairtool.DBHandlers.SSHDBHandler;
import com.ksoft.webrepairtool.R;
import com.ksoft.webrepairtool.Activities.NewSSHCommandsPage.NewSSHCommandActivity;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

public class ListSSHCommandsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Object[] commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SSH Connections");
        setContentView(R.layout.activity_sshcommands);
    }

    protected void onResume() {
        super.onResume();
        List<SSHCommand> SSHCommandList = listSSHCommands();
        commands = SSHCommandList.toArray();

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, commands);
        ListView listView3 = (ListView) findViewById(R.id.listView3);
        listView3.setAdapter(adapter);

        listView3.setOnItemClickListener(this);
        listView3.setOnItemLongClickListener(this);

    }

    private class SSHConnectionTask extends AsyncTask<SSHCommand, Void, String> {
        protected String doInBackground(SSHCommand... args) {

            SSHCommand sshCommand = args[0];
            JSch jsch = new JSch();
            Session session = null;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                session = jsch.getSession(sshCommand.getUserName(), sshCommand.getHost(), 22);

                session.setPassword(sshCommand.getPassword());

                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);

                session.connect();

                // SSH Channel
                ChannelExec channelssh = (ChannelExec)
                        session.openChannel("exec");
                channelssh.setOutputStream(baos);

                // Execute command
                channelssh.setCommand(sshCommand.getCommandString());
                channelssh.connect();
                //Toast.makeText(getApplicationContext(), "Server recieved request, waiting for reply...", Toast.LENGTH_SHORT).show();
                int timeCounter;
                String proba="";
                int replyLength=0;
                //Addig varunk amig még semmi valasz nem jött
                for (timeCounter = 0; /*timeCounter < 10 */ proba.equals("") ; timeCounter++) {
                    proba = baos.toString();
                    //replyLength=proba.length();
                    android.os.SystemClock.sleep(500);
                }
                //Ha elkezdett jöni, akkor még 2000 milisec van , hogy megérkezzen minden.
                android.os.SystemClock.sleep(2000);

                //String proba = baos.toString();
                channelssh.disconnect();
            } catch (JSchException e) {
                Log.d("WebRepairTool","JSchException");

                if (e.toString().equals("com.jcraft.jsch.JSchException: Auth fail")) {
                    return "Wrong username or password.";
                }
                return "Failed to connect to SSH server.";
            }

            return baos.toString();

        }

        protected void onPostExecute(String reply) {

            Intent intent = new Intent(ListSSHCommandsActivity.this, DisplaySSHReplyActivity.class);
            intent.putExtra("reply", reply);
            startActivity(intent);

        }
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

        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
        new SSHConnectionTask().execute((SSHCommand) commands[position]);

        /*String item = ((TextView) view).getText().toString();
        Intent intent = new Intent(this, DisplaySSHReplyActivity.class);
        intent.putExtra("reply", reply);

        startActivity(intent);*/

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                   int pos, long id) {

        Intent intent = new Intent(this, NewSSHCommandActivity.class);
        intent.putExtra("updateId", ((SSHCommand)commands[pos]).getId() );
        startActivity(intent);
        return true;
    }
}

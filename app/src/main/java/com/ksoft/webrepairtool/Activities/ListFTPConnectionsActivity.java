package com.ksoft.webrepairtool.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ksoft.webrepairtool.Beans.ConnectionRecord;
import com.ksoft.webrepairtool.DBHandlers.FTPConnectionDBHandler;
import com.ksoft.webrepairtool.R;
import com.ksoft.webrepairtool.Activities.RemoteDirectoryBrowserPage.DirecotyBrowserActivity;

import java.util.List;

public class ListFTPConnectionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    Object[] stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("FTPS Connections");
        setContentView(R.layout.activity_main);
    }

    protected void onResume(){
        super.onResume();

        List<ConnectionRecord> cl =  listConnections();
        stringArray = cl.toArray();

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

        listView1.setLongClickable(true);
        listView1.setOnItemClickListener(this);
        listView1.setOnItemLongClickListener(this);

    }

    public void newHost(View view) {
        Intent intent = new Intent(this, NewFTPHostActivity.class);
        startActivity(intent);
    }

    public List<ConnectionRecord> listConnections () {
        FTPConnectionDBHandler dbhandler = new FTPConnectionDBHandler(this,null,null,1);

        return dbhandler.findAllConnectionRecords();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String item = ((TextView) view).getText().toString();

        Intent intent = new Intent(this, DirecotyBrowserActivity.class);
        intent.putExtra("host", ((ConnectionRecord)stringArray[position]).getHost());
        intent.putExtra("username", ((ConnectionRecord) stringArray[position]).getUserName());
        intent.putExtra("password", ((ConnectionRecord)stringArray[position]).getPassword());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                   int pos, long id) {

        Intent intent = new Intent(this, NewFTPHostActivity.class);
        intent.putExtra("updateId", ((ConnectionRecord)stringArray[pos]).getID() );
        startActivity(intent);
        return true;
    }
}

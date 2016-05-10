package com.ksoft.webrepairtool.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ksoft.webrepairtool.R;
import com.ksoft.webrepairtool.Activities.RemoteDirectoryBrowserPage.DirecotyBrowserActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileEditorActivity extends AppCompatActivity {


    String host;
    String username ;
    String password;
    String filename;
    String workdir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit remote file");
        setContentView(R.layout.activity_file_viewer);

        StringBuilder sb= new StringBuilder();
        Intent intent = getIntent();

        host = intent.getStringExtra("host");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        filename = intent.getStringExtra("filename");
        workdir = intent.getStringExtra("workdir");

        try {
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            bufferedReader.close();
            isr.close();
            fis.close();

        } catch (Exception e) {
            Log.d("WebRepairTool", "IOException");
        }


        EditText ev4 = (EditText)findViewById(R.id.editText4);
        String displaystring = sb.toString();/* "@Override\n" +
                "    protected void onCreate(Bundle savedInstanceState) {\n" +
                "        super.onCreate(savedInstanceState);\n" +
                "        setContentView(R.layout.activity_ftpbrowser);\n" +
                "\n" +
                "        Intent intent = getIntent();\n" +
                "        String host = intent.getStringExtra(\"host\");\n" +
                "        String username = intent.getStringExtra(\"username\");\n" +
                "        String password = intent.getStringExtra(\"password\");\n" +
                "\n" +
                "        ListView listView2 = (ListView) findViewById(R.id.listView2);\n" +
                "        listView2.setOnItemClickListener(this);\n" +
                "\n" +
                "        new FtpTask().execute(host, username, password);\n" +
                "    }";*/
        ev4.setText(displaystring);

    }

    private void writeTempFile() {
        //read string from edittext
        EditText ed4 = (EditText)findViewById(R.id.editText4);
        String editTextString = ed4.getText().toString();

        //write temporary file with content of edittext
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(editTextString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(View view) {
        writeTempFile();

        Intent intent = new Intent(this, DirecotyBrowserActivity.class);
        intent.putExtra("host",host);
        intent.putExtra("username",username);
        intent.putExtra("password",password);
        intent.putExtra("shouldBeUploaded",filename);
        intent.putExtra("workdir",workdir);
        startActivity(intent);
    }
}

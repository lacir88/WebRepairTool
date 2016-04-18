package com.ksoft.webrepairtool.RemoteDirectoryBrowserPage;

import android.content.Context;
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

import com.ksoft.webrepairtool.FileEditorActivity;
import com.ksoft.webrepairtool.ListSSHCommandsActivity;
import com.ksoft.webrepairtool.R;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirecotyBrowserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Object[] stringArray;
    List<FileListEntry> incomingFileList;
    FTPSClient ftps;

    String host;
    String username ;
    String password;
    String shouldBeUploaded ;

    private class FtpConnectionTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {

            String host = args[0];
            String username = args[1];
            String password = args[2];
            connectAndLogin(host, username, password);

            return null;

        }

        protected void onPostExecute(String a) {
            /*int size = l.size();
            incomingFileList = l;

            commands = l.toArray();

            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(DirecotyBrowserActivity.this, android.R.layout.simple_list_item_1, commands);
            ListView listView2 = (ListView) findViewById(R.id.listView2);
            listView2.setAdapter(adapter);*/
            //ListView listView2 = (ListView) findViewById(R.id.listView2);
                new FTPListDirTask().execute();


        }
    }

    private class FTPlogoutCloseConnectionTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            try {
                ftps.logout();
            } catch (IOException e) {
                Log.d("WebRepairTool","IOException");
            }
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            return null;
        }
    }

    private class FtpDownloadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {

            String fileName = args[0];

            try {
                FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

                ftps.retrieveFile(fileName, outputStream);

                outputStream.close();
            } catch (FileNotFoundException e){
                //
            }catch (IOException e){
                //
            }
            return fileName;
        }
        protected void onPostExecute(String fileName) {
            new FTPlogoutCloseConnectionTask().execute();
            Intent intent = new Intent(DirecotyBrowserActivity.this, FileEditorActivity.class);
            intent.putExtra("host", host);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            intent.putExtra("filename", fileName);
            startActivity(intent);
        }
    }

    private class FtpUploadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {

            String fileName = args[0];
            Boolean success=false;
            try {
                /*InputStream input;
                input = new FileInputStream(fileName);*/
                FileInputStream input = openFileInput(fileName);
                success = ftps.storeFile(fileName, input);
                input.close();
            } catch (FileNotFoundException e){
                //
            }catch (IOException e){
                //
            }
            return fileName;
        }

        protected void onPostExecute(String fileName) {

            File dir = getFilesDir();
            File file = new File(dir, fileName);
            boolean deleted = file.delete();
        }
    }

    private class FTPListDirTask extends AsyncTask<String, Void, ArrayList<FileListEntry>> {
        protected ArrayList<FileListEntry> doInBackground(String... args) {
            ArrayList<FileListEntry> fileList = new ArrayList<FileListEntry>();
                try {
                    //way back to outer directory
                    fileList.add(new FileListEntry(R.drawable.folder, ".."));
                    String remote = null;
                    for (FTPFile f : ftps.mlistDir(remote)) {
                        //System.out.println(f.getRawListing());
                        //String[] separated = (f.getRawListing()).split(";");

                        int typePicturePath = -1;
                        if (f.isDirectory()) {
                            typePicturePath = R.drawable.folder;
                        }
                        if (f.isFile()) {
                            typePicturePath = R.drawable.file;
                        }
                        if (f.isSymbolicLink()) {
                            typePicturePath = R.drawable.file;
                        }

                        fileList.add(new FileListEntry(typePicturePath, f.getName()));

                        //System.out.println(f.toFormattedString(displayTimeZoneId));
                    }
                } catch (IOException e) {
                    Log.d("WebRepairTool","IOException");
                }

            return fileList;
        }

        protected void onPostExecute(ArrayList<FileListEntry> l) {
            int size = l.size();
            incomingFileList = l;

            stringArray = l.toArray();

            //ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(DirecotyBrowserActivity.this, android.R.layout.simple_list_item_1, stringArray);
            //ListView listView2 = (ListView) findViewById(R.id.listView2);
            //listView2.setAdapter(adapter);



            // if extending Activity
            //setContentView(R.layout.activity_main);

            // 1. pass context and data to the custom adapter
            CustomListAdapter adapter = new CustomListAdapter(DirecotyBrowserActivity.this, l);

            // if extending Activity 2. Get ListView from activity_main.xml
            //ListView listView = (ListView) findViewById(R.id.listview);

            // 3. setListAdapter
            //listView.setAdapter(adapter); if extending Activity
            ListView listView2 = (ListView) findViewById(R.id.listView2);
            listView2.setAdapter(adapter);


        }
    }


    private void connectAndLogin (String server, String username, String password) {

        boolean binaryTransfer = false, error = false;

        String protocol = "SSL";    // SSL/TLS

        ftps = new FTPSClient(protocol);

        try
        {
            int reply;

            ftps.connect(server);
            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftps.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftps.disconnect();
                Toast.makeText(getBaseContext(), "Unable to connect.", Toast.LENGTH_LONG).show();
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            Toast.makeText(getBaseContext(), "Unable to connect.", Toast.LENGTH_LONG).show();
            //e.printStackTrace();
            System.exit(1);
        }

        __main:
        try
        {
            ftps.setBufferSize(1000);

            if (!ftps.login(username, password))
            {
                ftps.logout();
                error = true;

                Toast.makeText(getBaseContext(), "Login failed.", Toast.LENGTH_LONG).show();
                break __main;
            }


            System.out.println("Remote system is " + ftps.getSystemName());

            if (binaryTransfer) ftps.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftps.enterLocalPassiveMode();
            ftps.execPROT("P");


        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
            if (ftps.isConnected())
            {
                try
                {
                    ftps.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpbrowser);

        Intent intent = getIntent();
        host = intent.getStringExtra("host");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        shouldBeUploaded = intent.getStringExtra("shouldBeUploaded");

        ListView listView2 = (ListView) findViewById(R.id.listView2);
        listView2.setOnItemClickListener(this);

        //CustomListAdapter adapter = new CustomListAdapter(this, generateData());


    }

    protected void onResume() {
        super.onResume();
        new FtpConnectionTask().execute(host, username, password);
        if (shouldBeUploaded!=null) {
            new FtpUploadTask().execute(shouldBeUploaded);
        }
    }

    private class FtpChangeWorkingDirTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            try {
                String dirName=args[0];
                if (dirName.equals("..")) {
                    ftps.changeToParentDirectory();
                } else {
                    ftps.changeWorkingDirectory(dirName);
                }
            }
             catch (IOException e) {
                Log.d("WebRepairTool","IOException e");
            }
            return null;
        }

        protected void onPostExecute(String a) {
            new FTPListDirTask().execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (incomingFileList.get(position).getTypePicturePath()==(R.drawable.folder)) {
                String folderName =incomingFileList.get(position).getName();
                new FtpChangeWorkingDirTask().execute(folderName);
        }
        if (incomingFileList.get(position).getTypePicturePath()==(R.drawable.file)) {
            String fileName = incomingFileList.get(position).getName();
            new FtpDownloadTask().execute(fileName);
        }


    }

    public void toSSH(View view) {
        Intent intent = new Intent(this, ListSSHCommandsActivity.class);
        startActivity(intent);
    }
}

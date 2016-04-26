package com.ksoft.webrepairtool.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ksoft.webrepairtool.R;

public class DisplaySSHReplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sshreply);

        Intent i = getIntent();
        String reply = i.getStringExtra("reply");

        TextView tv8= (TextView)findViewById(R.id.textView8);
        tv8.setText(reply);
    }
}

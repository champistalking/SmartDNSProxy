package com.dnasoftware.smartdnsproxy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "IP Changes are getting monitored.", Toast.LENGTH_SHORT).show();

        finish();
    }
}

package com.example.buildpropreader;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView propText = findViewById(R.id.prop_text);

        if (propText != null) {
            propText.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(propReader(), Html.FROM_HTML_MODE_COMPACT) : Html.fromHtml(propReader()));
        } else {
            Toast.makeText(this, "not found text view", Toast.LENGTH_LONG).show();
        }
    }

    private String propReader() {
        Process process;
        try {
            process = new ProcessBuilder().command("/system/bin/getprop")
                    .redirectErrorStream(true).start();
        } catch (IOException e) {
            e.printStackTrace();
            return "<center><b>Cannot found file /system/bin/getprop - failed</b></center>";
        }

        if(process == null) {
            return "<center><b>Cannot found file /system/bin/getprop</b></center>";
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder log = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String newLine = line.replace("[", "").replace("]", "");
                String[] htmlLine = newLine.split(":");

                log.append("<b>").append(htmlLine[0]).append("</b> <br>").append(htmlLine[1]).append("<br><br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        process.destroy();
        return log.toString();
    }
}
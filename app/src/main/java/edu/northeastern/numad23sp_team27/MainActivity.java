package edu.northeastern.numad23sp_team27;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.atYourServiceButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        switch(b.getId()) {
            case R.id.atYourServiceButton:
                //do something
                pingWebService();
                break;
        }
    }

    public void pingWebService() {
        HttpURLConnection urlConnection = null;
        final String url_str = "https://api.edamam.com";
        try {
            URL url = new URL(url_str);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    public String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            Log.d("data", line);
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
}
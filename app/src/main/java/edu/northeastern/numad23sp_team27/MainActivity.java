package edu.northeastern.numad23sp_team27;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String CHANNEL_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.atYourServiceButton).setOnClickListener(this);
        findViewById(R.id.aboutBtn).setOnClickListener(this);
        findViewById(R.id.stickerBtn).setOnClickListener(this);
        findViewById(R.id.projectButton).setOnClickListener(this);
        findViewById(R.id.drawingButton).setOnClickListener(this);
        CHANNEL_ID = "channel ID";
        createNotificationChannel();
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        switch(b.getId()) {
            case R.id.atYourServiceButton:
                //do something
                openNewActivity();
                break;
            case R.id.aboutBtn:
                openAboutMeActivity();
                break;
            case R.id.stickerBtn:
                openStickerActivity();
                break;
            case R.id.projectButton:
                openProjectLoginActivity();
                break;
            case R.id.drawingButton:
                startDrawingActivity();
                break;
        }
    }

    public void openStickerActivity() {
        Intent intent = new Intent(this, StickerActivity.class);
        startActivity(intent);
    }

    public void openAboutMeActivity() {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    public void openNewActivity() {
        Intent intent = new Intent(this, AtYourServiceActivity.class);
        startActivity(intent);
    }

    public void openProjectLoginActivity() {
        Intent intent = new Intent(this, ProjectLogin.class);
        startActivity(intent);
    }

    public void startDrawingActivity() {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

//    public void pingWebService() {
//        HttpURLConnection urlConnection = null;
//        final String url_str = "https://api.edamam.com";
//        try {
//            URL url = new URL(url_str);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            readStream(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null)
//                urlConnection.disconnect();
//        }
//    }

//    public String readStream(InputStream in) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        for (String line = br.readLine(); line != null; line = br.readLine()) {
//            Log.d("data", line);
//            sb.append(line);
//        }
//        in.close();
//        return sb.toString();
//    }
}
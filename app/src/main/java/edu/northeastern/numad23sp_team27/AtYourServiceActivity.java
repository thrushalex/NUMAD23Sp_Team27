package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class AtYourServiceActivity extends AppCompatActivity {
    private final static String APP_ID = "59b5d15b";
    private final static String APP_KEY = "03b411fc092f13b052dce490b2456432";

    Handler pingHandler = new Handler();

    String label;

    Button btn;
    EditText et;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        // get UI elements
        btn = findViewById(R.id.pingWebServiceBtn);
        et = findViewById(R.id.userInputEditText);
        tv = findViewById(R.id.resultTextView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pingWebService();
            }
        });
    }

    public void pingWebService() {
        Thread pingT = new Thread(new GetFromWebService());
        pingT.start();
    }


    class GetFromWebService implements Runnable {
        String lb;

        GetFromWebService() {}
        @Override
        public void run() {
            HttpURLConnection urlConnection = null;

            String userInput = et.getText().toString();
            // Log.i("user_input", userInput);

            // example
            // "https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=59b5d15b&app_key=03b411fc092f13b052dce490b2456432"
            final String url_str = String.format("https://api.edamam.com/api/recipes/v2?type=%s&q=%s&app_id=%s&app_key=%s", "public", userInput, APP_ID, APP_KEY);
            try {
                URL url = new URL(url_str);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();
                if (code !=  200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String res = readStream(in);
                // Log.i("res", res);

                JSONObject jObject = new JSONObject(res);
                JSONArray hits = jObject.getJSONArray("hits");
                JSONObject recipeObj = hits.getJSONObject(0);
                JSONObject recipe = recipeObj.getJSONObject("recipe");
                lb = recipe.getString("label");

                pingHandler.post(() -> {
                    tv.setText(lb);
                });
            } catch (Exception e) {
                e.printStackTrace();

                lb = "something went wrong";
                pingHandler.post(() -> {
                    tv.setText(lb);
                });

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
}
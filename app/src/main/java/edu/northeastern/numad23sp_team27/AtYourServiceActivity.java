package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AtYourServiceActivity extends AppCompatActivity {
    private final static String APP_ID = "59b5d15b";
    private final static String APP_KEY = "03b411fc092f13b052dce490b2456432";

    private RecipeListAdapter recipeListAdapter;
    private ExpandableListView recipeExpandableListView;
    private ArrayList<RecipeResultRow> recipeResultRowList = new ArrayList<>();
    private ArrayList<RecipeResultRow> displayRecipeResultRowList = new ArrayList<>();

    Handler pingHandler = new Handler();
    Button btn;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        // get UI elements
        btn = findViewById(R.id.pingWebServiceBtn);
        et = findViewById(R.id.userInputEditText);
        // tv = findViewById(R.id.resultTextView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pingWebService();
            }
        });

        recipeResultRowList = new ArrayList<RecipeResultRow>();
        displayRecipeResultRowList = new ArrayList<RecipeResultRow>();
        displayList();
        expandAll();
    }

    private void expandAll() {
        int count = recipeListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            recipeExpandableListView.expandGroup(i);
        } //end for (int i = 0; i < count; i++)
    }

    public void displayList() {
        recipeExpandableListView = (ExpandableListView) findViewById(R.id.exapandableRecipeListView);
        recipeListAdapter = new RecipeListAdapter(AtYourServiceActivity.this, recipeResultRowList);
        recipeExpandableListView.setAdapter(recipeListAdapter);
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

                JSONObject jObject = new JSONObject(res);
                JSONArray hits = jObject.getJSONArray("hits");
                if (hits.length() < 1) {
                    pingHandler.post(() -> {
                        recipeResultRowList.clear();
                        lb = String.format("No recipe found for %s", userInput);
                        RecipeResultRow empty = new RecipeResultRow();
                        empty.setRecipe(lb);
                        recipeResultRowList.add(empty);
                        displayList();
                        expandAll();
                    });
                } else {
                        pingHandler.post(() -> {
                            recipeResultRowList.clear();
                            for(int i = 0; i < hits.length(); i++) {
                                JSONObject recipeObject = null;;
                                try {
                                    recipeObject = hits.getJSONObject(i);
                                    JSONObject recipe = null;
                                    recipe = recipeObject.getJSONObject("recipe");
                                    String recipeText = null;
                                    recipeText = recipe.getString("label");
                                    JSONArray ingredients = null;
                                    ingredients = recipe.getJSONArray("ingredients");
                                    ArrayList<RecipeResultRowChild> recipeResultRowChildren = new ArrayList<>();
                                    for(int b = 0; b < ingredients.length(); b++) {
                                        JSONObject ingredientsJSONObject = ingredients.getJSONObject(b);
                                        String ingredientsText = "";
                                        ingredientsText = ingredientsJSONObject.getString("text");
                                        RecipeResultRowChild recipeResultRowChild = new RecipeResultRowChild();
                                        recipeResultRowChild.setRecipeResultRowText(ingredientsText);
                                        recipeResultRowChildren.add(recipeResultRowChild);
                                    }
                                    RecipeResultRow recipeResultRow = new RecipeResultRow();
                                    recipeResultRow.setRecipe(recipeText);
                                    if(!recipeResultRowChildren.isEmpty()){
                                        recipeResultRow.setChildList(recipeResultRowChildren);
                                    }
                                    recipeResultRowList.add(recipeResultRow);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            displayList();
                            expandAll();
                        });

                }
            } catch (Exception e) {
                e.printStackTrace();
                pingHandler.post(() -> {
                    String error = "something went wrong";
                    RecipeResultRow empty = new RecipeResultRow();
                    empty.setRecipe(error);
                    displayList();
                    expandAll();
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
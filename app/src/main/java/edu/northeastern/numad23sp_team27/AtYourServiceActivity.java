package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AtYourServiceActivity extends AppCompatActivity {
    private final static String APP_ID = "59b5d15b";
    private final static String APP_KEY = "03b411fc092f13b052dce490b2456432";
    private final static String RECIPE_KEY = "recipe name";
    private RecipeListAdapter recipeListAdapter;
    private ExpandableListView recipeExpandableListView;
    private List<RecipeResultRow> recipeResultRowList = new ArrayList<>();
    //private List<RecipeResultRow> displayRecipeResultRowList = new ArrayList<>();

    Handler pingHandler = new Handler();

    // String label;

    Button btn;
    EditText et;
    //TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_your_service);

        // get UI elements
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        btn = findViewById(R.id.pingWebServiceBtn);
        et = findViewById(R.id.userInputEditText);
        // tv = findViewById(R.id.resultTextView);

        btn.setOnClickListener(view -> {
            closeKeyboard();
            pingWebService();
        });

        recipeResultRowList = new ArrayList<>();
        if (savedInstanceState != null) {
            recipeResultRowList = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            displayList();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data before orientation change
        ArrayList<RecipeResultRow> rl = new ArrayList<>(recipeResultRowList);
        outState.putParcelableArrayList(RECIPE_KEY, rl);
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void expandAll() {
        int count = recipeListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            //recipeListAdapter.onGroupExpanded(i);
            recipeExpandableListView.expandGroup(i);
        } //end for (int i = 0; i < count; i++)
    }

    public void displayList() {
        recipeExpandableListView = findViewById(R.id.exapandableRecipeListView);
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
                // int size;
                URL url = new URL(url_str);
                urlConnection = (HttpURLConnection) url.openConnection();
                pingHandler.post(() -> findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE));
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
                                JSONObject recipeObject;
                                try {
                                    recipeObject = hits.getJSONObject(i);
                                    JSONObject recipe;
                                    recipe = recipeObject.getJSONObject("recipe");
                                    String recipeText;
                                    recipeText = recipe.getString("label");
                                    JSONArray ingredients = recipe.getJSONArray("ingredientLines");
                                    ArrayList<RecipeResultRowChild> childArr = new ArrayList<>();
                                    RecipeResultRow recipeResultRow = new RecipeResultRow();
                                    recipeResultRow.setRecipe(recipeText);
                                    for (int j = 0; j < ingredients.length(); j++) {
                                        RecipeResultRowChild rc = new RecipeResultRowChild(ingredients.getString(j));
                                        childArr.add(rc);
                                    }
                                    recipeResultRowList.add(recipeResultRow);
                                    if(!childArr.isEmpty()){
                                        recipeResultRow.setChildList(childArr);
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            displayList();
                        });
                }
                pingHandler.post(() -> findViewById(R.id.loadingPanel).setVisibility(View.GONE));
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
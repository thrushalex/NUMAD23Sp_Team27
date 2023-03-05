package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserStickerActivity extends AppCompatActivity implements View.OnClickListener{
    TextView userPage;

    Button stickerPurchase;
    Button stickerWallet;
    Button stickerHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sticker);

        SharedPreferences sharedPreferences = getSharedPreferences("application", MODE_PRIVATE);
        String usernm = sharedPreferences.getString("name", "");

        userPage = findViewById(R.id.userTextView);
        userPage.setText(usernm);

        stickerPurchase = findViewById(R.id.btnStickersPurchase);
        stickerPurchase.setOnClickListener(this);

        stickerWallet = findViewById(R.id.btnStickersWallet);
        stickerWallet.setOnClickListener(this);

        stickerHistory = findViewById(R.id.btnStickersHistory);
        stickerHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.d("UserStickerActivity", "On Click Recieved");
        switch (view.getId()) {
            case R.id.btnStickersPurchase:
                processStickerPurchase();
                break;
            case R.id.btnStickersWallet:
                processStickerWallet();
                break;
            case R.id.btnStickersHistory:
                processStickerHistory();
                break;
            default:
                Log.d("UserStickerActivity", "On Click Recieved for Default");
                break;
        }
    }

    public void processStickerPurchase(){
        Log.d("UserStickerActivity", "processStickerPurchase");
        Intent intent = new Intent(this,StickerPurchase.class);
        this.startActivity(intent);
    }

    public void processStickerWallet(){
        Log.d("UserStickerActivity", "processStickerWallet");
    }

    public void processStickerHistory(){
        Log.d("UserStickerActivity", "processStickerHistory");
    }
}
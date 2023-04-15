package edu.northeastern.numad23sp_team27;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChooseShape extends AppCompatActivity {
    String shape;
    String color;
    String shapeText;
    private EditText userInputEditText;
    private Button confirmButton;
    private Button blackButton;
    private Button blueButton;
    private Button redButton;
    private Button rectangleButton;
    private Button ovalButton;
    private Button triangleButton;
    private Button arrowRightButton;
    private Button arrowLeftButton;
    private Button arrowUpButton;
    private Button arrowDownButton;
    private static final String preferences = "projTalkPreferences";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shape);
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        shape = "rectangle";
        color = "black";

        userInputEditText = findViewById(R.id.textField);
        confirmButton = findViewById(R.id.confirmButton);
        blackButton = findViewById(R.id.blackChip);
        blueButton = findViewById(R.id.blueChip);
        redButton = findViewById(R.id.redChip);
        rectangleButton = findViewById(R.id.rectangleChip);
        ovalButton = findViewById(R.id.ovalChip);
        triangleButton = findViewById(R.id.triangleChip);
        arrowRightButton = findViewById(R.id.arrowRight);
        arrowLeftButton = findViewById(R.id.arrowLeft);
        arrowUpButton = findViewById(R.id.arrowUp);
        arrowDownButton = findViewById(R.id.arrowDown);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapeText = userInputEditText.getText().toString();
                editor.putString("shapeText", shapeText);
                editor.commit();
                finish();
            }
        });
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "black";
                editor.putString("color", "black");
                updateImageView();
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "blue";
                editor.putString("color", "blue");
                updateImageView();
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "red";
                editor.putString("color", "red");
                updateImageView();
            }
        });
        rectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "rectangle";
                editor.putString("shape", "rectangle");
                updateImageView();
            }
        });
        ovalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "oval";
                editor.putString("shape", "oval");
                updateImageView();
            }
        });
        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "triangle";
                editor.putString("shape", "triangle");
                updateImageView();
            }
        });
        arrowLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "arrowLeft";
                editor.putString("shape", "arrowLeft");
                updateImageView();
            }
        });
        arrowRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "arrowRight";
                editor.putString("shape", "arrowRight");
                updateImageView();
            }
        });
        arrowUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "arrowUp";
                editor.putString("shape", "arrowUp");
                updateImageView();
            }
        });
        arrowDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "arrowDown";
                editor.putString("shape", "arrowDown");
                updateImageView();
            }
        });
    }


    public void updateImageView(){
        ImageView shapeIV = findViewById(R.id.imageView2);
        if (shape == "rectangle") {
            if (color == "black") {
                shapeIV.setImageResource(R.drawable.rectangle_black);
            }
            if (color == "blue") {
                shapeIV.setImageResource(R.drawable.rectangle_blue);
            }
            if (color == "red") {
                shapeIV.setImageResource(R.drawable.rectangle_red);
            }
        }
        if (shape == "oval") {
            if (color == "black") {
                shapeIV.setImageResource(R.drawable.oval_black);
            }
            if (color == "blue") {
                shapeIV.setImageResource(R.drawable.oval_blue);
            }
            if (color == "red") {
                shapeIV.setImageResource(R.drawable.oval_red);
            }
        }
        if (shape == "triangle") {
            if (color == "black") {
                shapeIV.setImageResource(R.drawable.triangle_black);
            }
            if (color == "blue") {
                shapeIV.setImageResource(R.drawable.triangle_blue);
            }
            if (color == "red") {
                shapeIV.setImageResource(R.drawable.triangle_red);
            }
        }

    }
}
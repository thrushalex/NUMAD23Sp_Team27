package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ChooseShape extends AppCompatActivity {
    String shape;
    String color;
    private Button blackButton;
    private Button blueButton;
    private Button redButton;
    private Button rectangleButton;
    private Button ovalButton;
    private Button triangleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shape);

        shape = "rectangle";
        color = "black";

        blackButton = findViewById(R.id.blackChip);
        blueButton = findViewById(R.id.blueChip);
        redButton = findViewById(R.id.redChip);
        rectangleButton = findViewById(R.id.rectangleChip);
        ovalButton = findViewById(R.id.ovalChip);
        triangleButton = findViewById(R.id.triangleChip);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "black";
                updateImageView();
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "blue";
                updateImageView();
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = "red";
                updateImageView();
            }
        });
        rectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "rectangle";
                updateImageView();
            }
        });
        ovalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "oval";
                updateImageView();
            }
        });
        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shape = "triangle";
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
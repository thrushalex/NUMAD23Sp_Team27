package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class DrawingActivity extends AppCompatActivity implements View.OnTouchListener {

    private Canvas drawingCanvas;
    private Bitmap drawingBitmap;
    private ImageView drawingImageView;

    private boolean navigateMode;
    private boolean drawMode;
    private boolean canvasMade;
    private int xOffset;
    private int yOffset;
    private ConstraintLayout constraintLayout;
    private GestureDetector gestureDetector;
    private Paint paint;
    private Button saveButton;
    private Button shapeColorButton;
    private Button undoButton;
    private Button redoButton;
    private String shapeName;
    private String color;
    private String shapeText;
    private int xCoordinate;
    private int yCoordinate;
    private int diagramID;
    private ArrayList<String> undoStack;
    private ArrayList<String> redoStack;
    private ArrayList<String> drawCommandsLog;
    private static final String preferences = "projTalkPreferences";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private final int undoRedoMaxSize = 10;
    private DatabaseReference db;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        shapeColorButton = findViewById(R.id.shapeColorSelectButton);
        undoButton = findViewById(R.id.undoButton);
        redoButton = findViewById(R.id.redoButton);
        saveButton = findViewById(R.id.saveDrawingButton);
        drawingImageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        paint = new Paint();
        navigateMode = false;
        drawMode = true;
        canvasMade = false;
        shapeName = "rectangle";
        color = "black";
        shapeText = "";
        drawCommandsLog = new ArrayList<>();
        undoStack = new ArrayList<>();
        redoStack = new ArrayList<>();
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        gestureDetector = new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    scroll("up");
                }

                if (direction==Direction.down){
                    scroll("down");
                }

                if (direction==Direction.left){
                    scroll("left");
                }

                if (direction==Direction.right){
                    scroll("right");
                }
                return true;
            }
        });
        constraintLayout.setOnTouchListener(this);
        Switch onOffSwitch = findViewById(R.id.drawNavigateSwitch);
        onOffSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getApplicationContext(), "Draw mode on", Toast.LENGTH_SHORT).show();
                navigateMode = false;
                drawMode = true;
            } else {
                Toast.makeText(getApplicationContext(), "Draw mode off", Toast.LENGTH_SHORT).show();
                navigateMode = true;
                drawMode = false;
            }
        });
        shapeColorButton.setOnClickListener(view -> startChooseShapeActivity());
        undoButton.setOnClickListener(view -> {
            if (undoStack.size() > 0) {
                if (redoStack.size() == undoRedoMaxSize) {
                    redoStack.remove(redoStack.size() - 1);
                }
                redoStack.add(0,undoStack.get(0));
                undoStack.remove(0);
                drawCommandsLog.remove(drawCommandsLog.size() - 1);
                makeCanvas();
                for (int i = 0; i < drawCommandsLog.size(); i++) {
                    deserializeDraw(drawCommandsLog.get(i));
                    drawShape(xCoordinate, yCoordinate, shapeName, shapeText);
                }
            }
        });
        redoButton.setOnClickListener(view -> {
            if (redoStack.size() > 0) {
                if (undoStack.size() == undoRedoMaxSize) {
                    undoStack.remove(undoStack.size() - 1);
                }
                undoStack.add(redoStack.get(0));
                drawCommandsLog.add(redoStack.get(0));
                redoStack.remove(0);
                makeCanvas();
                for (int i = 0; i < drawCommandsLog.size(); i++) {
                    deserializeDraw(drawCommandsLog.get(i));
                    drawShape(xCoordinate, yCoordinate, shapeName, shapeText);
                }
            }
        });
        saveButton.setOnClickListener(view -> {
            saveDiagram();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShapeColor();
    }

    public void loadShapeColor(){
        String tempColor = sharedpreferences.getString("color", "DEFAULT");
        String tempShape = sharedpreferences.getString("shape", "DEFAULT");
        String tempShapeText = sharedpreferences.getString("shapeText", "DEFAULT");
        if (!tempColor.equals("DEFAULT")) {
            color = tempColor;
            setColor(color);
        }
        if (!tempShape.equals("DEFAULT")) {
            shapeName = tempShape;
        }
        if (!tempShapeText.equals("DEFAULT")) {
            shapeText = tempShapeText;
        }
    }

    public void scroll(String direction) {
        if (Objects.equals(direction, "right")) {
            drawingImageView.scrollBy(-100,0);
            xOffset = xOffset + 100;
        } else if (Objects.equals(direction, "left")) {
            drawingImageView.scrollBy(100,0);
            xOffset = xOffset - 100;
        } else if (Objects.equals(direction, "up")) {
            drawingImageView.scrollBy(0,100);
            yOffset = yOffset - 100;
        } else if (Objects.equals(direction, "down")) {
            drawingImageView.scrollBy(0,-100);
            yOffset = yOffset + 100;
        }
        View v = this.findViewById(android.R.id.content);
        v.invalidate();
    }

    public void setColor(String color){
        if(color.equals("blue")){
            paint.setColor(getResources().getColor(R.color.blue));
        }
        if(color.equals("black")){
            paint.setColor(getResources().getColor(R.color.black));
        }
        if(color.equals("red")){
            paint.setColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(!canvasMade) {
            makeCanvas();
            canvasMade = true;
        }
        if(navigateMode){
            gestureDetector.onTouchEvent(event);
        } else if(drawMode) {
            if (event.getAction() == 0) {
                xCoordinate = (int)event.getX() - xOffset;
                yCoordinate = (int)event.getY() - yOffset;
                drawShape(xCoordinate, yCoordinate,shapeName,shapeText);
                String serializedDrawCommand = serializeDraw(xCoordinate, yCoordinate,shapeName,shapeText);
                if (undoStack.size() == undoRedoMaxSize) {
                    undoStack.remove(undoStack.size() - 1);
                }
                undoStack.add(0,serializedDrawCommand);
                redoStack = new ArrayList<>();
                drawCommandsLog.add(serializedDrawCommand);
            }
        }
        return true;
    }

    public void drawShape(int x, int y, String shapeName, String textToDraw){
        if(shapeName.equals("rectangle")){
            drawRectangle(x,y,textToDraw);
        }
        if(shapeName.equals("oval")){
            drawOval(x,y,textToDraw);
        }
        if(shapeName.equals("triangle")){
            drawTriangle(x,y,textToDraw);
        }
        if(shapeName.equals("arrowLeft")){
            drawArrow(x,y, "left");
        }
        if(shapeName.equals("arrowRight")){
            drawArrow(x,y, "right");
        }
        if(shapeName.equals("arrowUp")){
            drawArrow(x,y, "up");
        }
        if(shapeName.equals("arrowDown")){
            drawArrow(x,y, "down");
        }
    }

    public String serializeDraw(int xCoordinate, int yCoordinate, String shapeName, String shapeText) {
        String drawCommandString = Integer.toString(xCoordinate);
        drawCommandString += ",";
        drawCommandString += Integer.toString(yCoordinate);
        drawCommandString += ",";
        drawCommandString += shapeName;
        if (!shapeText.equals("")) {
            drawCommandString += ",";
            drawCommandString += shapeText;
        }
        return drawCommandString;
    }

    public void deserializeDraw(String serializedDrawString){
        int groupNumber = 1;
        //default to empty string in case text not provided
        shapeText = "";

        String subString = "";
        for (int i = 0; i < serializedDrawString.length(); i++) {
            if (!(serializedDrawString.charAt(i) == ",".charAt(0))) {
                subString += serializedDrawString.charAt(i);
                if (groupNumber == 1) {
                    xCoordinate =  Integer.parseInt(subString);
                } else if (groupNumber == 2) {
                    yCoordinate =  Integer.parseInt(subString);
                } else if (groupNumber == 3) {
                    shapeName =  subString;
                } else if (groupNumber == 4) {
                    shapeText =  subString;
                }
            } else {
                groupNumber += 1;
                subString = "";
            }
        }

    }

    public void drawRectangle(int x, int y, String textToDraw){
        //Draw rectangle
        Rect r = new Rect(x-200, y+100, x+200, y-100);
        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawRect(r,paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 3;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within rectangle dimentions
        for (int i = 0; i < maxRows; i++) {
            int numOfChars = paint.breakText(remainingText,true,width,null);
            drawingCanvas.drawText(remainingText,start,start+numOfChars,r.exactCenterX(),r.bottom + verticalOffset,paint);
            verticalOffset = verticalOffset + offSetSize;
            if (remainingText.length()-numOfChars > 0) {
                remainingText = remainingText.substring(numOfChars);
            } else {
                break;
            }
        }
        v.invalidate();
    }

    public void drawOval(int x, int y, String textToDraw){
        //Draw oval
        Rect r = new Rect(x-200, y+70, x+200, y-70);
        RectF rectF = new RectF(x-250, y+120, x+250, y-120);

        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawOval(rectF, paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 2;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within oval dimensions
        for (int i = 0; i < maxRows; i++) {
            int numOfChars = paint.breakText(remainingText,true,width,null);
            drawingCanvas.drawText(remainingText,start,start+numOfChars,r.exactCenterX(),r.bottom + verticalOffset,paint);
            verticalOffset = verticalOffset + offSetSize;
            if (remainingText.length()-numOfChars > 0) {
                remainingText = remainingText.substring(numOfChars);
            } else {
                break;
            }
        }
        v.invalidate();
    }

    public void drawTriangle(int x, int y, String textToDraw){
        //Draw triangle
        Rect r = new Rect(x-150, y+40, x+150, y-40);
        int width = r.width();
        int height = r.height();
        Path path = new Path();
        path.moveTo(x, y + height*2); // Top
        path.lineTo(x - width, y - height/2); // Bottom left
        path.lineTo(x + width, y - height/2); // Bottom right
        path.lineTo(x, y + height*2); // Back to Top
        path.close();
        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawRect(r,paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 1;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within triangle dimensions
        for (int i = 0; i < maxRows; i++) {
            int numOfChars = paint.breakText(remainingText,true,width,null);
            drawingCanvas.drawText(remainingText,start,start+numOfChars,r.exactCenterX(),r.bottom + verticalOffset,paint);
            verticalOffset = verticalOffset + offSetSize;
            if (remainingText.length()-numOfChars > 0) {
                remainingText = remainingText.substring(numOfChars);
            } else {
                break;
            }
        }
        v.invalidate();
    }

    public void drawArrow(int x, int y, String direction){
        //Draw arrow
        Rect r = new Rect(x-60, y+60, x+60, y-60);
        int width = r.width();
        int height = r.height();
        Path path = new Path();
        if (direction.equals("down")) {
            path.moveTo(x, y - height/2); // Center Top
            path.lineTo(x + width/2, y); // Center Right
            path.lineTo(x + width/4, y); // Center Inner Right
            path.lineTo(x + width/4, y + height/2); // Bottom Inner Right
            path.lineTo(x - width/4, y + height/2); // Bottom Inner Left
            path.lineTo(x - width/4, y); // Center Inner Left
            path.lineTo(x - width/2, y); // Center Left
            path.lineTo(x, y - height/2); // Center Top
        } else if (direction.equals("up")) {
            path.moveTo(x, y + height/2); // Center Bottom
            path.lineTo(x + width/2, y); // Center Right
            path.lineTo(x + width/4, y); // Center Inner Right
            path.lineTo(x + width/4, y - height/2); // Top Inner Right
            path.lineTo(x - width/4, y - height/2); // Top Inner Left
            path.lineTo(x - width/4, y); // Center Inner Left
            path.lineTo(x - width/2, y); // Center Left
            path.lineTo(x, y + height/2); // Center Bottom
        } else if (direction.equals("right")) {
            path.moveTo(x + width/2, y); // Center Right
            path.lineTo(x, y - height/2); // Bottom Center
            path.lineTo(x, y - height/4); // Inner Bottom Center
            path.lineTo(x - width/2, y - height/4); // Inner Bottom Left
            path.lineTo(x - width/2, y + height/4); // Inner Top Left
            path.lineTo(x, y + height/4); // Inner Top Center
            path.lineTo(x, y + height/2); // Top Center
            path.lineTo(x + width/2, y); // Center Right
        } else if (direction.equals("left")) {
            path.moveTo(x - width/2, y); // Center Left
            path.lineTo(x, y - height/2); // Bottom Center
            path.lineTo(x, y - height/4); // Inner Bottom Center
            path.lineTo(x + width/2, y - height/4); // Inner Bottom Right
            path.lineTo(x + width/2, y + height/4); // Inner Top Right
            path.lineTo(x, y + height/4); // Inner Top Center
            path.lineTo(x, y + height/2); // Top Center
            path.lineTo(x - width/2, y); // Center Left
        }
        path.close();
        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawPath(path, paint);
        v.invalidate();
    }


    public void makeCanvas(){
        View v = this.findViewById(R.id.imageView);
        xOffset = (int) v.getX();
        yOffset = (int) v.getY();
        int vWidth = v.getWidth();
        int vHeight = v.getHeight();
        drawingBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(drawingBitmap);
        drawingCanvas = new Canvas(drawingBitmap);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.white));
        drawingCanvas.drawPaint(backgroundPaint);
    }

    public void saveDiagram(){
            db.child("diagrams").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int maxInt = 0;
                    diagramID = 0;
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            int currentID = Integer.parseInt(child.child("diagramID").getValue().toString());
                            if (currentID > maxInt) {
                                maxInt = currentID;
                                diagramID = maxInt + 1;
                            }
                        }
                    }
                    if (diagramID != 0) {
                        Log.i("drawing activity diagram id", Integer.toString(diagramID));
                        editor.putString(Integer.toString(diagramID), String.join("|", drawCommandsLog));
                        Log.i("join str",  String.join("|", drawCommandsLog));
                        editor.commit();
                        db.child("diagrams").child(Integer.toString(diagramID)).child("diagramID").setValue(diagramID);
                        db.child("diagrams").child(Integer.toString(diagramID)).child("diagram").setValue(drawCommandsLog);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("Diagram Save Error", error.getMessage());
                }
            });
    }

    public void startChooseShapeActivity() {
        Intent intent = new Intent(this, ChooseShape.class);
        startActivity(intent);
    }
}
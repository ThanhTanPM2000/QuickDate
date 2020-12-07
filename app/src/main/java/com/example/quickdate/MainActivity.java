package com.example.quickdate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv_quick, tv_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();

        TextPaint paint = tv_quick.getPaint();
        float width = paint.measureText("QUICK");

        tv_quick.setTextColor(Color.parseColor("#FF8960"));
        tv_date.setTextColor(Color.parseColor("#FF8960"));

        Shader textShader=new LinearGradient(0, 0, 0, 170,
                new int[]{
                        Color.parseColor("#FF8960"),
                        Color.parseColor("#FF62A5"),
                }, null, Shader.TileMode.CLAMP);
        tv_quick.getPaint().setShader(textShader);
        tv_date.getPaint().setShader(textShader);

    }

    private void findViewById(){
        tv_quick = (TextView) findViewById(R.id.tv_quick);
        tv_date = (TextView) findViewById(R.id.tv_date);
    }
}
package com.huailai.club.a2048.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ocean on 2017/4/27. 基本就一个onDraw通过number来绘制背景和数字；number通过调用setNumer进行设置；它的宽和高都是固定值
 */
public class Game2048Item extends View {
    /**
     *  数字
     */
    private int mNumber;
    private String mNumberVal;
    private Paint mPaint;
    /**
     *  绘制区域
     */
    private Rect mBound;

    public Game2048Item(Context context) {
        this(context,null);
    }

    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        Resources resourse=context.getResources();
        Typeface font = Typeface.createFromAsset(resourse.getAssets(), "ClearSans-Bold.ttf");
        mPaint.setTypeface(font);
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
        mNumberVal=mNumber+"";
        mPaint.setTextSize(80.0f);
        mBound=new Rect();
        mPaint.getTextBounds(mNumberVal,0,mNumberVal.length(),mBound);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String mBgColor="#EA7821";
        switch (mNumber) {
            case 0:
                mBgColor = "#d6cdc4";
                break;
            case 2:
                mBgColor = "#EEE4DA";
                break;
            case 4:
                mBgColor = "#EDE0C8";
                break;
            case 8:
                mBgColor = "#F2B179";// #F2B179
                break;
            case 16:
                mBgColor = "#F49563";
                break;
            case 32:
                mBgColor = "#F5794D";
                break;
            case 64:
                mBgColor = "#F55D37";
                break;
            case 128:
                mBgColor = "#EEE863";
                break;
            case 256:
                mBgColor = "#EDB04D";
                break;
            case 512:
                mBgColor = "#ECB04D";
                break;
            case 1024:
                mBgColor = "#EB9437";
                break;
            case 2048:
                mBgColor = "#EA7821";
                break;
            case 4096 :
                mBgColor = "#3c3a32";
                break;
            default:
                mBgColor = "#EA7821";
                break;
        }
        mPaint.setColor(Color.parseColor(mBgColor));
        mPaint.setStyle(Paint.Style.FILL);
        RectF r1=new RectF(0,0,getWidth(),getHeight());
        canvas.drawRoundRect(r1,10,10,mPaint);

        if (mNumber!=0){
            drawText(canvas);
        }

    }

    private void drawText(Canvas canvas) {
        if (mNumber>4){
            mPaint.setColor(Color.WHITE);
        }else {
            mPaint.setColor(Color.BLACK);
        }

        float x=(getWidth()-mBound.width())/2;
        float y= (getHeight()+mBound.height())/2;
        canvas.drawText(mNumberVal,x,y,mPaint);
    }
}

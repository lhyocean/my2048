package com.huailai.club.a2048.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ocean on 2017/4/27.
 */
public class Game2048Layout extends RelativeLayout{
    /**
     *  item数量 N*N
     */
    private int mColumn=4;

    /**
     *   存放item的数组
     */
    private Game2048Item[] mGame2048Items;
    /**
     *
     */
    private int mMargin=10;
    private int mPadding;

    /**
     *  用于确认是否需要生成新志
     */
    private boolean isMergeHappen=true;
    private boolean isMoveHappen=true;

    /**
     *  f分数
     */
    private int score;

    /**
     *   手势处理
     */
    private GestureDetector mGestureDetector;


    public Game2048Layout(Context context) {
        this(context,null);
    }

    public Game2048Layout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Game2048Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMargin,getResources().getDisplayMetrics());
        mPadding=min(getPaddingBottom(),getPaddingLeft(),getPaddingRight(),getPaddingTop());
        mGestureDetector=new GestureDetector(context,new MyGestureDetector() );

    }
    private boolean once;
    /**
     * 测量Layout的宽和高，以及设置Item的宽和高，这里忽略wrap_content 以宽、高之中的最小值绘制正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 2048正方形外框的边长
        int length=Math.min(getMeasuredHeight(),getMeasuredWidth());
        // item 边长
        int childWidth=(length-mPadding*2-mMargin*(mColumn-1))/mColumn;
        if (!once){
            if (mGame2048Items==null){
                mGame2048Items=new Game2048Item[mColumn*mColumn];
            }
            // 放置Item
            for (int i = 0; i < mGame2048Items.length; i++) {
                Game2048Item item=new Game2048Item(getContext());
                mGame2048Items[i]=item;
                item.setId(i+1);
                RelativeLayout.LayoutParams lp=new LayoutParams(childWidth,childWidth);
                // 设置横向边距
                if ((i+1)%mColumn!=0){
                    lp.rightMargin=mMargin;
                }

                if (i%mColumn!=0){
                    lp.addRule(RelativeLayout.RIGHT_OF,mGame2048Items[i-1].getId());
                }
                // 设置纵向边距
                if ((i+1)>mColumn){
                    lp.topMargin=mMargin;
                    lp.addRule(RelativeLayout.BELOW,mGame2048Items[i-mColumn].getId());
                }
                addView(item,lp);

            }
            generaterNum();

        }
        once=true;
        setMeasuredDimension(length,length);
    }

    /**
     *  产生 一个数字
     */
    private void generaterNum() {
        if (checkOver())
        {
            if (mGame2048Listener!=null){
            mGame2048Listener.onGameOver();
        }
            return;
        }

        if (!isFull()){
           if (isMergeHappen||isMoveHappen){
               Random random=new Random();
               int next=random.nextInt(16);
               Game2048Item item=mGame2048Items[next];
               while (item.getNumber()!=0){
                   next=random.nextInt(16);
                   item=mGame2048Items[next];
               }
               item.setNumber(Math.random()>0.75?4:2);
               isMoveHappen=isMergeHappen=false;
           }

        }
    }
    /**
     * 检测当前所有的位置都有数字，且相邻的没有相同的数字
     *
     * @return
     */
    private boolean checkOver() {
        if (!isFull()){
            return false;
        }
        for (int i = 0; i < mColumn; i++) {
            for (int j = 0; j < mColumn; j++) {
                int index=i*mColumn+j;
                Game2048Item item=mGame2048Items[index];
                // 检查右边的item
                if ((index+1)%mColumn!=0){

                    Game2048Item itemRight=mGame2048Items[index+1];
                    if (item.getNumber()==itemRight.getNumber()){
                        return false;
                    }
                }
                //  下边
                if ((index+mColumn<mColumn*mColumn)){
                    Game2048Item itemBottom =mGame2048Items[index+mColumn];
                    if (item.getNumber()==itemBottom.getNumber()){
                        return false;
                    }

                }
                // 左边
                if (index%mColumn!=0){
                    Game2048Item itemLeft=mGame2048Items[index-1];
                    if (item.getNumber()==itemLeft.getNumber()){
                        return false;
                    }
                }
                // 上边
                if (index+1>mColumn){
                    Game2048Item itemTop=mGame2048Items[index-mColumn];
                    if (item.getNumber()==itemTop.getNumber()){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isFull() {
        for (int i = 0; i < mGame2048Items.length; i++) {
            if (mGame2048Items[i].getNumber()==0){
                return false;
            }
        }

        return true;
    }

    /**
     *  监听变化
     */
    public  interface OnGame2048Listener{
        void  onScoreChange(int score);
        void  onGameOver();
    }

    private OnGame2048Listener mGame2048Listener;

    public OnGame2048Listener getGame2048Listener() {
        return mGame2048Listener;
    }

    public void setGame2048Listener(OnGame2048Listener game2048Listener) {
        mGame2048Listener = game2048Listener;
    }

    /**
     * 得到多值中的最小值
     *
     * @param params
     * @return
     */
    private int min(int... params)
    {
        int min = params[0];
        for (int param : params)
        {
            if (min > param)
            {
                min = param;
            }
        }
        return min;
    }
    /**
     * 运动方向的枚举
     *
     * @author zhy
     *
     */
    private enum ACTION
    {
        LEFT, RIGHT, UP, DOWM
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        // 判断滑动方向，处理事件就是判读用户上、下、左、右滑动；然后去调用action(ACTION)方法；ACTION是个枚举
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final  int FLING_MIN_DISTANCE=50;

            float x=e2.getX()-e1.getX();
            float y=e2.getY()-e1.getY();
            if (x>FLING_MIN_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
                action(ACTION.RIGHT);
            }else if (x<-FLING_MIN_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
                action(ACTION.LEFT);
            }else if (y>FLING_MIN_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
                action(ACTION.DOWM);
            }else if (y<-FLING_MIN_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
                action(ACTION.UP);
            }
            return true;
        }
    }

    private void action(ACTION action) {

        for (int i = 0; i < mColumn; i++) {

            List<Game2048Item> row=new ArrayList<>();
            for (int j = 0; j < mColumn; j++) {
                // 得到下标
                int index=getIndexByAction(action,i,j);
                Game2048Item item=mGame2048Items[index];
                if (item.getNumber()!=0){
                    row.add(item);
                }
            }
            for (int j = 0; j < mColumn&&j<row.size(); j++) {
                int index=getIndexByAction(action,i,j);
                Game2048Item item=mGame2048Items[index];
                if (item.getNumber()!=row.get(j).getNumber()){
                    isMoveHappen=true;
                }

            }
            // 合并Item
            mergeItem(row);

            for (int j = 0; j < mColumn; j++) {
                int index=getIndexByAction(action,i,j);
                if (row.size()>j){
                    mGame2048Items[index].setNumber(row.get(j).getNumber());
                }else {
                    mGame2048Items[index].setNumber(0);
                }
            }
        }
           generaterNum();

    }



    private void mergeItem(List<Game2048Item> row) {
        if (row.size()<2){
            return;
        }
        for (int j = 0; j < row.size()-1; j++) {
             Game2048Item item1=row.get(j);
            Game2048Item item2=row.get(j+1);
            if (item1.getNumber()==item2.getNumber()){
                isMergeHappen=true;

                int val=item1.getNumber()+item2.getNumber();
                item1.setNumber(val);

                score+=val;
                if (mGame2048Listener!=null){
                    mGame2048Listener.onScoreChange(score);
                }
                //    向前移动例如   0248----->>>  2480  每次调用setNumber  时Game2048Item会重汇
                for (int k = j+1; k <row.size()-1 ; k++) {
                    row.get(k).setNumber(row.get(k+1).getNumber());
                }
                row.get(row.size()-1).setNumber(0);
                return;

            }

        }
    }

    /**
     *
     * @param action
     * @param i
     * @param j
     * @return
     */
    private int getIndexByAction(ACTION action, int i, int j) {
        int index = -1;
        switch (action)
        {
            case LEFT:
                index = i * mColumn + j;
                break;
            case RIGHT:
                index = i * mColumn + mColumn - j - 1;
                break;
            case UP:
                index = i + j * mColumn;
                break;
            case DOWM:
                index = i + (mColumn - 1 - j) * mColumn;
                break;
        }
        return index;
    }


    /**
     * 重新开始游戏
     */
    public void restart()
    {
        for (Game2048Item item : mGame2048Items)
        {
            item.setNumber(0);
        }
        score = 0;
        if (mGame2048Listener != null)
        {
            mGame2048Listener.onScoreChange(score);
        }
        isMoveHappen = isMergeHappen = true;
        generaterNum();
    }
}

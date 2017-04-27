package com.huailai.club.a2048;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huailai.club.a2048.view.Game2048Layout;

public class MainActivity extends Activity implements Game2048Layout.OnGame2048Listener, View.OnClickListener {
    private Game2048Layout mGame2048Layout;

    private TextView mScore,add,dec,level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScore = (TextView) findViewById(R.id.id_score);
        add= (TextView) findViewById(R.id.add);
        dec= (TextView) findViewById(R.id.dec);
        level= (TextView) findViewById(R.id.level);
        mGame2048Layout = (Game2048Layout) findViewById(R.id.id_game2048);
        mGame2048Layout.setGame2048Listener(this);
        add.setOnClickListener(this);
        dec.setOnClickListener(this);
    }

    @Override
    public void onScoreChange(int score) {
        mScore.setText("SCORE: " + score);
    }

    @Override
    public void onGameOver() {
        new AlertDialog.Builder(this).setTitle("GAME OVER")
                .setMessage("YOU HAVE GOT " + mScore.getText())
                .setPositiveButton("RESTART", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mGame2048Layout.restart();
                    }
                }).setNegativeButton("EXIT", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dec:
                if (mGame2048Layout.getColumn()<=3){
                    Toast.makeText(MainActivity.this, "不能再调了", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mGame2048Layout.setColumn(mGame2048Layout.getColumn()-1);
                }
                level.setText(mGame2048Layout.getColumn()+"");
                break;
            case R.id.add:
                if (mGame2048Layout.getColumn()<=7){
                    mGame2048Layout.setColumn(mGame2048Layout.getColumn()+1);
                }else {
                    Toast.makeText(MainActivity.this, "不能再调了", Toast.LENGTH_SHORT).show();
                }
                level.setText(mGame2048Layout.getColumn()+"");
                break;
        }

    }
}

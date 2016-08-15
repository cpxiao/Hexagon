package com.cpxiao.hexagon.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cpxiao.commonlibrary.utils.PreferencesUtils;
import com.cpxiao.hexagon.ExtraKey;
import com.cpxiao.hexagon.R;

/**
 * HomeActivity
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initWidget();

        initAds("299750750363934_299751287030547");
    }

    private void initWidget() {
        Button btnContinue = (Button) findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
        btnContinue.setVisibility(View.GONE);

        Button btnNewGame = (Button) findViewById(R.id.btn_new_game);
        btnNewGame.setOnClickListener(this);

        Button btnBestScore = (Button) findViewById(R.id.btn_best_score);
        btnBestScore.setOnClickListener(this);

        Button btnOptions = (Button) findViewById(R.id.btn_options);
        btnOptions.setOnClickListener(this);
        btnOptions.setVisibility(View.GONE);

        Button btnHelp = (Button) findViewById(R.id.btn_help);
        btnHelp.setOnClickListener(this);
        btnHelp.setVisibility(View.GONE);

        Button btnMoreGames = (Button) findViewById(R.id.btn_more_games);
        btnMoreGames.setOnClickListener(this);
        btnMoreGames.setVisibility(View.GONE);

        Button btnQuit = (Button) findViewById(R.id.btn_quit);
        btnQuit.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_continue) {

        } else if (id == R.id.btn_new_game) {
            GameActivity.come2me(HomeActivity.this, 5);
        } else if (id == R.id.btn_best_score) {
            AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle(R.string.menu_best_score)
                    .setMessage(String.valueOf(PreferencesUtils.getInt(this, ExtraKey
                            .KEY_BEST_SCORE, 0)))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
            dialog.show();
        } else if (id == R.id.btn_options) {

        } else if (id == R.id.btn_help) {

        } else if (id == R.id.btn_more_games) {

        } else if (id == R.id.btn_quit) {
            finish();
        }
    }
}

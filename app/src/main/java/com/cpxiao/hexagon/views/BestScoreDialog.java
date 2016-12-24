package com.cpxiao.hexagon.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.hexagon.Extra;
import com.cpxiao.hexagon.R;

/**
 * @author cpxiao on 2015/10/22.
 */
public class BestScoreDialog extends Dialog {

    private TextView mBtnOK;

    public BestScoreDialog(Context context) {
        super(context, R.style.ScoreDialog);
        initWidget();

    }

    private void initWidget() {
        setContentView(R.layout.dialog_best_score);
        // 点击外面区域不会让dialog消失
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        TextView easyLabelTV = (TextView) findViewById(R.id.easy_label);
        //        String easyLabel = getContext().getString(R.string.btn_r5) + ": ";
        String easyLabel = "R5: ";
        easyLabelTV.setText(easyLabel);
        TextView normalLabelTV = (TextView) findViewById(R.id.normal_label);
        //        String normalLabel = getContext().getString(R.string.btn_r6) + ": ";
        String normalLabel = "R6: ";
        normalLabelTV.setText(normalLabel);
        TextView hardLabelTV = (TextView) findViewById(R.id.hard_label);
        //        String hardLabel = getContext().getString(R.string.btn_r7) + ": ";
        String hardLabel = "R7: ";
        hardLabelTV.setText(hardLabel);

        TextView easyScoreTV = (TextView) findViewById(R.id.easy_score);
        String easyScore = String.valueOf(PreferencesUtils.getInt(getContext(), Extra.Key.BEST_SCORE, 0));
        easyScoreTV.setText(easyScore);
        TextView normalScoreTV = (TextView) findViewById(R.id.normal_score);
        String normalScore = String.valueOf(PreferencesUtils.getInt(getContext(), Extra.Key.BEST_SCORE_R6, 0));
        normalScoreTV.setText(normalScore);
        TextView hardScoreTV = (TextView) findViewById(R.id.hard_score);
        String hardScore = String.valueOf(PreferencesUtils.getInt(getContext(), Extra.Key.BEST_SCORE_R7, 0));
        hardScoreTV.setText(hardScore);

        mBtnOK = (TextView) findViewById(R.id.dialog_btn_ok);
    }


    public void setButtonOK(View.OnClickListener click) {
        mBtnOK.setOnClickListener(click);
    }


}

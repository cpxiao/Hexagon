package com.cpxiao.hexagon.views;//package com.cpxiao.hexagon.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.cpxiao.hexagon.R;


/**
 * DialogUtils
 *
 * @author cpxiao on 2016/8/26
 */
public class DialogUtils {

    public static void createGameOverDialog(Context context,
                                            DialogInterface.OnClickListener positiveL,
                                            DialogInterface.OnClickListener negativeL) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.game_over))
                        .setMessage(context.getString(R.string.play_again))
                        .setPositiveButton(context.getString(R.string.ok), positiveL)
                        .setNegativeButton(context.getString(R.string.cancel), negativeL)
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
    }
}

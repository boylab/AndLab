package com.boylab.andlab.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.DrawableRes;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogUtil {

    private Context context;
    private @DrawableRes int iconId = -1;

    private static volatile DialogUtil instance = null;    //保证 instance 在所有线程中同步
    private DialogUtil() {
    }    //private 避免类在外部被实例化
    public static synchronized DialogUtil newInstance() {
        //getInstance 方法前加同步
        if (instance == null) {
            instance = new DialogUtil();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context, @DrawableRes int iconId){
        this.context = context;
        this.iconId = iconId;
    }

    /**
     * 普通弹窗
     */
    public void normalDialog(String title, String message, final OnNormalDialogListener onNormalDialogListener){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);

        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(24);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(0,20,0,20);
        titleView.setBackgroundColor(Color.parseColor("#B6DEF0"));
        normalDialog.setCustomTitle(titleView);
        // 中间的信息以一个view的形式设置进去
        TextView msg = new TextView(context);
        msg.setText(message);
        msg.setTextSize(22);
        msg.setGravity(Gravity.CENTER);
        msg.setPadding(20, 40, 20, 40);
        normalDialog.setView(msg);

        normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onNormalDialogListener != null){
                                onNormalDialogListener.onPositiveClick(dialog, which);
                            }
                        }
                    })
                .setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onNormalDialogListener != null){
                                onNormalDialogListener.onNegativeClick(dialog, which);
                            }
                        }
                    });
        AlertDialog alertDialog = normalDialog.create();
        alertDialog.show();
        weightButton(alertDialog);
    }

    /**
     * 三个按钮
     */
    public void multiDialog(String title, String message, final OnMultiDialogListener onMultiDialogListener){
        AlertDialog.Builder multiDialog = new AlertDialog.Builder(context);

        setTitle(multiDialog, title, message);

        multiDialog.setPositiveButton("按钮1", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onMultiDialogListener != null){
                            onMultiDialogListener.onPositiveClick(dialog, which);
                        }
                    }
                })
                .setNeutralButton("按钮2", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onMultiDialogListener != null){
                            onMultiDialogListener.onNeutralClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton("按钮3", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onMultiDialogListener != null){
                            onMultiDialogListener.onPositiveClick(dialog, which);
                        }
                    }
                });
        AlertDialog alertDialog = multiDialog.create();
        alertDialog.show();
        weightButton(alertDialog);
    }

    /**
     * 列表弹窗
     * @param title
     * @param message
     * @param items
     */
    public void listDialog(String title, String message, CharSequence[] items, final OnListDialogListener onListDialogListener) {
        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        setTitle(listDialog, title, message);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which 下标从0开始
                        // ...To-do
                        onListDialogListener.onListClick(dialog, which);
                    }
                })
                .show();
    }


    private int yourChoice;
    /**
     * 单选
     * @param title
     * @param items
     * @param position
     */
    public void singleChoiceDialog(String title, CharSequence[] items, int position, final OnSingleChoiceDialogListener onSingleChoiceDialogListener){
        yourChoice = position;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        setTitle(singleChoiceDialog, title, null);
        singleChoiceDialog.setSingleChoiceItems(items, yourChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            if (onSingleChoiceDialogListener != null){
                                onSingleChoiceDialogListener.onSingleChoiceClick(dialog, which);
                            }
                            /*Toast.makeText(MainActivity.this,
                                    "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    }
                })
                .show();
    }

    /**
     * 多选弹窗
     * @param title
     * @param items
     * @param checkedItems
     */
    public void multiChoiceDialog(String title, CharSequence[] items, final boolean[] checkedItems, final OnMultiChoiceDialogListener onMultiChoiceDialogListener) {
        // 设置默认选中的选项，全为false默认均未选中

        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(context);
        setTitle(multiChoiceDialog, title, null);
        multiChoiceDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onMultiChoiceDialogListener != null){
                            onMultiChoiceDialogListener.onMultiChoiceClick(dialog, which, checkedItems);
                        }
                    }
                })
                .show();
    }

    /**
     * 等待弹窗
     * @param title
     * @param message
     */
    public void waitingDialog(String title, String message, OnWaitingDialogListener onWaitingDialogListener) {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        ProgressDialog waitingDialog= new ProgressDialog(context);
        setTitle(waitingDialog, title, message);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.closeOptionsMenu();
        waitingDialog.show();
        if (onWaitingDialogListener != null){
            onWaitingDialogListener.onWaitingDismiss(waitingDialog, 0x01);
        }
    }

    public void progressDialog(String title, String message, OnProgressDialogListener onProgressDialogListener) {
        /* @setProgress 设置初始进度
         * @setProgressStyle 设置样式（水平进度条）
         * @setMax 设置进度最大值
         */
        final ProgressDialog progressDialog = new ProgressDialog(context);
        setTitle(progressDialog, title, message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        if (onProgressDialogListener != null){
            onProgressDialogListener.onProgressUpdate(progressDialog);
        }
        /**
         * progressDialog.setProgress(progress);    //更新进度
         *
         * progressDialog.cancel();     //取消显示
         */
    }

    public void inputDialog(String title, String message, final OnInputDialogListener onInputDialogListener) {
        final EditText inputText = new EditText(context);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(60, 20, 60, 20);
        inputText.setLayoutParams(layoutParams);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(5);
        drawable.setColor(Color.WHITE);
        drawable.setStroke(1, Color.GRAY);
        inputText.setBackground(drawable);

        RelativeLayout rootLayout = new RelativeLayout(context);
        rootLayout.addView(inputText);

        AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
        setTitle(inputDialog, title, message);
        inputDialog.setView(rootLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = inputText.getText().toString().trim();
                        if (onInputDialogListener != null){
                            onInputDialogListener.onInputClick(dialog, which, text);
                        }
                    }
                })
                .show();
    }

    public void showViewDialog(String title, final OnViewDialogListener onViewDialogListener) {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
         * dialog_customize.xml可自定义更复杂的View
         */
        if (onViewDialogListener == null){
            return;
        }
        AlertDialog.Builder viewDialog = new AlertDialog.Builder(context);
        setTitle(viewDialog, title, null);
        viewDialog.setView(onViewDialogListener.setView())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        if (onViewDialogListener != null){
                            onViewDialogListener.onPositiveClick(dialog, which);
                        }
                    }
                })
                .show();
    }

   public interface OnNormalDialogListener{
        void onPositiveClick(DialogInterface dialog, int which);
        void onNegativeClick(DialogInterface dialog, int which);
    }
    
   public interface OnMultiDialogListener{
       void onPositiveClick(DialogInterface dialog, int which);
       void onNeutralClick(DialogInterface dialog, int which);
       void onNegativeClick(DialogInterface dialog, int which);
    }
    
   public interface OnListDialogListener{
        void onListClick(DialogInterface dialog, int which);
    }
    
   public interface OnSingleChoiceDialogListener{
        void onSingleChoiceClick(DialogInterface dialog, int which);
    }
    
   public interface OnMultiChoiceDialogListener{
        void onMultiChoiceClick(DialogInterface dialog, int which, boolean[] choicesItem);
    }
    
   public interface OnWaitingDialogListener{
       /**
        * 需要在回调方法中任务结束调用 dialog.dismiss()
        * @param dialog
        * @param which
        * @return
        */
        void onWaitingDismiss(DialogInterface dialog, int which);
    }
    
   public interface OnProgressDialogListener{
       /**
        * dialog.setProgress(int progress);
        * dialog.dismiss();
        * @param dialog
        */
        void onProgressUpdate(ProgressDialog dialog);
        //void onProgressCancel(ProgressDialog dialog);
    }
    
   public interface OnInputDialogListener{
        void onInputClick(DialogInterface dialog, int which, String inputText);
    }

    public interface OnViewDialogListener {
        View setView();
        void onPositiveClick(DialogInterface dialog, int which);
    }

    private void setTitle(AlertDialog.Builder builder, String title, String message){
        if (this.iconId > 0){
            builder.setIcon(iconId);
        }
        if (!TextUtils.isEmpty(title)){
            TextView titleView = new TextView(context);
            titleView.setText(title);
            titleView.setTextSize(24);
            titleView.setGravity(Gravity.CENTER);
            titleView.setPadding(0,20,0,20);
            titleView.setBackgroundColor(Color.parseColor("#B6DEF0"));
            builder.setCustomTitle(titleView);
        }
        if (!TextUtils.isEmpty(message)){
            TextView msg = new TextView(context);
            msg.setText(message);
            msg.setTextSize(24);
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(20, 40, 20, 40);
            builder.setView(msg);
        }

    }

    private void setTitle(AlertDialog dialog, String title, String message){
        if (this.iconId > 0){
            dialog.setIcon(iconId);
        }
        if (!TextUtils.isEmpty(title)){
            TextView titleView = new TextView(dialog.getContext());
            titleView.setText(title);
            titleView.setTextSize(24);
            titleView.setGravity(Gravity.CENTER);
            titleView.setPadding(0,20,0,20);
            titleView.setBackgroundColor(Color.parseColor("#B6DEF0"));
            dialog.setCustomTitle(titleView);
        }
        if (!TextUtils.isEmpty(message)){
            TextView msg = new TextView(dialog.getContext());
            msg.setText(message);
            msg.setTextSize(24);
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(20, 40, 20, 40);
            dialog.setView(msg);
        }
        dialog.setCanceledOnTouchOutside(false);
    }

    private void weightButton(AlertDialog dialog){
        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button negativeButton=dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams positiveParams =(LinearLayout.LayoutParams)positiveButton.getLayoutParams();
        positiveParams.gravity = Gravity.CENTER;
        positiveParams.setMargins(10,10,10,10);
        positiveParams.width = 0;
        // 安卓下面有三个位置的按钮，默认权重为 1,设置成 500或更大才能让两个按钮看起来均分
        positiveParams.weight = 500;

        LinearLayout.LayoutParams negativeParams =(LinearLayout.LayoutParams)negativeButton.getLayoutParams();
        negativeParams.gravity = Gravity.CENTER;
        negativeParams.setMargins(10,10,10,10);
        negativeParams.width = 0;
        negativeParams.weight = 500;
        positiveButton.setLayoutParams(positiveParams);
        negativeButton.setLayoutParams(negativeParams);

        positiveButton.setBackgroundColor(Color.parseColor("#FF733E"));
        positiveButton.setTextColor(Color.parseColor("#FFFFFF"));
        negativeButton.setBackgroundColor(Color.parseColor("#DDDDDD"));
    }
}

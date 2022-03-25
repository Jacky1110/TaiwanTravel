package com.jotangi.nickyen;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;

public class DialogIOSUtility
{
    public static AlertDialog appDialog;

    public interface OnBtnClickListener {
        void onCheck();
        void onCancel();
    }

    public interface OnBtnClickListener2 {
        void onCheck(final String message);
        void onCancel();
    }

    public static String base64(final String s) {

        if (s == null) { return ""; }
        byte[] data = new byte[0];
        data = s.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);

    }

    // region ---- 小型confirm視窗
    public static void showMyDialog(Activity activity, String title,String content, String checkString, String cancelString, @NonNull final OnBtnClickListener listener){
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_ios, viewGroup, false);
        buildDialog(dialogView, activity, title, content , checkString, cancelString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }
    public static void buildDialog(View dialogView, Activity activity, String title, String message2, String checkString, String cancelString, @NonNull final OnBtnClickListener listener){
        TextView messageTV = dialogView.findViewById(R.id.message_title);
        TextView messageContent = dialogView.findViewById(R.id.message_content);
        Button checkBtn    = dialogView.findViewById(R.id.btnCheck);
        Button cancelBtn   = dialogView.findViewById(R.id.btnCancel);
        View dividingLine = dialogView.findViewById(R.id.dividing_line);

        messageTV.setText(title);
        messageContent.setText(message2);

        if (checkString == null || checkString.equals("")) {
            checkBtn.setVisibility(View.GONE);
            dividingLine.setVisibility(View.GONE);
        } else {
            checkBtn.setText(checkString);
        }

        if (cancelString == null || cancelString.equals("")) {
            cancelBtn.setVisibility(View.GONE);
            dividingLine.setVisibility(View.GONE);
        } else {
            cancelBtn.setText(cancelString);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCancel();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCheck();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);

        appDialog = builder.create();
        appDialog.show();
    }
    //region

    // region ---- 小型可回覆視窗
    public static void showRemarkDialog(Activity activity, String title,String content,String hint, String checkString, String cancelString, @NonNull final OnBtnClickListener2 listener){
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_ios_edit, viewGroup, false);
        buildRemarkDialog(dialogView, activity, title, content ,hint, checkString, cancelString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void buildRemarkDialog(View dialogView, Activity activity, String title, String message2, String hint, String checkString, String cancelString, @NonNull final OnBtnClickListener2 listener){
        TextView messageTV = dialogView.findViewById(R.id.message_title);
        TextView messageContent = dialogView.findViewById(R.id.message_content);
        TextView edtRemark = dialogView.findViewById(R.id.edt_remark);
        Button checkBtn    = dialogView.findViewById(R.id.btnCheck);
        Button cancelBtn   = dialogView.findViewById(R.id.btnCancel);
        View dividingLine = dialogView.findViewById(R.id.dividing_line);

        messageTV.setText(title);
        messageContent.setText(message2);
        edtRemark.setHint(hint);

        if (checkString == null || checkString.equals("")) {
            checkBtn.setVisibility(View.GONE);
            dividingLine.setVisibility(View.GONE);
        } else {
            checkBtn.setText(checkString);
        }

        if (cancelString == null || cancelString.equals("")) {
            cancelBtn.setVisibility(View.GONE);
            dividingLine.setVisibility(View.GONE);
        } else {
            cancelBtn.setText(cancelString);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCancel();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCheck(edtRemark.getText().toString().trim());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);

        appDialog = builder.create();
        appDialog.show();
    }
    //region
}

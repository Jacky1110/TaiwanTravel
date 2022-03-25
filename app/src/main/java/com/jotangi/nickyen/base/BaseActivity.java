package com.jotangi.nickyen.base;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jotangi.nickyen.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;

    //custom toast
    private static Toast toast;

    public interface OnCallbackListener {
        void onCallback(boolean success);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void makeToastTextAndShow( final String text, final int duration) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup)findViewById(R.id.toast_layout_root));

        TextView cText = (TextView)layout.findViewById(R.id.text);
        cText.setText(text);

        if (toast == null) {
            toast = new Toast(context);
        }

        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }


}

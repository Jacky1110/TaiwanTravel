package com.jotangi.nickyen.merch.checkout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.merch.reserve.OrderModel;
import com.jotangi.nickyen.merch.reserve.ReserveInputFragment;
import com.jotangi.nickyen.model.ShopBean;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Optional;

public class ReserveQR2Fragment extends Fragment {
    private String sid;
    private String total;
    private String storeName;

    private ImageButton btnGoBack;
    private ImageView imgCode;
    private TextView txtAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sid = getArguments().getString("sid", "");
            total = getArguments().getString("total", "");
            storeName = getArguments().getString("name", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reserve_q_r, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgCode = getView().findViewById(R.id.iv_qrcode);
        btnGoBack = getView().findViewById(R.id.ib_go_back);
        txtAccount = getView().findViewById(R.id.txtAccount);
        txtAccount.setText("本次消費金額 "+total+" 元");
        btnGoBack.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_reserveQR2Fragment_to_merchHomeFragment);
        });
        genCode();
    }

    private void genCode() {
        String sStoreName = AppUtility.convertStringToUTF8(storeName);
        String str = "";
        String service = "";
//        for (OrderModel s: ReserveInputFragment.orderModels)
//        {
//            service += AppUtility.convertStringToUTF8(s.getItem())+","+ s.getFee()+"///";
//        }

//        service = Optional.ofNullable(service)
//                .filter(s -> s.length() != 0)
//                .map(s -> s.substring(0, s.length() - 3))
//                .orElse(service);
        try {
            str = "spot://payment?s_id=" + sid + "&store_name=" + sStoreName + "&total=" + total + "&booking_no="
                    + "&designer=" + "&service=" + "&eat=";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("豪豪", "genCode: " + str);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(str, BarcodeFormat.QR_CODE,
                    250, 250);
            imgCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
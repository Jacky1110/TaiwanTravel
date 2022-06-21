package com.jotangi.nickyen;

import static com.jotangi.nickyen.api.ApiConstant.API_IMAGE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.model.ShopBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberProductFragment extends BaseFragment implements View.OnClickListener{

    private String TAG = MemberProductFragment.class.getSimpleName() + "(TAG)";

    //UI
    private ImageButton btnGoBack;
    private ImageView productView;

    private ApiEnqueue apiEnqueue;

    private FirebaseAnalytics mFirebaseAnalytics; //firebaseGA分析

    //資訊區塊內的控件
    private TextView txtStoreName, txtStoreAddress, txtStoreTel, txtStoreBusinessTime, txtStoreDescript, txtGlobe, txtFb, txtStoreInformation;

    public MemberProductFragment()
    {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_product, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        initView(view);
        return view;
    }

    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        productView = v.findViewById(R.id.iv_product);
        String imageUrl = API_IMAGE + DataMemberBeen.store_picture;
        PicassoTrustAll.getInstance(getContext()).load(imageUrl).into(productView);

        //店家資訊區塊控件
        txtStoreName = v.findViewById(R.id.tv_info_store_name);
        txtStoreAddress = v.findViewById(R.id.tv_info_store_address);
        txtStoreTel = v.findViewById(R.id.tv_info_store_tel);
        txtStoreBusinessTime = v.findViewById(R.id.tv_info_store_business_time);
        txtStoreDescript = v.findViewById(R.id.tv_info_store_descript);
        txtStoreInformation = v.findViewById(R.id.tv_StoreInformation);
        txtStoreTel.setOnClickListener(this);
        getStoreDescript();
    }

    private void getStoreDescript() {
        apiEnqueue.storeSetting(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            Log.d(TAG, "jsonObject: " + jsonObject);
                            DataMemberBeen.store_name = jsonObject.getString("store_name");
                            DataMemberBeen.store_address = jsonObject.getString("store_address");
                            DataMemberBeen.store_opentime = jsonObject.getString("store_opentime");
                            DataMemberBeen.store_phone = jsonObject.getString("store_phone");
                            DataMemberBeen.store_descript = jsonObject.getString("store_descript");
                            DataMemberBeen.store_picture = jsonObject.getString("store_picture");

                            txtStoreName.setText(DataMemberBeen.store_name);
                            txtStoreAddress.setText(DataMemberBeen.store_address);
                            txtStoreBusinessTime.setText(DataMemberBeen.store_opentime);
                            txtStoreTel.setText(DataMemberBeen.store_phone);
                            txtStoreDescript.setText(DataMemberBeen.store_descript);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
                Navigation.findNavController(v).popBackStack();
                break;
        }
    }
}

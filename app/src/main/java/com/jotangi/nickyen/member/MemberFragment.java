package com.jotangi.nickyen.member;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.beautySalons.SalonsRecordActivity;
import com.jotangi.nickyen.cost.CostGeneralActivity;
import com.jotangi.nickyen.home.MemberCardActivity;
import com.jotangi.nickyen.home.MemberCardFragment;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.MyPointActivity;
import com.jotangi.nickyen.industry.IndustryRecordActivity;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.pointshop.renew.PointShopOrderRecordActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class MemberFragment extends BaseFragment implements View.OnClickListener
{
    private CircleImageView imgHead;
    private Bitmap tempImage;
    //    private static final int pick_image = 1;
    //    Uri imageUri;

    private static final String path = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_PICTURES
            + File.separator + "Screenshots" + File.separator;

    private ConstraintLayout btnMemberCard, btnMyPoint, btnMyDiscount;
    //????????????
    private RelativeLayout btnMemberData;
    //????????????
    private RelativeLayout btnCostRecord;
    //????????????
    private RelativeLayout btnSalonsRecord;
    //??????????????????
    private RelativeLayout btnOrderRecord;
    //????????????
    private RelativeLayout btnIndustryRecord;
    //????????????
    private RelativeLayout btnAbout;
    //????????????
    private RelativeLayout btnQA;
    //??????
    private TextView btnLogout, txtName;

    private static final int REQUEST_CAMERA = 332;
    private static final int REQUEST_EXTERNAL_STORAGE = 333;
    private static final int REQUEST_SELECT_VIDEO = 334;

    public MemberFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//        FileObserver fileObserver = new FileObserver(path,FileObserver.CREATE)
//        {
//            @Override
//            public void onEvent(int event, @Nullable String path)
//            {
//                Log.d("??????", event+""+path);
//            }
//        };
//        fileObserver.startWatching();
        View v = inflater.inflate(R.layout.fragment_member, container, false);
        initView(v);
        return v;
    }

    private void initView(View v)
    {
        btnMemberCard = v.findViewById(R.id.constraint_member_card); //?????????
        btnMyPoint = v.findViewById(R.id.constraint_my_point); //????????????
        btnMyDiscount = v.findViewById(R.id.constraint_my_discount); //?????????

        btnMemberData = v.findViewById(R.id.member_data); //????????????
        btnCostRecord = v.findViewById(R.id.member_cost); //????????????
        btnSalonsRecord = v.findViewById(R.id.salons_record); //????????????
        btnOrderRecord = v.findViewById(R.id.order_record); //??????????????????
        btnIndustryRecord = v.findViewById(R.id.industry_record); //????????????
        btnAbout = v.findViewById(R.id.member_about); //????????????
        btnQA = v.findViewById(R.id.member_QA); //????????????
        btnLogout = v.findViewById(R.id.member_logout); //??????

        btnMemberCard.setOnClickListener(this);
        btnMyPoint.setOnClickListener(this);
        btnMyDiscount.setOnClickListener(this);
        btnMemberData.setOnClickListener(this);
        btnCostRecord.setOnClickListener(this);
        btnSalonsRecord.setOnClickListener(this);
        btnOrderRecord.setOnClickListener(this);
        btnIndustryRecord.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnQA.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        txtName = v.findViewById(R.id.tv_name);
        txtName.setText(MemberInfoBean.decryptName + "\n" + MemberInfoBean.decryptId);

        imgHead = v.findViewById(R.id.img_head);
        imgHead.setOnClickListener(this);

        Picasso.with(getActivity()).load(ApiConstant.API_IMAGE + MemberInfoBean.member_picture)
                .placeholder(R.drawable.default_head)
                .into(imgHead);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_head:
                setImage();
                break;
            case R.id.constraint_member_card:
                startActivity(new Intent(getActivity(), MemberCardActivity.class));
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.memberLayout, new MemberCardFragment(), null).addToBackStack(null).commit();
                break;
            case R.id.constraint_my_point:
                Intent myPointIntent = new Intent(getActivity(), MyPointActivity.class);
                startActivity(myPointIntent);
                break;
            case R.id.constraint_my_discount:
                Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                startActivity(myDiscountIntent);
                break;
            //????????????
            case R.id.member_data:
                Intent inData = new Intent(getActivity(), MemberInfoActivity.class);
                startActivity(inData);
                break;
            //????????????
            case R.id.member_cost:
                Intent inCost = new Intent(getActivity(), CostGeneralActivity.class);
                startActivity(inCost);
                break;
            //????????????????????????
            case R.id.salons_record:
                Intent inSalons = new Intent(getActivity(), SalonsRecordActivity.class);
                startActivity(inSalons);
                break;
            //??????????????????
            case R.id.order_record:
                Intent orderRecord = new Intent(getActivity(), PointShopOrderRecordActivity.class);
                startActivity(orderRecord);
                break;
            //????????????????????????
            case R.id.industry_record:
                Intent inIndustry = new Intent(getActivity(), IndustryRecordActivity.class);
                startActivity(inIndustry);
                break;
            //????????????
            case R.id.member_about:
                Intent ab = new Intent(getActivity(), AboutActivity.class);
                startActivity(ab);
                break;
            //????????????
            case R.id.member_QA:
                Intent faqIntent = new Intent(getActivity(), FrequentlyAskedQuestionsActivity.class);
                startActivity(faqIntent);
                break;
            case R.id.member_logout:
                AppUtility.showMyDialog(getActivity(), "??????????????????", "??????", "??????", new AppUtility.OnBtnClickListener()
                {
                    @Override
                    public void onCheck()
                    {
                        logout();
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
                break;
        }
    }

    private void setImage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("?????????????????????")
                .setItems(R.array.choose_images_from, (dialog, which) ->
                {

                    if (which == 0)
                    {

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);

                        } else
                        {
                            requestCamera();
                        }


                    } else if (which == 1)
                    {

                        if (ActivityCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);

                        } else
                        {
                            requestPick();
                        }
                    }
                });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.create();
        builder.show();
    }

    void requestCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    void requestPick()
    {
        final Uri mUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final Intent mIntent = new Intent(Intent.ACTION_PICK, mUri);
        final PackageManager mPackageManager = getActivity().getPackageManager();
        List<ResolveInfo> list = mPackageManager.queryIntentActivities(mIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 1)
        {
            startActivityForResult(
                    Intent.createChooser(
                            new Intent(Intent.ACTION_PICK, mUri), "????????????"
                    ),
                    REQUEST_SELECT_VIDEO
            );
        } else
        {
            startActivityForResult(mIntent, REQUEST_SELECT_VIDEO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {

            if (requestCode == REQUEST_SELECT_VIDEO)
            {
                // ...
                // ????????????????????????????????? android.permission.READ_EXTERNAL_STORAGE ????????? IOException:
                //  open failed: EACCES (Permission denied)
                // ...

                try
                {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    storeImage(selectedImage);

                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

            } else if (requestCode == REQUEST_CAMERA)
            {

                Bitmap photo = data.getParcelableExtra("data");
                storeImage(photo);
            }
        }
    }

    private void storeImage(Bitmap image)
    {
        int width = image.getWidth(), height = image.getHeight();

        // ????????????????????????????????????
        final int MAX_WIDTH = 256;

        float resize = 1; // ????????? resize ??????????????????
        if (width > height)
        {
            //landscape
            resize = ((float) MAX_WIDTH) / width;
        } else
        {
            //portrait
            resize = ((float) MAX_WIDTH) / height;
        }

        int nWidth = (int) ((int) width * resize);
        int nHeight = (int) ((int) height * resize);

        tempImage = Bitmap.createScaledBitmap(image, nWidth, nHeight, true);

//        Log.e(TAG,"new Width = " + tempImage.getWidth());
//        Log.e(TAG,"new Height = " + tempImage.getHeight());

        imgHead.setImageBitmap(tempImage);
        uploadImage();
    }

    private void uploadImage()
    {
        if (tempImage == null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    String message = "??????????????????";
                    AppUtility.showMyDialog(getActivity(), message, "??????", null, new AppUtility.OnBtnClickListener()
                    {
                        @Override
                        public void onCheck()
                        {
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
                }
            });
            return;
        }

        File filesDir = getActivity().getFilesDir();
        File imageFile = new File(filesDir, "image" + ".jpg");

        OutputStream os;
        try
        {
            os = new FileOutputStream(imageFile);
            tempImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        ApiConnection.uploadFile(imageFile, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {

                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),"?????????????????????",Toast.LENGTH_LONG).show());
            }

            @Override
            public void onFailure(final String message)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {Toast.makeText(getActivity(),"?????????????????????????????????",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void logout()
    {
        String account = null;
        String pwd = null;
        try
        {
            account = AppUtility.DecryptAES2(UserBean.member_id);
            pwd = AppUtility.DecryptAES2(UserBean.member_pwd);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ApiConnection.logout(account, pwd, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                SharedPreferences preferences = getContext().getSharedPreferences("loginInfo", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //?????? ??????????????????
                SharedPreferences preferences2 = getContext().getSharedPreferences("doll", getContext().MODE_PRIVATE);
                preferences2.edit().clear().commit();
                //????????? ??????????????????
                SharedPreferences preferences3 = getContext().getSharedPreferences("triangle", getContext().MODE_PRIVATE);
                preferences3.edit().clear().commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(String message)
            {
                SharedPreferences preferences = getContext().getSharedPreferences("loginInfo", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //?????? ??????????????????
                SharedPreferences preferences2 = getContext().getSharedPreferences("doll", getContext().MODE_PRIVATE);
                preferences2.edit().clear().commit();
                //????????? ??????????????????
                SharedPreferences preferences3 = getContext().getSharedPreferences("triangle", getContext().MODE_PRIVATE);
                preferences3.edit().clear().commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pick_image && resultCode == RESULT_OK) {
//            imageUri = data.getData();
//
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImageView.ActivittyResult result = CropImageView.getActivityResult(data);
//
//            if (requestCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    imgHead.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
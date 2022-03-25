package com.jotangi.nickyen.merch.industryReserve;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.databinding.FragmentIndustryReserveModifyBinding;
import com.jotangi.nickyen.industry.model.IndustryOrderBean;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class IndustryReserveModifyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    FragmentIndustryReserveModifyBinding binding;

    private IndustryOrderBean industryOrderBean;

    private int count = 1;
    private String fee = " ";
    private String price;
    private String strTime;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            industryOrderBean = new Gson().fromJson(getArguments().getString("order"), IndustryOrderBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentIndustryReserveModifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        binding.ibGoBack.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnSub.setOnClickListener(this);
        binding.btnReserveCancel.setOnClickListener(this);
        binding.btnReserveConfirm.setOnClickListener(this);

        count = Integer.parseInt(industryOrderBean.getProgramPerson()); // 預設訂單的人數
        strTime = industryOrderBean.getReserveTime(); // 預設訂單的時間
        Log.d("豪豪", "create: "+strTime);

        binding.tvCount.setText(String.valueOf(count));
        binding.txtProgramName.setText(industryOrderBean.getProgramName());
        binding.tvOrderDate.setText(industryOrderBean.getReserveDate());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.reserve_ime, R.layout.spinner_item);
//        String[] value = {};
//        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, arrayList);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);

        binding.tvName.setText(industryOrderBean.getMemberName());
        binding.tvTel.setText(industryOrderBean.getMemberId());
        binding.tvEmail.setText(industryOrderBean.getMemberEmail());
        binding.tvOrderItem.setText(industryOrderBean.getProgramName() + " X" + count);
        if (!industryOrderBean.getProgramPrice().contains("~"))
        {
            price = AppUtility.substringFunction(industryOrderBean.getProgramPrice(), "元", "", 1);
            fee = String.valueOf(Integer.parseInt(price) * Integer.parseInt(industryOrderBean.getProgramPerson()));
        } else
        {
            int min = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","",1));
            int max = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","元",3));
            String priceMin = String.valueOf(min * Integer.parseInt(industryOrderBean.getProgramPerson()));
            String priceMax = String.valueOf(max * Integer.parseInt(industryOrderBean.getProgramPerson()));
            fee = priceMin +"~" +priceMax;
        }
        binding.tvItemAmount.setText("$ " + fee);
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        count = 1;
        fee = "";
        binding = null;
    }

    @Override
    public void onClick(View v)
    {
        final String strRemark = binding.edtRemark.getText().toString().trim();
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_sub:
                count -= 1;
                if (count < 1)
                {
                    count = 1;
                    return;
                }
                binding.tvCount.setText(String.valueOf(count));
                binding.tvOrderItem.setText(industryOrderBean.getProgramName() + " X" + count);
                if (!industryOrderBean.getProgramPrice().contains("~"))
                {
                    price = AppUtility.substringFunction(industryOrderBean.getProgramPrice(), "元", "", 1);
                    fee = String.valueOf(Integer.parseInt(price) * Integer.parseInt(industryOrderBean.getProgramPerson()));
                } else
                {
                    int min = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","",1));
                    int max = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","元",3));
                    String priceMin = String.valueOf(min * count);
                    String priceMax = String.valueOf(max * count);
                    fee = priceMin +"~" +priceMax;
                }
                binding.tvItemAmount.setText("$ " + fee);
                break;
            case R.id.btn_add:
                count += 1;
//                if ( count > Integer.parseInt(industryOrderBean.))
                if (count > 20)
                {
//                    count = Integer.parseInt(programList.get(i).getProgramLimit());
                    count = 20;
                    Toast.makeText(getActivity(), "人數無法大於所選方案限制人數", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.tvCount.setText(String.valueOf(count));
                binding.tvOrderItem.setText(industryOrderBean.getProgramName() + " X" + count);
                if (!industryOrderBean.getProgramPrice().contains("~"))
                {
                    price = AppUtility.substringFunction(industryOrderBean.getProgramPrice(), "元", "", 1);
                    fee = String.valueOf(Integer.parseInt(price) * Integer.parseInt(industryOrderBean.getProgramPerson()));
                } else
                {
                    int min = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","",1));
                    int max = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","元",3));
                    String priceMin = String.valueOf(min * count);
                    String priceMax = String.valueOf(max * count);
                    fee = priceMin +"~" +priceMax;
                }
                binding.tvItemAmount.setText("$ " + fee);
                break;
            case R.id.btn_reserve_confirm:
                DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.confirm_to_modify_the_appointment), getString(R.string.after_confirmation), getString(R.string.text_confirm), getString(R.string.text_cancel), new DialogIOSUtility.OnBtnClickListener()
                {
                    @Override
                    public void onCheck()
                    {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        ApiConnection.classBookingUpdate(industryOrderBean.getBookingNo(), industryOrderBean.getReserveDate(), strTime, strRemark, "1", String.valueOf(count), new ApiConnection.OnConnectResultListener()
                        {
                            @Override
                            public void onSuccess(String jsonString)
                            {
                                fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_modify), getString(R.string.fcm_to_member_reserve_content, UserBean.MerchName), "industryRecord");

                                if (getActivity() == null)
                                {
                                    return;
                                }
                                getActivity().runOnUiThread(() ->
                                {
                                    binding.progressBar.setVisibility(View.GONE);
                                    Navigation.findNavController(getView()).popBackStack();
                                });
                            }

                            @Override
                            public void onFailure(String message)
                            {
                                if (getActivity() == null)
                                {
                                    return;
                                }
                                getActivity().runOnUiThread(() ->
                                {
                                    binding.progressBar.setVisibility(View.GONE);
                                    DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.order_has_not_been_successful), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
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
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
                break;
            case R.id.btn_reserve_cancel:
                Log.d("豪豪", "onClick: "+strTime);
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_ios_2);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                TextView txtTitle = dialog.findViewById(R.id.message_title);
                TextView txtContent = dialog.findViewById(R.id.message_content);
                EditText editMessage = dialog.findViewById(R.id.tv_message);
                Button btnConfirm = dialog.findViewById(R.id.btnCheck);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                txtTitle.setText(R.string.reserve_confirm_cancel);
                txtContent.setText(R.string.fcm_merch_refuse);
                String preset = getString(R.string.reserve_answer, UserBean.MerchName);
                editMessage.setHint(preset);
                btnConfirm.setOnClickListener(v1 ->
                {
                    String custom = editMessage.getText().toString().trim();
                    if (dialog != null)
                    {
//                        binding.progressBar.setVisibility(View.VISIBLE);
//                        ApiConnection.classBookingUpdate(industryOrderBean.getBookingNo(), industryOrderBean.getReserveDate(), industryOrderBean.getReserveTime(), strRemark, "2", industryOrderBean.getProgramPerson(), new ApiConnection.OnConnectResultListener()
//                        {
//                            @Override
//                            public void onSuccess(String jsonString)
//                            {
//
                        if (!custom.equals(""))
                        {
                            classBookingUpdate(custom);
                            fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_cancel), custom, "industryRecord");
                        } else
                        {
                            classBookingUpdate(preset);
                            fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_cancel), preset, "industryRecord");
                        }
//
//                                if (getActivity() == null)
//                                {
//                                    return;
//                                }
//                                getActivity().runOnUiThread(() ->
//                                {
//                                    binding.progressBar.setVisibility(View.GONE);
//                                    Navigation.findNavController(getView()).popBackStack();
//                                });
//                            }
//
//                            @Override
//                            public void onFailure(String message)
//                            {
//                                if (getActivity() == null)
//                                {
//                                    return;
//                                }
//                                getActivity().runOnUiThread(() ->
//                                {
//                                    binding.progressBar.setVisibility(View.GONE);
//                                    DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.order_has_not_been_successful), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
//                                    {
//                                        @Override
//                                        public void onCheck()
//                                        {
//
//                                        }
//
//                                        @Override
//                                        public void onCancel()
//                                        {
//
//                                        }
//                                    });
//                                });
//                            }
//                        });
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(v2 ->
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void classBookingUpdate(String remark)
    {
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiConnection.classBookingUpdate(industryOrderBean.getBookingNo(), industryOrderBean.getReserveDate(), industryOrderBean.getReserveTime(), remark, "2", industryOrderBean.getProgramPerson(), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(() ->
                {
                    binding.progressBar.setVisibility(View.GONE);
                    Navigation.findNavController(getView()).popBackStack();
                });
            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(() ->
                {
                    binding.progressBar.setVisibility(View.GONE);
                    DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.order_has_not_been_successful), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
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
                });

            }
        });
    }

    private void fcmToMember(String mobileNo, String title, String content, String extra)
    {
        ApiConnection.fcmToMember(mobileNo, title, content, extra, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Log.d("你好", "onSuccess: " + jsonString);
            }

            @Override
            public void onFailure(String message)
            {
                Log.d("你好", "onFailure: " + message);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        strTime = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
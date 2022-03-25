package com.jotangi.nickyen.pointshop.renew.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConnection.OnConnectResultListener
import com.jotangi.nickyen.databinding.FragmentPointShopPaymentBinding
import com.jotangi.nickyen.databinding.ToolbarBinding
import com.jotangi.nickyen.merch.model.MerchMemberInfoBean
import com.jotangi.nickyen.model.MemberInfoBean
import com.jotangi.nickyen.pointshop.OrderRequest
import com.jotangi.nickyen.pointshop.renew.PointShopPayActivity
import com.jotangi.nickyen.pointshop.renew.PointShopViewNewModel
import com.jotangi.nickyen.pointshop.renew.ShoppingCartModel
import com.jotangi.nickyen.pointshop.renew.adapter.ShoppingCartAdapter
import com.jotangi.nickyen.utils.Constants
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.jotangi.nickyen.utils.setupToolbarBtn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class PointShopPaymentFragment : PointShopBaseFragment(), View.OnClickListener {

    private var _binding: FragmentPointShopPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var shoppingCartAdapter: ShoppingCartAdapter
    private var memberInfoArrayList = mutableListOf<MerchMemberInfoBean>()
    private lateinit var viewModel: PointShopViewNewModel

    private var isStatus = false // 初始化是否直接折扣點數狀態
    private var countTotal = 0 // 清單的總額
    private var ship = 0 // 運費
    private var discount = 0 //優惠券折扣
    private var point = 0 // 紅利點數折扣
    private var superTotal = 0 // 最終送出金額

    override fun getToolBar(): ToolbarBinding {
        return binding.toolbar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointShopPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PointShopViewNewModel::class.java)
        viewModel.orderNo.postValue("")
        viewModel.orderNo.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                Log.d("eee_orderno",it)
                if(it[0] in '0'..'9'){ //訂單
                    Navigation.findNavController(requireView()).popBackStack()
                    val domain = "https://tripspottest.jotangi.net/ddotpay/ecpayindex.php?orderid="
                    var intent = Intent(context,PointShopPayActivity::class.java)
                    intent.putExtra("url",domain+it)
                    startActivity(intent)
                }else{
                    messageToast(it)
                }
            }
        }

        getToolBar().apply {
            txtToolbarTitle.apply {
                text = getString(R.string.fill_out_payment_details)
            }

            setupToolbarBtn(ivToolbarBack) {
                Navigation.findNavController(requireView()).popBackStack()
            }

            setupToolbarBtn(ivToolbarShoppingCart) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_pointShopPaymentFragment_to_shoppingCartFragment)
            }
        }
        binding.btnBuy.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        getPoint()
        setCheckInfo()
        setSpinner()
        getShoppingCartList()
        getNumberShoppingCart()
    }

    private fun editTxtChange() {
        binding.editAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                try {
                    if (s.toString().toInt() > MemberInfoBean.member_points
                    ) { // 捕捉輸入點數大於自身點數 || 輸入點數大於訂單金額
                        binding.editAmount.setText(MemberInfoBean.member_points)
                    } else if (s.toString().toInt() > countTotal - discount) { // 捕捉輸入點數大於訂單金額
                        binding.editAmount.setText((countTotal - discount).toString())
                    }
                } catch (e: java.lang.Exception) {
                    binding.editAmount.setText("0")
                }

                //判定首字為0消去
                if (s.toString().length >= 2 && s.toString().indexOf("0") == 0) {
                    binding.editAmount.setText(s.toString().substring(1, s.toString().length))
                }
                if (binding.editAmount.text.isEmpty()) {
                    binding.editAmount.setText("0")
                }
                binding.editAmount.setSelection(binding.editAmount.text.length)
                binding.txtPoint.text = "-${
                    binding.editAmount.text.toString().trim {
                        it <= ' '
                    }
                }"
                binding.txtSuperTotal.text = AppUtility.strAddComma(
                    (countTotal - discount - binding.editAmount.text.toString()
                        .trim { it <= ' ' }
                        .toInt()).toString())

            }
        })
    }

    private fun setCheckInfo() {
        checkSame(binding.cbSame.isChecked)
        binding.cbSame.setOnCheckedChangeListener { _, isChecked ->
            checkSame(isChecked)
        }
    }

    private fun setSpinner() {
        binding.shipSpinner.apply {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                Constants.Ship.ship
            )
            setAdapter(adapter)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
        binding.citySpinner.apply {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                Constants.City.counties
            )
            setAdapter(adapter)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, position: Int, id: Long
                ) {
                    when (position) {
                        // 臺北市
                        0 -> bindRegionSpinner(Constants.City.districts00)
                        // 新北市
                        1 -> bindRegionSpinner(Constants.City.districts02)
                        // 桃園市
                        2 -> bindRegionSpinner(Constants.City.districts04)
                        // 新竹縣
                        3 -> bindRegionSpinner(Constants.City.districts06)
                        // 新竹市
                        4 -> bindRegionSpinner(Constants.City.districts05)
                        // 苗栗縣
                        5 -> bindRegionSpinner(Constants.City.districts07)
                        // 臺中市
                        6 -> bindRegionSpinner(Constants.City.districts08)
                        // 彰化縣
                        7 -> bindRegionSpinner(Constants.City.districts09)
                        // 雲林縣
                        8 -> bindRegionSpinner(Constants.City.districts13)
                        // 嘉義縣
                        9 -> bindRegionSpinner(Constants.City.districts12)
                        // 嘉義市
                        10 -> bindRegionSpinner(Constants.City.districts11)
                        // 臺南市
                        11 -> bindRegionSpinner(Constants.City.districts14)
                        // 高雄市
                        12 -> bindRegionSpinner(Constants.City.districts15)
                        // 屏東縣
                        13 -> bindRegionSpinner(Constants.City.districts18)
                        // 南投縣
                        14 -> bindRegionSpinner(Constants.City.districts10)
                        // 基隆市
                        15 -> bindRegionSpinner(Constants.City.districts01)
                        // 宜蘭縣
                        16 -> bindRegionSpinner(Constants.City.districts03)
                        // 花蓮縣
                        17 -> bindRegionSpinner(Constants.City.districts20)
                        // 臺東縣
                        18 -> bindRegionSpinner(Constants.City.districts19)
                        // 澎湖縣
                        19 -> bindRegionSpinner(Constants.City.districts16)
                        // 連江縣
                        20 -> bindRegionSpinner(Constants.City.districts21)
                        // 金門縣
                        21 -> bindRegionSpinner(Constants.City.districts17)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            binding.receiptSpinner.apply {
                val receiptAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    context,
                    R.layout.spinner_item,
                    Constants.Receipt.receipt
                )
                setAdapter(receiptAdapter)
                onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            binding.receiptSpinner.selectedItem.toString()
                            val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

                            when (position) {
                                0 -> {
                                    binding.receiptSpinner.setSelection(0)
                                    binding.textView7.layoutParams.height = height
                                    binding.textView7.text = getString(R.string.pd_13)
                                    binding.etReceipt.layoutParams.height = height
                                    binding.pdBill003.layoutParams.height = 0
                                    binding.pdBill00301.layoutParams.height = 0
                                    binding.pdBill00302.layoutParams.height = 0
                                    binding.pdBill00303.layoutParams.height = 0
                                    binding.pdBill00304.layoutParams.height = 0
                                    binding.pdBill00305.layoutParams.height = 0
                                    binding.pdBill00201.layoutParams.height = 0
                                }
                                1 -> {
                                    binding.receiptSpinner.setSelection(1)
                                    binding.etReceipt.layoutParams.height = height
                                    binding.textView7.text = getString(R.string.pd_16)
                                    binding.pdBill00201.layoutParams.height = height
                                    binding.pdBill003.layoutParams.height = 0
                                    binding.pdBill00301.layoutParams.height = 0
                                    binding.pdBill00302.layoutParams.height = 0
                                    binding.pdBill00303.layoutParams.height = 0
                                    binding.pdBill00304.layoutParams.height = 0
                                    binding.pdBill00305.layoutParams.height = 0

                                }
                                2 -> {
                                    binding.receiptSpinner.setSelection(2)
                                    binding.etReceipt.layoutParams.height = 0
                                    binding.pdBill00201.layoutParams.height = 0
                                    binding.pdBill003.layoutParams.height = height
                                    binding.pdBill00301.layoutParams.height = height
                                    binding.pdBill00302.layoutParams.height = height
                                    binding.pdBill00303.layoutParams.height = height
                                    binding.pdBill00304.layoutParams.height = height
                                    binding.pdBill00305.layoutParams.height = height
                                }
                            }
                        }
                    }
            }
        }
    }

    fun bindRegionSpinner(data: Array<String>) {
        binding.regionSpinner.apply {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context, R.layout.spinner_item, data
            )
            setAdapter(adapter)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    private fun checkSame(isChecked: Boolean) {
        if (isChecked) {
            binding.apply {
                etName.setText(MemberInfoBean.decryptName)
                etPhone.setText(MemberInfoBean.decryptId)
                etEmail.setText(MemberInfoBean.decryptEmail)
            }
        } else {
            binding.apply {
                etName.setText("")
                etPhone.setText("")
                etEmail.setText("")
            }
        }
    }

    private fun getPoint() {
        isStatus = true
        ApiConnection.getMerchMemberInfo(object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                val type = object : TypeToken<ArrayList<MerchMemberInfoBean?>?>() {}.type
                memberInfoArrayList =
                    Gson().fromJson<ArrayList<MerchMemberInfoBean>>(jsonString, type)
                MemberInfoBean.member_points =
                    memberInfoArrayList[0].memberTotalpoints.toInt() - memberInfoArrayList[0]
                        .memberUsingpoints.toInt()
                GlobalScope.launch(Dispatchers.Main) {
                    if (isStatus) {
                        point = MemberInfoBean.member_points
                        calculationAmount()
                    }
                }
            }

            override fun onFailure(message: String) {
                Timber.e("onFailure -> $message")
                GlobalScope.launch(Dispatchers.Main) {
                    point = 0
                    calculationAmount()
                }
            }
        })
    }

    private fun bindPaymentUI() {
        binding.apply {
            editAmount.setText(point.toString())
            txtTotalAmount.text = "${AppUtility.strAddComma(countTotal.toString())}"
            txtShipFee.text = "$ship"
            txtDiscount.text = "-$discount"
            txtPoint.text = "-$point"
            txtSuperTotal.text = "${AppUtility.strAddComma(superTotal.toString())}"
        }
    }

    fun getNumberShoppingCart(onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getNumberShoppingCart(object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                Timber.e("onSuccess -> $jsonString")
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        getToolBar().txtToolNumber.text = jsonString
                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }

            override fun onFailure(message: String) {
                Timber.e("onFailure -> $message")
                onFailure?.invoke(message)
            }
        })
    }

    private fun getShoppingCartList() {
        ApiConnection.getShoppingCartList(object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                val type = object : TypeToken<ArrayList<ShoppingCartModel?>?>() {}.type
                Gson().fromJson<ArrayList<ShoppingCartModel>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.apply {
                            progressBar.makeGone()
                            btnBuy.makeVisible()
                            btnBack.makeVisible()
                            countTotal = addition(it)
                            bindPaymentUI()
                            setRecyclerView(it)
                            editTxtChange()
                        }
                    }
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.btnBuy.makeGone()
                    binding.btnBack.makeGone()
                }
            }
        })
    }

    /**
     * 計算規則 總金額-優惠券-點數
     * @return
     */
    private fun calculationAmount() {
        if (point > countTotal - discount) { // 點數大於總金額-優惠券
            point = countTotal - discount
        }
        superTotal = countTotal - discount - point
        bindPaymentUI()
    }

    private fun addition(itList: ArrayList<ShoppingCartModel>): Int {
        var count = 0
        itList.forEach {
            count += it.product_price!!.toInt() * it.order_qty!!.toInt()
        }
        return count
    }

    private fun setRecyclerView(itemList: ArrayList<ShoppingCartModel>) {
        shoppingCartAdapter =
            ShoppingCartAdapter(requireContext(), itemList, false, binding.progressBar)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingCartAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> {
                if (binding.progressBar.visibility == View.VISIBLE) {
                    return
                }
                Navigation.findNavController(requireView()).popBackStack()
            }
            R.id.btnBuy -> {
                binding.apply {
                    if (etName.text.isEmpty()) {
                        messageToast("收件人姓名未填")
                        return
                    }
                    if (etAddress.text.isEmpty()) {
                        messageToast("收件人地址未填")
                        return
                    }
                    if (etPhone.text.isEmpty()) {
                        messageToast("收件人手機未填")
                        return
                    }
                    if (etEmail.text.isEmpty()) {
                        messageToast("收件人信箱未填")
                        return
                    }
                }
                if (binding.progressBar.visibility == View.VISIBLE) {
                    return
                }
                // 最終在複查計算
                point = if (binding.editAmount.text.toString().trim { it <= ' ' }
                        .isEmpty() || binding.editAmount.text.toString()
                        .trim { it <= ' ' } == "0") {
                    0
                } else {
                    binding.editAmount.text.toString().trim { it <= ' ' }.toInt()
                }
                calculationAmount()
//                messageToast(
//                    "收件人姓名： ${binding.etName.text}\n收件人地址： ${binding.citySpinner.selectedItem.toString()}${binding.regionSpinner.selectedItem.toString()}${binding.etAddress.text}\n" +
//                            "小計： $countTotal\n運費： $ship\n折扣： $discount\n點數：$point\n應付金額：$superTotal"
//                )
                viewModel.addEcorder(OrderRequest(
                    recipient_name = binding.etName.text.toString(),
                    recipient_addr = "${binding.citySpinner.selectedItem}${binding.regionSpinner.selectedItem}${binding.etAddress.text}",
                    order_amount = countTotal,
                    discount_amount = discount,
                    bonus_point = point,
                    order_pay = superTotal
                ))

            }
        }
    }

    private fun messageToast(s: String) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
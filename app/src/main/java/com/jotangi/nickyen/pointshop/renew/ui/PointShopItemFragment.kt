package com.jotangi.nickyen.pointshop.renew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.databinding.FragmentPointShopItemBinding
import com.jotangi.nickyen.databinding.ToolbarBinding
import com.jotangi.nickyen.pointshop.renew.PointShopModel
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.jotangi.nickyen.utils.setupToolbarBtn
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*

class PointShopItemFragment : PointShopBaseFragment(), View.OnClickListener {

    private var _binding: FragmentPointShopItemBinding? = null
    private val binding get() = _binding!!
    private var item: PointShopModel? = null
    private var itemList = mutableListOf<PointShopModel>()

    private var count = 1

    override fun getToolBar(): ToolbarBinding {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = Gson().fromJson(it.getString("item"), PointShopModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getToolBar().apply {
            txtToolbarTitle.apply {
                text = getString(R.string.goods)
            }

            setupToolbarBtn(ivToolbarBack) {
                Navigation.findNavController(requireView()).popBackStack()
            }

            setupToolbarBtn(ivToolbarShoppingCart) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_pointShopItemFragment_to_shoppingCartFragment)
            }
        }
        getProductInfo()
        getNumberShoppingCart()
        binding.ibPlus.setOnClickListener(this)
        binding.ibSub.setOnClickListener(this)
        binding.btnAddCart.setOnClickListener(this)
        binding.btnBuy.setOnClickListener(this)
    }

    fun getNumberShoppingCart(onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getNumberShoppingCart(object : ApiConnection.OnConnectResultListener {
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

    private fun getProductInfo(onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getProductInfo(
            item?.product_no,
            object : ApiConnection.OnConnectResultListener {
                override fun onSuccess(jsonString: String) {
                    val type = object : TypeToken<ArrayList<PointShopModel?>?>() {}.type
                    Gson().fromJson<ArrayList<PointShopModel>>(jsonString, type)?.let {
                        Timber.e("onSuccess -> $it")
                        GlobalScope.launch(Dispatchers.Main) {
                            if (!it.isNullOrEmpty()) {
                                itemList.addAll(it)
                                binding.apply {
                                    btnBuy.makeVisible()
                                    btnAddCart.makeVisible()
                                    progressBar.makeGone()
                                    Picasso.with(requireContext())
                                        .load(ApiConstant.API_MALL_IMAGE + itemList[0].product_picture)
                                        .into(binding.ivItem)
                                    txtItemName.text = itemList[0].product_name
                                    txtItemFee.text = "$ ${AppUtility.strAddComma(itemList[0].product_price)} "
                                    txtProductDescription.text = itemList[0].product_description
                                    txtCount.text = count.toString()
                                }
                            }
                        }
                    } ?: onFailure?.invoke(null)
                }

                override fun onFailure(message: String) {
                    Timber.e("onFailure -> $message")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.btnBuy.makeGone()
                        binding.btnAddCart.makeGone()
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ibSub -> {
                count -= 1
                if (count < 1) {
                    count = 1
                    return
                }
                binding.txtCount.text = count.toString()
            }
            R.id.ibPlus -> {
                count += 1
                if (count > 20) {
                    count = 20
                    Toast.makeText(activity, "商品數量太多囉～", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.txtCount.text = count.toString()
            }
            R.id.btnAddCart -> {
                if (!AppUtility.isStr2Int(item?.product_price)) {
                    return
                }
                addCart(((item?.product_price?.toInt() ?: 9999999) * count).toString())
            }
            R.id.btnBuy -> {
                if (!AppUtility.isStr2Int(item?.product_price)) {
                    return
                }
                addCart2(((item?.product_price?.toInt() ?: 9999999) * count).toString())
            }
        }
    }

    private fun addCart(totalAmount: String) {
        binding.progressBar.makeVisible()
        ApiConnection.addShoppingCart(
            item?.product_no, item?.product_price, count.toString(), totalAmount,
            object : ApiConnection.OnConnectResultListener {
                override fun onSuccess(jsonString: String) {
                    Timber.e("onSuccess -> $jsonString , ${item?.product_no}, ${item?.product_price}, ${count}, $totalAmount")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.progressBar.makeGone()
                        AppUtility.showMyDialog(
                            requireActivity(),
                            getString(R.string.add_cart),
                            getString(R.string.text_confirm),
                            null,
                            object : AppUtility.OnBtnClickListener {
                                override fun onCheck() {

                                }

                                override fun onCancel() {
                                }
                            }
                        )
                    }
                }

                override fun onFailure(message: String) {
                    Timber.e("onFailure -> $message")

                }
            })
    }

    private fun addCart2(totalAmount: String) {
        binding.progressBar.makeVisible()
        ApiConnection.addShoppingCart(
            item?.product_no, item?.product_price, count.toString(), totalAmount,
            object : ApiConnection.OnConnectResultListener {
                override fun onSuccess(jsonString: String) {
                    Timber.e("onSuccess -> $jsonString , ${item?.product_no}, ${item?.product_price}, ${count}, $totalAmount")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.progressBar.makeGone()
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_pointShopItemFragment_to_pointShopPaymentFragment)
                    }
                }

                override fun onFailure(message: String) {
                    Timber.e("onFailure -> $message")

                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        count = 1
    }
}
package com.jotangi.nickyen.pointshop.renew.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.databinding.FragmentPointShopNewBinding
import com.jotangi.nickyen.databinding.ToolbarBinding
import com.jotangi.nickyen.pointshop.renew.PointShopActivity
import com.jotangi.nickyen.pointshop.renew.PointShopModel
import com.jotangi.nickyen.pointshop.renew.PointShopViewNewModel
import com.jotangi.nickyen.pointshop.renew.TabModel
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

class PointShopNewFragment : PointShopBaseFragment() {

    private var _binding: FragmentPointShopNewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viedModel: PointShopViewNewModel

    private var mActivity: PointShopActivity? = null
    private var rvAdapter: PointShopRecyclerAdapter? = null
    private var itemList = mutableListOf<PointShopModel>()
    private var tabList = mutableListOf<TabModel>()

    override fun getToolBar(): ToolbarBinding {
        return binding.toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mActivity = (activity as PointShopActivity)
        } catch (e: ClassCastException) {
            Timber.e("$e")
        }
        getListTab()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPointShopNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viedModel = ViewModelProvider(requireActivity()).get(PointShopViewNewModel::class.java)

        getToolBar().apply {
            txtToolbarTitle.apply {
                text = getString(R.string.point_mall)
            }

            setupToolbarBtn(ivToolbarBack) {
                mActivity?.finish()
            }

            setupToolbarBtn(ivToolbarShoppingCart) {
                mActivity?.nextPage(
                    requireView(),
                    R.id.action_pointShopNewFragment_to_shoppingCartFragment,
                    null
                )
            }
        }

        initRecyclerView(itemList)
        runTab()
        getNumberShoppingCart()

        viedModel.apply {
            getListItem("")
            listItem.observe(viewLifecycleOwner) {
                Timber.e("listItem -> $it")
                binding.progressBar.makeGone()
                itemList.clear()
                if (it.isNullOrEmpty()) {
//                binding.tvNoAppointment.makeVisible()
                } else {
//                binding.tvNoAppointment.makeGone()
                    itemList.addAll(it)
                }
                rvAdapter?.notifyDataSetChanged()
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.progressBar.makeVisible()
                tabList.forEach {
                    if (tab?.text === it.producttype_name) {
                        it.product_type?.let { it1 ->
                            viedModel.getListItem(it1)
                            viedModel.listItem.observe(viewLifecycleOwner) { it2 ->
                                binding.progressBar.makeGone()
                                itemList.clear()
                                if (!it2.isNullOrEmpty()) {
                                    itemList.addAll(it2)
                                }
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
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

    private fun initRecyclerView(mutableList: MutableList<PointShopModel>?) {
        rvAdapter =
            PointShopRecyclerAdapter(R.layout.item_commodity, mutableList)
        rvAdapter?.setOnItemClickListener { adapter, view, position ->
            val bundle = Bundle()
            bundle.putString("item", Gson().toJson(mutableList?.get(position)))
            Navigation.findNavController(requireView())
                .navigate(R.id.action_pointShopNewFragment_to_pointShopItemFragment, bundle)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter;
        }
    }

    private fun getListTab(onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getProductType(object : ApiConnection.OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                val type = object : TypeToken<ArrayList<TabModel?>?>() {}.type
                Gson().fromJson<ArrayList<TabModel>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    GlobalScope.launch(Dispatchers.Main) {
                        tabList.addAll(listOf(TabModel("0", "", "全部", "")))
                        tabList.addAll(it)
                        runTab()
                    }
                } ?: onFailure?.invoke(null)
            }

            override fun onFailure(message: String) {
                Timber.e("onFailure -> $message")
                onFailure?.invoke(message)
            }
        })
    }

    private fun runTab() {
        tabList.forEach { it ->
            binding.tabLayout.addTab(
                binding.tabLayout.newTab().setText(it.producttype_name)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class PointShopRecyclerAdapter(layoutResId: Int, data: MutableList<PointShopModel>?) :
        BaseQuickAdapter<PointShopModel, BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: PointShopModel) {
            val img: ImageView = holder.getView(R.id.img)
            val txtItemName: TextView = holder.getView(R.id.txtItemName)

            Picasso.with(context).load(ApiConstant.API_MALL_IMAGE + item.product_picture).into(img)
            txtItemName.text =
                "${item.product_name} \n NT $${AppUtility.strAddComma(item.product_price)}"
        }
    }
}
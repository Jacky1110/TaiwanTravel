package com.jotangi.nickyen.pointshop.renew.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.databinding.FragmentShoppingCartBinding
import com.jotangi.nickyen.databinding.ToolbarBinding
import com.jotangi.nickyen.pointshop.renew.ShoppingCartModel
import com.jotangi.nickyen.pointshop.renew.adapter.ShoppingCartAdapter
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.jotangi.nickyen.utils.setupToolbarBtn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ShoppingCartFragment : PointShopBaseFragment(), View.OnClickListener {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var shoppingCartAdapter: ShoppingCartAdapter

    override fun getToolBar(): ToolbarBinding {
        return binding.toolbar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getToolBar().apply {
            txtToolbarTitle.apply {
                text = getString(R.string.shopping_cart)
            }

            setupToolbarBtn(ivToolbarBack) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_shoppingCartFragment_to_pointShopNewFragment)
            }
        }
        binding.txtTerms.setOnClickListener(this)
        binding.btnBuy.setOnClickListener(this)
        binding.btnContinueShopping.setOnClickListener(this)
        getShoppingCartList()
    }

    private fun getShoppingCartList() {
        ApiConnection.getShoppingCartList(object : ApiConnection.OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                val type = object : TypeToken<ArrayList<ShoppingCartModel?>?>() {}.type
                Gson().fromJson<ArrayList<ShoppingCartModel>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.apply {
                            progressBar.makeGone()
                            btnBuy.makeVisible()
                            btnContinueShopping.makeVisible()
                            setRecyclerView(it)
                        }
                    }
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.btnBuy.makeGone()
                    binding.btnContinueShopping.makeGone()
                }
            }
        })
    }

    private fun setRecyclerView(itemList: ArrayList<ShoppingCartModel>) {
        shoppingCartAdapter =
            ShoppingCartAdapter(requireContext(), itemList, true, binding.progressBar)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shoppingCartAdapter
        }
        shoppingCartAdapter.onItemClickListener(object : ShoppingCartAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                removeItem(itemList, position)
            }
        })
    }

    fun removeItem(itemList: ArrayList<ShoppingCartModel>, position: Int) {
        if (itemList.size > 1) {
            itemList.removeAt(position)
            shoppingCartAdapter.notifyItemRemoved(position)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtTerms ->{
                dialogTerms()
            }
            R.id.btnContinueShopping -> {
                if (binding.progressBar.visibility == View.VISIBLE) {
                    return
                }
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_shoppingCartFragment_to_pointShopNewFragment)
            }
            R.id.btnBuy -> {
                if (!binding.cbAgree.isChecked) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cart_terms),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (binding.progressBar.visibility == View.VISIBLE) {
                    return
                }
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_shoppingCartFragment_to_pointShopPaymentFragment)
            }
        }
    }

    private fun dialogTerms() {
        Dialog(requireActivity()).apply {
            setContentView(R.layout.dialog_terms)
            window?.apply {
                setCanceledOnTouchOutside(false)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params: ViewGroup.LayoutParams = attributes
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                attributes = params as WindowManager.LayoutParams
            }

            findViewById<TextView>(R.id.btn_confirm).setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
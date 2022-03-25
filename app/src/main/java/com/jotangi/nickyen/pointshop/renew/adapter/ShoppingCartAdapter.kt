package com.jotangi.nickyen.pointshop.renew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.pointshop.renew.ShoppingCartModel
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by N!ck Yen on Date: 2022/2/16
 */
class ShoppingCartAdapter(
    val context: Context,
    private var dataList: List<ShoppingCartModel>,
    private var status: Boolean,
    private var progressBar: ProgressBar,
) : RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder>() {

    private var listener: OnItemClickListener? = null

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtItemName)
        val txtFee: TextView = view.findViewById(R.id.txtItemFee)
        val txtCount: TextView = view.findViewById(R.id.txtCount)
        val txtTotal: TextView = view.findViewById(R.id.txtItemTotal)
        val imgItem: ImageView = view.findViewById(R.id.ivItem)
        val btnDelete: ConstraintLayout = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_cart, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (status) {
            holder.btnDelete.makeVisible()
        }
        holder.apply {
            txtName.text = dataList[position].product_name
            txtFee.text = "NT $${AppUtility.strAddComma(dataList[position].product_price)}"
            txtCount.text = dataList[position].order_qty
            txtTotal.text =
                "$ ${AppUtility.strAddComma((dataList[position].product_price!!.toInt() * dataList[position].order_qty!!.toInt()).toString())}"
            btnDelete.setOnClickListener {
                val position: Int = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    progressBar.makeVisible()
                    ApiConnection.delShoppingCart(
                        dataList[position].product_no,
                        object : ApiConnection.OnConnectResultListener {
                            override fun onSuccess(jsonString: String?) {
                                Timber.e("onSuccess -> $jsonString")
                                GlobalScope.launch(Dispatchers.Main) {
                                    progressBar.makeGone()
                                    listener?.onDeleteClick(position)
                                }
                            }

                            override fun onFailure(message: String?) {
                                Timber.e("onFailure -> $message")
                            }

                        })
                }
            }
        }
        Picasso.with(context).load(ApiConstant.API_MALL_IMAGE + dataList[position].product_picture)
            .into(holder.imgItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
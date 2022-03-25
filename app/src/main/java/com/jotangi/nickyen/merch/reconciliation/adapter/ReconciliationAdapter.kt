package com.jotangi.nickyen.merch.reconciliation.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.merch.reconciliation.model.Reconciliation
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible

/**
 * Created by N!ck Yen on Date: 2021/12/29
 */
class ReconciliationAdapter(
    val context: Context,
    private var reconciliationList: List<Reconciliation>, private var status: String,
) : RecyclerView.Adapter<ReconciliationAdapter.MyViewHolder>() {

    fun setReconciliationList(reconciliationList: List<Reconciliation>?, status: String) {
        if (reconciliationList != null) {
            this.reconciliationList = reconciliationList
            this.status = status
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_merch_reconciliation, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(reconciliationList[position], context, status)
    }

    override fun getItemCount(): Int {
        return reconciliationList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtDate: TextView = view.findViewById(R.id.tv_order_date)
        val txtFee: TextView = view.findViewById(R.id.tv_amount)
        val btnDownload: TextView = view.findViewById(R.id.btn_download)
        val btnPayment: TextView = view.findViewById(R.id.btn_payment)

        fun bind(data: Reconciliation, context: Context, status: String) {
            txtDate.text = "${data.start_date}\n${data.end_date}"
            txtFee.text = "NT$${AppUtility.strAddComma(data.total_amountJ)}"

            btnDownload.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.parse(ApiConstant.API_IMAGE + data.profit_pdf)
                intent.data = uri
                context.startActivity(intent)
            }

            btnPayment.setOnClickListener {
//                Navigation.findNavController(it)
//                    .navigate(R.id.action_reconciliationFragment_to_paymentFragment)
                Dialog(context).apply {
                    setContentView(R.layout.dialog_payment)
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

            when (status) {
                "1" -> {
                    if (data.billing_flag == "1") btnPayment.makeGone() else btnPayment.makeVisible() // 0:未付 1:已付
                }
                "2" -> {
                    btnPayment.makeVisible()
                }
                "3" -> {
                    btnPayment.makeGone()
                }
            }
        }
    }
}
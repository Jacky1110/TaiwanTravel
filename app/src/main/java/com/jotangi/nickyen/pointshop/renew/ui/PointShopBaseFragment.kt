package com.jotangi.nickyen.pointshop.renew.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jotangi.nickyen.databinding.ToolbarBinding
import com.jotangi.nickyen.pointshop.renew.PointShopActivity
import com.jotangi.nickyen.utils.setupToolbarBtn
import timber.log.Timber
import java.lang.ClassCastException

/**
 * Created by N!ck Yen on Date: 2022/2/10
 */
abstract class PointShopBaseFragment : Fragment() {

    abstract fun getToolBar(): ToolbarBinding?
    private var mActivity: PointShopActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mActivity = (activity as PointShopActivity)
        } catch (e: ClassCastException) {
            Timber.e("$e")
        }
    }

    fun setupToolbarHome(titleName: String?, id: Int?) {
        getToolBar()?.apply {

            txtToolbarTitle.apply {
                titleName?.let { text = titleName }
            }

            setupToolbarBtn(ivToolbarBack) {
                mActivity?.back(requireView())
            }
            setupToolbarBtn(ivToolbarBack) {
                id?.let { mActivity?.nextPage(requireView(), it, null) }
            }
        }
    }

    private fun setShoppingCartNumber(num: Int?) {
        getToolBar()?.apply {
            txtToolNumber.apply {
                text = ((num ?: 0).toString())
            }
        }
    }
}
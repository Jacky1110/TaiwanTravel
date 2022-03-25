package com.jotangi.nickyen.argame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.FragmentBowldirectionBinding


class BowldirectionFragment : Fragment() {
    private var _binding: FragmentBowldirectionBinding? = null

    companion object {
        const val TAG = "BowldirectionFragment"
        fun newInstance() = BowldirectionFragment()
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentBowldirectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            bdBack.setOnClickListener {
                requireActivity().onBackPressed()
            }


        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
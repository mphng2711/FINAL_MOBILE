package com.example.purepawapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentPolicyBinding
import com.example.purepawapp.ui.common.BaseFragment

class PolicyFragment : BaseFragment<FragmentPolicyBinding>(FragmentPolicyBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rowPolicy1.setOnClickListener {
            toggle(binding.contentPolicy1, binding.tvChevron1)
        }
        binding.rowPolicy2.setOnClickListener {
            toggle(binding.contentPolicy2, binding.tvChevron2)
        }
    }

    private fun toggle(content: View, chevron: android.widget.TextView) {
        val expanded = content.visibility == View.VISIBLE
        content.visibility = if (expanded) View.GONE else View.VISIBLE
        chevron.text = if (expanded) "▼" else "▲"
    }
}

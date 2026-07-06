package com.example.purepawapp.ui.spa

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSpaConfirmBinding
import com.example.purepawapp.ui.common.BaseFragment
import com.google.android.material.card.MaterialCardView

class SpaConfirmFragment : BaseFragment<FragmentSpaConfirmBinding>(FragmentSpaConfirmBinding::inflate) {

    private var selectedPaymentIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val cards = listOf(
            binding.cardPaymentStore,
            binding.cardPaymentBank,
            binding.cardPaymentEwallet
        )
        val radios = listOf(
            binding.radioPaymentStore,
            binding.radioPaymentBank,
            binding.radioPaymentEwallet
        )

        cards.forEachIndexed { index, card ->
            card.setOnClickListener {
                selectedPaymentIndex = index
                updatePaymentSelection(cards, radios)
            }
        }

        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(R.id.action_spaConfirmFragment_to_spaBookingSuccessFragment)
        }
    }

    private fun updatePaymentSelection(
        cards: List<MaterialCardView>,
        radios: List<android.widget.RadioButton>
    ) {
        cards.forEachIndexed { index, card ->
            val isSelected = index == selectedPaymentIndex
            card.strokeWidth = if (isSelected) (2 * resources.displayMetrics.density).toInt() else (1 * resources.displayMetrics.density).toInt()
            card.strokeColor = resources.getColor(
                if (isSelected) R.color.pp_primary else R.color.pp_outline,
                null
            )
            card.setCardBackgroundColor(
                resources.getColor(
                    if (isSelected) R.color.pp_light_beige else R.color.pp_surface,
                    null
                )
            )
            radios[index].isChecked = isSelected

            val titleView = (card.getChildAt(0) as? android.view.ViewGroup)?.getChildAt(1) as? android.widget.TextView
            titleView?.setTextColor(
                resources.getColor(
                    if (isSelected) R.color.pp_primary else R.color.pp_text_primary,
                    null
                )
            )
        }
    }
}

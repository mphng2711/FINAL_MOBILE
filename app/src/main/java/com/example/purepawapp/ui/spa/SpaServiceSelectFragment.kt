package com.example.purepawapp.ui.spa

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSpaServiceSelectBinding
import com.example.purepawapp.ui.common.BaseFragment
import com.google.android.material.card.MaterialCardView

class SpaServiceSelectFragment :
    BaseFragment<FragmentSpaServiceSelectBinding>(FragmentSpaServiceSelectBinding::inflate) {

    private data class SpaService(
        val name: String,
        val price: String
    )

    private val services = listOf(
        SpaService("Tắm gội thú cưng", "120.000đ"),
        SpaService("Cắt tỉa lông", "180.000đ"),
        SpaService("Cắt móng", "60.000đ"),
        SpaService("Massage thư giãn", "150.000đ"),
        SpaService("Combo chăm sóc toàn diện", "350.000đ")
    )

    private var selectedIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cards = listOf(
            binding.cardService1,
            binding.cardService2,
            binding.cardService3,
            binding.cardService4,
            binding.cardService5
        )
        val radios = listOf(
            binding.radioService1,
            binding.radioService2,
            binding.radioService3,
            binding.radioService4,
            binding.radioService5
        )

        cards.forEachIndexed { index, card ->
            card.setOnClickListener {
                selectedIndex = index
                updateSelection(cards, radios)
            }
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_spaServiceSelectFragment_to_spaDateTimeFragment)
        }
        binding.btnViewAppointments.setOnClickListener {
            findNavController().navigate(R.id.action_spaServiceSelectFragment_to_appointmentsFragment)
        }
    }

    private fun updateSelection(cards: List<MaterialCardView>, radios: List<android.widget.RadioButton>) {
        cards.forEachIndexed { index, card ->
            val isSelected = index == selectedIndex
            card.strokeWidth = if (isSelected) 2.dp() else 1.dp()
            card.setCardBackgroundColor(
                resources.getColor(
                    if (isSelected) R.color.pp_light_beige else R.color.pp_surface,
                    null
                )
            )
            card.strokeColor = resources.getColor(
                if (isSelected) R.color.pp_primary else R.color.pp_outline,
                null
            )
            radios[index].isChecked = isSelected
        }

        val service = services[selectedIndex]
        binding.tvSelectedSummary.text = service.name
        binding.tvSelectedPrice.text = service.price
    }

    private fun Int.dp(): Int = (this * resources.displayMetrics.density).toInt()
}

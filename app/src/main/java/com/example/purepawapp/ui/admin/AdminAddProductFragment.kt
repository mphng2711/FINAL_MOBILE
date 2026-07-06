package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.data.model.ProductVariant
import com.example.purepawapp.databinding.FragmentAdminAddProductBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch
import java.util.UUID

class AdminAddProductFragment : BaseFragment<FragmentAdminAddProductBinding>(FragmentAdminAddProductBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty().trim()
            val shortDescription = binding.etShortDescription.text?.toString().orEmpty().trim()
            val description = binding.etDescription.text?.toString().orEmpty().trim()
            val price = binding.etPrice.text?.toString()?.toDoubleOrNull()
            val stock = binding.etStock.text?.toString()?.toIntOrNull()
            val categoryId = binding.chipGroupCategory.checkedChipId
                .let { binding.root.findViewById<android.view.View>(it) }
                ?.tag as? String ?: "food"

            if (name.isEmpty() || price == null || stock == null) {
                toast("Vui lòng nhập đầy đủ tên, giá và tồn kho")
                return@setOnClickListener
            }

            binding.btnSave.isEnabled = false
            val product = Product(
                name = name,
                slug = name.lowercase().replace(Regex("\\s+"), "-"),
                categoryId = categoryId,
                description = description,
                shortDescription = shortDescription,
                variants = listOf(
                    ProductVariant(
                        id = UUID.randomUUID().toString(),
                        name = "Mặc định",
                        price = price,
                        sku = "SKU-${System.currentTimeMillis()}",
                        stock = stock
                    )
                )
            )

            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.productRepository.addProduct(product)
                    .onSuccess {
                        toast("Đã thêm sản phẩm")
                        findNavController().popBackStack()
                    }
                    .onFailure {
                        binding.btnSave.isEnabled = true
                        toast(it.message ?: "Không thể thêm sản phẩm")
                    }
            }
        }
    }
}

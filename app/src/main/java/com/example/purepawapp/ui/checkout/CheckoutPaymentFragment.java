package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentCheckoutPaymentBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CheckoutConfig;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.List;

public class CheckoutPaymentFragment extends BaseFragment<FragmentCheckoutPaymentBinding> {

    public CheckoutPaymentFragment() {
        super(FragmentCheckoutPaymentBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        List<View> rows = List.of(getBinding().rowCod, getBinding().rowBankTransfer, getBinding().rowEwallet);
        List<RadioButton> radios = List.of(getBinding().rbCod, getBinding().rbBankTransfer, getBinding().rbEwallet);
        String[] codes = {"cod", "bank", "ewallet"};
        String[] labels = {"Thanh toán khi nhận hàng (COD)", "Chuyển khoản ngân hàng", "Ví điện tử"};

        String currentCode = ServiceLocator.getCheckoutDraft().getPaymentMethodCode();
        int selected = 0;
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(currentCode)) selected = i;
        }
        selectPayment(radios, selected);

        for (int i = 0; i < rows.size(); i++) {
            int index = i;
            rows.get(i).setOnClickListener(v -> {
                selectPayment(radios, index);
                ServiceLocator.getCheckoutDraft().setPaymentMethod(codes[index], labels[index]);
            });
        }
        ServiceLocator.getCheckoutDraft().setPaymentMethod(codes[selected], labels[selected]);

        renderSummary();

        getBinding().btnNext.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_checkoutPaymentFragment_to_checkoutReviewFragment));
    }

    private void selectPayment(List<RadioButton> radios, int selectedIndex) {
        for (int i = 0; i < radios.size(); i++) {
            radios.get(i).setChecked(i == selectedIndex);
        }
    }

    private void renderSummary() {
        var cart = ServiceLocator.getCartRepository();
        double subtotal = cart.subtotal();
        double discount = cart.discount();
        double shipping = cart.getItems().isEmpty() ? 0.0 : CheckoutConfig.SHIPPING_FEE;
        double total = Math.max(subtotal - discount, 0.0) + shipping;

        getBinding().tvSubtotal.setText(CurrencyUtils.toVndString(subtotal));
        getBinding().tvShipping.setText(CurrencyUtils.toVndString(shipping));
        getBinding().tvDiscount.setText("-" + CurrencyUtils.toVndString(discount));
        getBinding().tvTotal.setText(CurrencyUtils.toVndString(total));
    }
}

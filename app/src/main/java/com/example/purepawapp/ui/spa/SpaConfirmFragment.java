package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentSpaConfirmBinding;
import com.example.purepawapp.ui.common.BaseFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SpaConfirmFragment extends BaseFragment<FragmentSpaConfirmBinding> {

    private int selectedPaymentIndex = 0;

    public SpaConfirmFragment() {
        super(FragmentSpaConfirmBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        List<MaterialCardView> cards = List.of(getBinding().cardPaymentStore, getBinding().cardPaymentBank, getBinding().cardPaymentEwallet);
        List<RadioButton> radios = List.of(getBinding().radioPaymentStore, getBinding().radioPaymentBank, getBinding().radioPaymentEwallet);

        for (int i = 0; i < cards.size(); i++) {
            int index = i;
            cards.get(i).setOnClickListener(v -> {
                selectedPaymentIndex = index;
                updatePaymentSelection(cards, radios);
            });
        }

        getBinding().btnConfirm.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_spaConfirmFragment_to_spaBookingSuccessFragment));
    }

    private void updatePaymentSelection(List<MaterialCardView> cards, List<RadioButton> radios) {
        for (int i = 0; i < cards.size(); i++) {
            MaterialCardView card = cards.get(i);
            boolean isSelected = i == selectedPaymentIndex;
            float density = getResources().getDisplayMetrics().density;
            card.setStrokeWidth((int) ((isSelected ? 2 : 1) * density));
            card.setStrokeColor(getResources().getColor(isSelected ? R.color.pp_primary : R.color.pp_outline, null));
            card.setCardBackgroundColor(getResources().getColor(isSelected ? R.color.pp_light_beige : R.color.pp_surface, null));
            radios.get(i).setChecked(isSelected);

            if (card.getChildAt(0) instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) card.getChildAt(0);
                if (group.getChildAt(1) instanceof TextView) {
                    TextView titleView = (TextView) group.getChildAt(1);
                    titleView.setTextColor(getResources().getColor(isSelected ? R.color.pp_primary : R.color.pp_text_primary, null));
                }
            }
        }
    }
}

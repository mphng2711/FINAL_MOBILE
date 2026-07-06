package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentSpaServiceSelectBinding;
import com.example.purepawapp.ui.common.BaseFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SpaServiceSelectFragment extends BaseFragment<FragmentSpaServiceSelectBinding> {

    private static class SpaServiceOption {
        final String name;
        final String price;

        SpaServiceOption(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    private final List<SpaServiceOption> services = List.of(
            new SpaServiceOption("Tắm gội thú cưng", "120.000đ"),
            new SpaServiceOption("Cắt tỉa lông", "180.000đ"),
            new SpaServiceOption("Cắt móng", "60.000đ"),
            new SpaServiceOption("Massage thư giãn", "150.000đ"),
            new SpaServiceOption("Combo chăm sóc toàn diện", "350.000đ")
    );

    private int selectedIndex = 0;

    public SpaServiceSelectFragment() {
        super(FragmentSpaServiceSelectBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<MaterialCardView> cards = List.of(getBinding().cardService1, getBinding().cardService2,
                getBinding().cardService3, getBinding().cardService4, getBinding().cardService5);
        List<RadioButton> radios = List.of(getBinding().radioService1, getBinding().radioService2,
                getBinding().radioService3, getBinding().radioService4, getBinding().radioService5);

        for (int i = 0; i < cards.size(); i++) {
            int index = i;
            cards.get(i).setOnClickListener(v -> {
                selectedIndex = index;
                updateSelection(cards, radios);
            });
        }

        getBinding().btnNext.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_spaServiceSelectFragment_to_spaDateTimeFragment));
        getBinding().btnViewAppointments.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_spaServiceSelectFragment_to_appointmentsFragment));
    }

    private void updateSelection(List<MaterialCardView> cards, List<RadioButton> radios) {
        for (int i = 0; i < cards.size(); i++) {
            MaterialCardView card = cards.get(i);
            boolean isSelected = i == selectedIndex;
            card.setStrokeWidth(isSelected ? dp(2) : dp(1));
            card.setCardBackgroundColor(getResources().getColor(isSelected ? R.color.pp_light_beige : R.color.pp_surface, null));
            card.setStrokeColor(getResources().getColor(isSelected ? R.color.pp_primary : R.color.pp_outline, null));
            radios.get(i).setChecked(isSelected);
        }

        SpaServiceOption service = services.get(selectedIndex);
        getBinding().tvSelectedSummary.setText(service.name);
        getBinding().tvSelectedPrice.setText(service.price);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}

package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Address;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentCheckoutAddressBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

public class CheckoutAddressFragment extends BaseFragment<FragmentCheckoutAddressBinding> {

    private Address savedAddress;

    public CheckoutAddressFragment() {
        super(FragmentCheckoutAddressBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        Address draftAddress = ServiceLocator.getCheckoutDraft().getAddress();
        if (draftAddress != null) {
            fillForm(draftAddress);
        }

        getBinding().cardSavedAddress.setOnClickListener(v -> {
            if (savedAddress != null) fillForm(savedAddress);
        });

        loadSavedAddress();

        getBinding().btnNext.setOnClickListener(v -> {
            String fullName = text(getBinding().etFullName).trim();
            String phone = text(getBinding().etPhone).trim();
            String street = text(getBinding().etStreet).trim();
            String city = text(getBinding().etCity).trim();

            if (fullName.isEmpty() || phone.isEmpty() || street.isEmpty() || city.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập đầy đủ thông tin địa chỉ");
                return;
            }

            Address address = new Address();
            address.setFullName(fullName);
            address.setPhone(phone);
            address.setStreet(street);
            address.setCity(city);
            ServiceLocator.getCheckoutDraft().setAddress(address);

            NavHostFragment.findNavController(this).navigate(R.id.action_checkoutAddressFragment_to_checkoutPaymentFragment);
        });
    }

    private void loadSavedAddress() {
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) return;
        ServiceLocator.getProfileRepository().getUser(userId, new RepoCallback<>() {
            @Override
            public void onSuccess(User user) {
                if (getBinding() == null) return;
                Address address = user.getAddress();
                if (address == null || address.getFullName() == null || address.getFullName().isBlank()) return;
                savedAddress = address;
                getBinding().cardSavedAddress.setVisibility(View.VISIBLE);
                getBinding().tvSavedName.setText(address.getFullName());
                getBinding().tvSavedPhone.setText(address.getPhone());
                getBinding().tvSavedLocation.setText(locationLine(address));
                if (ServiceLocator.getCheckoutDraft().getAddress() == null) {
                    fillForm(address);
                }
            }

            @Override
            public void onError(Exception error) {
                // No saved profile address to prefill; user can enter one manually.
            }
        });
    }

    private void fillForm(Address address) {
        getBinding().etFullName.setText(address.getFullName());
        getBinding().etPhone.setText(address.getPhone());
        getBinding().etStreet.setText(address.getStreet());
        String location = locationLine(address);
        getBinding().etCity.setText(location.isEmpty() ? address.getCity() : location);
    }

    private String locationLine(Address address) {
        StringBuilder sb = new StringBuilder();
        for (String part : new String[]{address.getWard(), address.getDistrict(), address.getCity()}) {
            if (part == null || part.isBlank()) continue;
            if (sb.length() > 0) sb.append(", ");
            sb.append(part);
        }
        return sb.toString();
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }
}

package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.model.Pet;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentSpaConfirmBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.BookingStatus;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SpaConfirmFragment extends BaseFragment<FragmentSpaConfirmBinding> {

    private static final String[] PAYMENT_CODES = {"store", "bank", "ewallet"};
    private static final String[] PAYMENT_LABELS = {"Thanh toán tại cửa hàng", "Chuyển khoản ngân hàng", "Ví điện tử"};

    private int selectedPaymentIndex = 0;

    public SpaConfirmFragment() {
        super(FragmentSpaConfirmBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        renderBookingSummary();

        List<MaterialCardView> cards = List.of(getBinding().cardPaymentStore, getBinding().cardPaymentBank, getBinding().cardPaymentEwallet);
        List<RadioButton> radios = List.of(getBinding().radioPaymentStore, getBinding().radioPaymentBank, getBinding().radioPaymentEwallet);

        for (int i = 0; i < cards.size(); i++) {
            int index = i;
            cards.get(i).setOnClickListener(v -> {
                selectedPaymentIndex = index;
                updatePaymentSelection(cards, radios);
                ServiceLocator.getBookingDraft().setPaymentMethod(PAYMENT_CODES[index], PAYMENT_LABELS[index]);
            });
        }
        ServiceLocator.getBookingDraft().setPaymentMethod(PAYMENT_CODES[selectedPaymentIndex], PAYMENT_LABELS[selectedPaymentIndex]);

        getBinding().btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void renderBookingSummary() {
        var draft = ServiceLocator.getBookingDraft();
        getBinding().tvServiceValue.setText(draft.getServiceName());
        getBinding().tvDateValue.setText(draft.getBookingDateLabel());
        getBinding().tvTimeValue.setText(draft.getTimeSlot());
        getBinding().tvServicePrice.setText(CurrencyUtils.toVndString(draft.getPrice()));
        getBinding().tvPriceBreakdownService.setText(CurrencyUtils.toVndString(draft.getPrice()));
        getBinding().tvTotalValue.setText(CurrencyUtils.toVndString(draft.getPrice()));

        Pet pet = draft.getPet();
        if (pet != null) {
            String petLabel = pet.getName() + (pet.getSpecies().isEmpty() ? "" : " (" + pet.getSpecies() + ")");
            getBinding().tvPetValue.setText(petLabel);
            if (pet.getNote() != null && !pet.getNote().isBlank()) {
                getBinding().tvNoteTitle.setVisibility(View.VISIBLE);
                getBinding().llPetNote.setVisibility(View.VISIBLE);
                getBinding().tvPetNote.setText(pet.getNote());
            }
        } else {
            getBinding().tvPetValue.setText("");
        }
    }

    private void confirmBooking() {
        var draft = ServiceLocator.getBookingDraft();
        if (draft.getServiceName().isEmpty() || draft.getPet() == null) {
            ViewUtils.toast(this, "Thiếu thông tin đặt lịch, vui lòng thử lại từ đầu");
            return;
        }
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) {
            ViewUtils.toast(this, "Vui lòng đăng nhập để đặt lịch");
            return;
        }

        Booking booking = new Booking();
        booking.setBookingCode("LH" + System.currentTimeMillis());
        booking.setUserId(userId);
        booking.setPet(draft.getPet());
        booking.setServiceId(draft.getServiceId());
        booking.setServiceName(draft.getServiceName());
        booking.setServiceType(draft.getServiceType());
        booking.setBookingDate(draft.getBookingDate());
        booking.setTimeSlot(draft.getTimeSlot());
        booking.setPrice(draft.getPrice());
        booking.setPaymentMethod(draft.getPaymentMethodCode());
        booking.setStatus(BookingStatus.PENDING);

        getBinding().btnConfirm.setEnabled(false);
        showLoading();
        ServiceLocator.getSpaRepository().createBooking(booking, new RepoCallback<>() {
            @Override
            public void onSuccess(String bookingId) {
                hideLoading();
                ServiceLocator.getBookingDraft().reset();
                NavHostFragment.findNavController(SpaConfirmFragment.this)
                        .navigate(R.id.action_spaConfirmFragment_to_spaBookingSuccessFragment);
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                if (getBinding() != null) getBinding().btnConfirm.setEnabled(true);
                ViewUtils.toast(SpaConfirmFragment.this,
                        error.getMessage() != null ? error.getMessage() : "Không thể đặt lịch, vui lòng thử lại");
            }
        });
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

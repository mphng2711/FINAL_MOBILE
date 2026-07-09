package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Pet;
import com.example.purepawapp.databinding.FragmentSpaPetInfoBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

public class SpaPetInfoFragment extends BaseFragment<FragmentSpaPetInfoBinding> {

    public SpaPetInfoFragment() {
        super(FragmentSpaPetInfoBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        Pet draft = ServiceLocator.getBookingDraft().getPet();
        if (draft != null) {
            getBinding().etPetName.setText(draft.getName());
            getBinding().etPetSpecies.setText(draft.getSpecies());
            getBinding().etPetBreed.setText(draft.getBreed());
            if (draft.getWeightKg() > 0) getBinding().etPetWeight.setText(String.valueOf(draft.getWeightKg()));
            getBinding().etPetNote.setText(draft.getNote());
        }

        getBinding().btnNext.setOnClickListener(v -> {
            String name = text(getBinding().etPetName).trim();
            String species = text(getBinding().etPetSpecies).trim();
            if (name.isEmpty() || species.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập tên và loài thú cưng");
                return;
            }
            Pet pet = new Pet();
            pet.setName(name);
            pet.setSpecies(species);
            pet.setBreed(text(getBinding().etPetBreed).trim());
            pet.setNote(text(getBinding().etPetNote).trim());
            try {
                String weightText = text(getBinding().etPetWeight).trim();
                pet.setWeightKg(weightText.isEmpty() ? 0.0 : Double.parseDouble(weightText));
            } catch (NumberFormatException ignored) {
                pet.setWeightKg(0.0);
            }
            ServiceLocator.getBookingDraft().setPet(pet);
            NavHostFragment.findNavController(this).navigate(R.id.action_spaPetInfoFragment_to_spaConfirmFragment);
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }
}

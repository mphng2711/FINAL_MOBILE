package com.example.purepawapp.ui.onboarding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.databinding.ItemOnboardingPageBinding;

import java.util.List;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder> {

    private final List<OnboardingPage> pages;

    public OnboardingPagerAdapter(List<OnboardingPage> pages) {
        this.pages = pages;
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        final ItemOnboardingPageBinding binding;

        PageViewHolder(ItemOnboardingPageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnboardingPageBinding binding = ItemOnboardingPageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        OnboardingPage page = pages.get(position);
        holder.binding.tvPageTitle.setText(page.getTitle());
        holder.binding.tvPageDescription.setText(page.getDescription());
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }
}

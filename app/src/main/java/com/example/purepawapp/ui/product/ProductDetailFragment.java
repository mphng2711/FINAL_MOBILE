package com.example.purepawapp.ui.product;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.model.Review;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentProductDetailBinding;
import com.example.purepawapp.databinding.ItemProductReviewBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends BaseFragment<FragmentProductDetailBinding> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));

    private int quantity = 1;
    private int reviewRating = 0;
    private String productId;
    private Product product;
    private String currentUserName = "Khách hàng";

    public ProductDetailFragment() {
        super(FragmentProductDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productId = getArguments() != null ? getArguments().getString("productId") : null;

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnQuantityMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                getBinding().tvQuantity.setText(String.valueOf(quantity));
            }
        });

        getBinding().btnQuantityPlus.setOnClickListener(v -> {
            quantity++;
            getBinding().tvQuantity.setText(String.valueOf(quantity));
        });

        getBinding().btnAddToCart.setOnClickListener(v -> {
            if (product == null) {
                ViewUtils.toast(this, "Không thể thêm sản phẩm vào giỏ hàng");
                return;
            }
            ProductVariant variant = product.getDefaultVariant();
            if (variant == null) {
                ViewUtils.toast(this, "Sản phẩm hiện không có sẵn");
                return;
            }
            ServiceLocator.getCartRepository().addToCart(product, variant, quantity);
            int itemCount = 0;
            for (var item : ServiceLocator.getCartRepository().getItems()) itemCount += item.getQuantity();
            ViewUtils.toast(this, "Đã thêm vào giỏ hàng (" + itemCount + " sản phẩm)");
        });

        setupReviewForm();
        loadCurrentUserName();

        if (productId == null || productId.isBlank()) {
            ViewUtils.toast(this, "Không thể tải sản phẩm");
            getBinding().btnAddToCart.setEnabled(false);
            return;
        }

        getBinding().btnAddToCart.setEnabled(false);
        showLoading();
        ServiceLocator.getProductRepository().getProduct(productId, new RepoCallback<>() {
            @Override
            public void onSuccess(Product fetched) {
                product = fetched;
                ProductUi.loadImage(getBinding().ivProductImage, fetched);
                getBinding().tvProductName.setText(fetched.getName());
                getBinding().tvProductSubtitle.setText(fetched.getShortDescription());
                getBinding().tvProductPrice.setText(CurrencyUtils.toVndString(fetched.getDisplayPrice()));
                getBinding().tvProductRating.setText(String.format("⭐ %.1f (%d đánh giá)", fetched.getAverageRating(), fetched.getTotalReviews()));
                getBinding().tvProductDescription.setText(fetched.getDescription());
                getBinding().btnAddToCart.setEnabled(true);
                hideLoading();
                loadReviews();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(ProductDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải sản phẩm");
                hideLoading();
            }
        });
    }

    private void loadCurrentUserName() {
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) return;
        ServiceLocator.getProfileRepository().getUser(userId, new RepoCallback<>() {
            @Override
            public void onSuccess(User user) {
                if (user.getFullName() != null && !user.getFullName().isBlank()) currentUserName = user.getFullName();
            }

            @Override
            public void onError(Exception error) {
                // Keep the default display name.
            }
        });
    }

    private void loadReviews() {
        ServiceLocator.getProductRepository().getReviews(productId, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Review> reviews) {
                if (getBinding() == null) return;
                getBinding().llReviews.removeAllViews();
                getBinding().tvReviewsEmpty.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.GONE);
                if (!reviews.isEmpty()) {
                    double sum = 0;
                    for (Review review : reviews) sum += review.getRating();
                    getBinding().tvProductRating.setText(String.format("⭐ %.1f (%d đánh giá)", sum / reviews.size(), reviews.size()));
                }
                for (Review review : reviews) {
                    ItemProductReviewBinding itemBinding = ItemProductReviewBinding.inflate(
                            LayoutInflater.from(requireContext()), getBinding().llReviews, false);
                    itemBinding.tvUserName.setText(review.getUserName().isBlank() ? "Khách hàng" : review.getUserName());
                    itemBinding.tvDate.setText(dateFormat.format(review.getCreatedAt()));
                    itemBinding.tvStars.setText("⭐".repeat(Math.max(0, Math.min(5, review.getRating()))));
                    itemBinding.tvComment.setText(review.getComment());
                    getBinding().llReviews.addView(itemBinding.getRoot());
                }
            }

            @Override
            public void onError(Exception error) {
                // Silently keep the review list empty if it fails to load.
            }
        });
    }

    private void setupReviewForm() {
        List<TextView> stars = List.of(getBinding().star1, getBinding().star2, getBinding().star3, getBinding().star4, getBinding().star5);
        for (int i = 0; i < stars.size(); i++) {
            int index = i;
            stars.get(i).setOnClickListener(v -> {
                reviewRating = index + 1;
                updateReviewStars(stars);
            });
        }

        getBinding().btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void updateReviewStars(List<TextView> stars) {
        for (int i = 0; i < stars.size(); i++) {
            boolean filled = i < reviewRating;
            TextView star = stars.get(i);
            star.setText(filled ? "★" : "☆");
            star.setTextColor(getResources().getColor(filled ? R.color.pp_star_amber : R.color.pp_text_secondary, null));
        }
        getBinding().btnSubmitReview.setEnabled(reviewRating > 0);
        getBinding().btnSubmitReview.setBackgroundTintList(ColorStateList.valueOf(
                getResources().getColor(reviewRating > 0 ? R.color.pp_primary : R.color.pp_disabled, null)));
        getBinding().btnSubmitReview.setTextColor(
                getResources().getColor(reviewRating > 0 ? R.color.white : R.color.pp_text_secondary, null));
    }

    private void submitReview() {
        if (reviewRating <= 0 || productId == null) return;
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) {
            ViewUtils.toast(this, "Vui lòng đăng nhập để đánh giá sản phẩm");
            return;
        }
        String comment = getBinding().etReviewComment.getText() != null ? getBinding().etReviewComment.getText().toString().trim() : "";

        Review review = new Review();
        review.setUserId(userId);
        review.setUserName(currentUserName);
        review.setProductId(productId);
        review.setReviewType("product");
        review.setRating(reviewRating);
        review.setComment(comment);

        getBinding().btnSubmitReview.setEnabled(false);
        showLoading();
        ServiceLocator.getProductRepository().addReview(productId, review, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                hideLoading();
                if (getBinding() == null) return;
                ViewUtils.toast(ProductDetailFragment.this, "Cảm ơn bạn đã đánh giá!");
                reviewRating = 0;
                getBinding().etReviewComment.setText("");
                updateReviewStars(List.of(getBinding().star1, getBinding().star2, getBinding().star3, getBinding().star4, getBinding().star5));
                loadReviews();
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                if (getBinding() != null) getBinding().btnSubmitReview.setEnabled(true);
                ViewUtils.toast(ProductDetailFragment.this,
                        error.getMessage() != null ? error.getMessage() : "Không thể gửi đánh giá, vui lòng thử lại");
            }
        });
    }
}

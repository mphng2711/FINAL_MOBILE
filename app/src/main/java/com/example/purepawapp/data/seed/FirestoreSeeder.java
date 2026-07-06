package com.example.purepawapp.data.seed;

import com.example.purepawapp.data.model.BlogPost;
import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.model.SpaService;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class FirestoreSeeder {

    private final FirebaseFirestore firestore;

    public FirestoreSeeder(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void seedAll() {
        seedCategories();
        seedProducts();
        seedSpaServices();
        seedBlogs();
    }

    private static Category category(String id, String name, String slug, int sortOrder) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setSlug(slug);
        c.setSortOrder(sortOrder);
        return c;
    }

    private static ProductVariant variant(String id, String name, double price, String sku, int stock) {
        ProductVariant v = new ProductVariant();
        v.setId(id);
        v.setName(name);
        v.setPrice(price);
        v.setSku(sku);
        v.setStock(stock);
        return v;
    }

    private static Product product(String id, String name, String slug, String categoryId, String imageSeed,
                                    String description, String shortDescription, String petType,
                                    double rating, int reviews, int sold, List<ProductVariant> variants) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setSlug(slug);
        p.setCategoryId(categoryId);
        String image = "https://picsum.photos/seed/" + imageSeed + "/600/600";
        p.setImages(Collections.singletonList(image));
        p.setThumbnail(image);
        p.setDescription(description);
        p.setShortDescription(shortDescription);
        p.setPetType(petType);
        p.setVariants(variants);
        p.setAverageRating(rating);
        p.setTotalReviews(reviews);
        p.setTotalSold(sold);
        return p;
    }

    private static SpaService spaService(String id, String name, String serviceType, String description, int durationMinutes, double price) {
        SpaService s = new SpaService();
        s.setId(id);
        s.setName(name);
        s.setServiceType(serviceType);
        s.setDescription(description);
        s.setDurationMinutes(durationMinutes);
        s.setPrice(price);
        return s;
    }

    private static BlogPost blog(String id, String title, String imageSeed, String contentHtml, long publishedAt) {
        BlogPost b = new BlogPost();
        b.setId(id);
        b.setTitle(title);
        b.setCoverImage("https://picsum.photos/seed/" + imageSeed + "/800/450");
        b.setContentHtml(contentHtml);
        b.setAuthor("Đội ngũ PURE PAW");
        b.setPublishedAt(publishedAt);
        return b;
    }

    private void seedCategories() {
        List<Category> categories = List.of(
                category("food", "Thức ăn", "food", 0),
                category("toys", "Đồ chơi", "toys", 1),
                category("accessories", "Phụ kiện", "accessories", 2),
                category("clothing", "Quần áo", "clothing", 3),
                category("health", "Chăm sóc sức khỏe", "health-care", 4)
        );
        for (Category c : categories) {
            firestore.collection(FirestoreCollections.CATEGORIES).document(c.getId()).set(c);
        }
    }

    private void seedProducts() {
        List<Product> products = List.of(
                withFeatured(product("prod_dry_food_1", "Thức ăn hạt cao cấp không ngũ cốc", "premium-grain-free-dry-food", "food",
                        "purepaw-food1", "Dinh dưỡng cân bằng cho chó trưởng thành, làm từ thịt gà thật, không chứa chất độn nhân tạo.",
                        "Thịt gà thật, không ngũ cốc, không chất độn.", "dog", 4.6, 128, 340,
                        List.of(variant("v1", "2kg", 190000.0, "FOOD-DRY-2KG", 40), variant("v2", "5kg", 450000.0, "FOOD-DRY-5KG", 25)))),
                product("prod_cat_litter_1", "Cát vệ sinh đóng bánh cho mèo", "clumping-cat-litter", "food",
                        "purepaw-litter1", "Cát vệ sinh đóng bánh khử mùi, công thức không bụi, dễ dàng dọn dẹp.",
                        "Khử mùi, không bụi, dễ vệ sinh.", "cat", 4.4, 76, 210,
                        List.of(variant("v1", "10L", 180000.0, "LITTER-10L", 60))),
                withFeatured(product("prod_squeaky_toy_1", "Đồ chơi xương nhồi bông kêu chít chít", "squeaky-plush-bone-toy", "toys",
                        "purepaw-toy1", "Đồ chơi nhồi bông bền chắc có còi kêu bên trong, an toàn cho chó cỡ vừa và lớn.",
                        "Bền chắc, kêu chít chít, an toàn cho thú cưng.", "dog", 4.8, 54, 180,
                        List.of(variant("v1", "Standard", 65000.0, "TOY-BONE-STD", 100)))),
                product("prod_cat_wand_1", "Cần câu lông vũ cho mèo", "feather-teaser-wand", "toys",
                        "purepaw-toy2", "Cần câu lông vũ giúp mèo vận động và giải trí, phù hợp cho mèo nuôi trong nhà.",
                        "Trò chơi tương tác cho mèo trong nhà.", "cat", 4.5, 39, 95,
                        List.of(variant("v1", "Standard", 45000.0, "TOY-WAND-STD", 80))),
                product("prod_leash_1", "Dây dắt phản quang", "reflective-nylon-leash", "accessories",
                        "purepaw-leash1", "Đường chỉ phản quang giúp dễ quan sát vào ban đêm, tay cầm êm ái, thoải mái.",
                        "Phản quang, tay cầm êm ái, dài 1.5m.", "dog", 4.7, 61, 140,
                        List.of(variant("v1", "1.5m", 120000.0, "ACC-LEASH-15", 55))),
                product("prod_sweater_1", "Áo len cho chó nhỏ", "knit-sweater-small-dogs", "clothing",
                        "purepaw-sweater1", "Áo len ấm áp, giặt máy được, có đủ size từ XS đến L.",
                        "Ấm áp, giặt máy được, size XS-L.", "dog", 4.3, 22, 48,
                        List.of(variant("v1", "S", 150000.0, "CLOTH-SWTR-S", 15), variant("v2", "M", 160000.0, "CLOTH-SWTR-M", 15))),
                product("prod_vitamins_1", "Kẹo dinh dưỡng đa vitamin", "multivitamin-chews", "health",
                        "purepaw-vit1", "Kẹo bổ sung vitamin hàng ngày, hỗ trợ da, lông và khớp khỏe mạnh.",
                        "Hỗ trợ da, lông và khớp.", "other", 4.6, 33, 70,
                        List.of(variant("v1", "60 viên", 210000.0, "HEALTH-VIT-60", 45))),
                withFeatured(product("prod_tuna_pate_1", "Pate ướt vị cá ngừ cho mèo", "wet-tuna-pate-cat", "food",
                        "purepaw-food2", "Pate mềm mịn vị cá ngừ, giàu đạm, kích thích vị giác cho mèo biếng ăn.",
                        "Vị cá ngừ, giàu đạm, mềm mịn.", "cat", 4.7, 85, 420,
                        List.of(variant("v1", "Lon 85g", 25000.0, "FOOD-PATE-85", 200)))),
                product("prod_beef_treats_1", "Snack thưởng vị gan bò cho chó", "beef-liver-training-treats", "food",
                        "purepaw-food3", "Snack thưởng huấn luyện vị gan bò sấy khô, thơm ngon, kích thích chó nghe lời.",
                        "Gan bò sấy khô, dùng khi huấn luyện.", "dog", 4.8, 66, 300,
                        List.of(variant("v1", "Gói 100g", 55000.0, "FOOD-TREAT-100", 150))),
                product("prod_salmon_food_1", "Thức ăn hạt vị cá hồi cho mèo", "salmon-dry-food-cat", "food",
                        "purepaw-food4", "Hạt khô vị cá hồi, bổ sung Omega-3 giúp lông mèo bóng mượt, tiêu hóa tốt.",
                        "Vị cá hồi, giàu Omega-3.", "cat", 4.5, 40, 110,
                        List.of(variant("v1", "1.5kg", 165000.0, "FOOD-SALMON-15", 50))),
                product("prod_chew_ball_1", "Bóng gặm cao su chống mòn", "durable-chew-ball", "toys",
                        "purepaw-toy3", "Bóng cao su nguyên khối, chống mòn, giúp chó giải trí và làm sạch răng.",
                        "Cao su bền chắc, giúp làm sạch răng.", "dog", 4.5, 28, 88,
                        List.of(variant("v1", "Cỡ M", 70000.0, "TOY-BALL-M", 90))),
                product("prod_catnip_mouse_1", "Chuột giả nhồi cỏ mèo (catnip)", "catnip-mouse-toy", "toys",
                        "purepaw-toy4", "Đồ chơi hình chuột nhồi cỏ mèo (catnip), kích thích bản năng săn mồi của mèo.",
                        "Nhồi cỏ mèo, kích thích săn mồi.", "cat", 4.4, 19, 65,
                        List.of(variant("v1", "Bộ 3 con", 40000.0, "TOY-CATNIP-3", 120))),
                product("prod_cat_tunnel_1", "Đường hầm vui chơi cho mèo", "cat-play-tunnel", "toys",
                        "purepaw-toy5", "Đường hầm gấp gọn nhiều lối, có bóng treo, giúp mèo vận động và giảm stress.",
                        "Gấp gọn, nhiều lối chơi, có bóng treo.", "cat", 4.6, 24, 52,
                        List.of(variant("v1", "3 lối", 195000.0, "TOY-TUNNEL-3", 35))),
                product("prod_steel_bowl_1", "Bát ăn inox chống trượt", "anti-slip-steel-bowl", "accessories",
                        "purepaw-acc1", "Bát inox 304 cao cấp, đế cao su chống trượt, dễ vệ sinh, không gỉ sét.",
                        "Inox cao cấp, đế chống trượt.", "other", 4.7, 45, 150,
                        List.of(variant("v1", "500ml", 85000.0, "ACC-BOWL-500", 70))),
                withFeatured(product("prod_pet_backpack_1", "Balo vận chuyển thú cưng", "pet-carrier-backpack", "accessories",
                        "purepaw-acc2", "Balo vận chuyển thoáng khí, cửa sổ trong suốt, chịu lực tốt, phù hợp đi du lịch.",
                        "Thoáng khí, cửa sổ trong suốt, chắc chắn.", "other", 4.6, 31, 60,
                        List.of(variant("v1", "Size M", 350000.0, "ACC-BACKPACK-M", 20)))),
                product("prod_leather_collar_1", "Vòng cổ da thật cao cấp", "premium-leather-collar", "accessories",
                        "purepaw-acc3", "Vòng cổ làm từ da thật cao cấp, khóa kim loại chắc chắn, có thể khắc tên.",
                        "Da thật, khóa kim loại, khắc tên được.", "dog", 4.7, 27, 58,
                        List.of(variant("v1", "Size M", 145000.0, "ACC-COLLAR-M", 40))),
                product("prod_raincoat_1", "Áo mưa cho chó size M", "dog-raincoat-m", "clothing",
                        "purepaw-cloth1", "Áo mưa chống thấm nước, có mũ trùm đầu, dây phản quang an toàn khi đi dạo tối.",
                        "Chống thấm, có mũ, dây phản quang.", "dog", 4.4, 18, 40,
                        List.of(variant("v1", "Size M", 130000.0, "CLOTH-RAIN-M", 25))),
                product("prod_sun_hat_1", "Nón che nắng cho thú cưng", "pet-sun-hat", "clothing",
                        "purepaw-cloth2", "Nón vành rộng che nắng, dây đeo cằm điều chỉnh được, chất liệu thoáng mát.",
                        "Vành rộng, điều chỉnh được, thoáng mát.", "other", 4.2, 12, 26,
                        List.of(variant("v1", "Size S", 60000.0, "CLOTH-HAT-S", 35))),
                product("prod_deodor_spray_1", "Xịt khử mùi lông thú cưng", "pet-deodorizing-spray", "health",
                        "purepaw-health1", "Xịt khử mùi hôi lông tự nhiên, an toàn cho da, hương thơm dịu nhẹ lưu lâu.",
                        "Khử mùi tự nhiên, an toàn cho da.", "other", 4.5, 29, 75,
                        List.of(variant("v1", "Chai 200ml", 95000.0, "HEALTH-SPRAY-200", 60))),
                product("prod_herbal_shampoo_1", "Sữa tắm thảo dược khử khuẩn", "herbal-antibacterial-shampoo", "health",
                        "purepaw-health2", "Sữa tắm chiết xuất thảo dược, kháng khuẩn nhẹ nhàng, dịu da nhạy cảm.",
                        "Chiết xuất thảo dược, kháng khuẩn nhẹ nhàng.", "other", 4.6, 37, 92,
                        List.of(variant("v1", "Chai 300ml", 110000.0, "HEALTH-SHAMPOO-300", 55)))
        );
        for (Product p : products) {
            firestore.collection(FirestoreCollections.PRODUCTS).document(p.getId()).set(p);
        }
    }

    private static Product withFeatured(Product p) {
        p.setFeatured(true);
        return p;
    }

    private void seedSpaServices() {
        List<SpaService> services = List.of(
                spaService("spa_bath", "Tắm gội & Sấy lông", "bath",
                        "Tắm gội nhẹ nhàng với sản phẩm dịu nhẹ cho da, sấy khô hoàn chỉnh sau đó.", 45, 150000.0),
                spaService("spa_grooming", "Cắt tỉa & Tạo kiểu lông", "grooming",
                        "Cắt tỉa và tạo kiểu phù hợp với từng giống thú cưng, thực hiện bởi thợ có chứng chỉ.", 60, 250000.0),
                spaService("spa_nail", "Cắt tỉa móng", "grooming",
                        "Cắt và giũa móng an toàn, nhanh chóng.", 15, 60000.0),
                spaService("spa_massage", "Massage thư giãn", "other",
                        "Liệu trình massage giúp thú cưng thư giãn, giảm căng thẳng và cải thiện tuần hoàn.", 30, 180000.0),
                spaService("spa_ear_eye", "Vệ sinh tai & mắt", "health_check",
                        "Vệ sinh nhẹ nhàng giúp phòng ngừa nhiễm trùng và kích ứng.", 15, 50000.0),
                spaService("spa_combo", "Combo chăm sóc toàn diện", "combo",
                        "Tắm gội, cắt tỉa, chăm sóc móng và vệ sinh tai/mắt trong một lần đến.", 120, 480000.0)
        );
        for (SpaService s : services) {
            firestore.collection(FirestoreCollections.SPA_SERVICES).document(s.getId()).set(s);
        }
    }

    private void seedBlogs() {
        long day = 86_400_000L;
        List<BlogPost> blogs = List.of(
                blog("blog_1", "5 bí quyết chăm sóc lông chó khỏe mạnh", "purepaw-blog1",
                        "<h2>1. Chải lông thường xuyên</h2><p>Chải lông đều đặn giúp loại bỏ lông rụng và phân bổ dầu tự nhiên.</p>" +
                                "<h2>2. Chế độ ăn cân bằng</h2><p>Chọn thức ăn giàu axit béo omega-3 và omega-6.</p>",
                        System.currentTimeMillis() - day * 5),
                blog("blog_2", "Nên tắm cho mèo bao lâu một lần?", "purepaw-blog2",
                        "<p>Hầu hết mèo tự vệ sinh cơ thể, nhưng tắm định kỳ giúp giảm rụng lông và chăm sóc da tốt hơn. " +
                                "Nên tắm khoảng 4-6 tuần một lần trừ khi bác sĩ thú y có chỉ định khác.</p>",
                        System.currentTimeMillis() - day * 2),
                blog("blog_3", "Chuẩn bị gì cho lần đầu thú cưng đi spa?", "purepaw-blog3",
                        "<p>Lần đầu tiên có thể khiến thú cưng căng thẳng. Hãy mang theo món đồ chơi yêu thích, đến sớm " +
                                "và giữ tâm lý thoải mái — thú cưng sẽ cảm nhận được năng lượng từ bạn.</p>",
                        System.currentTimeMillis())
        );
        for (BlogPost b : blogs) {
            firestore.collection(FirestoreCollections.BLOGS).document(b.getId()).set(b);
        }
    }
}

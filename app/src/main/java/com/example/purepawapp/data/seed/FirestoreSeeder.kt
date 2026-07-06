package com.example.purepawapp.data.seed

import com.example.purepawapp.data.model.BlogPost
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.data.model.ProductVariant
import com.example.purepawapp.data.model.SpaService
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Populates a freshly created Firestore project with demo data so every screen has
 * something real to render. Call [seedAll] once (e.g. from the debug button on the
 * Login screen) after google-services.json is wired up.
 */
class FirestoreSeeder(private val firestore: FirebaseFirestore) {

    suspend fun seedAll() {
        seedCategories()
        seedProducts()
        seedSpaServices()
        seedBlogs()
    }

    private suspend fun seedCategories() {
        val categories = listOf(
            Category(id = "food", name = "Thức ăn", slug = "food", sortOrder = 0),
            Category(id = "toys", name = "Đồ chơi", slug = "toys", sortOrder = 1),
            Category(id = "accessories", name = "Phụ kiện", slug = "accessories", sortOrder = 2),
            Category(id = "clothing", name = "Quần áo", slug = "clothing", sortOrder = 3),
            Category(id = "health", name = "Chăm sóc sức khỏe", slug = "health-care", sortOrder = 4)
        )
        categories.forEach {
            firestore.collection(FirestoreCollections.CATEGORIES).document(it.id).set(it).await()
        }
    }

    private suspend fun seedProducts() {
        val products = listOf(
            Product(
                id = "prod_dry_food_1",
                name = "Thức ăn hạt cao cấp không ngũ cốc",
                slug = "premium-grain-free-dry-food",
                categoryId = "food",
                images = listOf("https://picsum.photos/seed/purepaw-food1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-food1/600/600",
                description = "Dinh dưỡng cân bằng cho chó trưởng thành, làm từ thịt gà thật, không chứa chất độn nhân tạo.",
                shortDescription = "Thịt gà thật, không ngũ cốc, không chất độn.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "2kg", price = 190000.0, sku = "FOOD-DRY-2KG", stock = 40),
                    ProductVariant(id = "v2", name = "5kg", price = 450000.0, sku = "FOOD-DRY-5KG", stock = 25)
                ),
                averageRating = 4.6,
                totalReviews = 128,
                totalSold = 340,
                isFeatured = true
            ),
            Product(
                id = "prod_cat_litter_1",
                name = "Cát vệ sinh đóng bánh cho mèo",
                slug = "clumping-cat-litter",
                categoryId = "food",
                images = listOf("https://picsum.photos/seed/purepaw-litter1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-litter1/600/600",
                description = "Cát vệ sinh đóng bánh khử mùi, công thức không bụi, dễ dàng dọn dẹp.",
                shortDescription = "Khử mùi, không bụi, dễ vệ sinh.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "10L", price = 180000.0, sku = "LITTER-10L", stock = 60)
                ),
                averageRating = 4.4,
                totalReviews = 76,
                totalSold = 210
            ),
            Product(
                id = "prod_squeaky_toy_1",
                name = "Đồ chơi xương nhồi bông kêu chít chít",
                slug = "squeaky-plush-bone-toy",
                categoryId = "toys",
                images = listOf("https://picsum.photos/seed/purepaw-toy1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-toy1/600/600",
                description = "Đồ chơi nhồi bông bền chắc có còi kêu bên trong, an toàn cho chó cỡ vừa và lớn.",
                shortDescription = "Bền chắc, kêu chít chít, an toàn cho thú cưng.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Standard", price = 65000.0, sku = "TOY-BONE-STD", stock = 100)
                ),
                averageRating = 4.8,
                totalReviews = 54,
                totalSold = 180,
                isFeatured = true
            ),
            Product(
                id = "prod_cat_wand_1",
                name = "Cần câu lông vũ cho mèo",
                slug = "feather-teaser-wand",
                categoryId = "toys",
                images = listOf("https://picsum.photos/seed/purepaw-toy2/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-toy2/600/600",
                description = "Cần câu lông vũ giúp mèo vận động và giải trí, phù hợp cho mèo nuôi trong nhà.",
                shortDescription = "Trò chơi tương tác cho mèo trong nhà.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Standard", price = 45000.0, sku = "TOY-WAND-STD", stock = 80)
                ),
                averageRating = 4.5,
                totalReviews = 39,
                totalSold = 95
            ),
            Product(
                id = "prod_leash_1",
                name = "Dây dắt phản quang",
                slug = "reflective-nylon-leash",
                categoryId = "accessories",
                images = listOf("https://picsum.photos/seed/purepaw-leash1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-leash1/600/600",
                description = "Đường chỉ phản quang giúp dễ quan sát vào ban đêm, tay cầm êm ái, thoải mái.",
                shortDescription = "Phản quang, tay cầm êm ái, dài 1.5m.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "1.5m", price = 120000.0, sku = "ACC-LEASH-15", stock = 55)
                ),
                averageRating = 4.7,
                totalReviews = 61,
                totalSold = 140
            ),
            Product(
                id = "prod_sweater_1",
                name = "Áo len cho chó nhỏ",
                slug = "knit-sweater-small-dogs",
                categoryId = "clothing",
                images = listOf("https://picsum.photos/seed/purepaw-sweater1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-sweater1/600/600",
                description = "Áo len ấm áp, giặt máy được, có đủ size từ XS đến L.",
                shortDescription = "Ấm áp, giặt máy được, size XS-L.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "S", price = 150000.0, sku = "CLOTH-SWTR-S", stock = 15),
                    ProductVariant(id = "v2", name = "M", price = 160000.0, sku = "CLOTH-SWTR-M", stock = 15)
                ),
                averageRating = 4.3,
                totalReviews = 22,
                totalSold = 48
            ),
            Product(
                id = "prod_vitamins_1",
                name = "Kẹo dinh dưỡng đa vitamin",
                slug = "multivitamin-chews",
                categoryId = "health",
                images = listOf("https://picsum.photos/seed/purepaw-vit1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-vit1/600/600",
                description = "Kẹo bổ sung vitamin hàng ngày, hỗ trợ da, lông và khớp khỏe mạnh.",
                shortDescription = "Hỗ trợ da, lông và khớp.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "60 viên", price = 210000.0, sku = "HEALTH-VIT-60", stock = 45)
                ),
                averageRating = 4.6,
                totalReviews = 33,
                totalSold = 70
            ),
            Product(
                id = "prod_tuna_pate_1",
                name = "Pate ướt vị cá ngừ cho mèo",
                slug = "wet-tuna-pate-cat",
                categoryId = "food",
                images = listOf("https://picsum.photos/seed/purepaw-food2/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-food2/600/600",
                description = "Pate mềm mịn vị cá ngừ, giàu đạm, kích thích vị giác cho mèo biếng ăn.",
                shortDescription = "Vị cá ngừ, giàu đạm, mềm mịn.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Lon 85g", price = 25000.0, sku = "FOOD-PATE-85", stock = 200)
                ),
                averageRating = 4.7,
                totalReviews = 85,
                totalSold = 420,
                isFeatured = true
            ),
            Product(
                id = "prod_beef_treats_1",
                name = "Snack thưởng vị gan bò cho chó",
                slug = "beef-liver-training-treats",
                categoryId = "food",
                images = listOf("https://picsum.photos/seed/purepaw-food3/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-food3/600/600",
                description = "Snack thưởng huấn luyện vị gan bò sấy khô, thơm ngon, kích thích chó nghe lời.",
                shortDescription = "Gan bò sấy khô, dùng khi huấn luyện.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Gói 100g", price = 55000.0, sku = "FOOD-TREAT-100", stock = 150)
                ),
                averageRating = 4.8,
                totalReviews = 66,
                totalSold = 300
            ),
            Product(
                id = "prod_salmon_food_1",
                name = "Thức ăn hạt vị cá hồi cho mèo",
                slug = "salmon-dry-food-cat",
                categoryId = "food",
                images = listOf("https://picsum.photos/seed/purepaw-food4/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-food4/600/600",
                description = "Hạt khô vị cá hồi, bổ sung Omega-3 giúp lông mèo bóng mượt, tiêu hóa tốt.",
                shortDescription = "Vị cá hồi, giàu Omega-3.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "1.5kg", price = 165000.0, sku = "FOOD-SALMON-15", stock = 50)
                ),
                averageRating = 4.5,
                totalReviews = 40,
                totalSold = 110
            ),
            Product(
                id = "prod_chew_ball_1",
                name = "Bóng gặm cao su chống mòn",
                slug = "durable-chew-ball",
                categoryId = "toys",
                images = listOf("https://picsum.photos/seed/purepaw-toy3/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-toy3/600/600",
                description = "Bóng cao su nguyên khối, chống mòn, giúp chó giải trí và làm sạch răng.",
                shortDescription = "Cao su bền chắc, giúp làm sạch răng.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Cỡ M", price = 70000.0, sku = "TOY-BALL-M", stock = 90)
                ),
                averageRating = 4.5,
                totalReviews = 28,
                totalSold = 88
            ),
            Product(
                id = "prod_catnip_mouse_1",
                name = "Chuột giả nhồi cỏ mèo (catnip)",
                slug = "catnip-mouse-toy",
                categoryId = "toys",
                images = listOf("https://picsum.photos/seed/purepaw-toy4/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-toy4/600/600",
                description = "Đồ chơi hình chuột nhồi cỏ mèo (catnip), kích thích bản năng săn mồi của mèo.",
                shortDescription = "Nhồi cỏ mèo, kích thích săn mồi.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Bộ 3 con", price = 40000.0, sku = "TOY-CATNIP-3", stock = 120)
                ),
                averageRating = 4.4,
                totalReviews = 19,
                totalSold = 65
            ),
            Product(
                id = "prod_cat_tunnel_1",
                name = "Đường hầm vui chơi cho mèo",
                slug = "cat-play-tunnel",
                categoryId = "toys",
                images = listOf("https://picsum.photos/seed/purepaw-toy5/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-toy5/600/600",
                description = "Đường hầm gấp gọn nhiều lối, có bóng treo, giúp mèo vận động và giảm stress.",
                shortDescription = "Gấp gọn, nhiều lối chơi, có bóng treo.",
                petType = "cat",
                variants = listOf(
                    ProductVariant(id = "v1", name = "3 lối", price = 195000.0, sku = "TOY-TUNNEL-3", stock = 35)
                ),
                averageRating = 4.6,
                totalReviews = 24,
                totalSold = 52
            ),
            Product(
                id = "prod_steel_bowl_1",
                name = "Bát ăn inox chống trượt",
                slug = "anti-slip-steel-bowl",
                categoryId = "accessories",
                images = listOf("https://picsum.photos/seed/purepaw-acc1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-acc1/600/600",
                description = "Bát inox 304 cao cấp, đế cao su chống trượt, dễ vệ sinh, không gỉ sét.",
                shortDescription = "Inox cao cấp, đế chống trượt.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "500ml", price = 85000.0, sku = "ACC-BOWL-500", stock = 70)
                ),
                averageRating = 4.7,
                totalReviews = 45,
                totalSold = 150
            ),
            Product(
                id = "prod_pet_backpack_1",
                name = "Balo vận chuyển thú cưng",
                slug = "pet-carrier-backpack",
                categoryId = "accessories",
                images = listOf("https://picsum.photos/seed/purepaw-acc2/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-acc2/600/600",
                description = "Balo vận chuyển thoáng khí, cửa sổ trong suốt, chịu lực tốt, phù hợp đi du lịch.",
                shortDescription = "Thoáng khí, cửa sổ trong suốt, chắc chắn.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Size M", price = 350000.0, sku = "ACC-BACKPACK-M", stock = 20)
                ),
                averageRating = 4.6,
                totalReviews = 31,
                totalSold = 60,
                isFeatured = true
            ),
            Product(
                id = "prod_leather_collar_1",
                name = "Vòng cổ da thật cao cấp",
                slug = "premium-leather-collar",
                categoryId = "accessories",
                images = listOf("https://picsum.photos/seed/purepaw-acc3/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-acc3/600/600",
                description = "Vòng cổ làm từ da thật cao cấp, khóa kim loại chắc chắn, có thể khắc tên.",
                shortDescription = "Da thật, khóa kim loại, khắc tên được.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Size M", price = 145000.0, sku = "ACC-COLLAR-M", stock = 40)
                ),
                averageRating = 4.7,
                totalReviews = 27,
                totalSold = 58
            ),
            Product(
                id = "prod_raincoat_1",
                name = "Áo mưa cho chó size M",
                slug = "dog-raincoat-m",
                categoryId = "clothing",
                images = listOf("https://picsum.photos/seed/purepaw-cloth1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-cloth1/600/600",
                description = "Áo mưa chống thấm nước, có mũ trùm đầu, dây phản quang an toàn khi đi dạo tối.",
                shortDescription = "Chống thấm, có mũ, dây phản quang.",
                petType = "dog",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Size M", price = 130000.0, sku = "CLOTH-RAIN-M", stock = 25)
                ),
                averageRating = 4.4,
                totalReviews = 18,
                totalSold = 40
            ),
            Product(
                id = "prod_sun_hat_1",
                name = "Nón che nắng cho thú cưng",
                slug = "pet-sun-hat",
                categoryId = "clothing",
                images = listOf("https://picsum.photos/seed/purepaw-cloth2/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-cloth2/600/600",
                description = "Nón vành rộng che nắng, dây đeo cằm điều chỉnh được, chất liệu thoáng mát.",
                shortDescription = "Vành rộng, điều chỉnh được, thoáng mát.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Size S", price = 60000.0, sku = "CLOTH-HAT-S", stock = 35)
                ),
                averageRating = 4.2,
                totalReviews = 12,
                totalSold = 26
            ),
            Product(
                id = "prod_deodor_spray_1",
                name = "Xịt khử mùi lông thú cưng",
                slug = "pet-deodorizing-spray",
                categoryId = "health",
                images = listOf("https://picsum.photos/seed/purepaw-health1/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-health1/600/600",
                description = "Xịt khử mùi hôi lông tự nhiên, an toàn cho da, hương thơm dịu nhẹ lưu lâu.",
                shortDescription = "Khử mùi tự nhiên, an toàn cho da.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Chai 200ml", price = 95000.0, sku = "HEALTH-SPRAY-200", stock = 60)
                ),
                averageRating = 4.5,
                totalReviews = 29,
                totalSold = 75
            ),
            Product(
                id = "prod_herbal_shampoo_1",
                name = "Sữa tắm thảo dược khử khuẩn",
                slug = "herbal-antibacterial-shampoo",
                categoryId = "health",
                images = listOf("https://picsum.photos/seed/purepaw-health2/600/600"),
                thumbnail = "https://picsum.photos/seed/purepaw-health2/600/600",
                description = "Sữa tắm chiết xuất thảo dược, kháng khuẩn nhẹ nhàng, dịu da nhạy cảm.",
                shortDescription = "Chiết xuất thảo dược, kháng khuẩn nhẹ nhàng.",
                petType = "other",
                variants = listOf(
                    ProductVariant(id = "v1", name = "Chai 300ml", price = 110000.0, sku = "HEALTH-SHAMPOO-300", stock = 55)
                ),
                averageRating = 4.6,
                totalReviews = 37,
                totalSold = 92
            )
        )
        products.forEach {
            firestore.collection(FirestoreCollections.PRODUCTS).document(it.id).set(it).await()
        }
    }

    private suspend fun seedSpaServices() {
        val services = listOf(
            SpaService(
                id = "spa_bath",
                name = "Tắm gội & Sấy lông",
                serviceType = "bath",
                description = "Tắm gội nhẹ nhàng với sản phẩm dịu nhẹ cho da, sấy khô hoàn chỉnh sau đó.",
                durationMinutes = 45,
                price = 150000.0
            ),
            SpaService(
                id = "spa_grooming",
                name = "Cắt tỉa & Tạo kiểu lông",
                serviceType = "grooming",
                description = "Cắt tỉa và tạo kiểu phù hợp với từng giống thú cưng, thực hiện bởi thợ có chứng chỉ.",
                durationMinutes = 60,
                price = 250000.0
            ),
            SpaService(
                id = "spa_nail",
                name = "Cắt tỉa móng",
                serviceType = "grooming",
                description = "Cắt và giũa móng an toàn, nhanh chóng.",
                durationMinutes = 15,
                price = 60000.0
            ),
            SpaService(
                id = "spa_massage",
                name = "Massage thư giãn",
                serviceType = "other",
                description = "Liệu trình massage giúp thú cưng thư giãn, giảm căng thẳng và cải thiện tuần hoàn.",
                durationMinutes = 30,
                price = 180000.0
            ),
            SpaService(
                id = "spa_ear_eye",
                name = "Vệ sinh tai & mắt",
                serviceType = "health_check",
                description = "Vệ sinh nhẹ nhàng giúp phòng ngừa nhiễm trùng và kích ứng.",
                durationMinutes = 15,
                price = 50000.0
            ),
            SpaService(
                id = "spa_combo",
                name = "Combo chăm sóc toàn diện",
                serviceType = "combo",
                description = "Tắm gội, cắt tỉa, chăm sóc móng và vệ sinh tai/mắt trong một lần đến.",
                durationMinutes = 120,
                price = 480000.0
            )
        )
        services.forEach {
            firestore.collection(FirestoreCollections.SPA_SERVICES).document(it.id).set(it).await()
        }
    }

    private suspend fun seedBlogs() {
        val blogs = listOf(
            BlogPost(
                id = "blog_1",
                title = "5 bí quyết chăm sóc lông chó khỏe mạnh",
                coverImage = "https://picsum.photos/seed/purepaw-blog1/800/450",
                contentHtml = "<h2>1. Chải lông thường xuyên</h2><p>Chải lông đều đặn giúp loại bỏ lông rụng và phân bổ dầu tự nhiên.</p>" +
                    "<h2>2. Chế độ ăn cân bằng</h2><p>Chọn thức ăn giàu axit béo omega-3 và omega-6.</p>",
                author = "Đội ngũ PURE PAW",
                publishedAt = System.currentTimeMillis() - 86_400_000L * 5
            ),
            BlogPost(
                id = "blog_2",
                title = "Nên tắm cho mèo bao lâu một lần?",
                coverImage = "https://picsum.photos/seed/purepaw-blog2/800/450",
                contentHtml = "<p>Hầu hết mèo tự vệ sinh cơ thể, nhưng tắm định kỳ giúp giảm rụng lông và chăm sóc da tốt hơn. " +
                    "Nên tắm khoảng 4-6 tuần một lần trừ khi bác sĩ thú y có chỉ định khác.</p>",
                author = "Đội ngũ PURE PAW",
                publishedAt = System.currentTimeMillis() - 86_400_000L * 2
            ),
            BlogPost(
                id = "blog_3",
                title = "Chuẩn bị gì cho lần đầu thú cưng đi spa?",
                coverImage = "https://picsum.photos/seed/purepaw-blog3/800/450",
                contentHtml = "<p>Lần đầu tiên có thể khiến thú cưng căng thẳng. Hãy mang theo món đồ chơi yêu thích, đến sớm " +
                    "và giữ tâm lý thoải mái — thú cưng sẽ cảm nhận được năng lượng từ bạn.</p>",
                author = "Đội ngũ PURE PAW",
                publishedAt = System.currentTimeMillis()
            )
        )
        blogs.forEach {
            firestore.collection(FirestoreCollections.BLOGS).document(it.id).set(it).await()
        }
    }
}

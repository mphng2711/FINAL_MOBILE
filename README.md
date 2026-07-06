# PurePaw

Ứng dụng Android bán hàng & đặt lịch spa cho thú cưng, viết bằng Kotlin. PurePaw cho phép khách hàng mua thức ăn/phụ kiện, đặt lịch spa, theo dõi đơn hàng, đọc blog chăm sóc thú cưng, đồng thời có một khu vực Quản trị viên (Admin) riêng để quản lý toàn bộ dữ liệu cửa hàng.

## Tính năng chính

**Phía khách hàng**
- Onboarding, đăng ký / đăng nhập (Firebase Auth)
- Trang chủ: danh mục sản phẩm, sản phẩm nổi bật, khuyến mãi
- Xem chi tiết sản phẩm, thêm giỏ hàng, thanh toán (địa chỉ → phương thức thanh toán → xác nhận)
- Lịch sử đơn hàng, chi tiết đơn hàng
- Đặt lịch spa cho thú cưng: chọn dịch vụ → thông tin thú cưng → ngày giờ → xác nhận → đánh giá sau khi hoàn thành
- Blog chăm sóc thú cưng (danh sách + chi tiết bài viết)
- Hồ sơ cá nhân, chỉnh sửa thông tin, chính sách, giới thiệu, liên hệ
- Thông báo đẩy (Firebase Cloud Messaging)

**Phía quản trị viên (Admin)**
- Dashboard tổng quan (đơn hàng, doanh thu, sản phẩm, người dùng, biểu đồ doanh thu)
- Quản lý sản phẩm, danh mục, khuyến mãi, blog
- Quản lý đơn hàng và lịch đặt spa
- Quản lý người dùng

## Công nghệ sử dụng

- **Ngôn ngữ:** Kotlin
- **UI:** Fragment + XML layout, View Binding, Navigation Component, ViewPager2, RecyclerView
- **Kiến trúc:** MVVM (ViewModel, LiveData/Coroutines), Repository pattern, dependency injection thủ công qua `ServiceLocator`
- **Backend:** Firebase Authentication, Cloud Firestore, Firebase Storage, Firebase Cloud Messaging
- **Local cache:** SQLite (qua `PurePawDbHelper`) để cache dữ liệu sản phẩm offline
- **Khác:** DataStore Preferences (lưu session), Glide (tải ảnh), Google Play Services Ads

## Cấu trúc thư mục

```
app/src/main/java/com/example/purepawapp/
├── data/
│   ├── local/        # Cache SQLite (ProductCacheDao, PurePawDbHelper...)
│   ├── model/        # Data class: Product, Order, Booking, User, Pet...
│   ├── repository/   # Tầng repository giao tiếp Firestore/local cache
│   ├── seed/         # Khởi tạo dữ liệu mẫu lên Firestore (FirestoreSeeder)
│   └── session/       # Quản lý phiên đăng nhập (SessionManager)
├── di/                # ServiceLocator (khởi tạo repository/dependency)
├── notification/      # Xử lý FCM & hiển thị thông báo
└── ui/
    ├── admin/         # Toàn bộ màn hình quản trị viên
    ├── auth/          # Đăng nhập / đăng ký
    ├── blog/          # Danh sách & chi tiết blog
    ├── cart/          # Giỏ hàng
    ├── checkout/       # Quy trình thanh toán
    ├── home/          # Trang chủ
    ├── onboarding/    # Màn hình giới thiệu lần đầu
    ├── order/         # Lịch sử & chi tiết đơn hàng
    ├── product/       # Danh sách & chi tiết sản phẩm
    ├── profile/       # Hồ sơ, chính sách, liên hệ, giới thiệu
    ├── spa/           # Quy trình đặt lịch spa
    └── splash/        # Splash screen
```

## Yêu cầu môi trường

- Android Studio (Ladybug trở lên khuyến nghị)
- JDK 11+
- Android SDK, `compileSdk = 36`, `minSdk = 24`, `targetSdk = 36`
- Một project Firebase (Auth, Firestore, Storage, Messaging đã bật) với file `google-services.json` đặt tại `app/google-services.json`

## Hướng dẫn chạy dự án

1. Clone repo:
   ```
   git clone https://github.com/mphng2711/FINAL_MOBILE.git
   ```
2. Mở project bằng Android Studio, để Gradle tự đồng bộ.
3. Đảm bảo file `app/google-services.json` (Firebase config) tồn tại. Nếu dùng project Firebase riêng, tải file config từ Firebase Console và thay thế file này.
4. Chạy trên thiết bị thật hoặc máy ảo (AVD) Android API 24 trở lên:
   ```
   ./gradlew assembleDebug
   ```
   hoặc bấm Run trực tiếp trong Android Studio.

## Package name

`com.example.purepawapp`

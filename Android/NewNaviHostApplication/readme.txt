一、适配Android Splash
    1.启动画面底部的 Brand 图片 require api 31，<item name="android:windowSplashScreenBrandingImage">@drawable/brand_img</item>
    2.默认启动闪屏画面无法关闭，无法自定义
    3.windowSplashScreenIconBackgroundColor没生效，可能是因为用的icon图比较大遮住了背景的缘故
    4.自定义退出动画需要在min api 31上才能使用

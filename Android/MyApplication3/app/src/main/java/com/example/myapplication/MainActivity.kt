package com.example.myapplication

import android.app.ActivityManager
import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var view:SimpleDraweeView
    private lateinit var view1:SimpleDraweeView
    private lateinit var mActivityManager:ActivityManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在加载图片之前，你必须初始化Fresco类
//        mActivityManager=getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Fresco.initialize(this,FrescoConfig.getImagePipelineConfig(this, false))
        //FLog.setMinimumLoggingLevel(FLog.VERBOSE)
        setContentView(R.layout.activity_main)
//        Log.e("wjc-heap-size",getMaxCacheSize().toString())
        view=findViewById(R.id.img_subbanner_icon)
        view1=findViewById(R.id.img_subbanner_icon1)
        //https://66chat-chat-video.obs.cn-north-4.myhuaweicloud.com/yysp_home_0112.webp
//        Glide.with(this).load("https://66chat-chat-video.obs.cn-north-4.myhuaweicloud.com/spsp_home_0112.webp").into(view)
//        Glide.with(this).load("https://66chat-chat-video.obs.cn-north-4.myhuaweicloud.com/yysp_home_0112.webp").into(view1)
        displayImage(view, "https://66chat-chat-video.obs.cn-north-4.myhuaweicloud.com/spsp_home_0112.webp", true,false,null)
        displayImage(view1, "https://66chat-chat-video.obs.cn-north-4.myhuaweicloud.com/yysp_home_0112.webp", true,false,null)
    }

    fun displayImage(
        view: SimpleDraweeView?,
        uri: String,
        autoPlayAnimations: Boolean,
        tapToRetry: Boolean,
        warningType: String?
    ) {
        if (TextUtils.isEmpty(uri)) return
        if (view == null) {
            return
        }
        val imageRequest =
            ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)) //                        .setResizeOptions(new ResizeOptions(view.getLayoutParams().width, view.getLayoutParams().height))
                .setProgressiveRenderingEnabled(true)
                .setImageDecodeOptions(getImageDecodeOptions(view))
                .setAutoRotateEnabled(true) //旋转角度
                .build()
        val draweeController: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(view.controller)
            .setTapToRetryEnabled(tapToRetry)
            .setControllerListener(
                FrescoControllerListener<ImageInfo>(
                    warningType,
                    uri,
                    null
                )
            )
            .setAutoPlayAnimations(autoPlayAnimations)
            .build()
        view.controller = draweeController
    }

    private fun getImageDecodeOptions(view: SimpleDraweeView?): ImageDecodeOptions? {
        if (view != null && view.hierarchy != null && view.hierarchy.roundingParams != null) {
            if (view.hierarchy.roundingParams!!.roundAsCircle || view.hierarchy.roundingParams!!
                    .cornersRadii != null
            ) {
                return ImageDecodeOptions.newBuilder().setForceStaticImage(true).build()
            }
        }
        return ImageDecodeOptions.defaults()
    }

    private class FrescoControllerListener<INFO> internal constructor(
        private val mWarningType: String?,
        private val mUrl: String,
        private val mControllerListener: ControllerListener<in INFO>?
    ) :
        ControllerListener<INFO?> {
        override fun onSubmit(id: String, callerContext: Any) {
            mControllerListener?.onSubmit(id, callerContext)
        }

        override fun onFinalImageSet(id: String, imageInfo: INFO?, animatable: Animatable?) {
            mControllerListener?.onFinalImageSet(id, imageInfo, animatable)
        }

        override fun onIntermediateImageSet(id: String, imageInfo: INFO?) {
            mControllerListener?.onIntermediateImageSet(id, imageInfo)
        }

        override fun onIntermediateImageFailed(id: String, throwable: Throwable) {
            mControllerListener?.onIntermediateImageFailed(id, throwable)
        }

        override fun onFailure(id: String, throwable: Throwable) {
        }

        override fun onRelease(id: String) {
            mControllerListener?.onRelease(id)
        }
    }

    private fun getMaxCacheSize(): Int {
        val maxMemory =
            (mActivityManager.memoryClass * ByteConstants.MB).coerceAtMost(Int.MAX_VALUE)
        return if (maxMemory < 32 * ByteConstants.MB) {
            4 * ByteConstants.MB
        } else if (maxMemory < 64 * ByteConstants.MB) {
            6 * ByteConstants.MB
        } else {
            // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
            // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                8 * ByteConstants.MB
            } else {
                maxMemory / 4
            }
        }
    }
}
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangjinyuan on 2016/4/21.
 */
public class FrescoConfig {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE =150 * ByteConstants.MB;// MAX_HEAP_SIZE / 4;//使用的缓存数量

    public static final int DELETE_MAX_MEMORY_CACHE_SIZE =30 * ByteConstants.MB;// MAX_HEAP_SIZE / 4;//使用的缓存数量

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 300 * ByteConstants.MB;//默认图磁盘缓存的最大值    //2017-01-12 zhangshuo----最大缓存由50改为300，私信图片预加载

    @SuppressLint("StaticFieldLeak")
    private static ImagePipelineConfig sImagePipelineConfig;

    private FrescoConfig(){}
    /**
     * 初始化配置，单例
     */
    public static ImagePipelineConfig getImagePipelineConfig(Context context, boolean useStethoInterceptor) {
        if (sImagePipelineConfig == null) {
            sImagePipelineConfig = configureCaches(context, useStethoInterceptor);
        }
        return sImagePipelineConfig;
    }
    /**
     * 初始化配置
     */
    private static ImagePipelineConfig configureCaches(Context context, boolean useStethoInterceptor) {
        int heap = Math.min(FrescoConfig.MAX_MEMORY_CACHE_SIZE, MAX_HEAP_SIZE/4);
        Log.e("wjc-heap",String.valueOf(heap));

        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                /*FrescoConfig.MAX_MEMORY_CACHE_SIZE*/heap, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中图片的最大数量。
                FrescoConfig.DELETE_MAX_MEMORY_CACHE_SIZE, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);                    // 内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams= new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig =  DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())//缓存图片基路径
//                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
//            .setCacheErrorLogger(new CacheErrorLogger() {
//                @Override
//                public void logError(CacheErrorCategory cacheErrorCategory, Class<?> aClass, String s, Throwable throwable) {
//                }
//            })//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();

        //默认图片的磁盘配置
        File cacheDir = context.getApplicationContext().getExternalFilesDir("imagecache");
        if (cacheDir == null) {
            cacheDir = context.getApplicationContext().getCacheDir();
        }
        DiskCacheConfig diskCacheConfig =  DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(cacheDir.getAbsoluteFile())//缓存图片基路径
//                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
//                .setCacheErrorLogger(new CacheErrorLogger() {
//                    @Override
//                    public void logError(CacheErrorCategory cacheErrorCategory, Class<?> aClass, String s, Throwable throwable) {
//                    }
//                })
//            .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoConfig.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();

        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener() {
            @Override
            public synchronized void onRequestFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch) {
                super.onRequestFailure(request, requestId, throwable, isPrefetch);
                String errorLog = "fresco base image error: "+String.valueOf(requestId);
                if(request != null) {
                    errorLog += " url:"+request.getSourceUri();
                }
                if(throwable != null) {
                    errorLog += " e:"+throwable.toString();
                }
            }

            @Override
            public synchronized void onRequestSuccess(ImageRequest request, String requestId, boolean isPrefetch) {
                super.onRequestSuccess(request, requestId, isPrefetch);
            }

            @Override
            public synchronized void onProducerFinishWithSuccess(String requestId, String producerName, Map<String, String> extraMap) {
                super.onProducerFinishWithSuccess(requestId, producerName, extraMap);
            }

            @Override
            public synchronized void onProducerFinishWithFailure(String requestId, String producerName, Throwable throwable, Map<String, String> extraMap) {
                super.onProducerFinishWithFailure(requestId, producerName, throwable, extraMap);
            }

            @Override
            public synchronized void onProducerFinishWithCancellation(String requestId, String producerName, Map<String, String> extraMap) {
                super.onProducerFinishWithCancellation(requestId, producerName, extraMap);
            }

        });


//        Set<RequestListener> requestListeners = new HashSet<>();
//        requestListeners.add(new RequestLoggingListener());
//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
//                // other setters
//                .setRequestListeners(requestListeners)
//                .build();
//        Fresco.initialize(context, config);
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, newClient())
//            .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
//            .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
            .setEncodedMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存和未解码的内存缓存的配置（二级缓存）
//            .setExecutorSupplier(executorSupplier)//线程池配置
//            .setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
//            .setImageDecoder(ImageDecoder imageDecoder) //图片解码器配置
//            .setIsPrefetchEnabledSupplier(Supplier<Boolean> isPrefetchEnabledSupplier)//图片预览（缩略图，预加载图等）预加载到文件缓存
                .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
//            .setMemoryTrimmableRegistry(memoryTrimmableRegistry) //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
//            .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
//            .setPoolFactory(poolFactory)//线程池工厂配置
//            .setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
//            .setRequestListeners(requestListeners)//图片请求监听
//            .setResizeAndRotateEnabledForNetwork(boolean resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                .setRequestListeners(requestListeners)
                .setDownsampleEnabled(true)
//                .experiment().setDecodeCancellationEnabled(true)
                ;

//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
//                // other setters
//                .setRequestListeners(requestListeners)
//                .build();
//        FLog.setMinimumLoggingLevel(FLog.VERBOSE);

        return configBuilder.build();
    }

    public static class NetInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder()
                    .addHeader("Connection", "close");
            Request request = builder.build();
            return chain.proceed(request);
        }
    }

    private static long TIME_OUT = 10*1000;
    /**
     * 创建默认的OkHttpClient
     */
    private static OkHttpClient newClient() {
        OkHttpClient.Builder builder = HttpClientNative.getInstance().getClient().newBuilder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);
        // FIX：fresco base image error: 263 url:http://image.peiliao.com/af432cb158f9cd38fdc08a454b4e0ff4-100_100.jpg
        // e:java.io.IOException: unexpected end of stream on Connection
        // 频繁请求图片 长连接未关闭导致的问题，需要请求完毕后，及时关闭连接
        builder.addNetworkInterceptor(new NetInterceptor());
        return builder.build();
    }

}

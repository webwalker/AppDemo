package com.webwalker.framework.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.webwalker.framework.http.OkHttpManager;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * Created by xujian on 2017/1/12.
 */
public class ImagePipelineConfigFactory {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    public static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";
    private static ImagePipelineConfig sImagePipelineConfig;
    private static ImagePipelineConfig sOkHttpImagePipelineConfig;

    /**
     * 使用Android自带的网络加载图片
     */
    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configBuilder.setProgressiveJpegConfig(mProgressiveJpegConfig);
            configBuilder.setBitmapsConfig(Bitmap.Config.ARGB_4444);
            configureCaches(configBuilder, context);
            configureLoggingListeners(configBuilder);
            configureOptions(configBuilder);
            sImagePipelineConfig = configBuilder.build();
        }
        return sImagePipelineConfig;
    }

    /**
     * 使用OkHttp网络库加载图片
     */
    public static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context) {
        if (sOkHttpImagePipelineConfig == null) {
            OkHttpClient.Builder okBuilder = new OkHttpClient().newBuilder();
            OkHttpManager.getInstance().ignoreSSL(okBuilder);
            ImagePipelineConfig.Builder builder = OkHttpImagePipelineConfigFactory.newBuilder(context, okBuilder.build());
            //要不要向下采样,它处理图片的速度比常规的裁剪scaling更快，
            //同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
            builder.setDownsampleEnabled(true);
            //如果不是重量级图片应用,就用这个省点内存吧.默认是RGB_888
            builder.setBitmapsConfig(Bitmap.Config.RGB_565);
            //缓存
            configureCaches(builder, context);
            configureLoggingListeners(builder);
            sOkHttpImagePipelineConfig = builder.build();
        }
        return sOkHttpImagePipelineConfig;
    }

    /**
     * 配置内存缓存和磁盘缓存
     */
    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, final Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                //.setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                                //还是推荐缓存到应用本身的缓存文件夹,这样卸载时能自动清除,其他清理软件也能扫描出来
                                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                                    @Override
                                    public File get() {
                                        return context.getCacheDir();
                                    }
                                })
                                .build());
    }

    private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());
        configBuilder.setRequestListeners(requestListeners);
    }

    private static void configureOptions(ImagePipelineConfig.Builder configBuilder) {
        configBuilder.setDownsampleEnabled(true);
    }

    //渐进式图片
    static ProgressiveJpegConfig mProgressiveJpegConfig = new ProgressiveJpegConfig() {
        @Override
        public int getNextScanNumberToDecode(int scanNumber) {
            return scanNumber + 2;
        }

        public QualityInfo getQualityInfo(int scanNumber) {
            boolean isGoodEnough = (scanNumber >= 5);
            return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
        }
    };
}

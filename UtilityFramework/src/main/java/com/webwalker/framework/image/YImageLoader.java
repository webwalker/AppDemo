package com.webwalker.framework.image;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.webwalker.framework.utils.UIUtils;

/**
 * 图片加载器，便于切换不同图片加载框架
 * Created by xujian on 2017/1/12.
 */
public class YImageLoader {
    public static void init(Context context) {
        Fresco.initialize(context,
                ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(context));
    }

    public static void display(SimpleDraweeView draweeView, String url) {
        if (TextUtils.isEmpty(url)) return;
        draweeView.setImageURI(url);
    }

    public static void display(final SimpleDraweeView draweeView, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(UIUtils.dip2px(draweeView.getContext(), width),
                        UIUtils.dip2px(draweeView.getContext(), height)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);
    }

    public static void displayAdjust(final SimpleDraweeView draweeView, String url) {
        if (TextUtils.isEmpty(url)) return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        if (imageInfo == null) return;
                        int width = imageInfo.getWidth();
                        int height = imageInfo.getHeight();
                        ViewGroup.LayoutParams layoutParams = draweeView.getLayoutParams();
                        layoutParams.width = width;
                        layoutParams.height = height; //(int) ((float) (width * height) / (float) width);
                        draweeView.setLayoutParams(layoutParams);
                    }
                })
                .build();
        draweeView.setController(controller);
    }

    public static void display(SimpleDraweeView draweeView, String url, float ratio) {
        if (TextUtils.isEmpty(url)) return;
        draweeView.setAspectRatio(ratio);
        draweeView.setImageURI(url);
    }

    public static void displayCircle(SimpleDraweeView draweeView, int bgColor) {
        RoundingParams roundingParams = RoundingParams.asCircle();//这个方法在某些情况下无法成圆,比如gif
        roundingParams.setOverlayColor(bgColor);//加一层遮罩,这个是关键方法
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }

    public static void pause() {
        Fresco.getImagePipeline().pause();
    }

    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

    //清除单张图片的磁盘缓存
    public static void clearCacheByUrl(String url) {
        ImagePipeline pipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        // pipeline.evictFromMemoryCache(uri);
        pipeline.evictFromDiskCache(uri);
        //pipeline.evictFromCache(uri);//这个包含了从内存移除和从硬盘移除
    }

    //清除磁盘缓存
    public static void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }
}
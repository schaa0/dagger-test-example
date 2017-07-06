package dagger.extension.example.service;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.AllowStubGeneration;
import dagger.Replaceable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

public class ImageRequestManager {

    private RequestManager requestManager;

    @Inject @Replaceable
    public ImageRequestManager(RequestManager requestManager){
        this.requestManager = requestManager;
    }

    public Observable<Bitmap> load(String iconUrl) {
        if (iconUrl == null || iconUrl.equals("")) {
            return Observable.empty();
        }else {
            return Observable.create(e -> getInto(iconUrl, e));
        }
    }

    private SimpleTarget<Bitmap> getInto(String iconUrl, final ObservableEmitter<Bitmap> emitter) {
        return requestManager.load(Uri.parse(iconUrl)).asBitmap().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                emitter.onNext(resource);
                emitter.onComplete();
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                emitter.onError(e);
            }
        });
    }

}

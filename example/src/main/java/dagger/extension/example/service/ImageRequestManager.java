package dagger.extension.example.service;

import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import dagger.AllowStubGeneration;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class ImageRequestManager {

    private RequestManager requestManager;

    @AllowStubGeneration
    @Inject
    public ImageRequestManager(Provider<RequestManager> requestManager){
        this.requestManager = requestManager.get();
    }

    public Observable<Bitmap> load(String iconUrl) {
        return Observable.create(e -> getInto(iconUrl, e));
    }

    private SimpleTarget<Bitmap> getInto(String iconUrl, final ObservableEmitter<Bitmap> observableEmitter) {
        return requestManager.load(Uri.parse(iconUrl)).asBitmap().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                observableEmitter.onNext(resource);
            }
        });
    }

}

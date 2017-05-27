package dagger.extension.example.view;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class BindingAdapters {
    @BindingAdapter("bind:imageBitmap")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        if (iv.getDrawable() instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable) iv.getDrawable()).getBitmap();
            if (b != null && !b.isRecycled()) {
                b.recycle();
            }
        }
        iv.setImageBitmap(bitmap);
    }
}

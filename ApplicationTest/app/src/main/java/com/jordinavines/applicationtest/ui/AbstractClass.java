package com.jordinavines.applicationtest.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import com.jordinavines.applicationtest.volley.ImageCache;
import com.jordinavines.applicationtest.volley.ImageFetcher;

/**
 * Created by jordinavines on 04/03/2015.
 */
abstract class AbstractClass extends ActionBarActivity {

    public ImageFetcher mImageFetcher;
    private static final String IMAGE_CACHE_DIR = "images";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels/2;
        final int width = displayMetrics.widthPixels ;

        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageFetcher!=null) {
            mImageFetcher.closeCache();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mImageFetcher!=null) {
            mImageFetcher.setExitTasksEarly(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mImageFetcher!=null) {
            mImageFetcher.setPauseWork(false);
            mImageFetcher.setExitTasksEarly(true);
            mImageFetcher.flushCache();
        }
    }
}

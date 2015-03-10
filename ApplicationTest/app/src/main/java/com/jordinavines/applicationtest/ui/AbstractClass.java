package com.jordinavines.applicationtest.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.utils.ListUtil;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_filter_name:
                return true;
            case R.id.action_filter_department:
                return true;
            case R.id.action_option_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "share user");

                startActivity(Intent.createChooser(intent, "Share by:"));
                return true;
            default:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder alert= new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageFetcher!=null) {
            mImageFetcher.closeCache();
        }

        ListUtil.closeDataSource();
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

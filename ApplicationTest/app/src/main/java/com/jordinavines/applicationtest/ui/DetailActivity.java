package com.jordinavines.applicationtest.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.ListUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jordinavines on 04/03/2015.
 */
public class DetailActivity extends AbstractClass implements View.OnClickListener{

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_USER = "user";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_IMAGE = "image";

    private ImageView mHeaderImageView;
    private TextView name, phone, email, address, dob, about, department;
    private LinearLayout container_sub;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Retrieve the correct Item instance, using the ID provided in the Intent
        mUser = (User)getIntent().getParcelableExtra(EXTRA_PARAM_USER);

        mHeaderImageView = (ImageView) findViewById(R.id.imageview_detail);
        name = (TextView) findViewById(R.id.textview_details_name);
        phone = (TextView) findViewById(R.id.textView_details_telephone);
        address = (TextView) findViewById(R.id.textView_details_address);
        email = (TextView) findViewById(R.id.textView_details_email);
        about = (TextView) findViewById(R.id.textView_details_about);
        dob = (TextView) findViewById(R.id.textView_details_dob);
        department = (TextView) findViewById(R.id.textView_details_department);

        container_sub= (LinearLayout) findViewById(R.id.container_sub);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
//            int buttonSize = (int) getResources().getDimension(R.dimen.button_size);
//            Outline circularOutline = new Outline();
//            circularOutline.setOval(0, 0, buttonSize, buttonSize);
//
//            ImageView sms_button = (ImageView) findViewById(R.id.layout_details_sms);
//            sms_button.setou(circularOutline);
//        }


        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(mHeaderImageView, VIEW_IMAGE);
        //ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);
        // END_INCLUDE(detail_set_view_name)

        loadItem();

        loadContent();
    }


    public void loadContent(){
        loadSub();

        name.setText(mUser.getCompletName());
        email.setText(mUser.getEmail());
        phone.setText("");
        address.setText(mUser.getAddress());

        department.setText(mUser.getDepartment());
        phone.setText(mUser.getPhone());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy");
            Date d =  sdf.parse(mUser.getDob());
            String formattedTime = output.format(d);
            dob.setText(formattedTime);
        } catch (ParseException e) {
            // something went wrong
        }

        ListUtil.getUserDatabase(this.getApplicationContext(), mUser.get_id());
    }


    private void loadSub(){
        if (mUser!=null && mUser.getSubordinates().length>0){

        }
    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
       // mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            loadFullSizeImage();
        }
    }



    /**
     * Load the item's thumbnail image into our {@link ImageView}.
     */
    private void loadThumbnail() {
        mImageFetcher.loadImage(mUser.getAvatar(), mHeaderImageView);
    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */
    private void loadFullSizeImage() {
        mImageFetcher.loadImage(mUser.getAvatar(), mHeaderImageView);
    }

    /**
     * Try and add a {@link android.transition.Transition.TransitionListener} to the entering shared element
     * {@link android.transition.Transition}. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_details_email:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, mUser.getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.layout_details_sms:
                Uri uri = Uri.parse("smsto:"+mUser.getPhone());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "The SMS text");
                startActivity(it);
                break;
            case R.id.layout_details_telephone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+mUser.getPhone()));
                startActivity(callIntent);
                break;
        }
    }
}

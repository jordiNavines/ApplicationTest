package com.jordinavines.applicationtest.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.ui.NetworkImageView;
import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.ListUtil;
import com.jordinavines.applicationtest.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private LinearLayout container_sub, container_boss;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().invalidateOptionsMenu();

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
        container_boss= (LinearLayout) findViewById(R.id.container_boss);

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_option_share);
        item.setVisible(true);
        MenuItem item_filter1 = menu.findItem(R.id.action_filter_name);
        MenuItem item_filter2 = menu.findItem(R.id.action_filter_department);
        item_filter1.setVisible(false);
        item_filter2.setVisible(false);
        return true;
    }


    /**
     *
     * Load content for the user details
     *
     */
    public void loadContent(){
        loadSub();
        loadBosses();

        name.setText(mUser.getCompletName());
        email.setText(mUser.getEmail());
        phone.setText(mUser.getPhone());
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

        about.setText(mUser.getAbout());
    }


    /**
     *
     * Load the user list of subordinates or bosses
     *
     * @param isSubordinate
     * @param users
     */
    public void loadUSersList(boolean isSubordinate, ArrayList<User> users){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < users.size(); i++) {
            LogUtils.LOGD("adding ", "adding sub");
            View convertView= inflater.inflate(R.layout.user_item, null);
            addUser(isSubordinate, convertView, users.get(i));
        }
    }


    /**
     *
     * Create the layout for the user and add it on the container for the subordinates or bosses
     *
     * @param isSubordinate
     * @param convertView
     * @param _user
     */
    public void addUser(boolean isSubordinate, View convertView, final User _user){
        TextView name = (TextView) convertView.findViewById(R.id.textView_name);
        TextView department = (TextView) convertView.findViewById(R.id.textView_department);
        RelativeLayout item = (RelativeLayout) convertView.findViewById(R.id.item);
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.image);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetails(null, v, 1, _user.getId(), _user);
            }
        });

        name.setText(_user.getCompletName());
        department.setText(_user.getDepartment());

        if (_user.getAvatar() != null)
            mImageFetcher.loadImage(_user.getAvatar(), image);

        if (isSubordinate)
            container_sub.addView(convertView);
        else
            container_boss.addView(convertView);
    }


    private void loadSub(){
        if (mUser!=null) {
            ArrayList<User> sub = ListUtil.retrieveSubordinates(mUser.getId());
            LogUtils.LOGD("SUB", sub.size() + "");

            if (sub!=null && sub.size()>0)
                loadUSersList(true, sub);
            else
                container_sub.setVisibility(LinearLayout.GONE);
        }
    }

    private void loadBosses(){
        if (mUser!=null) {
            ArrayList<User> sub = ListUtil.retrieveBosses(mUser.getId());
            LogUtils.LOGD("BOSSES", sub.size() + "");

            if (sub!=null && sub.size()>0)
                loadUSersList(false, sub);
            else
                container_boss.setVisibility(LinearLayout.GONE);
        }
    }


    /**
     *
     * Load user image with an animation
     *
     */
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


    /**
     *
     * Go to the user's details
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @param _user
     */
    public void gotoDetails(AdapterView<?> parent, View view, int position, long id, User _user){
        // Construct an Intent as normal
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_USER, _user);

        // BEGIN_INCLUDE(start_activity)
        /**
         * Now create an {@link android.app.ActivityOptions} instance using the
         * {@link android.support.v4.app.ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, android.support.v4.util.Pair[])} factory
         * method.
         */
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,

                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                new Pair<View, String>(view.findViewById(R.id.image),
                        DetailActivity.VIEW_IMAGE));


        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        // END_INCLUDE(start_activity)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_details_email:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, mUser.getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "message");

                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.layout_details_sms:
                Uri uri = Uri.parse("smsto:"+mUser.getPhone());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "message");
                startActivity(it);
                break;
            case R.id.layout_details_telephone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+mUser.getPhone()));
                startActivity(callIntent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_option_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mUser.toString());

                startActivity(Intent.createChooser(intent, "Share by:"));
                return true;
            default:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

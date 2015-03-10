package com.jordinavines.applicationtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.ListUtil;

import java.sql.SQLException;


public class MainActivity extends AbstractClass {

    ListUserFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (savedInstanceState == null) {

            fragment= new ListUserFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public void init(){
        try {
            ListUtil.initDataSource(this.getApplicationContext());
        } catch (SQLException e) {
            //e.printStackTrace();
        }

    }


    public void gotoDetails(AdapterView<?> parent, View view, int position, long id, User _user){
        // Construct an Intent as normal
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_USER, _user);

        // BEGIN_INCLUDE(start_activity)
        /**
         * Now create an {@link android.app.ActivityOptions} instance using the
         * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
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
//new Pair<View, String>(view.findViewById(R.id.textview_name), DetailActivity.VIEW_NAME_HEADER_TITLE));

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_option_share);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_filter_department:
                if(fragment!=null)
                    fragment.filterDepartment();
                return true;
            case R.id.action_filter_name:
                if(fragment!=null)
                    fragment.filterName();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListUtil.closeDataSource();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


}

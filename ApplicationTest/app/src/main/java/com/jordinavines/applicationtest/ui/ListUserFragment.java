package com.jordinavines.applicationtest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.adapter.ListUsersAdapter;
import com.jordinavines.applicationtest.adapter.listener.UserListAdapterListener;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.ListUtil;
import com.jordinavines.applicationtest.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class ListUserFragment extends Fragment implements ListUtil.ListUserListener, SwipeRefreshLayout.OnRefreshListener, UserListAdapterListener, View.OnClickListener, AdapterView.OnItemClickListener {

    public ListUserFragment() {
    }

    MainActivity activity;

    private RecyclerView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private ProgressBar progress;


    LinearLayoutManager mLayoutManager;

    ListUsersAdapter madapter;

    ArrayList<User> users= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        activity= (MainActivity) getActivity();

        Log.d("on create", "on create");

        initUI(rootView);

        ListUtil.retrieveUsers(false, this);

        return rootView;
    }


    private void initUI(View rootView){

        progress= (ProgressBar) rootView.findViewById(R.id.progressBar);

        mListView = (RecyclerView) rootView.findViewById(R.id.list_users);
        mListView.setHasFixedSize(true);

        madapter =new ListUsersAdapter(activity.getApplicationContext(), users, this, activity.mImageFetcher);

        //layout manager for recycleview
        mLayoutManager =  new LinearLayoutManager(getActivity());

        mListView.setAdapter(madapter);
        mListView.setLayoutManager(mLayoutManager);
        madapter.setOnItemClickListener(this);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnClickListener(this);


        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_users);
        mSwipeLayout.setOnRefreshListener(this);
        // mSwipeLayout.setProgressViewOffset(true, (int) Utils.convertDIPtoPixels(getActivity(),20), (int) Utils.convertDIPtoPixels(getActivity(), 70));

        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    activity.mImageFetcher.setPauseWork(true);
                } else {
                    activity.mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    public void showUsers(ArrayList<User> _users){
        progress.setVisibility(ProgressBar.GONE);

        LogUtils.LOGD("show", "show");
        users.addAll(_users);
        madapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListUserSuccess(ArrayList<User> users) {
        showUsers(users);
    }

    @Override
    public void onRefreshSuccess(ArrayList<User> users) {

    }

    @Override
    public void onGetListUserError() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onUserSelected(int position) {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.gotoDetails(parent, view, position, id, users.get(position));
    }
}


package com.jordinavines.applicationtest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Button;
import android.widget.ProgressBar;

import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.adapter.ListUsersAdapter;
import com.jordinavines.applicationtest.adapter.listener.UserListAdapterListener;
import com.jordinavines.applicationtest.common.DepartmentComparator;
import com.jordinavines.applicationtest.common.UserComparator;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.utils.ListUtil;
import com.jordinavines.applicationtest.utils.LogUtils;
import com.jordinavines.applicationtest.utils.Utils;

import java.util.ArrayList;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class ListUserFragment extends Fragment implements Handler.Callback, View.OnClickListener, ListUtil.ListUserListener, SwipeRefreshLayout.OnRefreshListener, UserListAdapterListener, AdapterView.OnItemClickListener {

    public ListUserFragment() {
    }

    MainActivity activity;


    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mListView;
    private ProgressBar progress;
    private Button try_again_button;


    LinearLayoutManager mLayoutManager;

    ListUsersAdapter madapter;

    ArrayList<User> users= new ArrayList<>();

    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        activity= (MainActivity) getActivity();

        mHandler= new Handler(this);

        Log.d("on create", "on create");

        initUI(rootView);

        getUsers();

        return rootView;
    }


    private void initUI(View rootView){

        try_again_button= (Button) rootView.findViewById(R.id.try_again_button);
        progress= (ProgressBar) rootView.findViewById(R.id.progressBar);


        mListView = (RecyclerView) rootView.findViewById(R.id.list_users);
        mListView.setHasFixedSize(true);
        //layout manager for recycleview
        mLayoutManager =  new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        madapter =new ListUsersAdapter(activity.getApplicationContext(), users, this, activity.mImageFetcher);
        madapter.setOnItemClickListener(this);
        mListView.setAdapter(madapter);

        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_users);
        mSwipeLayout.setOnRefreshListener(this);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnClickListener(this);
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

        try_again_button.setOnClickListener(this);
    }



    public void filterName(){
        UserComparator compare = new UserComparator();
        java.util.Collections.sort(users,compare);

        madapter.notifyDataSetChanged();
    }

    public void filterDepartment(){
        DepartmentComparator compare = new DepartmentComparator();
        java.util.Collections.sort(users,compare);

        madapter.notifyDataSetChanged();
    }

    public void clearToDownload(){
        progress.setVisibility(ProgressBar.VISIBLE);
        try_again_button.setVisibility(View.GONE);
    }

    public void getUsers(){
        if (ListUtil.isUsersStoredEmpty(activity.getApplicationContext())){
            if (Utils.isConnected(activity.getApplicationContext())) {
                ListUtil.retrieveUsers(false, this);
            }else{
                try_again_button.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                activity.showMessage(activity.getResources().getString(R.string.connection), activity.getResources().getString(R.string.offline_message));
            }
        }else{
            ListUtil.getUsersStored(activity.getApplicationContext(), this);
        }
    }

    public void showUsers(ArrayList<User> _users){
        mHandler.sendEmptyMessage(HIDE_ERROR_BUTTON);

        LogUtils.LOGD("show", "show");
        users.addAll(_users);
        madapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListUserSuccess(ArrayList<User> users) {
        showUsers(users);
    }

    @Override
    public void onGetStoredListUserSuccess(ArrayList<User> users) {
        showUsers(users);
    }

    @Override
    public void onGetListUserError() {
        mHandler.sendEmptyMessage(SHOW_ERROR_BUTTON);

        mHandler.sendEmptyMessage(SHOW_MESSAGE_ERROR);
    }

    @Override
    public void onGetStoredListUserError() {
        mHandler.sendEmptyMessage(SHOW_ERROR_BUTTON);

        mHandler.sendEmptyMessage(SHOW_MESSAGE_ERROR);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onUserSelected(int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.try_again_button:
                clearToDownload();
                getUsers();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.gotoDetails(parent, view, position, id, users.get(position));
    }

    final int SHOW_ERROR_BUTTON=1;
    final int SHOW_MESSAGE_ERROR=2;
    final int HIDE_ERROR_BUTTON=3;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_ERROR_BUTTON:
                try_again_button.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                break;
            case SHOW_MESSAGE_ERROR:
                activity.showMessage(activity.getResources().getString(R.string.error), activity.getResources().getString(R.string.error_message));progress.setVisibility(View.GONE);
                break;
            case HIDE_ERROR_BUTTON:
                progress.setVisibility(ProgressBar.GONE);
                try_again_button.setVisibility(View.GONE);
                break;
        }
        return false;
    }
}


package com.jordinavines.applicationtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.ui.NetworkImageView;
import com.jordinavines.applicationtest.R;
import com.jordinavines.applicationtest.adapter.listener.UserListAdapterListener;
import com.jordinavines.applicationtest.model.User;
import com.jordinavines.applicationtest.volley.ImageFetcher;

import java.util.ArrayList;

/**
 * Created by jordinavines on 03/03/2015.
 */
public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<User> users;
    private LayoutInflater mInflater;
    private UserListAdapterListener listener;
    private ImageFetcher mImageFetcher;


    private AdapterView.OnItemClickListener mOnItemClickListener;


    public ListUsersAdapter(Context context, ArrayList<User> _users, UserListAdapterListener _listener, ImageFetcher _mImageFetcher) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        users = _users;
        listener = _listener;
        mImageFetcher = _mImageFetcher;
    }


    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        else
            return 0;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(users.get(position).getCompletName());
        holder.department.setText(users.get(position).getDepartment());

        if (users.get(position).getAvatar() != null)
            mImageFetcher.loadImage(users.get(position).getAvatar(), holder.image);
        else
            mImageFetcher.loadImageRandom(position, holder.image);


    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, department;
        NetworkImageView image;
        RelativeLayout item;
        View v;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.textView_name);
            department = (TextView) itemView.findViewById(R.id.textView_department);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            image = (NetworkImageView) itemView.findViewById(R.id.image);
            v= itemView;
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClick(null, view, getPosition(),1);
        }
    }

}

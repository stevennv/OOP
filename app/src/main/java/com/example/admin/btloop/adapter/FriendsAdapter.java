package com.example.admin.btloop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.btloop.R;
import com.example.admin.btloop.model.Friends;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 11/11/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Friends> list;

    public FriendsAdapter(Context context, List<Friends> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_friends, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final Friends friends = list.get(position);
        String avatar = "http://graph.facebook.com/"
                + friends.getId() + "/picture?type=large";
        Glide.with(context).load(avatar).into(myViewHolder.civAvatar);
        myViewHolder.tvName.setText(friends.getName());
        myViewHolder.imgStatus.setImageResource(R.drawable.ic_online);
        myViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                list.get(position).setSelected(true);
                notifyDataSetChanged();
            }
        });
        if (friends.isSelected()) {
            myViewHolder.llItem.setBackgroundColor(Color.GRAY);
        } else {
            myViewHolder.llItem.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView civAvatar;
        public ImageView imgStatus;
        public LinearLayout llItem;
        public TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            imgStatus = itemView.findViewById(R.id.ic_status);
            tvName = itemView.findViewById(R.id.tv_name);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}

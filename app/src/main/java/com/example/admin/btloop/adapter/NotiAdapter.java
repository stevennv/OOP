package com.example.admin.btloop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.btloop.R;
import com.example.admin.btloop.model.Noti;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 11/13/2017.
 */

public class NotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Noti> list;
    private click click;

    public NotiAdapter(Context context, List<Noti> list, click click) {
        this.context = context;
        this.list = list;
        this.click = click;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_noti, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Noti noti = list.get(position);
        Glide.with(context).load(noti.getAvatar()).into(myViewHolder.civAvatar);
        String content = "<font color='blue'>" + noti.getName() + "</font> " + context.getString(R.string.invited);
        myViewHolder.tvContent.setText(Html.fromHtml(content), TextView.BufferType.SPANNABLE);
        myViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                list.get(position).setSelected(true);
                notifyDataSetChanged();
                click.click(position);
            }
        });
        if (noti.isSelected()) {
            myViewHolder.llItem.setBackgroundColor(Color.CYAN);
        } else {
            myViewHolder.llItem.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llItem;
        public CircleImageView civAvatar;
        public TextView tvContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.ll_item);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    public interface click {
        void click(int pos);
    }
}

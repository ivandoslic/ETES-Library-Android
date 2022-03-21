package com.example.eteslibauthproto.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.models.User;

import java.util.ArrayList;

public class UserSearchVerticalListAdapter extends RecyclerView.Adapter<UserSearchVerticalListAdapter.ViewHolder>{

    ArrayList<User> usersList;
    Context context;
    LayoutInflater layoutInflater;

    public UserSearchVerticalListAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.usersList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_search_item, parent, false);
        return new UserSearchVerticalListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.usernameTv.setText(usersList.get(position).getName());

        Glide.with(context)
                .load(usersList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userProfileIv);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private UserSearchVerticalListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(UserSearchVerticalListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView usernameTv;
        ImageView userProfileIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.userSearchUsername);
            userProfileIv = itemView.findViewById(R.id.userSearchProfileImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemClick(itemView, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void filterList(ArrayList<User> filteredUserList) {
        usersList = filteredUserList;
        notifyDataSetChanged();
    }
}

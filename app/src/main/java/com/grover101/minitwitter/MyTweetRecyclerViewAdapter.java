package com.grover101.minitwitter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.databinding.FragmentTweetBinding;
import com.grover101.minitwitter.retrofit.response.Like;
import com.grover101.minitwitter.retrofit.response.Tweet;

import java.lang.reflect.Type;
import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private final List<Tweet> mValues;
    private Context ctx;
    String username;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        ctx = context;
        mValues = items;
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTweetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvUsernamer.setText(holder.mItem.getUser().getUsername());
        holder.tvMessage.setText(holder.mItem.getMensaje());
        holder.tvLikesCount.setText(holder.mItem.getLikes().size());

        String photo = holder.mItem.getUser().getPhotoUrl();
        if (!photo.equals(""))
            Glide.with(ctx)
                    .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                    .into(holder.ivAvatar);

        for (Like like : holder.mItem.getLikes())
            if (like.getUsername().equals(username)) {
                Glide.with(ctx)
                        .load(R.drawable.ic_like_pink)
                        .into(holder.ivLike);
                holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                holder.tvLikesCount.setTypeface(null, Typeface.BOLD);
                break;
            }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final TextView tvUsernamer;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(FragmentTweetBinding binding) {
            super(binding.getRoot());
            ivAvatar = binding.imageViewAvatar;
            ivLike = binding.imageViewLike;
            tvUsernamer = binding.textViewUsername;
            tvMessage = binding.textViewMessage;
            tvLikesCount = binding.textViewLikes;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsernamer.getText() + "'";
        }
    }
}
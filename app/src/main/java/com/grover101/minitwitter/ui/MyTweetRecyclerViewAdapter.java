package com.grover101.minitwitter.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grover101.minitwitter.R;
import com.grover101.minitwitter.common.Constantes;
import com.grover101.minitwitter.common.SharedPreferencesManager;
import com.grover101.minitwitter.data.TweetViewModel;
import com.grover101.minitwitter.databinding.FragmentTweetBinding;
import com.grover101.minitwitter.retrofit.response.Like;
import com.grover101.minitwitter.retrofit.response.Tweet;

import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    String username;
    TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        ctx = context;
        mValues = items;
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER);
        tweetViewModel = new ViewModelProvider((FragmentActivity) ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTweetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (mValues != null) {
            holder.mItem = mValues.get(position);
            holder.tvUsernamer.setText("@" + holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photo = holder.mItem.getUser().getPhotoUrl();
            if (!photo.equals(""))
                Glide.with(ctx)
                        .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                        .into(holder.ivAvatar);
            else
                Glide.with(ctx)
                        .load(R.drawable.ic_baseline_account_circle_24)
                        .into(holder.ivAvatar);

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvLikesCount.setTextColor(ctx.getResources().getColor(android.R.color.black));
            holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

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
    }

    public void setData(List<Tweet> tweetList) {
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return mValues != null ? mValues.size() : 0; }

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
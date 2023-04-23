package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ViewHolder> {
    private List<Post> mPosts;
    private Context mContext;

    public ForumRecyclerAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forum_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.author.setText(post.getPostAuthor());
        holder.date.setText(post.getPostDatetime());
        holder.title.setText(post.getPostTitle());
        ArrayList<String> details = post.getPostDiagram();
//        if (details != null) {
//            Canvas canvas = new Canvas(holder.image);
//            for (String ds: details) {
//                String[] d = ds.split(",");
//                canvas.drawShape(Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2], d[3]);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView author;
        TextView title;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_text_view);
            date = (TextView) itemView.findViewById(R.id.date_text_view);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            image = (ImageView) itemView.findViewById(R.id.post_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Post post = mPosts.get(position);

            Intent intent = new Intent(mContext, CommentActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getPostTitle());
            intent.putExtra("postAuthor", post.getPostAuthor());
            intent.putExtra("postDatetime", post.getPostDatetime());
            intent.putExtra("postBody", post.getPostBody());
            mContext.startActivity(intent);
        }
    }
}

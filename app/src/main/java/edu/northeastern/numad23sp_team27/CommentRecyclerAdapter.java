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

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {
    private List<Comment> mComments;
    private Context mContext;

    public CommentRecyclerAdapter(Context context, List<Comment> comments) {
        mContext = context;
        mComments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.author.setText(comment.author);
        holder.date.setText(comment.dateTime);
        holder.content.setText(comment.comment_body);
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
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_comment_text_view);
            date = (TextView) itemView.findViewById(R.id.comment_date_text_view);
            content = (TextView) itemView.findViewById(R.id.comment_content_text_view);
        }

    }
}
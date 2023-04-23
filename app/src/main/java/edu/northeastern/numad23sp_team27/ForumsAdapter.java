package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ForumsAdapter extends ArrayAdapter<Post> {
    Context context;
    private static class ViewHolder {
        ImageView image;
        TextView author;
        TextView title;
        TextView date;
    }

    public ForumsAdapter(Context context, int resource, ArrayList<Post> posts) {
        super(context, resource, posts);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.forum_row, parent, false);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author_text_view);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_text_view);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_text_view);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.post_image_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate data
        viewHolder.author.setText(post.getPostAuthor());
        viewHolder.date.setText(post.getPostDatetime());
        viewHolder.title.setText(post.getPostTitle());
        ArrayList<String> details = post.getPostDiagram();
        if (details != null) {
            Canvas canvas = new Canvas(viewHolder.image);
            for (String ds: details) {
                String[] d = ds.split(",");
                canvas.drawShape(Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2], d[3]);
            }
        }

        // Set an OnClickListener on the view
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new Activity
                Intent intent = new Intent(getContext(), CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("postId",post.getPostId());
                intent.putExtras(bundle);
                // Start the new Activity
                getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}

package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ForumsAdapter extends ArrayAdapter<Post> {

    private static class ViewHolder {
        //ImageView image;
        TextView author;
        TextView title;
        TextView date;
    }

    public ForumsAdapter(Context context, int resource, ArrayList<Post> posts) {
        super(context, resource, posts);
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
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.post_image_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // populate data
        viewHolder.author.setText(post.getPostAuthor());
        viewHolder.date.setText(post.getPostDatetime());
        viewHolder.title.setText(post.getPostTitle());
        //viewHolder.image.setImageResource();
        // Return the completed view to render on screen
        return convertView;
    }
}

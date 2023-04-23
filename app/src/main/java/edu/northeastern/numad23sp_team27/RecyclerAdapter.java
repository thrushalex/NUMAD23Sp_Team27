package edu.northeastern.numad23sp_team27;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PostViewHolder>{
    private List<Post> listPost;

    public RecyclerAdapter(List<Post> listPost) {
        this.listPost = listPost;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new PostViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post, null));
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PostViewHolder holder, int position) {
        holder.postId.setText(listPost.get(position).getPostId());
        holder.postTitle.setText(String.valueOf(listPost.get(position).getPostTitle()));
        holder.postBody.setText(String.valueOf(listPost.get(position).getPostBody()));
        holder.postDiagram.setText(String.valueOf(listPost.get(position).getPostDiagram()));
    }

    @Override
    public int getItemCount() {
        if(listPost == null){
            return 0;
        } else {
            return listPost.size();
        }
    }

    protected class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView postId;
        public TextView postTitle;
        public TextView postBody;
        public TextView postDiagram;
        //private View container;


        public PostViewHolder(View view) {
            super(view);
            postId = view.findViewById(R.id.textViewPostId);
            postTitle = view.findViewById(R.id.textViewPostTitle);
            postBody = view.findViewById(R.id.textViewPostBody);
            postDiagram = view.findViewById(R.id.textViewPostDiagram);
            //container = view.findViewById(com.neu.numad23sp_blakekoontz.R.id.card_view);
        }
    }
}

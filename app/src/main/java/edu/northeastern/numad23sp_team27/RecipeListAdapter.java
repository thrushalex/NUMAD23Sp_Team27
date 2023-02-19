package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<RecipeResultRow> recipeRowList;
    private List<RecipeResultRow> originalRecipeRowList;

    public RecipeListAdapter(Context context, List<RecipeResultRow> originalRecipeRowList) {
        this.context = context;
        this.recipeRowList = new ArrayList<>();
        this.recipeRowList.addAll(originalRecipeRowList);
        this.originalRecipeRowList = new ArrayList<>();
        this.originalRecipeRowList.addAll(originalRecipeRowList);
    }

    @Override
    public int getGroupCount() {
        return recipeRowList.size();
    }

    @Override
    public int getChildrenCount(int recipeRowPosition) {
        return recipeRowList.get(recipeRowPosition).getChildList().size();
    }

    @Override
    public Object getGroup(int recipeRowPosition) {
        return recipeRowList.get(recipeRowPosition);
    }

    @Override
    public Object getChild(int recipeRowPosition, int recipeRowChildPosition) {
        return recipeRowList.get(recipeRowPosition).getChildList().get(recipeRowChildPosition);
    }

    @Override
    public long getGroupId(int recipeRowPosition) {
        return recipeRowPosition;
    }

    @Override
    public long getChildId(int recipeRowPosition, int recipeRowChildPosition) {
        return recipeRowChildPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int recipeRowPosition, boolean isExpended, View convertView, ViewGroup convertViewGroup) {
        RecipeResultRow recipeResultRow = (RecipeResultRow) getGroup(recipeRowPosition);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.result_row, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.result_row_text);

        heading.setText(recipeResultRow.getRecipe().trim());

        return convertView;
    }

    @Override
    public View getChildView(int recipeRowPosition, int recipeRowChildPosition, boolean isExpended, View convertView, ViewGroup convertViewGroup) {
        RecipeResultRowChild recipeResultRowChild = (RecipeResultRowChild) getChild(recipeRowPosition, recipeRowChildPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.result_row_child, null);
        }

        final TextView recipeInner = (TextView) convertView.findViewById(R.id.result_row_child_text);
        recipeInner.setText(recipeResultRowChild.getRecipeResultRowText().trim());


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int recipeRowPosition, int recipeRowChildPosition) {
        return  true;
    }


}

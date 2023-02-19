package edu.northeastern.numad23sp_team27;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeResultRow implements Parcelable {
    private String recipe;
    private List<RecipeResultRowChild> RecipeResultRowChildren;

    protected RecipeResultRow(Parcel in) {
        recipe = in.readString();
        RecipeResultRowChildren = in.createTypedArrayList(RecipeResultRowChild.CREATOR);
    }

    public static final Creator<RecipeResultRow> CREATOR = new Creator<RecipeResultRow>() {
        @Override
        public RecipeResultRow createFromParcel(Parcel in) {
            return new RecipeResultRow(in);
        }

        @Override
        public RecipeResultRow[] newArray(int size) {
            return new RecipeResultRow[size];
        }
    };

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<RecipeResultRowChild> getChildList() {
        return RecipeResultRowChildren;
    }

    public void setChildList(List<RecipeResultRowChild> childList) {
        this.RecipeResultRowChildren = childList;
    }

    public RecipeResultRow() {
        this.recipe = null;
        this.RecipeResultRowChildren = new ArrayList<>();
    }

    public RecipeResultRow(String recipe, List<RecipeResultRowChild> RecipeResultRowChildren) {
        this.recipe = recipe;
        this.RecipeResultRowChildren = RecipeResultRowChildren;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeList(RecipeResultRowChildren);
        parcel.writeString(recipe);
    }
}

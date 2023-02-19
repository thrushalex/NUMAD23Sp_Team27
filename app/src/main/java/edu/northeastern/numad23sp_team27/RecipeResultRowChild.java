package edu.northeastern.numad23sp_team27;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RecipeResultRowChild implements Parcelable {
    private String recipeResultRowText;

    protected RecipeResultRowChild(Parcel in) {
        recipeResultRowText = in.readString();
    }

    public static final Creator<RecipeResultRowChild> CREATOR = new Creator<RecipeResultRowChild>() {
        @Override
        public RecipeResultRowChild createFromParcel(Parcel in) {
            return new RecipeResultRowChild(in);
        }

        @Override
        public RecipeResultRowChild[] newArray(int size) {
            return new RecipeResultRowChild[size];
        }
    };

    public String getRecipeResultRowText() {
        return recipeResultRowText;
    }

    public void setRecipeResultRowText(String recipeResultRowText) {
        this.recipeResultRowText = recipeResultRowText;
    }

    public RecipeResultRowChild() {
        this.recipeResultRowText = null;
    }

    public RecipeResultRowChild(String recipeResultRowText) {
        this.recipeResultRowText = recipeResultRowText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(recipeResultRowText);
    }
}

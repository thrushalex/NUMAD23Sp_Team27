package edu.northeastern.numad23sp_team27;


import java.util.ArrayList;

public class RecipeResultRow {
    private String recipe;
    private ArrayList<RecipeResultRowChild> RecipeResultRowChildren;

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public ArrayList<RecipeResultRowChild> getChildList() {
        return RecipeResultRowChildren;
    }

    public void setChildList(ArrayList<RecipeResultRowChild> childList) {
        this.RecipeResultRowChildren = childList;
    }

    public RecipeResultRow() {
        this.recipe = null;
        this.RecipeResultRowChildren = new ArrayList<>();
    }

    public RecipeResultRow(String recipe, ArrayList<RecipeResultRowChild> RecipeResultRowChildren) {
        this.recipe = recipe;
        this.RecipeResultRowChildren = RecipeResultRowChildren;

    }
}

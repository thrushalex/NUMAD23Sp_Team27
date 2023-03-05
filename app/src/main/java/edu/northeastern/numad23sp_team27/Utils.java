package edu.northeastern.numad23sp_team27;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public ArrayList convertStringListToList(String stringFormList) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        stringFormList = stringFormList.replace("[","");
        stringFormList = stringFormList.replace("]","");
        stringFormList = stringFormList.replace(" ","");
        ArrayList<String> sentStringList = new ArrayList<>(Arrays.asList(stringFormList.split(",")));
        sentStringList.forEach((n) -> arrayList.add(Integer.parseInt(n)));
        return arrayList;
    }
}

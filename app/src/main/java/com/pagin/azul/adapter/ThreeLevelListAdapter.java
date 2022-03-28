package com.pagin.azul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.CategoryDataResponse;
import com.pagin.azul.bean.CommonResponse;
import com.pagin.azul.helper.MyInterface;

import java.util.List;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {
    String[] parentHeaders;
    // List<String[]> secondLevel;
    private Context context;
    MyInterface listener;
    // List<LinkedHashMap<String, String[]>> data;
    private List<CategoryDataResponse> categoryDataResponses;
    private List<CategoryDataResponse> subCategoryDataResponses;
    private List<CategoryDataResponse> subToSubCategoryDataResponses;
    private CommonResponse[] categoryData;


    public ThreeLevelListAdapter(Context context, List<CategoryDataResponse> categoryDataResponses, List<CategoryDataResponse> subCategoryDataResponses, List<CategoryDataResponse> subToSubCategoryDataResponses,MyInterface listener) {
        this.context = context;

        this.listener = listener;
        //this.secondLevel = secondLevel;
        this.categoryDataResponses = categoryDataResponses;
        this.subCategoryDataResponses = subCategoryDataResponses;
        this.subToSubCategoryDataResponses = subToSubCategoryDataResponses;
    }

    public ThreeLevelListAdapter(Context context, CommonResponse[] categoryData,MyInterface listener) {
        this.context = context;
        this.listener = listener;
        this.categoryData = categoryData;

    }

    @Override
    public int getGroupCount() {
        return categoryData != null?categoryData.length:0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // no idea why this code is working
        return categoryData[groupPosition].getSubCategoryData().length;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryData[groupPosition];
    }

    @Override
    public Object getChild(int group, int child) {
        return categoryData[group].getSubCategoryData()[child];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_first, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowParentText);
        ImageView img = (ImageView) convertView.findViewById(R.id.iv_image);
        text.setText(categoryData[groupPosition].getCategoryName());


        if (isExpanded) {
            img.setImageResource(R.drawable.down_arrow);
        } else {
            img.setImageResource(R.drawable.up_arrow);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);

        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);

        // String[] childArray=data.get(groupPosition);

        String text = categoryData[groupPosition].getSubCategoryData()[childPosition].getSubCategoryName();
        textView.setText(text);

        textView.setOnClickListener(view -> {

            if (listener != null) {
                listener.onCategory(text);
            }

        });

        return convertView;

//        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);
//
//        // String[] headers = secondLevel.get(groupPosition);
//        // String[] headers1 = subCategoryDataResponses.get(groupPosition).getCategoryName();
//
//
//      /*  List<String[]> childData = new ArrayList<>();
//        HashMap<String, String[]> secondLevelData=data.get(groupPosition);
//
//        for(String key : secondLevelData.keySet())
//        {
//
//
//            childData.add(secondLevelData.get(key));
//
//        }*/
//
//
//        secondLevelELV.setAdapter(new SecondLevelAdapter(context, subCategoryDataResponses, subToSubCategoryDataResponses,listener));
//
//        secondLevelELV.setGroupIndicator(null);
//
//        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int previousGroup = -1;
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup)
//                    secondLevelELV.collapseGroup(previousGroup);
//                previousGroup = groupPosition;
//            }
//        });
//
//
//        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

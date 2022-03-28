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
import com.pagin.azul.helper.MyInterface;

import java.util.List;


public class SecondLevelAdapter extends BaseExpandableListAdapter {
    public static MyInterface onMyInterface;
    private MyInterface listener;

//    public SecondLevelAdapter(MyInterface listener){
//        this.listener = listener;
//    }


    private Context context;
    // List<String[]> data;

    List<CategoryDataResponse> categoryDataResponses;
    List<CategoryDataResponse> categorySubDataResponses;


    //  String[] headers;


    public SecondLevelAdapter(Context context, List<CategoryDataResponse> categoryDataResponses, List<CategoryDataResponse> categorySubDataResponses,MyInterface listener) {
        this.context = context;
        this.listener = listener;
        this.categorySubDataResponses = categorySubDataResponses;
        // this.headers = headers;
        this.categoryDataResponses = categoryDataResponses;


    }


    @Override
    public Object getGroup(int groupPosition) {

        return categoryDataResponses.get(groupPosition).getSubCategoryName();
    }

    @Override
    public int getGroupCount() {

        return categoryDataResponses.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_second, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowSecondText);
        ImageView img = (ImageView) convertView.findViewById(R.id.iv_img);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);


        if (isExpanded) {
            img.setImageResource(R.drawable.down_arrow);
        } else {
            img.setImageResource(R.drawable.up_arrow);
        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

       /* String[] childData;

        childData = categorySubDataResponses.get(groupPosition);*/


        return categorySubDataResponses.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_third, null);

        TextView textView = (TextView) convertView.findViewById(R.id.rowThirdText);

        // String[] childArray=data.get(groupPosition);

        String text = categorySubDataResponses.get(childPosition).getSubCategoryName().toString();
        textView.setText(text);

        textView.setOnClickListener(view -> {

            if (listener != null) {
                listener.onCategory(text);
            }

        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //  String[] children = data.get(groupPosition);


        return categorySubDataResponses.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
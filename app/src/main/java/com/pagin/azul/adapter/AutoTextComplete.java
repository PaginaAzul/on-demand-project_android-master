package com.pagin.azul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.CategoriesResultInner;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobulous06 on 24/8/17.
 */
public class AutoTextComplete extends BaseAdapter implements Filterable {

    LayoutInflater inflater = null;
    View view;
    ArrayList<String> text;
    List<CategoriesResultInner> text_id;
    Context context;
    private FilterSubClass mFilter;
    private ArrayList<Object> suggestions = new ArrayList<>();
    private ArrayList<String> results = new ArrayList<>();


    public AutoTextComplete(Context mcontext, ArrayList<String> spinner_item) {
        this.context = mcontext;
        this.text = spinner_item;
        ;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public AutoTextComplete(Context mcontext, List<CategoriesResultInner> spinner_item, int id) {
        this.context = mcontext;
        this.text_id = spinner_item;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int position){
        try {
            if (suggestions.size() > 0 && position < suggestions.size())
                return suggestions.get(position);
            else
                return null;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        view = convertView;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.institude_spinner_layout, parent, false);

//         view = inflater.inflate(R.layout.spninner_item, null);
        TextView name = (TextView) view.findViewById(R.id.txt_spinner);
        try{
            name.setText(suggestions.get(position).toString());
        }catch (Exception e){
            e.printStackTrace();
        }




        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new FilterSubClass();
        }
        return mFilter;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class FilterSubClass extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

//            System.out.println("--->Filter constraint=" + constraint.toString());
            suggestions.clear();
            results.clear();

            if (text != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < text.size(); i++) {
                    if (text.get(i).toLowerCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                        suggestions.add(text.get(i));

                        // If TRUE add item in Suggestions.
                    }
                }
            }else if (text_id != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < text_id.size(); i++) {
                    if (text_id.get(i).getName().toLowerCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                        suggestions.add(text_id.get(i).getName());
                        results.add(text_id.get(i).getName());// If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public ArrayList<String> getIDList(){
        return results;
    }

}




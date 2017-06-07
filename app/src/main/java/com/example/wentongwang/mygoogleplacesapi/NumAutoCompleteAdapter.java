package com.example.wentongwang.mygoogleplacesapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wentongwang on 24/05/2017.
 */
public class NumAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> mResultList;
    private List<String> dropDownList;

    public NumAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        mResultList = new ArrayList<>();
        dropDownList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dropDownList.size();
    }

    @Override
    public String getItem(int position) {
        return dropDownList.get(position);
    }

    public void setmResultList(List<String> list) {
        mResultList = list;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {

                    String prefixString = constraint.toString();

                    dropDownList = getItems(prefixString);

                    results.values = dropDownList;
                    results.count = dropDownList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<String> getItems(String constraint) {
        List<String> unfilteredValues = mResultList;
        int count = unfilteredValues.size();

        List<String> newValues = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            String num = unfilteredValues.get(i);
            if (num.startsWith(constraint)) {
                newValues.add(num);
            }

        }
        return newValues;
    }
}
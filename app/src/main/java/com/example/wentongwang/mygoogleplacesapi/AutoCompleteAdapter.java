package com.example.wentongwang.mygoogleplacesapi;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wentongwang on 24/05/2017.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> {

    private List<DQEResult> mResultList;

    public AutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        mResultList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public String getItem(int position) {
        return mResultList.get(position).getVoie();
    }

    public DQEResult getCompleteItem(int position) {
        return mResultList.get(position);
    }

    public void setmResultList(List<DQEResult> list) {
        mResultList = list;
        if (mResultList != null && mResultList.size() > 0) {
            // The API returned at least one result, update the data.
            notifyDataSetChanged();
        } else {
            // The API did not return any results, invalidate the data set.
            notifyDataSetInvalidated();
        }
    }

}
package com.manaksh.milkdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.manaksh.milkdiary.utils.Constants;
import com.manaksh.milkdiary.utils.FileOperationsImpl;

import java.text.DateFormatSymbols;
import java.util.*;

import manaksh.com.milkdiary.R;

import static android.R.layout.simple_spinner_item;

public class ReportsFragment extends Fragment {
    Spinner spinner = null;
    ArrayList<String> reports = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        Calendar cal  = Calendar.getInstance();

        List<String> spinnerArray =  new ArrayList<String>();

        for(int i=0; i<12; i++){
            cal.add(Calendar.MONTH, -1);
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH);
            spinnerArray.add(getMonth(month) + " " + year.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) rootView.findViewById(R.id.months_spinner);
        spinner.setAdapter(adapter);

        String selected = spinner.getSelectedItem().toString();

        //read report and fetch the values for the selected month & year
        reports = FileOperationsImpl.readFromFile(getActivity().getBaseContext(), Constants.REPORTS_FILE);

        return rootView;
    }

    public String getMonth(int month){
        return new DateFormatSymbols().getMonths()[month];
    }

}

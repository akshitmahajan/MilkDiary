package com.manaksh.milkdiary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.manaksh.milkdiary.adapter.ImageAdapter;
import com.manaksh.milkdiary.adapter.ReportsAdapter;
import com.manaksh.milkdiary.model.DailyData;
import com.manaksh.milkdiary.model.ItemType;
import com.manaksh.milkdiary.model.TransactionType;
import com.manaksh.milkdiary.utils.Constants;
import com.manaksh.milkdiary.utils.FileOperationsImpl;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import manaksh.com.milkdiary.R;

import static android.R.layout.simple_spinner_item;

public class ReportsFragment extends Fragment {

    Spinner spinner = null;
    Context context = null;
    GridView colorGrid, tagGrid = null;
    HashMap<String, Double> hitCount = new HashMap<String, Double>();
    HashMap<String, Double> missCount = new HashMap<String, Double>();

    ArrayList<String> reports = new ArrayList<String>();
    String[] reportTags = new String[5];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        this.context = getActivity().getBaseContext();

        ArrayList<String> tagList = FileOperationsImpl.readFromFile(getActivity().getBaseContext(), Constants.TAGS_FILE);

        if (tagList == null) {
            reportTags = new String[]{"", "#tag1", "#tag2", "#tag3", "tag4"};
        } else {
            reportTags[0]="";
            int i = 1;
            for (String str : tagList) {
                reportTags[i] = str;
                i++;
            }
        }

        tagGrid = (GridView) rootView.findViewById(R.id.tagGrid);
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, reportTags);
        tagGrid.setAdapter(tagAdapter);

        final ReportsAdapter adapterObj = new ReportsAdapter(context);
        colorGrid = (GridView) rootView.findViewById(R.id.colorGrid);
        colorGrid.setAdapter(adapterObj);

        Calendar cal  = Calendar.getInstance();
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("-Select-");
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                String _splitSelected[] = selected.split(" ");
                String month = _splitSelected[0];
                int _monthSelected = 0;

                if(selected.equals("-Select-")){
                    //do nothing
                }
                else{
                    String _yearSelected = _splitSelected[1];

                    //get the month in int format
                    try {

                        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
                        Calendar calc = Calendar.getInstance();
                        calc.setTime(date);
                        _monthSelected = calc.get(Calendar.MONTH);
                        _monthSelected = _monthSelected+1;
                    }catch (ParseException e){
                        e.printStackTrace();
                    }

                    //read report and fetch the values for the selected month & year
                    reports = FileOperationsImpl.readFromFile(getActivity().getBaseContext(), Constants.REPORTS_FILE);

                    if((reports!=null) && (reports.size()!=0)){
                        for (String report : reports) {

                            // report : 4/1/2016,ORANGE,2.5,hit
                            String[] _report = report.split(",");
                            String[] date = _report[0].split("/");

                            // Type : _report[1], Quantity : _report[2], Transaction_Type : _report[3]
                            //check if fetched month matches the selected month
                            if (Integer.parseInt(date[1])==_monthSelected && date[2].equals(_yearSelected)) {
                                //showToast("Data found for the selected month!");
                                process(_report[1], _report[2], _report[3]);
                            }else{
                                //showToast("Data not found for the selected month!");
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Data not found for the selected month!");
                            }
                        }
                    }else{
                        showToast("Reports are empty!");
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Reports are empty!");
                    }
                }
                System.out.println("***Hit Info***");
                displayInfo(hitCount);
                System.out.println("***Miss Info***");
                displayInfo(missCount);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Month: unselected");
            }
        });
        return rootView;
    }

    public void process(String type, String Quantity, String transactionType){
            double quantity = Double.parseDouble(Quantity);

            if(transactionType.equals(TransactionType.hit.toString())){
                double tmpCount = hitCount.get(type)!= null ? hitCount.get(type):0;
                if(tmpCount > 0 ){
                    tmpCount = tmpCount + quantity;
                    hitCount.put(type, new Double(tmpCount));
                } else {
                    hitCount.put(type, quantity);
                }
            } else if(transactionType.equals(TransactionType.miss.toString())){
                double tmpCount = missCount.get(type)!=null?missCount.get(type):0;
                if(tmpCount > 0 ){
                    tmpCount = tmpCount + quantity;
                    missCount.put(type, new Double(tmpCount));
                } else {
                    missCount.put(type, quantity);
                }
            } else {
                System.out.println("Invalid Transaction Type");
            }
    }

    public String getMonth(int month){
        return new DateFormatSymbols().getMonths()[month];
    }

    public void displayInfo(HashMap<String, Double> dataMap){
        if(dataMap != null){
            Iterator  keys = dataMap.keySet().iterator();

            for (Map.Entry<String, Double> entry : dataMap.entrySet())
            {
                showToast("Data : "+entry.getKey() + " " + entry.getValue());
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }

    void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
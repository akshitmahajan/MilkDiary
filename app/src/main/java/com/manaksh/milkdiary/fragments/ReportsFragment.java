package com.manaksh.milkdiary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.manaksh.milkdiary.adapter.ImageAdapter;
import com.manaksh.milkdiary.adapter.ReportsAdapter;
import com.manaksh.milkdiary.model.DailyData;
import com.manaksh.milkdiary.model.ItemType;
import com.manaksh.milkdiary.model.TransactionType;
import com.manaksh.milkdiary.utils.Constants;
import com.manaksh.milkdiary.utils.FileOperationsImpl;

import org.w3c.dom.Text;

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
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv10, tv11, tv13 = null;
    EditText et1 = null;

    ArrayList<String> reports = new ArrayList<String>();
    String[] reportTags = new String[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> is Resumed ? : " + isResumed());
        //View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        final View rootView = inflater.inflate(R.layout.tablelayer, container, false);
        this.context = getActivity().getBaseContext();

        tv1 = (TextView) rootView.findViewById(R.id.tv1);
        tv2 = (TextView) rootView.findViewById(R.id.tv2);
        tv3 = (TextView) rootView.findViewById(R.id.tv3);
        tv4 = (TextView) rootView.findViewById(R.id.tv4);
        tv5 = (TextView) rootView.findViewById(R.id.tv5);
        tv6 = (TextView) rootView.findViewById(R.id.tv6);
        tv7 = (TextView) rootView.findViewById(R.id.tv7);
        tv8 = (TextView) rootView.findViewById(R.id.tv8);
        et1 = (EditText) rootView.findViewById(R.id.et1);
        tv13 = (TextView) rootView.findViewById(R.id.tv13);
        ArrayList<String> tagList = FileOperationsImpl.readFromFile(getActivity().getBaseContext(), Constants.TAGS_FILE);

        if (tagList == null) {
            reportTags = new String[]{"", "#tag1", "#tag2", "#tag3", "tag4"};
        } else {
            reportTags[0] = "";
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

        /*final ReportsAdapter adapterObj = new ReportsAdapter(context);
        colorGrid = (GridView) rootView.findViewById(R.id.colorGrid);
        colorGrid.setAdapter(adapterObj);*/

        Calendar cal = Calendar.getInstance();
        List<String> spinnerArray = new ArrayList<String>();
        //spinnerArray.add("-Select-");

        //Adding last 12 months exclusing current month
        for (int i = 0; i < 12; i++) {
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

                hitCount.clear();
                missCount.clear();

                String selected = spinner.getSelectedItem().toString();
                String _splitSelected[] = selected.split(" ");
                String month = _splitSelected[0];
                int _monthSelected = 0;

                if (selected.equals("-Select-")) {
                    //do nothing
                } else {
                    String _yearSelected = _splitSelected[1];

                    //get the month in int format
                    try {

                        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
                        Calendar calc = Calendar.getInstance();
                        calc.setTime(date);
                        _monthSelected = calc.get(Calendar.MONTH);
                        _monthSelected = _monthSelected + 1;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //read report and fetch the values for the selected month & year
                    reports = FileOperationsImpl.readFromFile(getActivity().getBaseContext(), Constants.REPORTS_FILE);

                    if ((reports != null) && (reports.size() != 0)) {
                        for (String report : reports) {

                            // report : 4/1/2016,ORANGE,2.5,hit
                            String[] _report = report.split(",");
                            String[] date = _report[0].split("/");

                            // Type : _report[1], Quantity : _report[2], Transaction_Type : _report[3]
                            //check if fetched month matches the selected month
                            if (Integer.parseInt(date[1]) == _monthSelected && date[2].equals(_yearSelected)) {
                                //showToast("Data found for the selected month!");
                                process(_report[1], _report[2], _report[3]);
                            } else {
                                //showToast("Data not found for the selected month!");
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Data not found for the selected month!");
                            }
                        }
                    } else {
                        showToast("Reports are empty!");
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Reports are empty!");
                    }
                }
                /*System.out.println("***Hit Info***");
                displayInfo(hitCount);
                System.out.println("***Miss Info***");
                displayInfo(missCount);*/

                //populate row1 -> hit data
                tv1.setText(hitCount.get("ORANGE") != null ? hitCount.get("ORANGE").toString() : "0");
                tv2.setText(hitCount.get("BLUE") != null ? hitCount.get("BLUE").toString() : "0");
                tv3.setText(hitCount.get("YELLOW") != null ? hitCount.get("YELLOW").toString() : "0");
                tv4.setText(hitCount.get("BLACK") != null ? hitCount.get("BLACK").toString() : "0");

                //populate row -> miss data

                tv5.setText(missCount.get("ORANGE") != null ? missCount.get("ORANGE").toString() : "0");
                tv6.setText(missCount.get("BLUE") != null ? missCount.get("BLUE").toString() : "0");
                tv7.setText(missCount.get("YELLOW") != null ? missCount.get("YELLOW").toString() : "0");
                tv8.setText(missCount.get("BLACK") != null ? missCount.get("BLACK").toString() : "0");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Month: unselected");
            }
        });

        tv10 = (TextView) rootView.findViewById(R.id.tv10);
        //Give TableLayout 2 & 3 onClick Listener

        if (tv1 != null) {
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv1.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv1.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv2 != null) {
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tv2.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));

                    tv10.setText(tv2.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv3 != null) {
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv3.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv3.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv4 != null) {
            tv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tv4.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv4.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv5 != null) {
            tv5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv5.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv5.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv6 != null) {
            tv6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv6.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv6.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv7 != null) {
            tv7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv7.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv8.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv7.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        if (tv8 != null) {
            tv8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv8.setBackgroundColor(getResources().getColor(R.color.grey));
                    tv1.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv3.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv4.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv6.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv7.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv5.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv10.setText(tv8.getText().toString());
                    Double price = computePrice(et1);
                    Double total = Double.parseDouble(tv10.getText().toString()) * price;
                    tv13.setText(total.toString());
                }
            });
        }

        /*et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et1.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });*/

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et1.getText().toString().equals("") || et1.getText().toString() == null) {
                    tv13.setText("0");
                } else {
                    Double total = Double.parseDouble(tv10.getText().toString()) * Double.parseDouble(et1.getText().toString());
                    tv13.setText(total.toString());
                }
            }
        });
        return rootView;
    }

    public double computePrice(EditText et1) {
        Double price = null;
        if (et1.getText().toString().equals("") || et1.getText().toString() == null) {
            price = new Double(0);
        } else {
            price = Double.parseDouble(et1.getText().toString());
        }
        return price;
    }

    public void process(String type, String Quantity, String transactionType) {
        double quantity = Double.parseDouble(Quantity);

        if (transactionType.equals(TransactionType.hit.toString())) {
            double tmpCount = hitCount.get(type) != null ? hitCount.get(type) : 0;
            if (tmpCount > 0) {
                tmpCount = tmpCount + quantity;
                hitCount.put(type, new Double(tmpCount));
            } else {
                hitCount.put(type, quantity);
            }
        } else if (transactionType.equals(TransactionType.miss.toString())) {
            double tmpCount = missCount.get(type) != null ? missCount.get(type) : 0;
            if (tmpCount > 0) {
                tmpCount = tmpCount + quantity;
                missCount.put(type, new Double(tmpCount));
            } else {
                missCount.put(type, quantity);
            }
        } else {
            System.out.println("Invalid Transaction Type");
        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public void displayInfo(HashMap<String, Double> dataMap) {
        if (dataMap != null) {
            Iterator keys = dataMap.keySet().iterator();

            for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
                showToast("Data : " + entry.getKey() + " " + entry.getValue());
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        }
    }

    void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>Resumed");
        super.onResume();
    }


}
package com.manaksh.milkdiary.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manaksh.milkdiary.adapter.ImageAdapter;
import com.manaksh.milkdiary.model.DailyData;
import com.manaksh.milkdiary.model.ItemType;
import com.manaksh.milkdiary.model.TransactionType;
import com.manaksh.milkdiary.utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import manaksh.com.milkdiary.R;

public class DiaryFragment extends Fragment {
    static String[] tags = new String[]{"#tag1", "#tag2", "#tag3", "#tag4"};
    Context context = null;
    GridView grid, gridTag = null;
    List<DailyData> ls_databean = new ArrayList<DailyData>();
    Button btn_Save = null;
    ArrayList<String> reports = new ArrayList<String>();
    StringBuilder _thisDay = null;
    ImageButton arrowLeft, arrowRight = null;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            ls_databean.clear();
            // arg1 = year
            // arg2 = month
            // arg3 = day
            StringBuilder date = showDate(arg1, arg2 + 1, arg3);
            if (!(date.equals(_thisDay))) {

                ImageAdapter adapterObj = new ImageAdapter(context);

                //adapterObj = loadAdapter(reports, dateView, adapterObj);
                reports = readFromFile(Constants.REPORTS_FILE);
                for (String str : reports) {

                    String[] data = str.split(",");
                    //split with . & form the image name
                    String[] image_name = data[2].split("\\.");

                    if (data[0].equals(dateView.getText().toString())) {
                        String txt = "_" + image_name[0] + "_" + image_name[1] + "_" + data[3];
                        int idNo = getResources().getIdentifier("_" + image_name[0] + "_" + image_name[1] + "_" + data[3], "drawable", context.getPackageName());
                        int position = getPosition(data[2], data[1]);
                        adapterObj.mThumbIds[position] = idNo;
                    }
                }
                grid = (GridView) rootView.findViewById(R.id.valueGrid);
                grid.setAdapter(adapterObj);
            }
        }
    };
    TextView calenderBtn = null;
    View rootView = null;
    private DatePicker datePicker = null;
    private Calendar calendar = null;
    private TextView dateView = null;
    private int year, month, day = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        this.context = getActivity().getBaseContext();
        //this.context = getActivity().getApplicationContext();
        this.rootView = rootView;
        //READ TAGS FILE and load the #tags
        ArrayList<String> tagList = readFromFile(Constants.TAGS_FILE);

        if (tagList == null) {
            tags = new String[]{"#tag1", "#tag2", "#tag3", "#tag4"};
        } else {
            int i = 0;
            for (String str : tagList) {
                tags[i] = str;
                i++;
            }
        }

        //Initializes the calendarview
        dateView = (TextView) rootView.findViewById(R.id.dateView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        _thisDay = showDate(year, month + 1, day);

        reports = readFromFile(Constants.REPORTS_FILE);
        final ImageAdapter adapterObj = new ImageAdapter(context);
        //adapterObj = loadAdapter(reports, dateView, adapterObj);

        if (reports != null) {

            for (String str : reports) {

                String[] data = str.split(",");

                //split with . & form the image name
                String[] image_name = data[2].split("\\.");

                if (data[0].equals(dateView.getText().toString())) {

                    int idNo = getResources().getIdentifier("_" + image_name[0] + "_" + image_name[1] + "_" + data[3], "drawable", context.getPackageName());
                    int position = getPosition(data[2], data[1]);
                    adapterObj.mThumbIds[position] = idNo;
                }
            }
        }

        final Integer[] mThumbs = adapterObj.mThumbIds;
        grid = (GridView) rootView.findViewById(R.id.valueGrid);
        grid.setAdapter(adapterObj);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View imgView, int position, long id) {
                ImageView imageView = (ImageView) imgView;
                String name = getResources().getResourceEntryName(mThumbs[position]);
                //_1_0_DEFAULT
                String[] splitName = name.split("_");
                int idNo = 0;
                ItemType _type = Constants.PositionTypeMap.get(position);

                if (position == 0 || position == 1 || position == 2 || position == 3) {
                    //Do nothing
                } else {
                    //Create a new object and set everything
                    DailyData dailyData = new DailyData();

                    if (dateView != null) {
                        dailyData.setDate(dateView.getText().toString());

                    } else {
                        dailyData.setDate("01/01/2000");
                    }

                    dailyData.setType(_type);
                    dailyData.setQuantity(Double.parseDouble(splitName[1] + "." + splitName[2]));

                    switch (splitName[3]) {
                        //DEFAULT->HIT
                        case Constants.STEADY:
                            idNo = getResources().getIdentifier("_" + splitName[1] + "_" + splitName[2] + "_hit", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            dailyData.setTransactionType(TransactionType.hit);
                            break;
                        //HIT->MISS
                        case Constants.HIT:
                            idNo = getResources().getIdentifier("_" + splitName[1] + "_" + splitName[2] + "_miss", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            dailyData.setTransactionType(TransactionType.miss);
                            break;
                        //MISS->DEFAULT
                        case Constants.MISS:
                            idNo = context.getResources().getIdentifier("_" + splitName[1] + "_" + splitName[2] + "_steady", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            dailyData.setTransactionType(TransactionType.steady);
                            break;
                    }
                    ls_databean.add(dailyData);
                }
            }
        });

        //#Tags Grid
        gridTag = (GridView) rootView.findViewById(R.id.labelGrid);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, tags);
        gridTag.setAdapter(adapter);
        gridTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                ls_databean.clear();
                final View v1 = v;

                //get prompt.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(context);

                View promptView = layoutInflater.inflate(R.layout.prompts, null);
                //View promptView = layoutInflater.inflate(R.layout.prompts, parent,false);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);

                //Get new tag from the user
                final EditText input = (EditText) promptView.findViewById(R.id.userInput);

                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                ((TextView) v1).setText("#" + input.getText());

                                //update tags array and update the file
                                String newTag = "#" + input.getText().toString();
                                tags[position] = newTag;
                                String allTags = "";

                                for (int i = 0; i < tags.length; i++) {
                                    allTags = allTags + tags[i] + ",";
                                }
                                writeToFile(allTags);
                                //Set color of the tag
                                String colorHex = "";

                                if (position == 0) {
                                    colorHex = "#ff803e";
                                } else if (position == 1) {
                                    colorHex = "#1a9def";
                                } else if (position == 2) {
                                    colorHex = "#faff00";

                                } else if (position == 3) {
                                    colorHex = "BLACK";
                                }
                                ((TextView) v1).setTextColor(Color.parseColor(colorHex));
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();
            }
        });

        //Save Button
        btn_Save = (Button) rootView.findViewById(R.id.saveBtn);
        btn_Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean _result = writeToFile(ls_databean);

                if (_result) {
                    Toast.makeText(context, Constants.FILE_SAVE_SUCCESS, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Constants.FILE_SAVE_FAILURE, Toast.LENGTH_SHORT).show();
                }
            }

        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpdFromDate = new DatePickerDialog(getActivity(), myDateListener, year, month, day);
                dpdFromDate.show();

            }
        });


        arrowLeft = (ImageButton) rootView.findViewById(R.id.arrowLeft);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date date = formatter.parse(dateView.getText().toString());
                    Calendar cal  = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, -1);
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    _thisDay = showDate(year, month + 1, day);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        arrowRight = (ImageButton) rootView.findViewById(R.id.arrowRight);
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date date = formatter.parse(dateView.getText().toString());
                    Calendar cal  = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 1);
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    _thisDay = showDate(year, month+1, day);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    private StringBuilder showDate(int year, int month, int day) {
        StringBuilder date = new StringBuilder().append(day).append("/").
                append(month).append("/").append(year);
        dateView.setText(date);
        return date;
    }

    public int getPosition(String input, String type) {
        //compute position
        int position = 0;
        if (type.equals(Constants.TYPE1)) {
            position = Constants.Type1Map.get(input);
        } else if (type.equals(Constants.TYPE2)) {
            position = Constants.Type2Map.get(input);
        } else if (type.equals(Constants.TYPE3)) {
            position = Constants.Type3Map.get(input);
        } else if (type.equals(Constants.TYPE4)) {
            position = Constants.Type4Map.get(input);
        }
        return position;
    }

    public void writeToFile(String txt) {
        Toast.makeText(context, txt,
                Toast.LENGTH_SHORT).show();

        try {
            FileOutputStream fileout = context.openFileOutput(Constants.TAGS_FILE, context.MODE_PRIVATE);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fileout));
            //OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            //comma , split string tags
            String[] tags = txt.split(",");
            for (String tag : tags) {
                outputWriter.write(tag);
                outputWriter.newLine();
            }
            outputWriter.close();

            Toast.makeText(context, "Tags saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean writeToFile(List<DailyData> dailyDataList) {

        try {
            FileOutputStream fileout = context.openFileOutput(Constants.REPORTS_FILE, context.MODE_APPEND);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fileout));
            //OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);

            if (dailyDataList == null) {
                return false;
            } else {
                for (DailyData data : dailyDataList) {
                    String txt = "";
                    if (data.getDate().equals("") || data.getType() == null || data.getQuantity() == 0 || data.getTransactionType() == null) {
                        return false;
                    } else {
                        txt = data.getDate() + "," + data.getType() + "," + data.getQuantity() + "," + data.getTransactionType();
                        outputWriter.write(txt);
                        outputWriter.newLine();
                    }
                }
                outputWriter.close();
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> readFromFile(String FILE_NAME) {

        ArrayList<String> listfromFile = new ArrayList<String>();
        FileInputStream fileIn = null;
        InputStreamReader InputRead = null;
        BufferedReader br = null;

        try {

            fileIn = context.openFileInput(FILE_NAME);
            String sCurrentLine;

            br = new BufferedReader(new InputStreamReader(fileIn));
            while ((sCurrentLine = br.readLine()) != null) {
                listfromFile.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return listfromFile;
    }
}

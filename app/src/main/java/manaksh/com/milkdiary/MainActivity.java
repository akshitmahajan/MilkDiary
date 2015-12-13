package manaksh.com.milkdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manaksh.milkdiary.adapter.ImageAdapter;
import com.manaksh.milkdiary.model.DailyData;
import com.manaksh.milkdiary.model.ItemType;
import com.manaksh.milkdiary.model.TransactionType;
import com.manaksh.milkdiary.utils.Constants;

public class MainActivity extends Activity {

    static String[] tags = new String[]{"#tag1", "#tag2", "#tag3", "#tag4"};
    final Context context = this;
    GridView grid, gridTag = null;
    List<DailyData> ls_databean = new ArrayList<DailyData>();
    Button btn_Save, btn_Calender = null;
    ImageButton imageButton = null;
    ArrayList<String> reports = new ArrayList<String>();
    private DatePicker datePicker = null;
    private Calendar calendar = null;
    private TextView dateView = null;
    private int year, month, day = 0;
    StringBuilder _thisDay = null;

    public ImageAdapter loadAdapter(String[] reports, TextView dateView, ImageAdapter adapterObj){
        return adapterObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //READ TAGS FILE and load the #tags
        ArrayList<String> tagList = readFromFile(Constants.TAGS_FILE);

        if(tagList==null){
            tags = new String[]{"#tag1", "#tag2", "#tag3", "#tag4"};
        }
        else{
            int i=0;
            for(String str : tagList){
                tags[i] = str;
                i++;
            }
        }
        //Initializes the calendarview
        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        _thisDay = showDate(year, month + 1, day);

        //Format : 12/12/2015,ORANGE,1.0,HIT
        //READ REPORTS FILE and load the view
        reports = readFromFile(Constants.REPORTS_FILE);

        final ImageAdapter adapterObj = new ImageAdapter(this);
        //adapterObj = loadAdapter(reports, dateView, adapterObj);

        if(reports!=null){

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
        grid = (GridView) findViewById(R.id.gridView);
        //grid.setAdapter(new ImageAdapter(this));
        grid.setAdapter(adapterObj);
        grid.setOnItemClickListener(new OnItemClickListener() {
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
                }
                else {
                    //Create a new object and set everything
                    DailyData dailyData = new DailyData();

                    if (dateView != null) {
                        dailyData.setDate(dateView.getText().toString());

                    } else {
                        dailyData.setDate("01/01/2000");
                    }

                    dailyData.setType(_type);
                    dailyData.setQuantity(Double.parseDouble(splitName[1]+"."+splitName[2]));

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
                            idNo = getResources().getIdentifier("_" + splitName[1] + "_" + splitName[2]  + "_miss", "drawable", context.getPackageName());
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

        btn_Calender = (Button) findViewById(R.id.button1);
        btn_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        //#Tags Grid
        gridTag = (GridView) findViewById(R.id.gridView0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tags);
        gridTag.setAdapter(adapter);
        gridTag.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                ls_databean.clear();
                final View v1 = v;

                //get prompt.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(context);

                View promptView = layoutInflater.inflate(R.layout.prompts, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

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
        btn_Save = (Button) findViewById(R.id.button);
        btn_Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Execute some code after 2 seconds have passed
                //Handler handler = new Handler();
                /*handler.postDelayed(new Runnable() {
                    public void run() {
                        ReadFromFile();
                    }
                }, 2000);*/

                boolean _result = writeToFile(ls_databean);

                if (_result) {
                    Toast.makeText(MainActivity.this, Constants.FILE_SAVE_SUCCESS, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, Constants.FILE_SAVE_FAILURE, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private StringBuilder showDate(int year, int month, int day) {
        StringBuilder date = new StringBuilder().append(day).append("/").
                append(month).append("/").append(year);
        dateView.setText(date);
        return date;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            ls_databean.clear();
            // arg1 = year
            // arg2 = month
            // arg3 = day
            StringBuilder date = showDate(arg1, arg2 + 1, arg3);
            if(!(date.equals(_thisDay))){

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
                grid = (GridView) findViewById(R.id.gridView);
                grid.setAdapter(adapterObj);
            }
        }
    };

    public int getPosition(String input, String type){
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

    /*public void addListenerOnDate(TextView dateView) {

        dateView = (TextView) findViewById(R.id.textView3);

        dateView.setOnClickListener(new View.OnClickListener() {

        @Override
        @SuppressWarnings("deprecation")
        public void onClick(View view) {

            setDate(view);
        }

    });

   }*/

    public void writeToFile(String txt) {
        Toast.makeText(getBaseContext(), txt,
                Toast.LENGTH_SHORT).show();

        try {
            FileOutputStream fileout = openFileOutput(Constants.TAGS_FILE, MODE_PRIVATE);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fileout));
            //OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            //comma , split string tags
            String[] tags = txt.split(",");
            for(String tag : tags){
                outputWriter.write(tag);
                outputWriter.newLine();
            }
            outputWriter.close();

            Toast.makeText(getBaseContext(), "Tags saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean writeToFile(List<DailyData> dailyDataList) {

        try {
            FileOutputStream fileout = openFileOutput(Constants.REPORTS_FILE, MODE_APPEND);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fileout));
            //OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);

            if (dailyDataList == null) {
                return false;
            }
            else {
                for (DailyData data : dailyDataList) {
                    String txt = "";
                    if (data.getDate().equals("") || data.getType()==null || data.getQuantity()==0 || data.getTransactionType()==null) {
                        return false;
                    }
                    else {
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

            fileIn = openFileInput(FILE_NAME);
            String sCurrentLine;

            br = new BufferedReader(new InputStreamReader(fileIn));
            while ((sCurrentLine = br.readLine()) != null) {
                listfromFile.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return listfromFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

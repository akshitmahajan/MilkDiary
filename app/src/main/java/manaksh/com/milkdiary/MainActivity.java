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
import com.manaksh.milkdiary.model.DataBean;
import com.manaksh.milkdiary.utils.Constants;

public class MainActivity extends Activity {

    static String[] tags = new String[]{"#tag1", "#tag2", "#tag3", "#tag4"};
    final Context context = this;
    GridView grid, gridTag;
    List<DataBean> ls_databean = new ArrayList<>();
    List<DataBean> saved_ls_databean = new ArrayList<>();
    Button btn_Save, btn_Calender = null;
    ImageButton imageButton;
    String[] reports = null;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    StringBuilder _thisDay = null;

    public ImageAdapter loadAdapter(String[] reports, TextView dateView, ImageAdapter adapterObj){
        return adapterObj;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //READ TAGS FILE and load the #tags
        tags = readFromFile(Constants.TAGS_FILE);

        //Initializes the calendarview
        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        _thisDay = showDate(year, month + 1, day);

        //Format : Date;Type;Value;Status$Date;Type;Value;Status$...
        //READ REPORTS FILE and load the view
        reports = readFromFile(Constants.REPORTS_FILE);

        final ImageAdapter adapterObj = new ImageAdapter(this);

        //adapterObj = loadAdapter(reports, dateView, adapterObj);

        for (int i = 0; i < reports.length; i++) {
            String[] data = reports[i].split(";");

            if (data[0].equals(dateView.getText().toString())) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>Inside reports loop");
                int idNo = getResources().getIdentifier(data[2] + "_" + data[3], "drawable", context.getPackageName());

                //compute position
                int position = 0;
                if (data[1].equals(Constants.ORANGE)) {
                    if (data[2].equals("one")) {
                        position = 4;
                    } else if (data[2].equals("onefive")) {
                        position = 8;
                    } else if (data[2].equals("two")) {
                        position = 12;
                    } else if (data[2].equals("twofive")) {
                        position = 16;
                    } else if (data[2].equals("three")) {
                        position = 20;
                    }
                } else if (data[1].equals(Constants.BLUE)) {
                    if (data[2].equals("one")) {
                        position = 5;
                    } else if (data[2].equals("onefive")) {
                        position = 9;
                    } else if (data[2].equals("two")) {
                        position = 13;
                    } else if (data[2].equals("twofive")) {
                        position = 17;
                    } else if (data[2].equals("three")) {
                        position = 21;
                    }
                } else if (data[1].equals(Constants.YELLOW)) {
                    if (data[2].equals("one")) {
                        position = 6;
                    } else if (data[2].equals("onefive")) {
                        position = 10;
                    } else if (data[2].equals("two")) {
                        position = 14;
                    } else if (data[2].equals("twofive")) {
                        position = 18;
                    } else if (data[2].equals("three")) {
                        position = 22;
                    }
                } else if (data[1].equals(Constants.BLACK)) {
                    if (data[2].equals("one")) {
                        position = 7;
                    } else if (data[2].equals("onefive")) {
                        position = 11;
                    } else if (data[2].equals("two")) {
                        position = 15;
                    } else if (data[2].equals("twofive")) {
                        position = 19;
                    } else if (data[2].equals("three")) {
                        position = 23;
                    }
                }
                adapterObj.mThumbIds[position] = idNo;
            }
        }

        final Integer[] mThumbs = adapterObj.mThumbIds;
        grid = (GridView) findViewById(R.id.gridView);
        //grid.setAdapter(new ImageAdapter(this));
        grid.setAdapter(adapterObj);

        grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View imgView, int position, long id) {
                /*Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();*/

                ImageView imageView = (ImageView) imgView;
                String name = getResources().getResourceEntryName(mThumbs[position]);

                String[] splitName = name.split("_");
                int idNo = 0;
                String _type = null;

                if (position == 4 || position == 8 || position == 12 || position == 16 || position == 20) {
                    //data.set_type(tags[0]);
                    _type = Constants.ORANGE;
                } else if (position == 5 || position == 8 || position == 13 || position == 17 || position == 21) {
                    _type = Constants.BLUE;
                } else if (position == 6 || position == 9 || position == 14 || position == 18 || position == 22) {
                    _type = Constants.YELLOW;
                } else if (position == 7 || position == 10 || position == 15 || position == 19 || position == 23) {
                    _type = Constants.BLACK;
                } else {
                    //do nothing
                }

                if (position == 0 || position == 1 || position == 2 || position == 3) {
                    //Do nothing
                }
                else {
                    /*for(DataBean data : ls_databean){
                        //check date
                        if(data.get_date().equals(dateView.getText().toString())){
                            //check type
                            if(data.get_type().equals(_type)){
                                //check value
                                if(data.get_value().equals(splitName[0])){
                                    //only change the status
                                    //set a flag
                                }
                            }
                        }
                    }*/

                    //Create a new object and set everything
                    //DataBean data_new = new DataBean();
                    DataBean data = new DataBean();

                    if (dateView != null) {
                        data.set_date(dateView.getText().toString());
                    } else {
                        data.set_date("01/01/2000");
                    }
                    data.set_value(splitName[0]);
                    data.set_type(_type);

                    switch (splitName[1]) {
                        //DEFAULT->HIT
                        case Constants.DEFAULT:
                            idNo = getResources().getIdentifier(splitName[0] + "_1", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            data.set_state(Constants.HIT);
                            break;
                        //HIT->MISS
                        case Constants.HIT:
                            idNo = getResources().getIdentifier(splitName[0] + "_2", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            data.set_state(Constants.MISS);
                            break;
                        //MISS->DEFAULT
                        case Constants.MISS:
                            idNo = context.getResources().getIdentifier(splitName[0] + "_0", "drawable", context.getPackageName());
                            imageView.setImageResource(idNo);
                            adapterObj.mThumbIds[position] = idNo;
                            data.set_state(Constants.DEFAULT);
                            break;
                    }

                    ls_databean.add(data);
                }
            }
        });

        /*imageButton = (ImageButton) findViewById(R.id.imageButton1);
        imageButton.setLayoutParams(new RelativeLayout.LayoutParams(160, 160));
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageButton.setPadding(10, 10, 10, 10);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "" + "Calender clicked",
                        Toast.LENGTH_SHORT).show();
                setDate(v);
            }
        });*/

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

                                    System.out.print(tags[i] + " ");
                                    allTags = allTags + tags[i] + ";";
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

                                //change the value of #tag
                                //Toast.makeText(getApplicationContext(),
                                //      ((TextView) v1).getText(), Toast.LENGTH_SHORT).show();
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
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            StringBuilder date = showDate(arg1, arg2 + 1, arg3);
            if(!(date.equals(_thisDay))){

                ImageAdapter adapterObj = new ImageAdapter(context);

                //adapterObj = loadAdapter(reports, dateView, adapterObj);

                for (int i = 0; i < reports.length; i++) {
                    String[] data = reports[i].split(";");

                    if (data[0].equals(dateView.getText().toString())) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>Inside reports loop");
                        int idNo = getResources().getIdentifier(data[2] + "_" + data[3], "drawable", context.getPackageName());

                        //compute position
                        int position = 0;
                        if (data[1].equals(Constants.ORANGE)) {
                            if (data[2].equals("one")) {
                                position = 4;
                            } else if (data[2].equals("onefive")) {
                                position = 8;
                            } else if (data[2].equals("two")) {
                                position = 12;
                            } else if (data[2].equals("twofive")) {
                                position = 16;
                            } else if (data[2].equals("three")) {
                                position = 20;
                            }
                        } else if (data[1].equals(Constants.BLUE)) {
                            if (data[2].equals("one")) {
                                position = 5;
                            } else if (data[2].equals("onefive")) {
                                position = 9;
                            } else if (data[2].equals("two")) {
                                position = 13;
                            } else if (data[2].equals("twofive")) {
                                position = 17;
                            } else if (data[2].equals("three")) {
                                position = 21;
                            }
                        } else if (data[1].equals(Constants.YELLOW)) {
                            if (data[2].equals("one")) {
                                position = 6;
                            } else if (data[2].equals("onefive")) {
                                position = 10;
                            } else if (data[2].equals("two")) {
                                position = 14;
                            } else if (data[2].equals("twofive")) {
                                position = 18;
                            } else if (data[2].equals("three")) {
                                position = 22;
                            }
                        } else if (data[1].equals(Constants.BLACK)) {
                            if (data[2].equals("one")) {
                                position = 7;
                            } else if (data[2].equals("onefive")) {
                                position = 11;
                            } else if (data[2].equals("two")) {
                                position = 15;
                            } else if (data[2].equals("twofive")) {
                                position = 19;
                            } else if (data[2].equals("three")) {
                                position = 23;
                            }
                        }
                        adapterObj.mThumbIds[position] = idNo;
                    }
                }
                grid = (GridView) findViewById(R.id.gridView);
                grid.setAdapter(adapterObj);

            }
        }
    };

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
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(txt);
            outputWriter.close();

            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean writeToFile(List<DataBean> dataBean) {

        try {
            FileOutputStream fileout = openFileOutput(Constants.REPORTS_FILE, MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);

            if (dataBean == null) {
                return false;
            }
            else {
                for (DataBean data : dataBean) {
                    String txt = "";
                    if (data.get_date().equals("") || data.get_state().equals("") || data.get_value().equals("") || data.get_type().equals("")) {
                        return false;
                    } else {
                        txt = data.get_date() + ";" + data.get_type() + ";" + data.get_value() + ";" + data.get_state() + "$";
                        outputWriter.write(txt);
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

    public String[] readFromFile(String FILE_NAME) {

        String[] _fromFile = null;

        FileInputStream fileIn = null;
        InputStreamReader InputRead = null;

        //reading text from file
        try {
            fileIn = openFileInput(FILE_NAME);
            InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[Constants.READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            System.out.println(">>>>>>>>>>>>>>>>>>>>" + s);
            if (s.contains("$") && s.contains(";")) {
                _fromFile = s.split("\\$");

            } else if (s.contains(";")) {
                _fromFile = s.split(";");
            }

            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (InputRead != null) {
                try {
                    InputRead.close();
                } catch (IOException e) {
                }
            }
        }

        return _fromFile;
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

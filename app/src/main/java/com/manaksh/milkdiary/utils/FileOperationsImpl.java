package com.manaksh.milkdiary.utils;

import android.content.Context;
import android.widget.Toast;

import com.manaksh.milkdiary.model.DailyData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akshmaha on 1/3/2016.
 */
public class FileOperationsImpl {

    /*
    Reads data from file
    Returns : ArrayList
     */
    public static ArrayList<String> readFromFile(Context context, String FILE_NAME) {

        ArrayList<String> listfromFile = new ArrayList<String>();
        FileInputStream fileIn = null;
        InputStreamReader InputRead = null;
        BufferedReader br = null;

        try {
            File file = context.getFileStreamPath(FILE_NAME);
            if (file == null || !file.exists()) {
                return null;
            } else {
                fileIn = context.openFileInput(FILE_NAME);
                String sCurrentLine;

                br = new BufferedReader(new InputStreamReader(fileIn));
                while ((sCurrentLine = br.readLine()) != null) {
                    listfromFile.add(sCurrentLine);
                }
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

    /*
    Writes Tags to the Tag File
     */
    public static void writeToFile(Context context, String txt) {
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

    /*
    Writes data to the reports file

    Date,ORANGE,2.5,HIT
     */
    public static boolean writeToFile(Context context, HashMap<Integer, DailyData> dailyDataMap) {

        try {
            FileOutputStream fileout = context.openFileOutput(Constants.REPORTS_FILE, context.MODE_APPEND);
            BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(fileout));
            //OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);

            if (dailyDataMap == null) {
                return false;
            } else {

                for (Map.Entry map : dailyDataMap.entrySet()) {
                    DailyData data = (DailyData) map.getValue();

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
}

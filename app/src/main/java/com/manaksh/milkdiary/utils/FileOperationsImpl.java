package com.manaksh.milkdiary.utils;

import android.content.Context;
import android.widget.Toast;

import com.manaksh.milkdiary.model.DailyData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshmaha on 1/3/2016.
 */
public class FileOperationsImpl {

    public static ArrayList<String> readFromFile(Context context, String FILE_NAME) {

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

    public static boolean writeToFile(Context context, List<DailyData> dailyDataList) {

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
}

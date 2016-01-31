package com.manaksh.milkdiary.utils;

import com.manaksh.milkdiary.model.ItemType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akshmaha on 12/_1/2015.
 */
public final class Constants {
    public static final int READ_BLOCK_SIZE = 100;
    public static final String STEADY = "steady";
    public static final String HIT = "hit";
    public static final String MISS = "miss";
    public static final String TYPE1 = "ORANGE";
    public static final String TYPE2 = "BLUE";
    public static final String TYPE3 = "YELLOW";
    public static final String TYPE4 = "BLACK";
    public static final String color_Orange = "#ff803e";
    public static final String color_Blue = "#1a9def";
    public static final String color_Yellow = "#faff00";
    public static final String color_Black = "BLACK";
    public static final String TAGS_FILE = "milkdiary_tags.txt";
    public static final String REPORTS_FILE = "milkdiary_report.txt";
    public static final String FILE_SAVE_SUCCESS = "Diary Saved!";
    public static final String FILE_SAVE_FAILURE = "Operation Failed!";
    public static final String NO_CHANGE = "Nothing to Save!";
    public static Map<String, Integer> Type1Map;
    public static Map<String, Integer> Type2Map;
    public static Map<String, Integer> Type3Map;
    public static Map<String, Integer> Type4Map;
    public static Map<Integer, ItemType> PositionTypeMap;

    static {
        Map<Integer, ItemType> tempMap = new HashMap<Integer, ItemType>();
        tempMap.put(4, ItemType.ORANGE);
        tempMap.put(8, ItemType.ORANGE);
        tempMap.put(12, ItemType.ORANGE);
        tempMap.put(16, ItemType.ORANGE);
        tempMap.put(20, ItemType.ORANGE);
        tempMap.put(5, ItemType.BLUE);
        tempMap.put(9, ItemType.BLUE);
        tempMap.put(13, ItemType.BLUE);
        tempMap.put(17, ItemType.BLUE);
        tempMap.put(21, ItemType.BLUE);
        tempMap.put(6, ItemType.YELLOW);
        tempMap.put(10, ItemType.YELLOW);
        tempMap.put(14, ItemType.YELLOW);
        tempMap.put(18, ItemType.YELLOW);
        tempMap.put(22, ItemType.YELLOW);
        tempMap.put(7, ItemType.BLACK);
        tempMap.put(11, ItemType.BLACK);
        tempMap.put(15, ItemType.BLACK);
        tempMap.put(19, ItemType.BLACK);
        tempMap.put(23, ItemType.BLACK);
        PositionTypeMap = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        tempMap.put("1.0", 4);
        tempMap.put("1.5", 8);
        tempMap.put("2.0", 12);
        tempMap.put("2.5", 16);
        tempMap.put("3.0", 20);
        Type1Map = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        /*tempMap.put("_1_0_DEFAULT",5);
        tempMap.put("_1_5_DEFAULT",9);
        tempMap.put("_2_0_DEFAULT",13);
        tempMap.put("_2_5_DEFAULT",17);
        tempMap.put("_3_0_DEFAULT",21);*/

        tempMap.put("1.0", 5);
        tempMap.put("1.5", 9);
        tempMap.put("2.0", 13);
        tempMap.put("2.5", 17);
        tempMap.put("3.0", 21);
        Type2Map = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        tempMap.put("1.0", 6);
        tempMap.put("1.5", 10);
        tempMap.put("2.0", 14);
        tempMap.put("2.5", 18);
        tempMap.put("3.0", 22);
        Type3Map = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        tempMap.put("1.0", 7);
        tempMap.put("1.5", 11);
        tempMap.put("2.0", 15);
        tempMap.put("2.5", 19);
        tempMap.put("3.0", 23);
        Type4Map = Collections.unmodifiableMap(tempMap);
    }
}

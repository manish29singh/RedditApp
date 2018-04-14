package com.manish.redditapp.redditapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manish on 4/14/2018.
 */

public class ExtractXML {
    private static final String TAG = "ExtractXML";

    private String tag;
    private  String xml;

    public ExtractXML(String xml,String tag ) {
        this.tag = tag;
        this.xml = xml;
    }

    public List<String> start(){
        List<String> result = new ArrayList<>();
        String[] xmlSplit = xml.split(tag + "\"");
        int count = xmlSplit.length;

        for (int i = 1; i < count; i++){
            String temp = xmlSplit[i];
            int index = temp.indexOf("\"");

            Log.d(TAG, "start: index "+ index);
            Log.d(TAG, "start: Extracted : " + temp);
            
            temp = temp.substring(0, index);
            Log.d(TAG, "start: snipped : " + temp);
            result.add(temp);
        }

        return result ;
    }
}

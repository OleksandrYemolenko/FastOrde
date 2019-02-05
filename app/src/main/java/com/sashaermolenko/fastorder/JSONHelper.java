package com.sashaermolenko.fastorder;

import android.content.Context;

import com.google.gson.Gson;
import com.sashaermolenko.fastorder.Items.CartItem;
import com.sashaermolenko.fastorder.Items.HistoryItem;

import java.io.CharArrayReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class JSONHelper {

    private static final String FILE_NAME = "History.json";

    static boolean exportToJSON(Context context, ArrayList<HistoryItem> dataList, ArrayList<ArrayList<CartItem> > allItems) {

        // JSON object
        Gson gson = new Gson();
        // list of phones
        DataItems dataItems = new DataItems();
        dataItems.setItems(dataList);
        dataItems.setAllHistory(allItems);
        // Translating full JSON array into string
        String jsonString = gson.toJson(dataItems);

        FileOutputStream fileOutputStream = null;

        try {

            // Writing data into memory

            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    static ArrayList<HistoryItem> importFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{

            // reading data from memory using file(FILE_NAME)

            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);

            // writing all received data into list

            return  dataItems.getItems();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    static ArrayList<ArrayList<CartItem> > importAllFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{

            // reading data from memory using file(FILE_NAME)

            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);

            // writing all received data into list

            return  dataItems.getAllHistory();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class DataItems {
        private ArrayList<HistoryItem> items;
        private ArrayList<ArrayList<CartItem> > allHistory;

        ArrayList<HistoryItem> getItems() {
            return items;
        }
        ArrayList<ArrayList<CartItem> > getAllHistory() {return allHistory;}
        void setItems(ArrayList<HistoryItem> items) {
            this.items = items;
        }
        void setAllHistory(ArrayList<ArrayList<CartItem> > allHistory) {
            this.allHistory = allHistory;
        }
    }
}
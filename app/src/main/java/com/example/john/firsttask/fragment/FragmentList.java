package com.example.john.firsttask.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.john.firsttask.R;
import com.example.john.firsttask.support.DatabaseHelper;
import com.example.john.firsttask.support.ListViewAdapter;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by john on 17.10.14.
 */
public class FragmentList extends Fragment {

    DatabaseHelper dbHelper = null;
    SQLiteDatabase db = null;
    HashMap<String, String> item = new HashMap<String, String>();
    private View rootView;
    private ListView listView;
    private ArrayList<HashMap<String, String>> arrayList;
    private ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.listview_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview);
        dbHelper = new DatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        new DownloadJson().execute();

        Cursor c = db.query(DatabaseHelper.TABLE_SCENARIO, new String[]{DatabaseHelper.UID, DatabaseHelper.TEXT,
                DatabaseHelper.CASE_ID}, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("text", c.getString(c.getColumnIndex(DatabaseHelper.TEXT)));
//                map.put("caseId", c.getString(c.getColumnIndex(DatabaseHelper.CASE_ID)));
//                arrayList.add(map);

                Log.d("myLogs", "ID = " + c.getString(c.getColumnIndex(DatabaseHelper.UID)) + ", text = "
                        + c.getString(c.getColumnIndex(DatabaseHelper.TEXT)) + "CaseId = " +
                        c.getString(c.getColumnIndex(DatabaseHelper.CASE_ID)));

            } while (c.moveToNext());

        }else
            c.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("SendCaseId");
                intent.putExtra("caseId", arrayList.get(position).get("caseId"));
                getActivity().sendBroadcast(intent);

            }
        });

        return rootView;
    }

    private class DownloadJson extends AsyncTask<Object, Long, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Object[] params) {
            arrayList = new ArrayList<HashMap<String, String>>();
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray;
            HttpRequest request = HttpRequest.get("http://expert-system.internal.shinyshark.com/scenarios/");
            if (request.code() == 200) {
                String response = request.body();
                try {
                    jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("scenarios");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        jsonObject = jsonArray.getJSONObject(i);
                        map.put("text", jsonObject.getString("text"));
                        map.put("caseId", jsonObject.getString("caseId"));
                        arrayList.add(map);
                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseHelper.TEXT, jsonObject.getString("text"));
                        cv.put(DatabaseHelper.CASE_ID, jsonObject.getString("caseId"));
                        db.insert(DatabaseHelper.TABLE_SCENARIO, null, cv);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return jsonObject;

        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            adapter = new ListViewAdapter(rootView.getContext(), arrayList);
            listView.setAdapter(adapter);
        }

    }


}

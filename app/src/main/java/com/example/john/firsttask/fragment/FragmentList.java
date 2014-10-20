package com.example.john.firsttask.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.john.firsttask.R;
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
    //    TaskDataBase dbHelper = null;
//    SQLiteDatabase db = null;
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
        new DownloadJson().execute();
//        dbHelper = new TaskDataBase(rootView.getContext());
//        db = dbHelper.getWritableDatabase();


        //

//        Cursor c = db.query(TaskDataBase.TABLE_NAME, new String[]{TaskDataBase.UID, TaskDataBase.NAME,
//                TaskDataBase.TEXT, TaskDataBase.SMALL_IMAGE, TaskDataBase.BIG_IMAGE}, null, null, null, null, null);
//
//        if (c.moveToFirst()) {
//
//            do {
//
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("name", c.getString(c.getColumnIndex(TaskDataBase.NAME)));
//                map.put("image", c.getString(c.getColumnIndex(TaskDataBase.SMALL_IMAGE)));
//                arrayList.add(map);
//
//                Log.d("myLogs", "ID = " + c.getString(c.getColumnIndex(TaskDataBase.UID)) + ", name = "
//                        + c.getString(c.getColumnIndex(TaskDataBase.NAME)) + ", text = " +
//                        c.getString(c.getColumnIndex(TaskDataBase.TEXT))
//                        + ", s_img = " + c.getString(c.getColumnIndex(TaskDataBase.SMALL_IMAGE)) +
//                        ", b_img = " + c.getString(c.getColumnIndex(TaskDataBase.BIG_IMAGE)));
//
//            } while (c.moveToNext());
//
//        } else
//            c.close();

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
//                    ContentValues cv = new ContentValues();
                        jsonObject = jsonArray.getJSONObject(i);
                        map.put("text", jsonObject.getString("text"));
//                    cv.put(TaskDataBase.NAME, jsonObject.getString("name"));
                        map.put("caseId", jsonObject.getString("caseId"));
//                    cv.put(TaskDataBase.SMALL_IMAGE, jsonObject.getString("image"));
                        // cv.put(TaskDataBase.BIG_IMAGE, jsonObject.getString("image"));
//
//   db.insert(TaskDataBase.TABLE_NAME, null, cv);
                        arrayList.add(map);

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

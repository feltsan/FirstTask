package com.example.john.firsttask.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.john.firsttask.R;
import com.example.john.firsttask.support.ImageLoader;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by john on 17.10.14.
 */
public class AskFragment extends Fragment {
    String text, image, caseId, yesCaseID, noCaseID;
    ImageView img;
    //    TaskDataBase dbHelper = null;
//    SQLiteDatabase db = null;
    TextView tvText;
    private View rootView;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.button_yes:
                    intent = new Intent("SendCaseId");
                    intent.putExtra("caseId", yesCaseID);
                    getActivity().sendBroadcast(intent);
                    break;
                case R.id.button_no:
                    intent = new Intent("SendCaseId");
                    intent.putExtra("caseId", noCaseID);
                    getActivity().sendBroadcast(intent);
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        caseId = bundle.getString("caseId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.questions, container, false);
        Button yes_button = (Button) rootView.findViewById(R.id.button_yes);
        Button no_button = (Button) rootView.findViewById(R.id.button_no);
        yes_button.setOnClickListener(onClickListener);
        no_button.setOnClickListener(onClickListener);

        new AsyncJSON().execute();

        img = (ImageView) rootView.findViewById(R.id.imageView);
        tvText = (TextView) rootView.findViewById(R.id.name);


//        dbHelper = new TaskDataBase(rootView.getContext());
//        db = dbHelper.getWritableDatabase();
        return rootView;

    }

    private class AsyncJSON extends AsyncTask<Object, Long, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Object[] params) {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
//            ContentValues cv = new ContentValues();
            HttpRequest request = HttpRequest.get("http://expert-system.internal.shinyshark.com/cases/" + caseId);
            if (request.code() == 200) {
                String response = request.body();
                try {
                    jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("case");
                    text = jsonObject.getString("text");
//                    cv.put("text", text);
                    image = jsonObject.getString("image");
                    jsonArray = jsonObject.getJSONArray("answers");
                    jsonObject = jsonArray.getJSONObject(0);
                    yesCaseID = jsonObject.getString("caseId");
                    jsonObject = jsonArray.getJSONObject(1);
                    noCaseID = jsonObject.getString("caseId");
//  cv.put(TaskDataBase.BIG_IMAGE, image);
//                    db.update(TaskDataBase.TABLE_NAME, cv, "name = ?", new String[]{name});
                    Log.e("SAAM", yesCaseID + "" + noCaseID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return jsonObject;
        }

        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            ImageLoader imageLoader = new ImageLoader(rootView.getContext());
            imageLoader.DisplayImage(image, img);
            tvText.setText(text);

//            Cursor c = db.query(TaskDataBase.TABLE_NAME, new String[]{TaskDataBase.UID, TaskDataBase.NAME,
//                    TaskDataBase.TEXT, TaskDataBase.SMALL_IMAGE, TaskDataBase.BIG_IMAGE}, "name = ?", new String[]{name}, null, null, null);
//
//            c.moveToFirst();

//            tvText.setText(c.getString(c.getColumnIndex(TaskDataBase.TEXT)));
//            tvFName.setText(c.getString(c.getColumnIndex(TaskDataBase.NAME)));
//            imageLoader.DisplayImage(c.getString(c.getColumnIndex(TaskDataBase.BIG_IMAGE)), img);

//            c.close();

        }
    }
}

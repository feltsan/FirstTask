package com.example.john.firsttask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.john.firsttask.fragment.AskFragment;
import com.example.john.firsttask.fragment.BlankFragment;
import com.example.john.firsttask.fragment.FragmentList;


public class MainActivity extends Activity {

    private FragmentMessBroadcastReceiver fragmentMessBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentMessBroadcastReceiver = new FragmentMessBroadcastReceiver();
        int orient = this.getResources().getConfiguration().orientation;

         if (orient == 1) {
            final Fragment fragment = new FragmentList();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commitAllowingStateLoss();
        } else {
            final Fragment fragment = new BlankFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commitAllowingStateLoss();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("SendCaseId");
        registerReceiver(fragmentMessBroadcastReceiver, filter);

    }

    class FragmentMessBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SendCaseId")) {
                Bundle bundle = new Bundle();
                bundle.putString("caseId", intent.getStringExtra("caseId"));
                Fragment fragment = new AskFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                fragment.setArguments(bundle);
                ft.commitAllowingStateLoss();
            }
        }
    }




}

package kr.devs.geniusbean.room;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import kr.devs.geniusbean.R;

public class Fragment_ready extends Fragment
{

    Context mContext;
    Button btn_kick,btn_kick_cancel,btn_start,btn_ready,btn_ready_cancel,btn_out;
    LinearLayout btn_kick_parent;
    View view;
    boolean ISWAITINGFORBUTTONRESULT = false;

    public Fragment_ready(){
        super();
    }

    @SuppressLint("ValidFragment")
    public Fragment_ready(Context context){
        super();
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_ready, null);

        btn_kick = (Button) view.findViewById(R.id.frag_ready_btn_kick);
        btn_kick_cancel = (Button) view.findViewById(R.id.frag_ready_btn_kick_cancel);
        btn_kick_parent = (LinearLayout) view.findViewById(R.id.frag_ready_btn_parent);
        btn_start = (Button) view.findViewById(R.id.frag_ready_btn_start);
        btn_ready = (Button) view.findViewById(R.id.frag_ready_btn_ready);
        btn_ready_cancel = (Button) view.findViewById(R.id.frag_ready_btn_ready_cancel);
        btn_out = (Button) view.findViewById(R.id.frag_ready_btn_out);

        btn_kick.setVisibility(Button.VISIBLE);
        btn_kick_cancel.setVisibility(Button.GONE);

        btn_kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).showKickButton();
                btn_kick.setVisibility(Button.GONE);
                btn_kick_cancel.setVisibility(Button.VISIBLE);
            }
        });

        btn_kick_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).hideKickButton();
                btn_kick.setVisibility(Button.VISIBLE);
                btn_kick_cancel.setVisibility(Button.GONE);
            }
        });

        btn_kick_parent.setVisibility(LinearLayout.GONE);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).GameStart();
            }
        });

        btn_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ready.setEnabled(false);
                btn_ready_cancel.setEnabled(false);
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).SendReady();
            }
        });

        btn_ready_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ready.setEnabled(false);
                btn_ready_cancel.setEnabled(false);
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).SendReadyCancel();
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).GameOut();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW CHANGE");
        intentFilter.addAction("IS ADMIN");
        intentFilter.addAction("NEW READY");
        intentFilter.addAction("CANCEL READY");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UserNumReceiver, intentFilter);

        return view;
    }

    private BroadcastReceiver UserNumReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            if(data.getAction().equalsIgnoreCase("NEW CHANGE")) {
                UserEnter(data.getIntExtra("USER NUM", 0));
            }
            if(data.getAction().equalsIgnoreCase("IS ADMIN")) {
                ShowKick();
                ShowStart();
            }
            if(data.getAction().equalsIgnoreCase("NEW READY")){
                ISWAITINGFORBUTTONRESULT = false;
                btn_ready.setVisibility(Button.GONE);
                btn_ready_cancel.setVisibility(Button.VISIBLE);
                Handler ahan = new Handler();
                ahan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_ready.setEnabled(true);
                        btn_ready_cancel.setEnabled(true);
                    }
                },1000);
            }
            if(data.getAction().equalsIgnoreCase("CANCEL READY")){
                ISWAITINGFORBUTTONRESULT = false;
                btn_ready_cancel.setVisibility(Button.GONE);
                btn_ready.setVisibility(Button.VISIBLE);
                Handler ahan = new Handler();
                ahan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_ready.setEnabled(true);
                        btn_ready_cancel.setEnabled(true);
                    }
                }, 1000);
            }
        }
    };

    public void UserEnter(int CURRENT_USER){
        if (CURRENT_USER == 1){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.ready_start_i));
                btn_start.setBackgroundResource(R.drawable.ready_start_i);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.ready_start_i));
            }

        } else if (CURRENT_USER == 2){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.ready_start_ii));
                btn_start.setBackgroundResource(R.drawable.ready_start_ii);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.ready_start_ii));
            }
        } else if (CURRENT_USER == 3){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.ready_start_iii));
                btn_start.setBackgroundResource(R.drawable.ready_start_iii);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.ready_start_iii));
            }
        } else if (CURRENT_USER == 4) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.ready_start_iv));
                btn_start.setBackgroundResource(R.drawable.ready_start_iv);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.ready_start_iv));
            }
        } else if (CURRENT_USER == 5) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.ready_start_v));
                btn_start.setBackgroundResource(R.drawable.ready_start_v);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.ready_start_v));
            }
        } else if (CURRENT_USER == 6){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                btn_start.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_frag_ready_selector_start_fin));
                btn_start.setBackgroundResource(R.drawable.btn_frag_ready_selector_start_fin);
            }else{
                btn_start.setBackground(getResources().getDrawable(R.drawable.btn_frag_ready_selector_start_fin));
            }
        }
    }

    public void ShowKick(){
        btn_kick_parent.setVisibility(LinearLayout.VISIBLE);
    }

    public void ShowStart(){
        btn_start.setVisibility(Button.VISIBLE);
        btn_ready.setVisibility(Button.GONE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(UserNumReceiver);
    }

}

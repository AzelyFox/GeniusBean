package kr.devs.geniusbean.room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;
import kr.devs.geniusbean.game.LoadGateActivity;
import kr.devs.geniusbean.utils.DataClass;
import kr.devs.geniusbean.utils.GameViewPager;
import kr.devs.geniusbean.utils.GameViewPagerAdapter;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class WaitingActivity extends FragmentActivity {

    BeanApplication BEANAPP;

    private GameViewPager mViewPager;
    private GameViewPagerAdapter GameAdapter;
    private int ROOM_NUM = 0,ROOM_CHANNEL = 0,ROOM_PW = 0,ROOM_BAT = 0;
    private String ROOM_NAME;
    boolean USER_ISCHIEF = false;

    int USER_I,USER_II,USER_III,USER_IV,USER_V,USER_VI;
    boolean USER_I_STATE,USER_II_STATE,USER_III_STATE,USER_IV_STATE,USER_V_STATE,USER_VI_STATE;
    boolean USER_II_READY,USER_III_READY,USER_IV_READY,USER_V_READY,USER_VI_READY;
    int USER_I_SKIN,USER_II_SKIN,USER_III_SKIN,USER_IV_SKIN,USER_V_SKIN,USER_VI_SKIN;
    String USER_I_NICK,USER_II_NICK,USER_III_NICK,USER_IV_NICK,USER_V_NICK,USER_VI_NICK;
    int USER_ME = 0;

    boolean ISKICKED = false;

    LinearLayout user_box_i,user_box_ii,user_box_iii,user_box_iv,user_box_v,user_box_vi;
    LinearLayout user_ready_ii,user_ready_iii,user_ready_iv,user_ready_v,user_ready_vi;
    TextView user_level_i,user_level_ii,user_level_iii,user_level_iv,user_level_v,user_level_vi;
    ImageView user_kick_ii,user_kick_iii,user_kick_iv,user_kick_v,user_kick_vi;
    TextView user_nick_i,user_nick_ii,user_nick_iii,user_nick_iv,user_nick_v,user_nick_vi;

    TextView DUMMY_CURRENT_MODIFING;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(DataClass.URL_SERVER_SocketUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_waiting);

        BEANAPP = (BeanApplication) getApplication();

        Intent getter = getIntent();
        ROOM_NUM = getter.getExtras().getInt("ROOM_NUM");
        ROOM_CHANNEL = getter.getExtras().getInt("ROOM_CHANNEL");
        USER_ISCHIEF = getter.getExtras().getBoolean("USER_ISCHIEF");

        GameAdapter = new GameViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        mViewPager = (GameViewPager) findViewById(R.id.scr_waiting_pager);
        mViewPager.setAdapter(GameAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);

        setViews();

        hideKickButton();

        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("user chat", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("change ready", onNewReady);
        mSocket.on("new kick",onNewKick);
        mSocket.connect();

        mSocket.emit("add user", BeanApplication.USER_NICK);
        mSocket.emit("subscribe", ROOM_NUM);
        mSocket.emit("in channel", ROOM_CHANNEL);
    }

    @Override
    public void onStart(){
        super.onStart();

        get_DATA_fromROOMNUM(ROOM_NUM, ROOM_CHANNEL);
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  getSupportFragmentManager().findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    public void hideKickButton(){
        user_kick_ii.setVisibility(ImageView.GONE);
        user_kick_iii.setVisibility(ImageView.GONE);
        user_kick_iv.setVisibility(ImageView.GONE);
        user_kick_v.setVisibility(ImageView.GONE);
        user_kick_vi.setVisibility(ImageView.GONE);
    }

    public void showKickButton(){
        if (USER_II_STATE) user_kick_ii.setVisibility(ImageView.VISIBLE);
        if (USER_III_STATE) user_kick_iii.setVisibility(ImageView.VISIBLE);
        if (USER_IV_STATE) user_kick_iv.setVisibility(ImageView.VISIBLE);
        if (USER_V_STATE) user_kick_v.setVisibility(ImageView.VISIBLE);
        if (USER_VI_STATE) user_kick_vi.setVisibility(ImageView.VISIBLE);
    }

    public void GameStart(){
        Intent mover = new Intent(WaitingActivity.this, LoadGateActivity.class);
        startActivity(mover);
        overridePendingTransition(0,0);
        finish();

    }

    public void GameOut(){
        finish();
        overridePendingTransition(0, 0);
    }

    public void SendMessage(String MESSAGE){
        mSocket.emit("new chat", MESSAGE);
    }

    public void SendReady(){
        ready_CHANGE_TASK rCT = new ready_CHANGE_TASK();
        rCT.execute(ROOM_CHANNEL,ROOM_NUM,1);
    }

    public void SendReadyCancel(){
        ready_CHANGE_TASK rCT = new ready_CHANGE_TASK();
        rCT.execute(ROOM_CHANNEL, ROOM_NUM, 0);
    }

    private class ready_CHANGE_TASK extends AsyncTask<Integer, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(Integer... DATA) {
            try{
                String body = "user_channel="+DATA[0]+"&room_num="+DATA[1]+"&user_location="+USER_ME+"&ready_state="+DATA[2];
                URL u = new URL(DataClass.URL_SERVER_ReadyChange);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            ready_DecryptResult(sResult);
        }
    }

    private void ready_DecryptResult(String RESULT){
        if (RESULT.contains("SUCCESS")){
            mSocket.emit("ready change", USER_ME);
            Log.d("BEANAPP","SENT : "+RESULT);
        } else {
            BEANAPP.showDecryptError(RESULT,"","WHEN GETTING READY RESULT");
        }
    }

    private void get_DATA_fromROOMNUM(int ROOM_NUM,int ROOM_CHANNEL){
        get_DATA_TASK gDT = new get_DATA_TASK();
        gDT.execute(Integer.toString(ROOM_CHANNEL), Integer.toString(ROOM_NUM));
    }

    private class get_DATA_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            try{
                String body = "user_channel="+DATA[0]+"&room_num="+DATA[1]+"&user_num="+BeanApplication.USER_NUM;
                URL u = new URL(DataClass.URL_SERVER_UpdateRoom);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            get_decryptResult(sResult);
        }
    }

    private void getSetUserData(int USERNUM,TextView textview_nick){
    //    DUMMY_CURRENT_MODIFING = textview_nick;
        GET_USER_INFO_TASK GUIT = new GET_USER_INFO_TASK();
        GUIT.execute(USERNUM, textview_nick.getId());
    }

    private class GET_USER_INFO_TASK extends AsyncTask<Integer, Void, String> {

        String sResult;
        int MYTEXTVIEW_ID;
        @Override
        protected String doInBackground(Integer... DATA) {
            String respond;

            try{
                String body = "user_num="+Integer.toString(DATA[0]);
                MYTEXTVIEW_ID = DATA[1];
                URL u = new URL(DataClass.URL_SERVER_GetMinInfo);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try {
                GET_USER_INFO_decryptResult(sResult, MYTEXTVIEW_ID);
            } catch (final Exception err){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BEANAPP.showDecryptError(sResult, err.toString(), "GETTING USER INFO - EXCEPTION");
                    }
                });

            }
        }
    }

    private void GET_USER_INFO_decryptResult(String RESULT,int TEXTVIEW_ID) throws JSONException{
        Log.d("BEANAPP",RESULT);
        try {
            JSONArray mJsonArray = new JSONArray(RESULT);
            JSONObject json_data = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++) {
                json_data = mJsonArray.getJSONObject(i);
                TextView mytextview = (TextView) findViewById(TEXTVIEW_ID);
                mytextview.setText(json_data.getString("user_nick"));
            }
        } catch (Exception err) {
            BEANAPP.showDecryptError(RESULT,err.toString(),"GETANDSETTING USERDATA - EXCEPTION");
        }
    }

    private void cleanUserData(){
        user_box_i.setBackgroundResource(R.drawable.waiting_box_out);
        user_box_ii.setBackgroundResource(R.drawable.waiting_box_out);
        user_box_iii.setBackgroundResource(R.drawable.waiting_box_out);
        user_box_iv.setBackgroundResource(R.drawable.waiting_box_out);
        user_box_v.setBackgroundResource(R.drawable.waiting_box_out);
        user_box_vi.setBackgroundResource(R.drawable.waiting_box_out);
        user_level_i.setBackgroundResource(R.drawable.icon_bean);
        user_level_ii.setBackgroundResource(R.drawable.icon_bean);
        user_level_iii.setBackgroundResource(R.drawable.icon_bean);
        user_level_iv.setBackgroundResource(R.drawable.icon_bean);
        user_level_v.setBackgroundResource(R.drawable.icon_bean);
        user_level_vi.setBackgroundResource(R.drawable.icon_bean);
        user_kick_ii.setVisibility(ImageView.GONE);
        user_kick_iii.setVisibility(ImageView.GONE);
        user_kick_iv.setVisibility(ImageView.GONE);
        user_kick_v.setVisibility(ImageView.GONE);
        user_kick_vi.setVisibility(ImageView.GONE);
    //    user_nick_i.setText("");
    //    user_nick_ii.setText("");
    //    user_nick_iii.setText("");
    //    user_nick_iv.setText("");
    //    user_nick_v.setText("");
    //    user_nick_vi.setText("");
        USER_I_STATE = false;
        USER_II_STATE = false;
        USER_III_STATE = false;
        USER_IV_STATE = false;
        USER_V_STATE = false;
        USER_VI_STATE = false;
    }

    private void get_decryptResult(String RESULT){
        cleanUserData();
        if (RESULT == null){
            BEANAPP.showDecryptError(RESULT,"RESULT IS NULL","GETTING DATA - NULLPOINTER EXCEPTION");
            return;
        }
        if(RESULT.contains("ERROR")){
            return;
        }
        try {
            JSONArray mJsonArray = new JSONArray(RESULT);
            JSONObject json_data = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++) {
                json_data = mJsonArray.getJSONObject(i);

                ROOM_NAME = json_data.getString("room_name");
                String ROOM_PW_DUMMY = json_data.getString("room_pw");
                try {
                    ROOM_PW = Integer.valueOf(ROOM_PW_DUMMY);
                } catch (Exception e){ ROOM_PW = 0; }
                int TOTAL_USER = 0;
                ROOM_BAT = json_data.getInt("room_bat");
                USER_I = json_data.getInt("room_user_num_i");
                if (USER_I != 0) {
                    TOTAL_USER++;
                    USER_I_STATE = true;
                    getSetUserData(USER_I,user_nick_i);
                } else {user_nick_i.setText("");}
                USER_II = json_data.getInt("room_user_num_ii");
                if (USER_II != 0) {
                    TOTAL_USER ++;
                    USER_II_STATE = true;
                    getSetUserData(USER_II,user_nick_ii);
                } else {user_nick_ii.setText("");}
                USER_III = json_data.getInt("room_user_num_iii");
                if (USER_III != 0) {
                    TOTAL_USER ++;
                    USER_III_STATE = true;
                    getSetUserData(USER_III, user_nick_iii);
                } else {user_nick_iii.setText("");}
                USER_IV = json_data.getInt("room_user_num_iv");
                if (USER_IV != 0) {
                    TOTAL_USER ++;
                    USER_IV_STATE = true;
                    getSetUserData(USER_IV, user_nick_iv);
                } else {user_nick_iv.setText("");}
                USER_V = json_data.getInt("room_user_num_v");
                if (USER_V != 0) {
                    TOTAL_USER ++;
                    USER_V_STATE = true;
                    getSetUserData(USER_V, user_nick_v);
                } else {user_nick_v.setText("");}
                USER_VI = json_data.getInt("room_user_num_vi");
                if (USER_VI != 0) {
                    TOTAL_USER ++;
                    USER_VI_STATE = true;
                    getSetUserData(USER_VI, user_nick_vi);
                } else {user_nick_vi.setText("");}

                Intent NUMDATA = new Intent("NEW CHANGE");
                NUMDATA.putExtra("USER NUM", TOTAL_USER);
                LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(NUMDATA);

                if (USER_I == BeanApplication.USER_NUM){
                    USER_ME = 1;
                    Intent ADMINDATA = new Intent("IS ADMIN");
                    LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(ADMINDATA);
                    user_level_i.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_i.setBackgroundResource(R.drawable.waiting_box_in);
                }
                if (USER_II == BeanApplication.USER_NUM) {
                    USER_ME = 2;
                    user_level_ii.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_ii.setBackgroundResource(R.drawable.waiting_box_in);
                }
                if (USER_III == BeanApplication.USER_NUM) {
                    USER_ME = 3;
                    user_level_iii.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_iii.setBackgroundResource(R.drawable.waiting_box_in);
                }
                if (USER_IV == BeanApplication.USER_NUM) {
                    USER_ME = 4;
                    user_level_iv.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_iv.setBackgroundResource(R.drawable.waiting_box_in);
                }
                if (USER_V == BeanApplication.USER_NUM) {
                    USER_ME = 5;
                    user_level_v.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_v.setBackgroundResource(R.drawable.waiting_box_in);
                }
                if (USER_VI == BeanApplication.USER_NUM) {
                    USER_ME = 6;
                    user_level_vi.setBackgroundResource(R.drawable.icon_bean_master);
                    user_box_vi.setBackgroundResource(R.drawable.waiting_box_in);
                }

                Log.d("BEANAPP",json_data.toString());
                Log.d("BEANAPP","USER_ME : "+Integer.toString(USER_ME));
                if (json_data.getInt("room_ready_ii") == 1) {
                    USER_II_READY = true;
                    user_ready_ii.setVisibility(LinearLayout.VISIBLE);
                    Log.d("BEANAPP","SETVISIBILITY : VISIBLE");
                    if (USER_ME == 2){
                        Intent READYDATA = new Intent("NEW READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                } else {
                    USER_II_READY = false;
                    user_ready_ii.setVisibility(LinearLayout.INVISIBLE);
                    Log.d("BEANAPP", "SETVISIBILITY : INVISIBLE");
                    if (USER_ME == 2){
                        Intent READYDATA = new Intent("CANCEL READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                }
                if (json_data.getInt("room_ready_iii") == 1) {
                    USER_III_READY = true;
                    user_ready_iii.setVisibility(LinearLayout.VISIBLE);
                    if (USER_ME == 3){
                        Intent READYDATA = new Intent("NEW READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                } else {
                    USER_III_READY = false;
                    user_ready_iii.setVisibility(LinearLayout.INVISIBLE);
                    if (USER_ME == 3){
                        Intent READYDATA = new Intent("CANCEL READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                }
                if (json_data.getInt("room_ready_iv") == 1) {
                    USER_IV_READY = true;
                    user_ready_iv.setVisibility(LinearLayout.VISIBLE);
                    if (USER_ME == 4){
                        Intent READYDATA = new Intent("NEW READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                } else {
                    USER_IV_READY = false;
                    user_ready_iv.setVisibility(LinearLayout.INVISIBLE);
                    if (USER_ME == 4){
                        Intent READYDATA = new Intent("CANCEL READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                }
                if (json_data.getInt("room_ready_v") == 1) {
                    USER_V_READY = true;
                    user_ready_v.setVisibility(LinearLayout.VISIBLE);
                    if (USER_ME == 5){
                        Intent READYDATA = new Intent("NEW READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                } else {
                    USER_V_READY = false;
                    user_ready_v.setVisibility(LinearLayout.INVISIBLE);
                    if (USER_ME == 5){
                        Intent READYDATA = new Intent("CANCEL READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                }
                if (json_data.getInt("room_ready_vi") == 1) {
                    USER_VI_READY = true;
                    user_ready_vi.setVisibility(LinearLayout.VISIBLE);
                    if (USER_ME == 6){
                        Intent READYDATA = new Intent("NEW READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                } else {
                    USER_VI_READY = false;
                    user_ready_vi.setVisibility(LinearLayout.INVISIBLE);
                    if (USER_ME == 6){
                        Intent READYDATA = new Intent("CANCEL READY");
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(READYDATA);
                    }
                }


            }
        } catch (JSONException e){
            BEANAPP.showDecryptError(RESULT,e.toString(),"GETTING DATA - JSONEXCEPTION");
        }
    }

    private void setViews(){
        user_nick_i = (TextView) findViewById(R.id.scr_waiting_name_i);
        user_nick_ii = (TextView) findViewById(R.id.scr_waiting_name_ii);
        user_nick_iii = (TextView) findViewById(R.id.scr_waiting_name_iii);
        user_nick_iv = (TextView) findViewById(R.id.scr_waiting_name_iv);
        user_nick_v = (TextView) findViewById(R.id.scr_waiting_name_v);
        user_nick_vi = (TextView) findViewById(R.id.scr_waiting_name_vi);
        user_box_i = (LinearLayout) findViewById(R.id.scr_waiting_box_i);
        user_box_ii = (LinearLayout) findViewById(R.id.scr_waiting_box_ii);
        user_box_iii = (LinearLayout) findViewById(R.id.scr_waiting_box_iii);
        user_box_iv = (LinearLayout) findViewById(R.id.scr_waiting_box_iv);
        user_box_v = (LinearLayout) findViewById(R.id.scr_waiting_box_v);
        user_box_vi = (LinearLayout) findViewById(R.id.scr_waiting_box_vi);
        user_ready_ii = (LinearLayout) findViewById(R.id.scr_waiting_ready_ii);
        user_ready_iii = (LinearLayout) findViewById(R.id.scr_waiting_ready_iii);
        user_ready_iv = (LinearLayout) findViewById(R.id.scr_waiting_ready_iv);
        user_ready_v = (LinearLayout) findViewById(R.id.scr_waiting_ready_v);
        user_ready_vi = (LinearLayout) findViewById(R.id.scr_waiting_ready_vi);
        user_level_i = (TextView) findViewById(R.id.scr_waiting_level_i);
        user_level_ii = (TextView) findViewById(R.id.scr_waiting_level_ii);
        user_level_iii = (TextView) findViewById(R.id.scr_waiting_level_iii);
        user_level_iv = (TextView) findViewById(R.id.scr_waiting_level_iv);
        user_level_v = (TextView) findViewById(R.id.scr_waiting_level_v);
        user_level_vi = (TextView) findViewById(R.id.scr_waiting_level_vi);
        user_kick_ii = (ImageView) findViewById(R.id.scr_waiting_kick_ii);
        user_kick_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kick_User(2);
            }
        });
        user_kick_ii.setVisibility(ImageView.GONE);
        user_kick_iii = (ImageView) findViewById(R.id.scr_waiting_kick_iii);
        user_kick_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kick_User(3);
            }
        });
        user_kick_iii.setVisibility(ImageView.GONE);
        user_kick_iv = (ImageView) findViewById(R.id.scr_waiting_kick_iv);
        user_kick_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kick_User(4);
            }
        });
        user_kick_iv.setVisibility(ImageView.GONE);
        user_kick_v = (ImageView) findViewById(R.id.scr_waiting_kick_v);
        user_kick_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kick_User(5);
            }
        });
        user_kick_v.setVisibility(ImageView.GONE);
        user_kick_vi = (ImageView) findViewById(R.id.scr_waiting_kick_vi);
        user_kick_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kick_User(6);
            }
        });
        user_kick_vi.setVisibility(ImageView.GONE);
        USER_I_STATE = false;
        USER_II_STATE = false;
        USER_III_STATE = false;
        USER_IV_STATE = false;
        USER_V_STATE = false;
        USER_VI_STATE = false;
        user_nick_i.setText("");
        user_nick_ii.setText("");
        user_nick_iii.setText("");
        user_nick_iv.setText("");
        user_nick_v.setText("");
        user_nick_vi.setText("");
        user_ready_ii.setVisibility(LinearLayout.INVISIBLE);
        user_ready_iii.setVisibility(LinearLayout.INVISIBLE);
        user_ready_iv.setVisibility(LinearLayout.INVISIBLE);
        user_ready_v.setVisibility(LinearLayout.INVISIBLE);
        user_ready_vi.setVisibility(LinearLayout.INVISIBLE);
        Log.d("BEANAPP", "SETVISIBILITY : INVISIBLE");
    }

    private void Kick_User(int POSITION){
        mSocket.emit("new kick",POSITION);
        kick_TASK kT = new kick_TASK();
        kT.execute(Integer.toString(ROOM_CHANNEL), Integer.toString(ROOM_NUM), Integer.toString(POSITION));
    }

    private class kick_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            try{
                String body = "user_channel="+DATA[0]+"&user_roomnum="+DATA[1]+"&user_usernum="+BeanApplication.USER_NUM+"&kick_usernum="+Integer.parseInt(DATA[2]);
                URL u = new URL(DataClass.URL_SERVER_KickUser);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    private class exit_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            try{
                String body = "user_channel="+DATA[0]+"&room_num="+DATA[1]+"&user_num="+BeanApplication.USER_NUM+"&user_location="+USER_ME;
                URL u = new URL(DataClass.URL_SERVER_ExitRoom);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SweetAlertDialog(WaitingActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.waiting_error_socket))
                            .setContentText(getResources().getString(R.string.waiting_error_socket_content))
                            .setConfirmText(getResources().getString(R.string.waiting_error_socket_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");

                        Intent CHATDATA = new Intent("NEW CHAT");
                        CHATDATA.putExtra("CHAT FROM", username);
                        CHATDATA.putExtra("CHAT MESSAGE", message);
                        CHATDATA.putExtra("CHAT TYPE",1);
                        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(CHATDATA);
                    } catch (JSONException e) {
                        BEANAPP.showDecryptError(data.toString(),e.toString(),"LISTENED ONNEWMESSAGE");
                        return;
                    }

                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    get_DATA_fromROOMNUM(ROOM_NUM,ROOM_CHANNEL);
                    Intent CHATDATA = new Intent("NEW CHAT");
                    CHATDATA.putExtra("CHAT FROM", "");
                    CHATDATA.putExtra("CHAT MESSAGE", getResources().getString(R.string.chat_system_user_enter));
                    CHATDATA.putExtra("CHAT TYPE",0);
                    LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(CHATDATA);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    get_DATA_fromROOMNUM(ROOM_NUM,ROOM_CHANNEL);
                    Intent CHATDATA = new Intent("NEW CHAT");
                    CHATDATA.putExtra("CHAT FROM", "");
                    CHATDATA.putExtra("CHAT MESSAGE", getResources().getString(R.string.chat_system_user_quit));
                    CHATDATA.putExtra("CHAT TYPE",0);
                    LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(CHATDATA);
                }
            });
        }
    };

    private Emitter.Listener onNewReady = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    get_DATA_fromROOMNUM(ROOM_NUM,ROOM_CHANNEL);
                }
            });
        }
    };

    private Emitter.Listener onNewKick = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String Kicknum;
                    try {
                        Kicknum = data.getString("kicknum");

                        int KICKNUM = Integer.parseInt(Kicknum);
                        if (KICKNUM == USER_ME) {
                            Toast.makeText(WaitingActivity.this, getResources().getString(R.string.waiting_user_kicked)+"\nMYNUM="+Integer.toString(USER_ME)+" KICKNUM="+Integer.toString(KICKNUM), Toast.LENGTH_LONG).show();
                            ISKICKED = true;
                            finish();
                        } else {
                            get_DATA_fromROOMNUM(ROOM_NUM, ROOM_CHANNEL);
                        }

                    } catch (JSONException e) {
                        BEANAPP.showDecryptError(data.toString(),e.toString(),"LISTENED ONNEWKICK");
                        return;
                    }
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!ISKICKED) {
            exit_TASK eT = new exit_TASK();
            eT.execute(Integer.toString(ROOM_CHANNEL), Integer.toString(ROOM_NUM));
        }

        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("user chat", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("change ready", onNewReady);
        mSocket.off("new kick",onNewKick);
        mSocket.disconnect();

        Intent REDATA = new Intent("ROOM QUIT");
        LocalBroadcastManager.getInstance(WaitingActivity.this).sendBroadcast(REDATA);

    }

    @Override
    public void onResume(){
        super.onResume();
        BEANAPP.playMusic(3,DataClass.FILE_MUSIC_WAITING);
    }

    @Override
    public void onStop(){
        super.onStop();
        BEANAPP.stopMusic(3);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText(getResources().getString(R.string.waiting_quit));
                pDialog.setContentText(getResources().getString(R.string.waiting_quit_content));
                pDialog.setCancelText(getResources().getString(R.string.waiting_quit_no));
                pDialog.setConfirmText(getResources().getString(R.string.waiting_quit_yes));
                pDialog.showCancelButton(true);
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                });
                pDialog.show();
            break;
        }
        return false;
    }

}

package kr.devs.geniusbean.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;
import kr.devs.geniusbean.utils.DataClass;
import kr.devs.geniusbean.utils.FloatingEditText;
import kr.devs.geniusbean.utils.HoloGraph.PieGraph;
import kr.devs.geniusbean.utils.HoloGraph.PieSlice;
import kr.devs.geniusbean.utils.PaperButton;

public class MenuInfoActivity extends Activity {

    BeanApplication BEANAPP;
    ImageView menu_btn_i,menu_btn_ii,menu_btn_iii,menu_btn_iv,menu_btn_v,menu_btn_vi;
    TextView tv_nickname,tv_hello,tv_grade,tv_rating,tv_level,tv_exp,tv_money,tv_cash,tv_played,tv_win,tv_lose;
    ImageView image_grade,image_bean_background;
    PieGraph statistics_graph;
    AlertDialog helloDialog;
    SweetAlertDialog pDialog;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_menu_info);

        BEANAPP = (BeanApplication) getApplication();

        setViewIDs();
        setDefaultValue();
        setListeners();
        getInfo();

    }

    private void setViewIDs(){
        menu_btn_i = (ImageView) findViewById(R.id.scr_menu_info_menu_i);
        menu_btn_ii = (ImageView) findViewById(R.id.scr_menu_info_menu_ii);
        menu_btn_iii = (ImageView) findViewById(R.id.scr_menu_info_menu_iii);
        menu_btn_iv = (ImageView) findViewById(R.id.scr_menu_info_menu_iv);
        menu_btn_v = (ImageView) findViewById(R.id.scr_menu_info_menu_v);
        menu_btn_vi = (ImageView) findViewById(R.id.scr_menu_info_menu_vi);
        tv_nickname = (TextView) findViewById(R.id.scr_menu_info_nickname);
        tv_hello = (TextView) findViewById(R.id.scr_menu_info_hello);
        tv_grade = (TextView) findViewById(R.id.scr_menu_info_grade_name);
        tv_rating = (TextView) findViewById(R.id.scr_menu_info_rating);
        tv_level = (TextView) findViewById(R.id.scr_menu_info_level);
        tv_exp = (TextView) findViewById(R.id.scr_menu_info_exp);
        tv_money = (TextView) findViewById(R.id.scr_menu_info_money);
        tv_cash = (TextView) findViewById(R.id.scr_menu_info_gold);
        tv_played = (TextView) findViewById(R.id.scr_menu_info_played);
        tv_win = (TextView) findViewById(R.id.scr_menu_info_win);
        tv_lose = (TextView) findViewById(R.id.scr_menu_info_lose);
        image_grade = (ImageView) findViewById(R.id.scr_menu_info_grade);
        image_bean_background = (ImageView) findViewById(R.id.scr_menu_info_profile_box);
        statistics_graph = (PieGraph)findViewById(R.id.scr_menu_info_graph);
    }

    private void setDefaultValue(){
        tv_nickname.setText(BeanApplication.USER_NICK);
        if (BeanApplication.USER_HELLO == null || BeanApplication.USER_HELLO.length() < 1){
            tv_hello.setText(getResources().getString(R.string.menu_info_hello));
        } else {
            tv_hello.setText(BeanApplication.USER_HELLO);
        }
        if (BeanApplication.USER_GRADE == 1) {
            tv_grade.setText(getResources().getString(R.string.grade_i));
            image_grade.setImageResource(R.drawable.grade_red);
        }
        if (BeanApplication.USER_GRADE == 2) {
            tv_grade.setText(getResources().getString(R.string.grade_ii));
            image_grade.setImageResource(R.drawable.grade_yellow);
        }
        if (BeanApplication.USER_GRADE == 3) {
            tv_grade.setText(getResources().getString(R.string.grade_iii));
            image_grade.setImageResource(R.drawable.grade_green);
        }
        if (BeanApplication.USER_GRADE == 4) {
            tv_grade.setText(getResources().getString(R.string.grade_iv));
            image_grade.setImageResource(R.drawable.grade_blue);
        }
        if (BeanApplication.USER_GRADE == 5) {
            tv_grade.setText(getResources().getString(R.string.grade_v));
            image_grade.setImageResource(R.drawable.grade_purple);
        }
        tv_rating.setText(Integer.toString(BeanApplication.USER_RATING));
        tv_level.setText(Html.fromHtml("<b>" + "LV. " + "</b>" + "<font color=#6799FF><b>" + Integer.toString(BeanApplication.USER_LV) + "</b></font>"));
        tv_exp.setText(Integer.toString(BeanApplication.USER_EXP));
        tv_money.setText(Integer.toString(BeanApplication.USER_MONEY));
        tv_cash.setText(Integer.toString(BeanApplication.USER_CASH));
        tv_played.setText(Html.fromHtml("<b>" + getResources().getString(R.string.menu_info_statistics_played) + "</b>" + "<b><font color=#0184FF> " + Integer.toString(BeanApplication.USER_PLAYED) + "</font></b>"));
        tv_win.setText(Integer.toString(BeanApplication.USER_WIN));
        tv_win.setTextColor(Color.parseColor("#99CC00"));
        tv_lose.setText(Integer.toString(BeanApplication.USER_LOSE));
        tv_lose.setTextColor(Color.parseColor("#FFBB33"));

        statistics_graph.removeSlices();
        PieSlice slice_ii = new PieSlice();
        slice_ii.setColor(Color.parseColor("#FFBB33"));
        slice_ii.setValue(BeanApplication.USER_LOSE);
        statistics_graph.addSlice(slice_ii);
        PieSlice slice_i = new PieSlice();
        slice_i.setColor(Color.parseColor("#99CC00"));
        slice_i.setValue(BeanApplication.USER_WIN);
        statistics_graph.addSlice(slice_i);
    }

    private void setListeners(){
        menu_btn_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuInfoActivity.this,MenuFriendActivity.class);
                startActivity(mover);
                overridePendingTransition(0,0);
                finish();
            }
        });
        menu_btn_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuInfoActivity.this, MenuGuildActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuInfoActivity.this, MenuNoticeActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuInfoActivity.this,MenuMarketActivity.class);
                startActivity(mover);
                overridePendingTransition(0,0);
                finish();
            }
        });
        menu_btn_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuInfoActivity.this, MenuSettingActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        tv_hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelloChanger();
            }
        });
    }

    private void showHelloChanger(){
        AlertDialog.Builder dialogbuilder = null;
        View gateView;
        final FloatingEditText edit_hello;
        PaperButton btn_change,btn_cancel;

        LayoutInflater inflater = LayoutInflater.from(this);
        gateView = inflater.inflate(R.layout.dialog_hello_changer, null);
        edit_hello = (FloatingEditText) gateView.findViewById(R.id.dialog_hello_edittext);
        btn_change = (PaperButton) gateView.findViewById(R.id.dialog_hello_btn);
        btn_cancel = (PaperButton) gateView.findViewById(R.id.dialog_hello_cancel);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_HELLO;
                try {
                    USER_HELLO = edit_hello.getText().toString();
                } catch (NullPointerException err){
                    USER_HELLO = "";
                }
                UPDATE_HELLO_TASK UHT = new UPDATE_HELLO_TASK();
                UHT.execute(Integer.toString(BeanApplication.USER_NUM),USER_HELLO);
                helloDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helloDialog.dismiss();
            }
        });

        dialogbuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        dialogbuilder.setView(gateView);
        helloDialog = dialogbuilder.create();
        helloDialog.setView(gateView, 0, 0, 0, 0);
        helloDialog.setInverseBackgroundForced(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(helloDialog.getWindow().getAttributes());
        lp.height = 300;
        lp.width = 550;
        helloDialog.getWindow().setAttributes(lp);
        helloDialog.show();
    }

    private class UPDATE_HELLO_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            String respond;

            try{
                String body = "user_num="+DATA[0]+"&user_hello="+DATA[1];
                URL u = new URL(DataClass.URL_SERVER_UpdateHello);
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
            super.onPostExecute ( result );
            try {
                hello_decryptResult(sResult);
            }catch (final NullPointerException err){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BEANAPP.showDecryptError(sResult, err.toString(), "GETTING ROOM - NULLPOINTER EXCEPTION");
                    }
                });

            }
        }
    }

    private void hello_decryptResult(String RESULT) throws NullPointerException{
        if (RESULT.contains("SUCCESS")){
            new SweetAlertDialog(MenuInfoActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.menu_info_dialog_ok_title))
                    .setContentText(getResources().getString(R.string.menu_info_dialog_ok_content))
                    .setConfirmText(getResources().getString(R.string.menu_info_dialog_ok_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        } else {
            new SweetAlertDialog(MenuInfoActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.menu_info_dialog_error_title))
                    .setContentText(getResources().getString(R.string.menu_info_dialog_error_content))
                    .setConfirmText(getResources().getString(R.string.menu_info_dialog_error_confirm))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();
        }
        getInfo();
    }

    private void getInfo(){
        GET_INFO_TASK GIT = new GET_INFO_TASK();
        GIT.execute();
    }

    private class GET_INFO_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            String respond;

            try{
                String body = "user_num="+BeanApplication.USER_NUM;
                URL u = new URL(DataClass.URL_SERVER_GetInfo);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf8"));
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
            super.onPostExecute ( result );
            GET_decryptResult(sResult);
        }
    }

    private void GET_decryptResult(String RESULT){
        try {
            JSONArray mJsonArray = new JSONArray(RESULT);
            JSONObject json_data = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++) {
                json_data = mJsonArray.getJSONObject(i);
                BeanApplication.USER_CLASS = json_data.getInt("user_class");
                BeanApplication.USER_LV = json_data.getInt("user_lv");
                BeanApplication.USER_EXP = json_data.getInt("user_exp");
                BeanApplication.USER_MONEY = json_data.getInt("user_money");
                BeanApplication.USER_CASH = json_data.getInt("user_cash");
                BeanApplication.USER_PLAYED = json_data.getInt("user_played");
                BeanApplication.USER_WIN = json_data.getInt("user_win");
                BeanApplication.USER_LOSE = json_data.getInt("user_lose");
                BeanApplication.USER_GRADE = json_data.getInt("user_grade");
                BeanApplication.USER_RATING = json_data.getInt("user_rating");
                BeanApplication.USER_SKIN = json_data.getInt("user_skin");
                BeanApplication.USER_GUILD = json_data.getInt("user_guild");
                BeanApplication.USER_HELLO = json_data.getString("user_hello");
                setDefaultValue();
            }
        } catch (Exception err){
            BEANAPP.showDecryptError(RESULT,err.toString(),"GETTING MENU INFO - EXCEPTION");
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        BEANAPP.playMusicfromRaw(4, R.raw.bluetime);
    }

    @Override
    public void onStop(){
        super.onStop();
    //    BEANAPP.stopMusic(4);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SweetAlertDialog pDialog = new SweetAlertDialog(MenuInfoActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText(getResources().getString(R.string.main_menu_alert_back_title));
                pDialog.setContentText(getResources().getString(R.string.main_menu_alert_back_content));
                pDialog.setCancelText(getResources().getString(R.string.main_menu_alert_back_cancel));
                pDialog.setConfirmText(getResources().getString(R.string.main_menu_alert_back_confirm));
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
                        BEANAPP.stopMusic(4);
                        finish();
                        overridePendingTransition(0,0);
                    }
                });
                pDialog.show();
                break;
        }
        return false;
    }
}

package kr.devs.geniusbean.door;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;
import kr.devs.geniusbean.other.MenuFriendActivity;
import kr.devs.geniusbean.other.MenuGuildActivity;
import kr.devs.geniusbean.other.MenuInfoActivity;
import kr.devs.geniusbean.other.MenuMarketActivity;
import kr.devs.geniusbean.other.MenuNoticeActivity;
import kr.devs.geniusbean.other.MenuSettingActivity;
import kr.devs.geniusbean.room.WaitingActivity;
import kr.devs.geniusbean.utils.CheckBox;
import kr.devs.geniusbean.utils.DataClass;
import kr.devs.geniusbean.utils.FloatingEditText;
import kr.devs.geniusbean.utils.PaperButton;
import kr.devs.geniusbean.utils.RoomListData;
import kr.devs.geniusbean.utils.RoomListViewAdapter;
import kr.devs.geniusbean.utils.RssFeed;
import kr.devs.geniusbean.utils.RssItem;
import kr.devs.geniusbean.utils.RssReader;
import me.grantland.widget.AutofitTextView;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends Activity {

    RelativeLayout layout_register,layout_channel,layout_room;
    LinearLayout layout_content,layout_feature;

    BeanApplication BEANAPP;

    //  TOP
    ImageView top_type;
    TextView top_news;
    Timer timer_news;
    int CURRENT_NEWS = 0;
    int MAX_NEWS = 4;

    //  PROFILE
    ImageView profile_btn_i,profile_btn_ii,profile_btn_iii,profile_btn_iv,profile_btn_v,profile_btn_vi;
    ImageView profile_box;
    GifImageView profile_bean;

    //  BAR
    ImageView bar_grade_icon;
    TextView bar_grade_text,bar_money_game,bar_money_cash;
    AutofitTextView bar_guild,bar_nickname;

    //  REGISTER
    FloatingEditText register_edittext;
    TextView register_textview_top,register_textview_check_i,register_textview_check_ii;
    CheckBox register_checkbox_i,register_checkbox_ii;
    PaperButton register_button;
    SweetAlertDialog register_dialog;
    boolean CHECKED_I = false,CHECKED_II = false;

    //  CHANNEL
    LinearLayout channel_list_i,channel_list_ii,channel_list_iii,channel_list_iv,channel_list_v;

    //  ROOM
    boolean ISROOMSHOWING = false;
    ImageView room_image_channel,room_image_plus,room_image_question,room_image_chat,room_image_ranking;
    int CURRENTC_CHANNEL = 0;

    //  ROOM - LIST
    ListView room_list;
    LinearLayout room_parent,room_nothing;
    ImageView room_scrollbar;
    RoomListViewAdapter room_list_adapter = null;

    //  ROOM - GATE
    int ENTERING_ROOMNUM;
    AlertDialog gateDialog;

    //  FEATURES
    boolean ISFEATURESHOWING = false;
    LinearLayout feature_box_create,feature_box_question,feature_box_chat,feature_box_ranking;
    ImageView feature_icon,feature_exit;
    TextView feature_title;

    //  FEATURE - CREATE
    FloatingEditText feature_create_editText_name,feature_create_editText_pw,feature_create_editText_bat;
    CheckBox feature_create_checkBox_pw,feature_create_checkBox_bat;
    PaperButton feature_create_button;
    SweetAlertDialog feature_create_dialog;

    boolean ISPAUSED = false;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_main);

        BEANAPP = (BeanApplication) getApplication();

        layout_register = (RelativeLayout) findViewById(R.id.scr_main_register);
        layout_channel = (RelativeLayout) findViewById(R.id.scr_main_channel);
        layout_room = (RelativeLayout) findViewById(R.id.scr_main_room);

        layout_register.setVisibility(RelativeLayout.GONE);
        layout_channel.setVisibility(RelativeLayout.VISIBLE);
        layout_room.setVisibility(RelativeLayout.GONE);

        if(BeanApplication.USER_NEW){
            layout_register.setVisibility(RelativeLayout.VISIBLE);
            layout_channel.setVisibility(RelativeLayout.GONE);
            layout_room.setVisibility(RelativeLayout.GONE);
            register_edittext = (FloatingEditText) findViewById(R.id.scr_main_register_edittext);
            register_textview_top = (TextView) findViewById(R.id.scr_main_register_text_top);
            register_textview_check_i = (TextView) findViewById(R.id.scr_main_register_checkbox_i_tv);
            register_textview_check_ii = (TextView) findViewById(R.id.scr_main_register_checkbox_ii_tv);
            register_checkbox_i = (CheckBox) findViewById(R.id.scr_main_register_checkbox_i);
            register_checkbox_ii = (CheckBox) findViewById(R.id.scr_main_register_checkbox_ii);
            register_button = (PaperButton) findViewById(R.id.scr_main_register_button);
            REG_setForRegister();
        } else {
            BAR_setForBar();
            BAR_getForBar();
        }

        PRO_setForProfile();

        CHA_setForChannel();
        FEA_setForFeature();
        TOP_getNews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ROOM QUIT");
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(QuitReceiver, intentFilter);

    }

    private void PRO_setForProfile(){
        profile_bean = (GifImageView) findViewById(R.id.scr_main_profile_bean);
        profile_box = (ImageView) findViewById(R.id.scr_main_profile_box);
        profile_btn_i = (ImageView) findViewById(R.id.scr_main_profile_btn_i);
        profile_btn_ii = (ImageView) findViewById(R.id.scr_main_profile_btn_ii);
        profile_btn_iii = (ImageView) findViewById(R.id.scr_main_profile_btn_iii);
        profile_btn_iv = (ImageView) findViewById(R.id.scr_main_profile_btn_iv);
        profile_btn_v = (ImageView) findViewById(R.id.scr_main_profile_btn_v);
        profile_btn_vi = (ImageView) findViewById(R.id.scr_main_profile_btn_vi);
        profile_btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuInfoActivity.class);
                PRO_showFeature(mover);
            }
        });
        profile_btn_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuFriendActivity.class);
                PRO_showFeature(mover);
            }
        });
        profile_btn_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuGuildActivity.class);
                PRO_showFeature(mover);
            }
        });
        profile_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuNoticeActivity.class);
                PRO_showFeature(mover);
            }
        });
        profile_btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuMarketActivity.class);
                PRO_showFeature(mover);
            }
        });
        profile_btn_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MainActivity.this, MenuSettingActivity.class);
                PRO_showFeature(mover);
            }
        });
    }

    private void PRO_showFeature(Intent intent){
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void BAR_setForBar(){
        bar_grade_icon = (ImageView) findViewById(R.id.scr_main_bar_grade_icon);
        bar_grade_text = (TextView) findViewById(R.id.scr_main_bar_grade_text);
        bar_guild = (AutofitTextView) findViewById(R.id.scr_main_bar_guild);
        bar_nickname = (AutofitTextView) findViewById(R.id.scr_main_bar_nickname);
        bar_money_game = (TextView) findViewById(R.id.scr_main_bar_money);
        bar_money_cash = (TextView) findViewById(R.id.scr_main_bar_cash);
    }

    private void BAR_getForBar(){
        BAR_TASK BT = new BAR_TASK();
        BT.execute();
    }

    private class BAR_TASK extends AsyncTask<String, Void, String> {

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
            BAR_decryptResult(sResult);
        }
    }

    private void BAR_decryptResult(String RESULT){
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
                BAR_setToBar();
            }
        } catch (Exception err){
            BEANAPP.showDecryptError(RESULT,err.toString(),"GETTING BAR - EXCEPTION");
        }
    }

    private void BAR_setToBar(){
        if (BeanApplication.USER_GRADE == 1){
            bar_grade_icon.setImageResource(R.drawable.grade_red);
        } else if (BeanApplication.USER_GRADE == 2){
            bar_grade_icon.setImageResource(R.drawable.grade_yellow);
        } else if (BeanApplication.USER_GRADE == 3){
            bar_grade_icon.setImageResource(R.drawable.grade_green);
        } else if (BeanApplication.USER_GRADE == 4){
            bar_grade_icon.setImageResource(R.drawable.grade_blue);
        } else if (BeanApplication.USER_GRADE == 5){
            bar_grade_icon.setImageResource(R.drawable.grade_purple);
        }
        bar_grade_text.setText(Integer.toString(BeanApplication.USER_RATING));
        if (BeanApplication.USER_GUILD == 0) bar_guild.setText(getResources().getString(R.string.main_bar_guild_none));
        else bar_guild.setText(Integer.toString(BeanApplication.USER_SKIN));

        bar_nickname.setText(BeanApplication.USER_NICK);
        bar_money_game.setText(Integer.toString(BeanApplication.USER_MONEY));
        bar_money_cash.setText(Integer.toString(BeanApplication.USER_CASH));
    }

    private void TOP_setForTop(){
        top_type = (ImageView) findViewById(R.id.scr_main_top_type);
        top_news = (TextView) findViewById(R.id.scr_main_top_news);
    }

    private void TOP_getNews(){
        TOP_setForTop();
        NEWS_TASK NT = new NEWS_TASK();
        NT.execute();
    }

    private class NEWS_TASK extends AsyncTask<String, Void, String> {

        String sURL;
        @Override
        protected String doInBackground(String... sId) {
            try {
                URL url = new URL(DataClass.URL_SERVER_News);
                RssFeed feed = RssReader.read(url);

                ArrayList<RssItem> rssItems = feed.getRssItems();
                timer_news = new Timer();
                TOPTASK TT = new TOPTASK();
                TT.rssItems = rssItems;
                timer_news.scheduleAtFixedRate(TT,0,3500);

            } catch (final IOException err){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        err.printStackTrace();
                        BEANAPP.showDecryptError(DataClass.URL_SERVER_News, err.toString()+err.getLocalizedMessage(), "GETTING NEWS - ERROR - IOEXCEPTION");
                    }
                });
            } catch (final SAXException err) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        err.printStackTrace();
                        BEANAPP.showDecryptError(DataClass.URL_SERVER_News, err.toString(), "GETTING NEWS - ERROR - SAXEXCEPTION");
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    class TOPTASK extends TimerTask {
        ArrayList<RssItem> rssItems;
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CURRENT_NEWS++;
                    if (CURRENT_NEWS == MAX_NEWS ) CURRENT_NEWS = 0;
                    YoYo.with(Techniques.FadeOutUp).duration(700).playOn(top_news);
                    YoYo.with(Techniques.FadeOutUp).duration(700).playOn(top_type);
                    Handler ahan = new Handler();
                    ahan.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String NEWS_TITLE_DUMMY = rssItems.get(CURRENT_NEWS).getTitle();
                            String NEWS_TITLE = NEWS_TITLE_DUMMY.substring(NEWS_TITLE_DUMMY.indexOf("]") + 1, NEWS_TITLE_DUMMY.length()).trim();
                            String NEWS_TYPE = NEWS_TITLE_DUMMY.substring(NEWS_TITLE_DUMMY.indexOf("[") + 1, NEWS_TITLE_DUMMY.indexOf("]"));
                            top_news.setText(NEWS_TITLE);
                            if(NEWS_TYPE.contains("이벤트")){
                                top_type.setImageResource(R.drawable.icon_news_type_event);
                            } else if(NEWS_TYPE.contains("점검")){
                                top_type.setImageResource(R.drawable.icon_news_type_fix);
                            } else if(NEWS_TYPE.contains("업데이트")){
                                top_type.setImageResource(R.drawable.icon_news_type_update);
                            } else {
                                top_type.setImageResource(R.drawable.icon_news_type_notice);
                            }
                            YoYo.with(Techniques.FadeInUp).duration(700).playOn(top_news);
                            YoYo.with(Techniques.FadeInUp).duration(700).playOn(top_type);
                        }
                    },500);
                }
            });
        }
    }

    private void REG_setForRegister(){
        register_textview_top.setText(BeanApplication.USER_EMAIL);
        register_textview_check_i.setText(Html.fromHtml("<font color=#6799FF><u>"+getResources().getString(R.string.main_register_check_i)+"</u></font>"+getResources().getString(R.string.main_register_check_agree)));
        register_textview_check_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REG_showPopup(0);
            }
        });
        register_textview_check_ii.setText(Html.fromHtml("<font color=#6799FF><u>" + getResources().getString(R.string.main_register_check_ii) + "</u></font>" + getResources().getString(R.string.main_register_check_agree)));
        register_textview_check_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REG_showPopup(1);
            }
        });
        register_edittext.setNormalColor(Color.parseColor("#222222"));
        register_button.setVisibility(PaperButton.GONE);
        register_checkbox_i.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CHECKED_I = b;
                register_button.setVisibility(PaperButton.GONE);
                if (CHECKED_I && CHECKED_II) {
                    register_button.setVisibility(PaperButton.VISIBLE);
                }
            }
        });
        register_checkbox_ii.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CHECKED_II = b;
                register_button.setVisibility(PaperButton.GONE);
                if (CHECKED_I && CHECKED_II) {
                    register_button.setVisibility(PaperButton.VISIBLE);
                }
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_NICK = register_edittext.getText().toString();
                REG_tryToRegister(USER_NICK);
            }
        });
        register_edittext.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                register_edittext.setHighlightedColor(Color.BLUE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void REG_showPopup(int TYPE){
        String POPUP_TEXT = null;
        InputStream inputStream = null;
        String file_path = "";
        if (TYPE == 0) file_path = Environment.getExternalStorageDirectory()+DataClass.BEAN_FOLDER+DataClass.FILE_TEXT_PRIVATEDATA;
        if (TYPE == 1) file_path = Environment.getExternalStorageDirectory()+DataClass.BEAN_FOLDER+DataClass.FILE_TEXT_GAMESERVICE;
        try {
            File f_path = new File(file_path);
            inputStream = new BufferedInputStream(new FileInputStream(f_path));
        } catch (Exception err) { POPUP_TEXT = getResources().getString(R.string.main_register_check_raw_error); }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String readLine;
            StringBuffer buf = new StringBuffer();

            while((readLine = reader.readLine()) != null){
                buf.append("\n"+readLine);
            }
            POPUP_TEXT = buf.toString();

        } catch (Exception err){
            POPUP_TEXT = getResources().getString(R.string.main_register_check_raw_error);
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popView = inflater.inflate(R.layout.dialog_register_checker, (ViewGroup) findViewById(R.id.dialog_register_check_popup));
        TextView popText = (TextView) popView.findViewById(R.id.dialog_register_check_text);
        popText.setText(POPUP_TEXT);
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(MainActivity.this);

        if (TYPE == 0) popDialog.setTitle(getResources().getString(R.string.main_register_check_i));
        if (TYPE == 1) popDialog.setTitle(getResources().getString(R.string.main_register_check_ii));
        popDialog.setView(popView);

        popDialog.setNegativeButton(getResources().getString(R.string.main_register_check_popup_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog popupDialog = popDialog.create();
        popupDialog.show();

    }

    private void REG_tryToRegister(String NICK){
        register_dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        register_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        register_dialog.setTitleText(getResources().getString(R.string.main_register_dialog_doing));
        register_dialog.setCancelable(false);
        register_dialog.show();
        register_button.setEnabled(false);
        if(NICK.length()<1){
            REG_handleResult(false, getResources().getString(R.string.main_register_error_empty));
        } else if(NICK.length()>8){
            REG_handleResult(false,getResources().getString(R.string.main_register_error_long));
        } else {
            REG_sendNICK(NICK);
        }
    }

    private void REG_handleResult(boolean SUCCESS,String MESSAGE){
        register_dialog.dismiss();
        if(!SUCCESS){
            register_button.setEnabled(true);
            register_edittext.setHighlightedColor(Color.RED);
            register_edittext.setValidateResult(false,MESSAGE);
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getResources().getString(R.string.main_register_success))
                    .setContentText(getResources().getString(R.string.main_register_success_content))
                    .show();
            layout_register.setVisibility(RelativeLayout.GONE);
            layout_channel.setVisibility(RelativeLayout.VISIBLE);
            layout_room.setVisibility(RelativeLayout.GONE);
            BAR_setForBar();
            BAR_getForBar();
            CHA_setForChannel();
        }
    }


    private void REG_sendNICK(String NICK){
        REG_TASK RT = new REG_TASK();
        RT.execute(NICK);
    }

    private class REG_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            String respond;

            try{
                String body = "user_email="+BeanApplication.USER_EMAIL+"&user_nick="+sId[0]+"&user_sobid="+BeanApplication.USER_SOBID;
                URL u = new URL(DataClass.URL_SERVER_Register);
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
            REG_decryptResult(sResult);
        }
    }

    private void REG_decryptResult(final String RESULT){
        Handler ahan = new Handler();
        ahan.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (RESULT.contains("&SUCCESS&")) {
                        REG_handleResult(true, null);
                    } else if (RESULT.contains("&DUPLICATE&")) {
                        REG_handleResult(false, getResources().getString(R.string.main_register_error_duplicate));
                    } else {
                        BEANAPP.showDecryptError(RESULT,"","REGISTERING - UNKNOWN");
                        REG_handleResult(false, getResources().getString(R.string.unknown_error));
                    }
                } catch (Exception err){
                    BEANAPP.showDecryptError(RESULT,err.toString(),"REGISTERING - EXCEPTION");
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                            .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                            .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    String USER_NICK = register_edittext.getText().toString();
                                    REG_tryToRegister(USER_NICK);
                                }
                            })
                            .show();
                }
            }
        }, 1000);
    }

    private void CHA_setForChannel(){
        channel_list_i = (LinearLayout) findViewById(R.id.scr_main_channel_list_i);
        channel_list_ii = (LinearLayout) findViewById(R.id.scr_main_channel_list_ii);
        channel_list_iii = (LinearLayout) findViewById(R.id.scr_main_channel_list_iii);
        channel_list_iv = (LinearLayout) findViewById(R.id.scr_main_channel_list_iv);
        channel_list_v = (LinearLayout) findViewById(R.id.scr_main_channel_list_v);
        channel_list_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHA_showRoom(1);
            }
        });
        channel_list_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHA_showRoom(2);
            }
        });
    }

    private void CHA_showRoom(int CHANNEL){
        ROOM_setForRoom();
        if(CHANNEL == 1){
            CURRENTC_CHANNEL = 1;
            room_image_channel.setImageResource(R.drawable.main_channel_i);
        }
        if (CHANNEL == 2){
            CURRENTC_CHANNEL = 2;
            room_image_channel.setImageResource(R.drawable.main_channel_ii);
        }
        layout_register.setVisibility(RelativeLayout.GONE);
        layout_channel.setVisibility(RelativeLayout.GONE);
        layout_room.setVisibility(RelativeLayout.VISIBLE);
        ISROOMSHOWING = true;
        ROOM_getROOMs(CURRENTC_CHANNEL);
    }

    private void ROOM_setForRoom(){
        room_image_channel = (ImageView) findViewById(R.id.scr_main_image_channel);
        room_image_plus = (ImageView) findViewById(R.id.scr_main_image_create);
        room_image_question = (ImageView) findViewById(R.id.scr_main_image_question);
        room_image_chat = (ImageView) findViewById(R.id.scr_main_image_chat);
        room_image_ranking = (ImageView) findViewById(R.id.scr_main_image_ranking);

        room_image_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ROOM_getROOMs(CURRENTC_CHANNEL);
            }
        });
        room_image_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ROOM_showFeature(1);
            }
        });
        room_image_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ROOM_showFeature(2);
            }
        });
        room_image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ROOM_showFeature(3);
            }
        });
        room_image_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ROOM_showFeature(4);
            }
        });

        room_list = (ListView) findViewById(R.id.scr_main_room_list);

        room_list_adapter = new RoomListViewAdapter(this);
        room_list.setAdapter(room_list_adapter);

        room_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                RoomListData mData = room_list_adapter.mListData.get(position);
                ROOM_showGATE(mData);
            }
        });

        room_parent = (LinearLayout) findViewById(R.id.scr_main_room_parent);
        room_nothing = (LinearLayout) findViewById(R.id.scr_main_room_nothing);
        room_nothing.setVisibility(LinearLayout.GONE);

        room_scrollbar = (ImageView) findViewById(R.id.scr_main_room_scrollbar);
        room_scrollbar.setVisibility(ImageView.GONE);

    }

    private void ROOM_showGATE(final RoomListData ROOMDATA){
        AlertDialog.Builder dialogbuilder = null;
        View gateView;
        TextView text_roomname,text_roomnum,text_roombat;
        final FloatingEditText edit_password;
        PaperButton btn_enter;

        ENTERING_ROOMNUM = ROOMDATA.room_num;

        LayoutInflater inflater = LayoutInflater.from(this);
        gateView = inflater.inflate(R.layout.dialog_room_gate, null);
        text_roomname = (TextView) gateView.findViewById(R.id.dialog_gate_roomname);
        text_roomnum = (TextView) gateView.findViewById(R.id.dialog_gate_roomnum);
        text_roombat = (TextView) gateView.findViewById(R.id.dialog_gate_roombat);
        edit_password = (FloatingEditText) gateView.findViewById(R.id.dialog_gate_password);
        btn_enter = (PaperButton) gateView.findViewById(R.id.dialog_gate_enter);

        text_roomname.setText(ROOMDATA.room_name);
        text_roomnum.setText(getResources().getString(R.string.main_gate_roomnum)+Integer.toString(ROOMDATA.room_num));
        if (ROOMDATA.room_bat.length()>1 && ROOMDATA.room_bat != "0"){
            text_roombat.setText(Html.fromHtml(getResources().getString(R.string.main_gate_roombat)+"<font color=#FFAA00>"+ROOMDATA.room_bat+"</font>"));
        } else {
            text_roombat.setText(Html.fromHtml("<font color=#00FFAA>"+getResources().getString(R.string.main_gate_roombat_none)+"</font>"));
        }

        edit_password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                edit_password.setHighlightedColor(Color.WHITE);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        if (!ROOMDATA.room_locked){
            edit_password.setVisibility(FloatingEditText.GONE);
        }

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ROOMDATA.room_locked){
                    ROOM_tryEnter(ROOMDATA.room_num);
                    gateDialog.dismiss();
                } else {
                    String entered_password = edit_password.getText().toString();
                    if (entered_password == null) {
                        edit_password.setHighlightedColor(Color.RED);
                        edit_password.setValidateResult(false, getResources().getString(R.string.main_gate_error_password_enter));
                        return;
                    }
                    if (entered_password.equals(ROOMDATA.room_password)) {
                        ROOM_tryEnter(ROOMDATA.room_num);
                        gateDialog.dismiss();
                    } else {
                        edit_password.setHighlightedColor(Color.RED);
                        edit_password.setValidateResult(false, getResources().getString(R.string.main_gate_error_password_match));
                        Toast.makeText(MainActivity.this, ROOMDATA.room_password, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialogbuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));
        dialogbuilder.setView(gateView);
        gateDialog = dialogbuilder.create();
        gateDialog.setView(gateView, 0, 0, 0, 0);
        gateDialog.setInverseBackgroundForced(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(gateDialog.getWindow().getAttributes());
        lp.height = 300;
        lp.width = 550;
        gateDialog.getWindow().setAttributes(lp);
        gateDialog.show();

    }

    private void ROOM_showFeature(int FEATURE){
        layout_content.setVisibility(LinearLayout.GONE);
        layout_feature.setVisibility(LinearLayout.VISIBLE);
        feature_box_create.setVisibility(LinearLayout.GONE);
        feature_box_question.setVisibility(LinearLayout.GONE);
        feature_box_chat.setVisibility(LinearLayout.GONE);
        feature_box_ranking.setVisibility(LinearLayout.GONE);
        if(FEATURE==1){
            feature_box_create.setVisibility(LinearLayout.VISIBLE);
            feature_icon.setImageResource(R.drawable.icon_room_plus);
            feature_title.setText(getResources().getString(R.string.main_feature_box_create)+" ["+getResources().getString(R.string.main_feature_box_channel)+Integer.toString(CURRENTC_CHANNEL)+"]");
        }
        if(FEATURE==2){
            feature_box_question.setVisibility(LinearLayout.VISIBLE);
            feature_icon.setImageResource(R.drawable.icon_room_question);
            feature_title.setText(getResources().getString(R.string.main_feature_box_question));
        }
        if(FEATURE==3){
            feature_box_chat.setVisibility(LinearLayout.VISIBLE);
            feature_icon.setImageResource(R.drawable.icon_room_chat);
            feature_title.setText(getResources().getString(R.string.main_feature_box_chat)+" ["+getResources().getString(R.string.main_feature_box_channel)+Integer.toString(CURRENTC_CHANNEL)+"]");
        }
        if(FEATURE==4){
            feature_box_ranking.setVisibility(LinearLayout.VISIBLE);
            feature_icon.setImageResource(R.drawable.icon_room_ranking);
            feature_title.setText(getResources().getString(R.string.main_feature_box_ranking));
        }
        ISFEATURESHOWING = true;
        ISROOMSHOWING = false;
    }

    private void ROOM_getROOMs(int CHANNEL){
        ROOM_GET_TASK RGT = new ROOM_GET_TASK();
        RGT.execute(Integer.toString(CHANNEL));
    }

    private class ROOM_GET_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            String respond;

            try{
                String body = "user_channel="+DATA[0];
                URL u = new URL(DataClass.URL_SERVER_GetRoom);
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
                ROOM_decryptResult(sResult);
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

    private void ROOM_decryptResult(String RESULT){
        if (RESULT.contains("&NOROOM&")){
            room_parent.setVisibility(LinearLayout.GONE);
            room_scrollbar.setVisibility(ImageView.GONE);
            room_nothing.setVisibility(LinearLayout.VISIBLE);
            room_list_adapter.clearData();
            ROOM_LIST_DataChange();
            return;
        }
        try {
            room_nothing.setVisibility(LinearLayout.GONE);
            room_parent.setVisibility(LinearLayout.VISIBLE);
            room_scrollbar.setVisibility(ImageView.VISIBLE);
            JSONArray mJsonArray = new JSONArray(RESULT);
            JSONObject json_data = new JSONObject();
            room_list_adapter.clearData();
            ROOM_LIST_DataChange();
            for (int i = 0; i < mJsonArray.length(); i++) {
                json_data = mJsonArray.getJSONObject(i);
                String room_pw = json_data.getString("room_pw");
                if (room_pw.length()<4 || room_pw == null || room_pw.equals("null")){
                    ROOM_LIST_AddItem(json_data.getInt("room_num"),json_data.getString("room_current")+"/6",json_data.getString("room_name"),false,getResources().getDrawable(R.drawable.icon_locker),json_data.getString("room_bat"),null);
                } else {
                    ROOM_LIST_AddItem(json_data.getInt("room_num"),json_data.getString("room_current")+"/6",json_data.getString("room_name"),true,getResources().getDrawable(R.drawable.icon_locker),json_data.getString("room_bat"),room_pw);
                }
            }
        } catch (Exception e){
        //    BEANAPP.showDecryptError(RESULT,e.toString(),"GETTING ROOM - JSON EXCEPTION");
        }


    }

    private void ROOM_LIST_AddItem(int room_num,String room_size,String room_name,boolean room_locked,Drawable room_locker,String room_bat,String room_password){
        RoomListData AddInfo = new RoomListData();
        AddInfo.room_num = room_num;
        AddInfo.room_size = room_size;
        AddInfo.room_name = room_name;
        AddInfo.room_locked = room_locked;
        AddInfo.room_locker = room_locker;
        AddInfo.room_bat = room_bat;
        AddInfo.room_password = room_password;

        room_list_adapter.mListData.add(AddInfo);
        ROOM_LIST_DataChange();
    }

    private void ROOM_LIST_DataChange(){
        room_list_adapter.notifyDataSetChanged();
    }

    private void ROOM_tryEnter(int ROOM_NUM){
        ROOM_ENTER_TASK RET = new ROOM_ENTER_TASK();
        RET.execute(Integer.toString(ROOM_NUM));
    }

    private class ROOM_ENTER_TASK extends AsyncTask<String, Void, String> {
        String sResult;
        int sSend;
        @Override
        protected String doInBackground(String... DATA) {
            try{
                sSend = Integer.valueOf(DATA[0]);
                String body = "user_channel="+CURRENTC_CHANNEL+"&user_num="+BeanApplication.USER_NUM+"&room_num="+DATA[0];
                URL u = new URL(DataClass.URL_SERVER_EnterRoom);
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
            ROOM_decryptEnterResult(sResult,sSend);
        }
    }

    private void ROOM_decryptEnterResult(String RESULT,int ROOM_NUM){
        try {
            if (RESULT.contains("ERROR")) {
                return;
            }
        } catch (Exception e) {
            BEANAPP.showDecryptError(RESULT,e.toString(),"GETTING ROOM - EXCEPTION");
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                    .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                    .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            ROOM_tryEnter(ENTERING_ROOMNUM);
                        }
                    })
                    .show();
        }
        try {
            JSONArray mJsonArray = new JSONArray(RESULT);
            JSONObject json_data = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++) {
                json_data = mJsonArray.getJSONObject(i);

                String ROOM_NAME,ROOM_PW_DUMMY;
                int ROOM_BAT,ROOM_USER_I,ROOM_USER_II,ROOM_USER_III,ROOM_USER_IV,ROOM_USER_V,ROOM_USER_VI,ROOM_PW;
                ROOM_NAME = json_data.getString("room_name");
                ROOM_PW_DUMMY = json_data.getString("room_pw");
                try {
                    ROOM_PW = Integer.valueOf(ROOM_PW_DUMMY);
                } catch (Exception e){ ROOM_PW = 0; }
                ROOM_BAT = json_data.getInt("room_bat");
                ROOM_USER_I = json_data.getInt("room_user_num_i");
                ROOM_USER_II = json_data.getInt("room_user_num_ii");
                ROOM_USER_III = json_data.getInt("room_user_num_iii");
                ROOM_USER_IV = json_data.getInt("room_user_num_iv");
                ROOM_USER_V = json_data.getInt("room_user_num_v");
                ROOM_USER_VI = json_data.getInt("room_user_num_vi");

                Intent mover = new Intent(MainActivity.this, WaitingActivity.class);
                mover.putExtra("ROOM_NUM",ROOM_NUM);
                mover.putExtra("ROOM_CHANNEL",CURRENTC_CHANNEL);
                mover.putExtra("USER_ISCHIEF",false);
                startActivity(mover);
                overridePendingTransition(0,0);
            }
        } catch (JSONException e){
            BEANAPP.showDecryptError(RESULT,e.toString(),"GETTING ROOM - JSONEXCEPTION");
        }
    }


    private void FEA_setForFeature(){
        layout_content = (LinearLayout) findViewById(R.id.scr_main_content);
        layout_feature = (LinearLayout) findViewById(R.id.scr_main_feature);

        layout_content.setVisibility(LinearLayout.VISIBLE);
        layout_feature.setVisibility(LinearLayout.GONE);

        feature_icon = (ImageView) findViewById(R.id.scr_main_feature_icon);
        feature_exit = (ImageView) findViewById(R.id.scr_main_feature_exit);
        feature_title = (TextView) findViewById(R.id.scr_main_feature_title);

        feature_box_create = (LinearLayout) findViewById(R.id.scr_main_feature_box_create);
        feature_box_question = (LinearLayout) findViewById(R.id.scr_main_feature_box_question);
        feature_box_chat = (LinearLayout) findViewById(R.id.scr_main_feature_box_chat);
        feature_box_ranking = (LinearLayout) findViewById(R.id.scr_main_feature_box_ranking);

        feature_box_create.setVisibility(LinearLayout.GONE);
        feature_box_question.setVisibility(LinearLayout.GONE);
        feature_box_chat.setVisibility(LinearLayout.GONE);
        feature_box_ranking.setVisibility(LinearLayout.GONE);

        feature_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FEA_hideFeature();
            }
        });
        FEA_setForFeature_Create();
    }

    private void FEA_hideFeature(){
        layout_content.setVisibility(LinearLayout.VISIBLE);
        layout_feature.setVisibility(LinearLayout.GONE);
        feature_box_create.setVisibility(LinearLayout.GONE);
        feature_box_question.setVisibility(LinearLayout.GONE);
        feature_box_chat.setVisibility(LinearLayout.GONE);
        feature_box_ranking.setVisibility(LinearLayout.GONE);
        ISFEATURESHOWING = false;
        ISROOMSHOWING = true;
    }

    private void FEA_setForFeature_Create(){
        feature_create_editText_name = (FloatingEditText) findViewById(R.id.scr_main_feature_box_create_name);
        feature_create_editText_pw = (FloatingEditText) findViewById(R.id.scr_main_feature_box_create_pw);
        feature_create_editText_bat = (FloatingEditText) findViewById(R.id.scr_main_feature_box_create_bat);
        feature_create_checkBox_pw = (CheckBox) findViewById(R.id.scr_main_feature_box_create_pw_use);
        feature_create_checkBox_bat = (CheckBox) findViewById(R.id.scr_main_feature_box_create_bat_use);
        feature_create_button = (PaperButton) findViewById(R.id.scr_main_feature_box_create_button);

        feature_create_editText_name.setNormalColor(Color.WHITE);
        feature_create_editText_name.setHighlightedColor(Color.YELLOW);
        feature_create_editText_name.setTextColor(Color.WHITE);
        feature_create_editText_pw.setEnabled(false);
        feature_create_editText_pw.setNormalColor(Color.WHITE);
        feature_create_editText_pw.setHighlightedColor(Color.YELLOW);
        feature_create_editText_pw.setTextColor(Color.WHITE);
        feature_create_editText_bat.setEnabled(false);
        feature_create_editText_bat.setNormalColor(Color.WHITE);
        feature_create_editText_bat.setHighlightedColor(Color.YELLOW);
        feature_create_editText_bat.setTextColor(Color.WHITE);

        feature_create_checkBox_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean CHECKED) {
                feature_create_editText_pw.setEnabled(CHECKED);
                feature_create_editText_pw.setText(null);
                feature_create_editText_pw.setHighlightedColor(Color.WHITE);
            }
        });
        feature_create_checkBox_bat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean CHECKED) {
                feature_create_editText_bat.setEnabled(CHECKED);
                feature_create_editText_bat.setText(null);
                feature_create_editText_bat.setHighlightedColor(Color.WHITE);
            }
        });

        feature_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FEA_CREATE_Validate(feature_create_editText_name.getText().toString(), feature_create_checkBox_pw.isChecked(), feature_create_editText_pw.getText().toString(),
                        feature_create_checkBox_bat.isChecked(), feature_create_editText_bat.getText().toString());
            }
        });
    }

    private void FEA_CREATE_Validate(String ROOM_NAME,boolean ROOM_PW_USE,String ROOM_PW,boolean ROOM_BAT_USE,String ROOM_BAT_DUMMY){
        int ROOM_BAT = 0;
        int ROOM_REAL_PW = 0;
        try {
            ROOM_BAT = Integer.valueOf(ROOM_BAT_DUMMY);
        } catch (NumberFormatException e) { }
        if(ROOM_NAME.length()<3){
            if(ROOM_NAME.length()<1){
                //ERROR
                feature_create_editText_name.setValidateResult(false,getResources().getString(R.string.main_feature_create_error_name_empty));
            } else {
                //ERROR
                feature_create_editText_name.setValidateResult(false,getResources().getString(R.string.main_feature_create_error_name_short));
            }
            return;
        }
        if(ROOM_PW_USE){
            if(ROOM_PW.length()<4){
                //ERROR
                feature_create_editText_pw.setValidateResult(false,getResources().getString(R.string.main_feature_create_error_pw_short));
                return;
            } else {
                ROOM_REAL_PW = Integer.valueOf(ROOM_PW);
            }
        } else {
            ROOM_PW = null;
            ROOM_REAL_PW = 0;
        }

        if(ROOM_BAT_USE) {
            if(ROOM_BAT<100){
                //ERROR
                feature_create_editText_bat.setValidateResult(false,getResources().getString(R.string.main_feature_create_error_bat_low));
                return;
            }
            if(ROOM_BAT>10000){
                //ERROR
                feature_create_editText_bat.setValidateResult(false,getResources().getString(R.string.main_feature_create_error_bat_high));
                return;
            }
        } else {
            ROOM_BAT = 0;
        }
        //TRYMAKE
        feature_create_dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        feature_create_dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        feature_create_dialog.setTitleText(getResources().getString(R.string.main_feature_create_dialog));
        feature_create_dialog.setCancelable(false);
        feature_create_dialog.show();

        FEA_CREATE_MakeROOM(CURRENTC_CHANNEL, ROOM_NAME.trim(), Integer.toString(ROOM_REAL_PW), ROOM_BAT);
    }

    private void FEA_CREATE_MakeROOM(final int CHANNEL,final String ROOM_NAME,final String ROOM_PW,final int ROOM_BAT){
        Handler ahan = new Handler();
        ahan.postDelayed(new Runnable() {
            @Override
            public void run() {
                FEA_CREATE_TASK FCT = new FEA_CREATE_TASK();
                FCT.execute(Integer.toString(CHANNEL), ROOM_NAME, ROOM_PW, Integer.toString(ROOM_BAT));
            }
        }, 600);

    }

    private class FEA_CREATE_TASK extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {
            String respond;

            try{
                String body = "user_num="+BeanApplication.USER_NUM+"&user_channel="+DATA[0]+"&user_room_name="+DATA[1]+"&user_room_pw="+DATA[2]+"&user_room_bat="+DATA[3];
                URL u = new URL(DataClass.URL_SERVER_CreateRoom);
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
                FEA_CREATE_decryptResult(sResult);
            } catch (final NullPointerException err){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BEANAPP.showDecryptError(sResult, err.toString(), "CREATING ROOM - NULLPOINTER EXCEPTION");
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                                .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                                .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        FEA_CREATE_Validate(feature_create_editText_name.getText().toString(), feature_create_checkBox_pw.isChecked(), feature_create_editText_pw.getText().toString(),
                                                feature_create_checkBox_bat.isChecked(), feature_create_editText_bat.getText().toString());
                                    }
                                })
                                .show();
                    }
                });

            }
        }
    }

    private void FEA_CREATE_decryptResult(String RESULT){
        if(RESULT.contains("&SUCCESS&")){
            feature_create_dialog.dismiss();
            String ROOM_NUM_DUMMY = RESULT.substring(10,RESULT.length());
            int ROOM_NUM = Integer.valueOf(ROOM_NUM_DUMMY);
            FEA_hideFeature();
            feature_create_editText_name.setText("");
            feature_create_editText_pw.setText("");
            feature_create_editText_bat.setText("");
            feature_create_checkBox_pw.setChecked(false);
            feature_create_checkBox_pw.setChecked(false);
            Intent mover = new Intent(MainActivity.this, WaitingActivity.class);
            mover.putExtra("ROOM_NUM",ROOM_NUM);
            mover.putExtra("ROOM_CHANNEL", CURRENTC_CHANNEL);
            mover.putExtra("USER_ISCHIEF",true);
            startActivity(mover);
            overridePendingTransition(0,0);
        } else {
            BEANAPP.showDecryptError(RESULT,"","CREATiNG ROOM - NO DESCRIPTION");
        }
    }

    private BroadcastReceiver QuitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            if(data.getAction().equalsIgnoreCase("ROOM QUIT")) {
                try {
                    ROOM_getROOMs(CURRENTC_CHANNEL);
                    BAR_getForBar();
                } catch (Exception err){
                    BEANAPP.showDecryptError("",err.toString(),"QUIT RECEIVED - EXCEPTION");
                }
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        BEANAPP.playMusic(2, DataClass.FILE_MUSIC_MAIN);
        ISPAUSED = false;
        if (CURRENTC_CHANNEL == 1){
            ROOM_getROOMs(1);
        }
        if (CURRENTC_CHANNEL == 2){
            ROOM_getROOMs(2);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        ISPAUSED = true;
    }

    @Override
    public void onStop(){
        super.onStop();
        BEANAPP.stopMusic(2);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (ISROOMSHOWING){
                    layout_register.setVisibility(RelativeLayout.GONE);
                    layout_channel.setVisibility(RelativeLayout.VISIBLE);
                    layout_room.setVisibility(RelativeLayout.GONE);
                    ISROOMSHOWING = false;
                } else if(ISFEATURESHOWING){
                    FEA_hideFeature();
                } else {
                    try {
                        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText(getResources().getString(R.string.main_quit));
                        pDialog.setContentText(getResources().getString(R.string.main_quit_content));
                        pDialog.setCancelText(getResources().getString(R.string.main_quit_no));
                        pDialog.setConfirmText(getResources().getString(R.string.main_quit_yes));
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
                    }
                    catch (Exception err) { }
                }
            break;
        }
        return false;
    }
}
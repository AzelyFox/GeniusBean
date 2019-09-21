package kr.devs.geniusbean.door;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;
import kr.devs.geniusbean.utils.CustomHorizontalScrollView;
import kr.devs.geniusbean.utils.DataClass;

public class LoginActivity extends Activity implements com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks,com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

    private BeanApplication BEANAPP;

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private GoogleApiClient mClient;
    private SignInButton btn_google;

    private CustomHorizontalScrollView scroller_bean_i,scroller_bean_ii;
    Timer scroller;
    int scroller_direction_i = 1,scroller_direction_ii = 1;
    float scroller_direction_i_multiplier = 1,scroller_direction_ii_multiplier = 2;

    private ImageView img_gamelogo;

    private NumberProgressBar progressBar;
    private Timer mTimer;

    boolean islogining = false;

    private TextView tv_top,tv_version;

    SweetAlertDialog pDialog;

    FrameLayout layout_login;
    LinearLayout layout_download;

    TextView down_tv_size,down_tv_current;
    NumberProgressBar down_progressBar;
    int down_totalsize = 0,down_downloaded = 0;
    String[] down_downlist;
    boolean down_error = false;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_login);

        BEANAPP = (BeanApplication) getApplication();

        img_gamelogo = (ImageView) findViewById(R.id.scr_login_gamelogo);

    //    Animation animation_float = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.anim_floating);
    //    img_gamelogo.startAnimation(animation_float);

        scroller_bean_i = (CustomHorizontalScrollView) findViewById(R.id.scr_login_beans_i);
        scroller_bean_ii = (CustomHorizontalScrollView) findViewById(R.id.scr_login_beans_ii);

        scroller_bean_i.setOnEdgeTouchListener(new CustomHorizontalScrollView.OnEdgeTouchListener() {
            @Override
            public void onEdgeTouch(CustomHorizontalScrollView.DIRECTION direction) {
                if(direction== CustomHorizontalScrollView.DIRECTION.LEFT){
                    scroller_direction_i = 1;
                } else if(direction== CustomHorizontalScrollView.DIRECTION.RIGHT){
                    scroller_direction_i = -1;
                }
            }
        });

        scroller_bean_ii.setOnEdgeTouchListener(new CustomHorizontalScrollView.OnEdgeTouchListener() {
            @Override
            public void onEdgeTouch(CustomHorizontalScrollView.DIRECTION direction) {
                if(direction== CustomHorizontalScrollView.DIRECTION.LEFT){
                    scroller_direction_ii = 1;
                } else if(direction== CustomHorizontalScrollView.DIRECTION.RIGHT){
                    scroller_direction_ii = -1;
                }
            }
        });

        btn_google = (SignInButton) findViewById(R.id.scr_login_google_sign_in_button);
        btn_google.setSize(SignInButton.SIZE_WIDE);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!islogining)
                    signInToGoogleAccount();
            }
        });
        btn_google.setEnabled(false);

        tv_top = (TextView) findViewById(R.id.scr_login_tv_top);
        tv_version = (TextView) findViewById(R.id.scr_login_tv_version);
        tv_version.setText(getResources().getString(R.string.login_version)+Integer.toString(BeanApplication.GAME_VERSION_M)+"."+Integer.toString(BeanApplication.GAME_VERSION_S));

        progressBar = (NumberProgressBar) findViewById(R.id.scr_login_progressbar);

        layout_login = (FrameLayout) findViewById(R.id.scr_login_parent_inner);
        layout_download = (LinearLayout) findViewById(R.id.scr_login_parent_download);

        down_tv_size = (TextView) findViewById(R.id.scr_login_download_text_size);
        down_tv_current = (TextView) findViewById(R.id.scr_login_download_text_current);
        down_progressBar = (NumberProgressBar) findViewById(R.id.scr_login_download_progressbar);

        layout_login.setVisibility(FrameLayout.VISIBLE);
        layout_download.setVisibility(LinearLayout.GONE);

        tryCheckNetwork();
        startScroll();

    }

    public void startScroll(){
        ScrollTask ST = new ScrollTask();
        scroller = new Timer();
        scroller.scheduleAtFixedRate(ST, 0, 55);
    }

    class ScrollTask extends TimerTask {
        public void run() {
            doScroll();
        }
    }

    private void doScroll(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scroller_bean_i.scrollBy(scroller_direction_i * (int) scroller_direction_i_multiplier, 0);
                scroller_bean_ii.scrollBy(scroller_direction_ii * (int) scroller_direction_ii_multiplier, 0);
            }
        });
    }

    public void showConnectFail(String errmsg){
        Toast.makeText(LoginActivity.this,errmsg,Toast.LENGTH_SHORT).show();
    }

    public void reLoadAll(){
        progressBar.setProgress(0);
        tryCheckNetwork();
    }

    public void showConnectSuccess(){
        tv_top.setText(getResources().getString(R.string.login_loading_top_success));
        btn_google.setEnabled(true);
    }

    public void tryCheckNetwork(){
        tv_top.setText(R.string.login_loading_top_doing_i);
        mTimer = new Timer();
        mTimer.schedule(new maTask(),200, 10);

        final boolean isconnected = BEANAPP.getNETWORK();

        Handler ahan = new Handler();
        ahan.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mTimer.cancel();
                    mTimer = null;
                } catch (Exception e) {
                }
                if (isconnected) {
                    progressBar.setProgress(20);
                    tryConnectServer();
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.login_loading_fail_network))
                            .setContentText(getResources().getString(R.string.login_loading_fail_network_content))
                            .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    reLoadAll();
                                }
                            })
                            .show();
                }
            }
        }, 700);

    }

    public void tryConnectServer(){
        tv_top.setText(getResources().getString(R.string.login_loading_top_doing_ii));
        mTimer = new Timer();
        mTimer.schedule(new mbTask(), 0, 10);

        connectServerTask cST = new connectServerTask();
        cST.execute();

    }

    private class connectServerTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            String respond;

            try{
                String body = "";
                URL u = new URL(DataClass.URL_SERVER_ServerState);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "euc-kr"));
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
                endConnectServer(sResult);
        }
    }

    private void endConnectServer(String result){

        try {
            if (result.contains("server:")) {
                try {
                    String SERVER_STATE = result.substring(result.indexOf("server:") + 7, result.length());
                    if (SERVER_STATE.contains("ON")) {
                        Handler ahan = new Handler();
                        ahan.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mTimer.cancel();
                                    mTimer = null;
                                } catch (Exception e) {
                                }
                                progressBar.setProgress(40);
                                tryGetVersion();
                            }
                        }, 300);
                    } else if (SERVER_STATE.contains("OFF")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.login_loading_fail_server_off))
                                .setContentText(getResources().getString(R.string.login_loading_fail_server_off_content))
                                .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        reLoadAll();
                                    }
                                })
                                .show();
                    } else if (SERVER_STATE.contains("FIX")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.login_loading_fail_server_fix))
                                .setContentText(getResources().getString(R.string.login_loading_fail_server_fix_content))
                                .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        reLoadAll();
                                    }
                                })
                                .show();
                    } else {
                        BEANAPP.showDecryptError(result,"","GETTING SERVER - NO DESCRIPTION");
                    }
                } catch (Exception e) {
                    BEANAPP.showDecryptError(result,e.toString(),"GETTING SERVER - EXCEPTION");
                }
            } else {
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                        .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                        .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                reLoadAll();
                            }
                        })
                        .show();
            }
        }catch (NullPointerException err){
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                    .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                    .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            reLoadAll();
                        }
                    })
                    .show();
        }

    }

    public void tryGetVersion(){
        tv_top.setText(getResources().getString(R.string.login_loading_top_doing_iii));
        mTimer = new Timer();
        mTimer.schedule(new mcTask(), 0, 10);

        connectVersionTask cVT = new connectVersionTask();
        cVT.execute();

    }

    private class connectVersionTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            String respond;

            try{
                String body = "";
                URL u = new URL(DataClass.URL_SERVER_GameVersion);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(3000); huc.setConnectTimeout(3000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();

                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "euc-kr"));
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
            endGetVersion(sResult);
        }
    }

    private void endGetVersion(String result){

        try {
            if (result.contains("version:")){

                    String VERSION_DUMMY = result.substring(result.indexOf("version:") + 8, result.length());
                    String VERSION_M_DUMMY = VERSION_DUMMY.substring(0,VERSION_DUMMY.indexOf("."));
                    String VERSION_S_DUMMY = VERSION_DUMMY.substring(VERSION_DUMMY.indexOf(".")+1,VERSION_DUMMY.length());
                    int VERSION_M = Integer.valueOf(VERSION_M_DUMMY);
                    int VERSION_S = Integer.valueOf(VERSION_S_DUMMY);

                    if (BEANAPP.GAME_VERSION_M <= VERSION_M && BEANAPP.GAME_VERSION_S < VERSION_S) { // GAME NEEDS UPDATE
                    //    showConnectFail(getResources().getString(R.string.login_loading_fail_version_low));
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.login_loading_fail_version_low))
                                .setContentText(getResources().getString(R.string.login_loading_fail_version_low_now)+Integer.toString(BeanApplication.GAME_VERSION_M)+"."+Integer.toString(BeanApplication.GAME_VERSION_S)+" "
                                +getResources().getString(R.string.login_loading_fail_version_low_server)+Integer.toString(VERSION_M)+"."+Integer.toString(VERSION_S))
                                .setCancelText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                                .setConfirmText(getResources().getString(R.string.login_loading_fail_version_dialog_update))
                                .showCancelButton(true)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        reLoadAll();
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DataClass.URL_SERVER_UPDATE));
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    } else { // GAME IS LASTEST VERSION
                        Handler ahan = new Handler();
                        ahan.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mTimer.cancel();
                                    mTimer = null;
                                } catch (Exception e) { }
                                progressBar.setProgress(60);
                                tryCheckResource();
                            }
                        },250);
                    }

            } else {
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                        .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                        .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                reLoadAll();
                            }
                        })
                        .show();
            }
        } catch (Exception e){
        Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
        BEANAPP.showDecryptError(result,e.toString(),"GETTING VERSION - EXCEPTION");
        }
    }

    public void tryCheckResource(){
        tv_top.setText(getResources().getString(R.string.login_loading_top_doing_iv));
        mTimer = new Timer();
        mTimer.schedule(new mdTask(), 0, 10);

        BEANAPP.checkDir();

        getResourceListTask gRLT = new getResourceListTask();
        gRLT.execute();
    }

    private class getResourceListTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... DATA) {

            try{
                URL url = new URL(DataClass.URL_SERVER_RESOURCE_Guide);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str; int line = 0;
                while ((str = in.readLine()) != null) {
                    if (line ==0){
                        sResult = str;
                    } else {
                        sResult = sResult + "\n" + str;
                    }
                    line ++;
                }
                in.close();
            } catch (Exception e) {
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            endGetResourceList(sResult);
        }
    }

    private void endGetResourceList(String FILELIST){

        FILELIST = FILELIST.substring(FILELIST.indexOf("<")+1,FILELIST.lastIndexOf(">"));

        String[] FILELINKS,FILEDOWN,FILEEXIST;
        int FILEDOWN_LENGTH = 0,FILEEXIST_LENGTH = 0;
        FILELINKS = FILELIST.split("\n");
        FILEDOWN = new String[FILELIST.length()];FILEEXIST = new String[FILELIST.length()];

        for (int i = 0;i < FILELINKS.length;i++){
            String filepath = Environment.getExternalStorageDirectory()+DataClass.BEAN_FOLDER+FILELINKS[i];
            File file_checking = new File(filepath);
            if ((!file_checking.exists() || FILELINKS[i].contains("*"))&& FILELINKS[i].length()>0){
                FILELINKS[i] = FILELINKS[i].replace("*","");
                FILEDOWN[FILEDOWN_LENGTH] = FILELINKS[i];
                FILEDOWN_LENGTH ++;
            } else {
                FILEEXIST[FILEEXIST_LENGTH] = FILELINKS[i];
                FILEEXIST_LENGTH ++;
            }
        }

        try {
            mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
        }
        progressBar.setProgress(80);
        tryGetResource(FILEDOWN,FILEDOWN_LENGTH);

    }

    public void tryGetResource(final String[] FILETOGET,final int FILETOGET_LENGTH) {
        tv_top.setText(getResources().getString(R.string.login_loading_top_doing_v));
        boolean NETWORK_MOBILE = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo != null)
            NETWORK_MOBILE = networkInfo.isConnected();
        if (FILETOGET_LENGTH>0){
            if (NETWORK_MOBILE){
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.login_download_start_alert_title))
                        .setContentText(getResources().getString(R.string.login_download_start_alert_content))
                        .setConfirmText(getResources().getString(R.string.login_download_start_alert_confirm))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                layout_login.setVisibility(FrameLayout.GONE);
                                layout_download.setVisibility(LinearLayout.VISIBLE);
                                sDialog.cancel();
                                tryDownResource(FILETOGET, FILETOGET_LENGTH);
                            }
                        })
                        .show();
            } else {
                layout_login.setVisibility(FrameLayout.GONE);
                layout_download.setVisibility(LinearLayout.VISIBLE);
                Handler ahan = new Handler();
                ahan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tryDownResource(FILETOGET, FILETOGET_LENGTH);
                    }
                }, 800);
            }
        } else {
            try {
                mTimer.cancel();
                mTimer = null;
            } catch (Exception e) {
            }
            progressBar.setProgress(100);
            showConnectSuccess();
        }

    }

    private void tryDownResource(String[] FILETOGET,int FILETOGET_LENGTH){
        int FILE_CURRENT = 0,FILE_TODOWN = FILETOGET_LENGTH;
        down_totalsize = FILE_TODOWN;
        down_downlist = FILETOGET;

        down_tv_size.setText(Integer.toString(FILE_CURRENT+1)+"/"+Integer.toString(FILETOGET_LENGTH));
        down_tv_current.setText(FILETOGET[FILE_CURRENT]);

        for(FILE_CURRENT = 0;FILE_CURRENT < FILE_TODOWN;FILE_CURRENT++){
            Log.d("GeniusBean", FILETOGET[FILE_CURRENT]);
            downResourceTask dRT = new downResourceTask();
            dRT.execute(FILETOGET[FILE_CURRENT]);
        }

    }

    private class downResourceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... DATA) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(DataClass.URL_SERVER_RESOURCE+DATA[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory()+DataClass.BEAN_FOLDER+DATA[0]);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.d("GeniusBean","UNKNOWN ERROR WHILE DOWNLOADING RESOURCE : "+e.toString());
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return "SUCCESS";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            down_progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Log.d("GeniusBean", "RESULT : "+result);
            if (result.contains("ERROR")){
                down_error = true;
            }
            down_downloaded++;
            endDownResource();
        }
    }

    private void endDownResource(){
        if (down_downloaded == down_totalsize){
            try {
                mTimer.cancel();
                mTimer = null;
            } catch (Exception e) {
            }
            if (!down_error){
                Handler ahan = new Handler();
                ahan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(100);
                        showConnectSuccess();
                        layout_download.setVisibility(LinearLayout.GONE);
                        layout_login.setVisibility(FrameLayout.VISIBLE);
                    }
                },1000);

            } else {
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.login_download_error_alert_title))
                        .setContentText(getResources().getString(R.string.login_download_error_alert_content))
                        .setConfirmText(getResources().getString(R.string.login_download_error_alert_retry))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                layout_download.setVisibility(LinearLayout.GONE);
                                layout_login.setVisibility(FrameLayout.VISIBLE);
                                tryCheckResource();
                            }
                        })
                        .show();
            }
        } else {
            down_tv_size.setText(Integer.toString(down_downloaded)+"/"+Integer.toString(down_totalsize));
            down_tv_current.setText(down_downlist[down_downloaded]);
        }
    }


    class maTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    if(progressBar.getProgress() < 20) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e){}
                    }
                }
            });
        }
    }

    class mbTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < 40) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }
    }

    class mcTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < 60) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }
    }

    class mdTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < 80) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }
    }

    class meTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < 100) {
                        progressBar.incrementProgressBy(1);
                    } else {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }
    }


    private void signInToGoogleAccount() {
        islogining = true;
        btn_google.setClickable(false);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(getResources().getString(R.string.login_dialog_doing));
        pDialog.setCancelable(false);
        pDialog.show();

        Handler ahan = new Handler();
        ahan.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClient = new GoogleApiClient.Builder(LoginActivity.this)
                        .addConnectionCallbacks(LoginActivity.this)
                        .addOnConnectionFailedListener(LoginActivity.this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_LOGIN)
                        .build();
                mClient.connect();
            }
        }, 100);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mClient.connect();
            }
        }
        islogining = false;
        btn_google.setClickable(true);
    }


    @Override
    public void onConnectionSuspended(int cause){
        Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_retry),Toast.LENGTH_SHORT).show();
        islogining = false;
        btn_google.setClickable(true);
        try {
            pDialog.dismiss();
        } catch (Exception e){ }
    }

    @Override
    public void onConnected(Bundle savedInstanceState) {
        final String accountName = Plus.AccountApi.getAccountName(mClient);
        Handler ahan = new Handler();
        sendLogin(accountName);
        islogining = false;
        try {
            pDialog.dismiss();
        }catch(Exception e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mClient.connect();
        }
    }

    public void sendLogin(String useremail){
        BeanApplication.USER_EMAIL = useremail;
        LoginTask LT = new LoginTask();
        LT.execute();
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            String respond;

            try{
                String body = "user_email="+BeanApplication.USER_EMAIL+"&user_sobid="+BeanApplication.USER_SOBID;
                URL u = new URL(DataClass.URL_SERVER_Login);
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
            doLogin(sResult);
        }
    }

    public void doLogin(String result){
        try {
            if (result.contains("&NEW USER&")) {
                BeanApplication.USER_NEW = true;
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                pDialog.setTitleText(getResources().getString(R.string.login_dialog_makecharacter));
                pDialog.setCancelable(false);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent mover = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mover);
                        overridePendingTransition(R.transition.anim_fadein, R.transition.anim_fadeout);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });
                pDialog.show();

            } else {
                try {
                    JSONArray mJsonArray = new JSONArray(result);
                    JSONObject jObject = mJsonArray.getJSONObject(0);
                    int usernum = jObject.getInt("user_num");
                    String usernick = jObject.getString("user_nick");
                    BeanApplication.USER_NUM = usernum;
                    BeanApplication.USER_NICK = usernick;
                    BeanApplication.USER_NEW = false;
                    Intent mover = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mover);
                    overridePendingTransition(R.transition.anim_fadein, R.transition.anim_fadeout);
                    overridePendingTransition(0, 0);

                    finish();
                } catch (JSONException e) {
                    BEANAPP.showDecryptError(result,e.toString(),"GETTING LOGIN RESULT - JSONEXCETION");
                }
            }
        } catch (NullPointerException err) {
            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.login_loading_fail_server_norespond))
                    .setContentText(getResources().getString(R.string.login_loading_fail_server_norespond_content))
                    .setConfirmText(getResources().getString(R.string.login_loading_fail_dialog_retry))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            signInToGoogleAccount();
                        }
                    })
                    .show();
            btn_google.setEnabled(true);
            islogining = false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        BEANAPP.playMusicfromRaw(1, R.raw.bluetime);
    }

    @Override
    public void onStop(){
        super.onStop();
        BEANAPP.stopMusic(1);
    }

}
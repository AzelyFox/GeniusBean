package kr.devs.geniusbean;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.utils.DataClass;
import kr.devs.geniusbean.utils.FontClass;

public class BeanApplication extends Application{

    public static int GAME_VERSION_M = 1;
    public static int GAME_VERSION_S = 0;

    public static String USER_EMAIL = "";
    public static int USER_NUM = 0;
    public static String USER_NICK = "";
    public static boolean USER_NEW = false;
    public static String USER_SOBID = "";
    public static String USER_HELLO = "";
    public static int USER_CLASS,USER_LV,USER_EXP,USER_MONEY,USER_CASH,USER_PLAYED,USER_WIN,USER_LOSE,USER_GRADE,USER_RATING,USER_SKIN,USER_GUILD;

    public static Typeface BEAN_FONT;

    public static MediaPlayer BEAN_PLAYER = null;

    private int CURRENT_MUSIC;

    @Override
    public void onCreate(){
        super.onCreate();

        Parse.initialize(this, "JM59p3wVHDFLNiftaPtKDl1XeVABI1St7x2hhnf5", "ioVKCLlps7GBZxBwI8bZPFcFHQelSKENm7mKQPsh");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        USER_SOBID = ParseInstallation.getCurrentInstallation().getObjectId();

        FontClass.setDefaultFont(this, "DEFAULT", "fonts/NanumGothic.otf");
        FontClass.setDefaultFont(this, "MONOSPACE", "fonts/NanumGothic.otf");

        BEAN_FONT = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.otf");

        checkDir();


    }

    public void checkDir(){
        File folder = new File(Environment.getExternalStorageDirectory() + DataClass.BEAN_FOLDER_DIR);
        if(!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
    }

    public void playMusic(int MUSICID,String MUSICRESOURCE){
        try {
            if (BEAN_PLAYER.isPlaying()) {
                BEAN_PLAYER.stop();
            }
        } catch (Exception err){ }
        try {
            CURRENT_MUSIC = MUSICID;
            BEAN_PLAYER = new MediaPlayer();
            BEAN_PLAYER.setDataSource(Environment.getExternalStorageDirectory() + DataClass.BEAN_FOLDER + MUSICRESOURCE);
            BEAN_PLAYER.setLooping(true);
            BEAN_PLAYER.setVolume(0.7f, 0.7f);
            BEAN_PLAYER.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    BEAN_PLAYER.start();
                }
            });
            BEAN_PLAYER.prepareAsync();

        } catch (Exception err){ showDecryptError(Environment.getExternalStorageDirectory() + DataClass.BEAN_FOLDER + MUSICRESOURCE,err.toString(),"WHEN MAKING MEDIAPLAYER"); }
    }

    public void playMusicfromRaw(int MUSICID,int MUSICRESOURCE){
        if (CURRENT_MUSIC == MUSICID){
            return;
        }
        try {
            if (BEAN_PLAYER.isPlaying()) {
                BEAN_PLAYER.stop();
            }
        } catch (Exception err){ }
        try {
            CURRENT_MUSIC = MUSICID;
            BEAN_PLAYER = MediaPlayer.create(this,MUSICRESOURCE);
            BEAN_PLAYER.setLooping(true);
            BEAN_PLAYER.setVolume(0.7f, 0.7f);
            BEAN_PLAYER.start();

        } catch (Exception err){ showDecryptError("",err.toString(),"WHEN MAKING MEDIAPLAYER"); }
    }

    public void stopMusic(int MUSICID){
        try {
            if (BEAN_PLAYER.isPlaying() && CURRENT_MUSIC == MUSICID) {
                BEAN_PLAYER.stop();
                BEAN_PLAYER.release();
            }
        } catch (Exception err){ }
    }

    public boolean getNETWORK(){
        boolean NETWORK = false,NETWORK_WIFI = false,NETWORK_MOBILE = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo != null)
            NETWORK_WIFI = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo != null)
            NETWORK_MOBILE = networkInfo.isConnected();
        if(NETWORK_WIFI||NETWORK_MOBILE){
            NETWORK = true;
        }
        return NETWORK;
    }

    public void showDecryptError(String ERRMSG,String ERROR,String WHEN){
        Log.e("BEANAPP","ERROR : "+ERROR+"\nERROR CAUSED BY : "+ERRMSG+"\nWHEN : "+WHEN);
    }

}

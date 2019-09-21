package kr.devs.geniusbean.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;
import kr.devs.geniusbean.utils.DataClass;

public class MenuSettingActivity extends Activity {

    BeanApplication BEANAPP;
    ImageView menu_btn_i,menu_btn_ii,menu_btn_iii,menu_btn_iv,menu_btn_v,menu_btn_vi;
    TextView resource_text_dir,resource_text_folder;
    Button resource_btn;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_menu_setting);

        BEANAPP = (BeanApplication) getApplication();

        setViewIDs();
        setSettingDatas();

    }

    private void setViewIDs(){
        menu_btn_i = (ImageView) findViewById(R.id.scr_menu_setting_menu_i);
        menu_btn_ii = (ImageView) findViewById(R.id.scr_menu_setting_menu_ii);
        menu_btn_iii = (ImageView) findViewById(R.id.scr_menu_setting_menu_iii);
        menu_btn_iv = (ImageView) findViewById(R.id.scr_menu_setting_menu_iv);
        menu_btn_v = (ImageView) findViewById(R.id.scr_menu_setting_menu_v);
        menu_btn_vi = (ImageView) findViewById(R.id.scr_menu_setting_menu_vi);
        menu_btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuSettingActivity.this,MenuInfoActivity.class);
                startActivity(mover);
                overridePendingTransition(0,0);
                finish();
            }
        });
        menu_btn_ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuSettingActivity.this, MenuFriendActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuSettingActivity.this, MenuGuildActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuSettingActivity.this, MenuNoticeActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuSettingActivity.this, MenuMarketActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        resource_text_dir = (TextView) findViewById(R.id.scr_menu_setting_resource_dir);
        resource_text_folder = (TextView) findViewById(R.id.scr_menu_setting_resource_folder);
        resource_btn = (Button) findViewById(R.id.scr_menu_setting_resource_btn);
    }

    public void setSettingDatas(){
        resource_text_dir.setText(resource_text_dir.getText() + " " + DataClass.BEAN_FOLDER_DIR);
        final File Resource_Folder = new File(Environment.getExternalStorageDirectory()+DataClass.BEAN_FOLDER_DIR);
        float RESOURCE_SIZE = (float) getFolderSize(Resource_Folder)/(1024*1024);
        resource_text_folder.setText(resource_text_folder.getText() + " " + String.format("%.2f", RESOURCE_SIZE)+"MB");
        resource_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog pDialog = new SweetAlertDialog(MenuSettingActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText(getResources().getString(R.string.menu_setting_resource_alert_delete_title));
                pDialog.setContentText(getResources().getString(R.string.menu_setting_resource_alert_delete_content));
                pDialog.setCancelText(getResources().getString(R.string.menu_setting_resource_alert_delete_cancel));
                pDialog.setConfirmText(getResources().getString(R.string.menu_setting_resource_alert_delete_confirm));
                pDialog.showCancelButton(true);
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        DeleteRecursive(Resource_Folder);
                        sDialog.cancel();
                        new SweetAlertDialog(MenuSettingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(getResources().getString(R.string.menu_setting_resource_alert_deleted_title))
                                .setContentText(getResources().getString(R.string.menu_setting_resource_alert_deleted_content))
                                .setConfirmText(getResources().getString(R.string.menu_setting_resource_alert_deleted_confirm))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                });
                pDialog.show();

            }
        });
    }

    public void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);
        fileOrDirectory.delete();
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size=f.length();
        }
        return size;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                SweetAlertDialog pDialog = new SweetAlertDialog(MenuSettingActivity.this, SweetAlertDialog.WARNING_TYPE);
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

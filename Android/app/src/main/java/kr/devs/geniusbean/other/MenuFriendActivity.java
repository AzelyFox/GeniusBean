package kr.devs.geniusbean.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;

public class MenuFriendActivity extends Activity {

    BeanApplication BEANAPP;
    ImageView menu_btn_i,menu_btn_ii,menu_btn_iii,menu_btn_iv,menu_btn_v,menu_btn_vi;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_menu_friend);

        BEANAPP = (BeanApplication) getApplication();

        setViewIDs();

    }

    private void setViewIDs(){
        menu_btn_i = (ImageView) findViewById(R.id.scr_menu_friend_menu_i);
        menu_btn_ii = (ImageView) findViewById(R.id.scr_menu_friend_menu_ii);
        menu_btn_iii = (ImageView) findViewById(R.id.scr_menu_friend_menu_iii);
        menu_btn_iv = (ImageView) findViewById(R.id.scr_menu_friend_menu_iv);
        menu_btn_v = (ImageView) findViewById(R.id.scr_menu_friend_menu_v);
        menu_btn_vi = (ImageView) findViewById(R.id.scr_menu_friend_menu_vi);
        menu_btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuFriendActivity.this,MenuInfoActivity.class);
                startActivity(mover);
                overridePendingTransition(0,0);
                finish();
            }
        });
        menu_btn_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuFriendActivity.this,MenuGuildActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuFriendActivity.this,MenuNoticeActivity.class);
                startActivity(mover);
                overridePendingTransition(0,0);
                finish();
            }
        });
        menu_btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuFriendActivity.this, MenuMarketActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        menu_btn_vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mover = new Intent(MenuFriendActivity.this, MenuSettingActivity.class);
                startActivity(mover);
                overridePendingTransition(0, 0);
                finish();
            }
        });
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
                SweetAlertDialog pDialog = new SweetAlertDialog(MenuFriendActivity.this, SweetAlertDialog.WARNING_TYPE);
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

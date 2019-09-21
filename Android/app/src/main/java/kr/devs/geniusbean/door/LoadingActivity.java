package kr.devs.geniusbean.door;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;

public class LoadingActivity extends Activity {

    LinearLayout lay_parent;
    LinearLayout lay_devskr;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.scr_loading);

        lay_parent = (LinearLayout) findViewById(R.id.scr_loading_layout_parent);
        lay_devskr = (LinearLayout) findViewById(R.id.scr_loading_layout_devskr);

        lay_devskr.setVisibility(LinearLayout.VISIBLE);

        Handler ehan = new Handler();
        ehan.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mover = new Intent(LoadingActivity.this,LoginActivity.class);
                startActivity(mover);
                overridePendingTransition(R.transition.anim_fadein,R.transition.anim_fadeout);
                finish();
            }
        },3300);
    }

}

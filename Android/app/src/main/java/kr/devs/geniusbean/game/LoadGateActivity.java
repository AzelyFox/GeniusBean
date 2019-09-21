package kr.devs.geniusbean.game;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import kr.devs.geniusbean.R;
import kr.devs.geniusbean.utils.FlakeView;

public class LoadGateActivity extends Activity {

    FrameLayout layout_parent;
    LinearLayout layout_flaker;
    FlakeView flakeView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scr_load_gate);

        layout_flaker = (LinearLayout) findViewById(R.id.scr_load_gate_flaker);
        flakeView = new FlakeView(this);
        layout_flaker.addView(flakeView);

    }
}

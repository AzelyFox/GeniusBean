package kr.devs.geniusbean.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.devs.geniusbean.R;

public class Fragment_introduce extends Fragment
{

    Context mContext;

    public Fragment_introduce(){
        super();
    }

    @SuppressLint("ValidFragment")
    public Fragment_introduce(Context context){
        super();
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_introduce, null);



        return view;
    }

}

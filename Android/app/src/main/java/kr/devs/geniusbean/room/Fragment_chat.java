package kr.devs.geniusbean.room;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import kr.devs.geniusbean.BeanApplication;
import kr.devs.geniusbean.R;

public class Fragment_chat extends Fragment
{

    Context mContext;
    EditText chatbox;
    Button chatbtn;
    TextView chatlist;
    ScrollView scroller;
    View view;
    boolean SENDLOCKED = false;

    public Fragment_chat(){
        super();
    }

    @SuppressLint("ValidFragment")
    public Fragment_chat(Context context){
        super();
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_chat, null);

        chatbox = (EditText) view.findViewById(R.id.frag_chat_box);
        chatbtn = (Button) view.findViewById(R.id.frag_chat_btn);
        chatlist = (TextView) view.findViewById(R.id.frag_chat_list);
        scroller = (ScrollView) view.findViewById(R.id.frag_chat_list_scroller);

        chatbox.setBackgroundColor(Color.TRANSPARENT);
        chatbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if ((id == R.id.chat_chatbox_send || id == EditorInfo.IME_NULL) && event.getAction() == KeyEvent.ACTION_DOWN) {
                    attemptSend(chatbox.getText().toString());
                    return true;
                }
                return false;
            }
        });

        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend(chatbox.getText().toString());
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW CHAT");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(AddChatReceiver, intentFilter);

        return view;
    }

    private BroadcastReceiver AddChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            if(data.getAction().equalsIgnoreCase("NEW CHAT")) {
                ShowMessage(data.getStringExtra("CHAT FROM"),data.getStringExtra("CHAT MESSAGE"),data.getIntExtra("CHAT TYPE", 1));
            }
        }
    };

    private void attemptSend(String MESSAGE){
        chatbtn.setEnabled(false);
        chatbtn.setText("");
        Handler ahan = new Handler();
        try {
            ahan.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SENDLOCKED = false;
                    chatbtn.setEnabled(true);
                    chatbtn.setText(getResources().getString(R.string.chat_chatbox_btn));
                }
            }, 2000);
        } catch (IllegalStateException err){

        }
        if (MESSAGE == null || MESSAGE.length()<1 || SENDLOCKED){
            return;
        }
        ((kr.devs.geniusbean.room.WaitingActivity) getActivity()).SendMessage(MESSAGE);
        chatbox.setText("");
        SENDLOCKED = true;
    }

    public void ShowMessage(String FROM,String MESSAGE,int TYPE){
        Log.d("GENIUSBEAN", "SHOWMESSAGE CALLED : " + MESSAGE);

        if (TYPE == 1){
            if (FROM.equals(BeanApplication.USER_NICK)){
                chatlist.append(Html.fromHtml("<br/>"+"<font color=#FFBB00>"+FROM+"</font> : "+MESSAGE));
            } else {
                chatlist.append(Html.fromHtml("<br/>"+"<font color=#FFFFFF>"+FROM+"</font> : "+MESSAGE));
            }
        } else if (TYPE == 0){
            chatlist.append(Html.fromHtml("<br/><font color=#B2EBF4>"+MESSAGE+"</font>"));
        }

        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(AddChatReceiver);
    }

}
package kr.devs.geniusbean.utils;

import android.support.v4.app.*;
import android.content.*;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kr.devs.geniusbean.room.Fragment_chat;
import kr.devs.geniusbean.room.Fragment_introduce;
import kr.devs.geniusbean.room.Fragment_ready;

public class GameViewPagerAdapter extends FragmentPagerAdapter{
	Context mContext;
	private Map<Integer, String> mFragmentTags;
	private FragmentManager mFragmentManager;

	public GameViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;
		mFragmentManager = fm;
		mFragmentTags = new HashMap<Integer, String>();

	}

	@Override
	public Fragment getItem(int position) {

		switch(position) {
			case 0:
				return Fragment.instantiate(mContext, Fragment_ready.class.getName(), null);
			case 1:
				return Fragment.instantiate(mContext, Fragment_chat.class.getName(), null);
			case 2:
				return Fragment.instantiate(mContext, Fragment_introduce.class.getName(), null);

		}
		return null;

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object obj = super.instantiateItem(container, position);
		if (obj instanceof Fragment) {
			Fragment f = (Fragment) obj;
			String tag = f.getTag();
			mFragmentTags.put(position, tag);
		}
		return obj;
	}

	public Fragment getFragment(int position) {
		String tag = mFragmentTags.get(position);
		if (tag == null)
			return null;
		return mFragmentManager.findFragmentByTag(tag);
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public int getItemPosition(Object object){
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

}

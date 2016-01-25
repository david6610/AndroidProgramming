package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
	@Override 
	protected Fragment createFragment() {
		//==============================
		// 在Activity中创建 Fragment 
		//
		return new CrimeListFragment();
		//==============================
	}

}

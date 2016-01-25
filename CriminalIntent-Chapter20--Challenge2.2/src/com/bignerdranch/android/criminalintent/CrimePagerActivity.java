package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;



public class CrimePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	
	private ArrayList<Crime> mCrimes;
	private CrimeFragment cFg;
	
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCrimes = CrimeLab.get(this).getCrimes();

		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mCrimes.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);

				/*
				 * 为了在CrimeFragment中捕获 返回键，存储 CrimeFragment cFg 
				 */
				//return CrimeFragment.newInstance(crime.getmId());
				cFg = CrimeFragment.newInstance(crime.getmId());
				return cFg;
						
			}
		});
		// 显示在标题的activity标题设置成Crime标题
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				Crime crime = mCrimes.get(pos) ;
				
				if(crime.getmTitle() != null) {
					setTitle(crime.getmTitle());
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		// 显示点击的 ID 页面
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for(int i =0; i<mCrimes.size(); i++) {
			if(mCrimes.get(i).getmId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("ActionBar", "OnKey事件");
		
		if(cFg instanceof CrimeFragment){
			//CrimeFragment.onKeyDown(keyCode, event);
			cFg.onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

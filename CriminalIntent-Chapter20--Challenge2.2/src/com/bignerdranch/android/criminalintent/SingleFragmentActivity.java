package com.bignerdranch.android.criminalintent;

//import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {

	protected abstract Fragment createFragment();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fragment);
		
		FragmentManager fm = getSupportFragmentManager();// getFragmentManager();
		
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		
		}
		
	}	
}



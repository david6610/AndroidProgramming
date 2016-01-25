package com.bignerdranch.android.criminalintent;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private ArrayList<Crime> mCrimes;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	//================
	//Chapter 17
	private static final String TAG = "CrimeLab";
	private static final String FILENAME = "crimes.json";
	private static final String PATHNAME = "crimes";
	
	private CriminalIntentJSONSerializer mSerializer;
	
	//=================
	
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		//mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
		mSerializer = new CriminalIntentJSONSerializer(mAppContext,PATHNAME, FILENAME);
		
		//mCrimes = new ArrayList<Crime>();
		
		
		/*
		for(int i = 0; i<100 ; i++) {
			Crime c = new Crime();
			c.setmTitle("Crime #" + i );
			c.setSolved(i % 2 == 0) ;
			mCrimes.add(c);
		}
		*/
		
		try {
			mCrimes = mSerializer.loadCrimes();
			
		}catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Erroe loading crimes: ", e);
		}
		
		
	}

	public static CrimeLab get(Context c) {
		if(sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return sCrimeLab;
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id) {
		for (Crime c:mCrimes) {
			if(c.getmId().equals(id))
				return c;
			
		}
		return null;
	}
	
	public void addCrime(Crime c) {
		mCrimes.add(c);
	}	
	
	public void removeCrime(UUID id) {
		for (Crime c:mCrimes) {
			if(c.getmId().equals(id))
				mCrimes.remove(c);
		}
	}	

	public void deleteCrime(Crime c) {
		// If Photo exists,delete it 
		
		if(c.getPhoto()!=null) {
			File file = new File(c.getPhoto().getPathname() +"/"+ 
								 c.getPhoto().getFilename());
			if(file.exists()) 
				file.delete();
		}
		
		mCrimes.remove(c);
	}	
	//================
	//Chapter 17
	public boolean saveCrimes() {
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes save to file");
			return true ;
		}catch (Exception e) {
			Log.e(TAG,"Erroe save crimes:",e);
			return false;
		}
	}
	
	public static String crimesPath() {
		return PATHNAME;
	}
	

}

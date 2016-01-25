package com.bignerdranch.android.criminalintent;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3070938125581959186L;
	
	private static final String JSON_PATHNAME = "pathname";
	private static final String JSON_FILENAME = "filename";
	
	private String mPathname;
	private String mFilename;
	
	// Create a photo representing an existing file on disk 
	
	public Photo(String filename) {
		mPathname = Environment.getExternalStorageDirectory() + "/"+
				CrimeLab.crimesPath() ;
		mFilename = filename;	
	}
	
	public Photo(JSONObject json) throws JSONException {
		mPathname = json.getString(JSON_PATHNAME);
		mFilename = json.getString(JSON_FILENAME);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_PATHNAME, mPathname);
		json.put(JSON_FILENAME, mFilename);
		return json;
	}
	
	
	public String getPathname() {
		return mPathname;
	}
	
	public String getFilename() {
		return mFilename;
	}

}

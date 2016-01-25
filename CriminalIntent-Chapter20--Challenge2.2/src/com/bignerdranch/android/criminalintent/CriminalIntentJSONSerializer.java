package com.bignerdranch.android.criminalintent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


public class CriminalIntentJSONSerializer {
	
	private static final String TAG = "CriminalIntentJSONSerializer";

	
	private Context mContext;
	private String mFilename;
	private String mPath;
	
	
	public CriminalIntentJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
		
	}

	public CriminalIntentJSONSerializer(Context c, String path, String f) {
		mContext = c;
		mFilename = f;
		mPath = path;
	}
	
	public ArrayList<Crime> loadCrimes() throws IOException , JSONException {
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			//Open and read the file into a StringBuilder
			//============================================
			if(!checkSdCard()) return null;
			File dir = createSDDir(mPath);
			File file = new File(dir,mFilename);  			
			
			//File file = new File(Environment.getExternalStorageDirectory()+"/"+mPath+"/",mFilename);  			
			FileInputStream in = new FileInputStream(file);  
			//============================================
			//InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				//Line breaks are omitted and irrelent 
				jsonString.append(line);
			}
			//parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
								.nextValue();
			//build the array of crimes from JSONObjects
			for(int i = 0; i<array.length(); i++) {
				crimes.add(new Crime(array.getJSONObject(i)));
				// debug 
				/*
				 * if( crimes.get(i).getPhoto().getFilename() == null) 
					Log.i(TAG,"Photo not Exist");
				   else
					Log.i(TAG,crimes.get(i).getPhoto().getFilename());
				*/
			} 
				
		} catch (FileNotFoundException e ) {
			// Ignore this one; it happens when starting fresh
		} finally {
			if(reader != null)
				reader.close();
			
		}
		return crimes;
		
	}
	public void saveCrimes(ArrayList<Crime> crimes) 
						throws JSONException, IOException {
		//Build an array in JSON
		JSONArray array = new JSONArray() ;
		for(Crime c : crimes)
			array.put(c.toJSON());
			
		
		//write the file to disk
		Writer writer = null;
		try {
			// ===============================
			if(!checkSdCard()) return ;
			File dir = createSDDir(mPath);
			
			File file = new File(dir,mFilename);  			
			FileOutputStream out = new FileOutputStream(file);  
			// ===============================
			//OutputStream out = mContext
			//		.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
			
		} finally {
			if (writer != null )
				writer.close();
		}
		
	}
    public boolean  checkSdCard() {  
        /* 判断存储卡是否存在 */  
        if (android.os.Environment.getExternalStorageState().equals(  
                android.os.Environment.MEDIA_MOUNTED)) {  
            return true;  
        } else {  
            return  false;  
        }  
    }  
	
  //在SD卡上创建一个文件夹
    public static File createSDDir(String dirName) throws IOException {
    	String SDPATH = Environment.getExternalStorageDirectory() + "/" ;
    			
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir.getAbsolutePath();
            dir.mkdir();
        }
        return dir;
    }
    
}

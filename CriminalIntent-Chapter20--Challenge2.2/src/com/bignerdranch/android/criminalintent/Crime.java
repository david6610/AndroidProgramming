package com.bignerdranch.android.criminalintent;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime implements Serializable {
	private static final long serialVersionUID = -6919461967497580385L;
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	//==============
	//Chapter 17
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	
	
	//================
	public Crime() {
		setmId(UUID.randomUUID());
		mDate = new Date();
		//================
		mTitle = new String("Crime #");
		mSolved  = false ;
		//================
	}

	/**
	 * @return the mId
	 */
	public UUID getmId() {
		return mId;
	}

	/**
	 * @param mId the mId to set
	 */
	public void setmId(UUID mId) {
		this.mId = mId;
	}

	/**
	 * @return the mTitle
	 */
	public String getmTitle() {
		return mTitle;
	}

	/**
	 * @param mTitle the mTitle to set
	 */
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	/**
	 * @return the mDate
	 */
	public Date getmDate() {
		return mDate;
	}

	/**
	 * @param mDate the mDate to set
	 */
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}
	
	public boolean isSolved() {
		return mSolved;
	}
	public void setSolved(boolean solved){
		mSolved = solved;
	}
	
	@Override 
	public String toString() {
		return mTitle;
	}
	
	public static Crime clone(Crime c1) {
		Crime c = new Crime();
		c.mId = c1.getmId();
		c.mTitle=new String(c1.getmTitle());
		c.mDate = (Date)c1.getmDate().clone();
		c.mSolved = c1.isSolved();
		if(c1.getPhoto() != null) {
			c.mPhoto = new Photo(c1.getPhoto().getFilename());
		}
		
		return c;
	}
	public void setTo(Crime c1) {
		mId = c1.getmId();
		mTitle= c1.getmTitle();
		mDate = c1.getmDate();
		mSolved = c1.isSolved();
		if(c1.getPhoto()!=null)
			mPhoto = c1.getPhoto();
	}
	
	//Chapter 17
	public Crime(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if(json.has(JSON_TITLE)){
			mTitle = json.getString(JSON_TITLE);
			
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		
		//Chapter 20
		if(json.has(JSON_PHOTO)) 
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID,  mId.toString());
		json.put(JSON_TITLE,  mTitle);
		json.put(JSON_SOLVED,  mSolved);
		json.put(JSON_DATE,  mDate.getTime());
		if(mPhoto != null){
			json.put(JSON_PHOTO,  mPhoto.toJSON());
		}
		
		return json;
		
	}
	
	//Chapter 20
	public Photo getPhoto() {
		return mPhoto;
	}
	
	public void setPhoto(Photo p) {
		mPhoto = p;
	}
}



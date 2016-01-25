package com.bignerdranch.android.criminalintent;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";
	
	private Date mDate;
	
	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		//create a calendar to get the year,month, and day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int hour =  calendar.get(Calendar.HOUR_OF_DAY);
		final int minute =  calendar.get(Calendar.MINUTE);
		final int second = calendar.get(calendar.SECOND);
		
		//-------------------------------------------
				
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener(){
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				//translate year , month, day,  into a Date object using a calendar
	    		mDate = new GregorianCalendar(year,month,day,hour,minute,second).getTime();
				
				//Update argument to preserve selected value on rotation
				getArguments().putSerializable(EXTRA_DATE, mDate);
				
			}
			
		});
		
		return new AlertDialog.Builder(getActivity())
					.setView(v)
					.setTitle(R.string.date_picker_title)
					//.setPositiveButton(android.R.string.ok,null)
					.setPositiveButton(android.R.string.ok, 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
										sendResult(Activity.RESULT_OK);
								}
							})
					.setNegativeButton(android.R.string.cancel, 
							new DialogInterface.OnClickListener() {
						
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									sendResult(Activity.RESULT_CANCELED);
								}
							})
					.create();
	}
	
	//chapter 12 ------------------------
	private void sendResult(int resultCode) {
		if(getTargetFragment() == null)
			return ;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
		
	}
	
	//-------------------------------------

}
package com.bignerdranch.android.criminalintent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.UUID;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CrimeFragment extends Fragment {
	private static final String TAG = "CrimeFragment";
	public static final String EXTRA_CRIME_ID =
			  "com.bingnerdranch.android.criminalintent.crime_id";
	public static final String EXTRA_CRIME = 
			"com.bingnerdranch.android.criminalintent.crime";
    public static final  String SER_KEY = 
    		"com.bingnerdranch.android.criminalintent.serial_key";  

    public static final int DELETE_ITEM = 10; 
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_PHOTO = 1;
	
	
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	
	private Button mSetOk,mSetCancel;

	private CheckBox mSolvedCheckBox;
	
	private DateFormat  mFormateDate; 
	
	private static final String DIALOG_DATE = "date";

	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	
	private static final String DIALOG_IMAGE = "image";
		
	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//UUID crimeID = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		UUID crimeID = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		Crime mCrimeInLab;
		//mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
		mCrimeInLab = CrimeLab.get(getActivity()).getCrime(crimeID);
		
		//mCrime = new Crime();
		mCrime = Crime.clone(mCrimeInLab);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle saveInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent,false);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		
		mTitleField.setText(mCrime.getmTitle());
		
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					CharSequence c,int start,int before,int count){
				mCrime.setmTitle(c.toString());
			}
			public void beforeTextChanged(
					CharSequence c,int start,int count,int after){
				//this space intentionally left blank
			}
			
			public void afterTextChanged(Editable c) {
				//This one too
			}
			
		});
		// chapter 8 
		
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		
		//-----exercise ---
		//mDateButton.setText(mCrime.getmDate().toString());
		//DateFormat.format("EEEE, MMMM dd, yyyy h:mmaa", mCrime.getmDate());
		/*
		 * 	"MM/dd/yy h:mmaa" -> "11/03/87 11:23am"
			"MMM dd, yyyy h:mmaa" -> "Nov 3, 1987 11:23am"
			"MMMM dd, yyyy h:mmaa" -> "November  3, 1987 11:23am"
			"E, MMMM dd, yyyy h:mmaa" -> "Tues , November 3, 1987 11:23am"
			"EEEE, MMMM dd, yyyy h:mmaa" -> "Tues day, Nov 3, 1987 11:23am"
		 */
		
		//chapter 12
		//mDateButton.setText(DateFormat.format("EEEE, MMMM dd, yyyy", mCrime.getmDate()));
		updateDate();
		
		// Chapter 12 -----------------------------------
		//mDateButton.setEnabled(false);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getSupportFragmentManager();
				
				//DatePickerFragment dialog = new DatePickerFragment();
				
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
				
				//Chapter 12-----
				
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				//---------------
								
				dialog.show(fm, DIALOG_DATE);
				
			}
		});
		
		//----------------------------------------------

		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		
		mSolvedCheckBox.setChecked(mCrime.isSolved());
				
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);				
			}
		});
		
		mSetOk = (Button)v.findViewById(R.id.crime_set_ok);
		mSetOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				returnResult(Activity.RESULT_OK);
			}
		});
		
		mSetCancel = (Button)v.findViewById(R.id.crime_set_cancel);
		mSetCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				returnResult(Activity.RESULT_CANCELED);
			}
		});
		
		//================
		//Chapter 18 Challenge 1
		//================
		v.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				v.startActionMode(new Callback(){

					@Override
					public boolean onActionItemClicked(ActionMode mode,
							MenuItem item) {
						// TODO Auto-generated method stub
						switch (item.getItemId()){
						case R.id.menu_item_delete_crime:
							mode.finish();
							returnResult(DELETE_ITEM);
							return true;
						default :
							return false;
						}
						
					}

					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						// TODO Auto-generated method stub
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.crime_list_item_context, menu);
						return true;
					}

					@Override
					public void onDestroyActionMode(ActionMode mode) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public boolean onPrepareActionMode(ActionMode mode,
							Menu menu) {
						// TODO Auto-generated method stub
						return false;
					}
					
				});

				return false;
			}
			
		}) ;
		
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				
				startActivityForResult(i, REQUEST_PHOTO);
				
			}
		});
		
		// chapter 20 section 3
		mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Challenge 20.2
				
				Photo p = mCrime.getPhoto();
				if(p == null) {
					Log.i(TAG,"There is not photo in this crime.");
					Toast.makeText(getActivity(),
							"The Photo not extist!", 
							Toast.LENGTH_LONG).show();
					return ;
				}

				Log.i(TAG,"Path:" + mCrime.getPhoto().getPathname() + "  File:"+ 
			             mCrime.getPhoto().getFilename());

				File file = new File(mCrime.getPhoto().getPathname() +"/" +
			             mCrime.getPhoto().getFilename());
				
				if(!file.exists()) {
					Toast.makeText(getActivity(), 
							"The Photo file not found !", 
							Toast.LENGTH_LONG).show();
					return;
				}

				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				
				String path = p.getPathname() + "/" + p.getFilename(); 
				
				//=====================================
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
				
			}
		});
		
		
		// Chapter 20 challende section 20.7
		//Chapter 20.7 OnLongClick delete photo file
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
			// Chapter 20.7.1; reference Chapter 18.1/18.2
			registerForContextMenu(mPhotoView);
		else {
			// Chapter 20.7.2; reference Chapter 18.3

			mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
			
				@Override
				public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
					getActivity().startActionMode(new ActionMode.Callback() {
						
						@Override
						public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override
						public void onDestroyActionMode(ActionMode arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public boolean onCreateActionMode(ActionMode mode, Menu menu) {
							// TODO Auto-generated method stub
							MenuInflater inflater = mode.getMenuInflater();
							inflater.inflate(R.menu.crime_photo_delete, menu);
							
							return true;
						}
						
						@Override
						public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
							// TODO Auto-generated method stub
							deletePhoto();

							return false;
						}
					});
				
					return true;
				}
			});
		}
		
		//if camear is not available ,disable camera functionality
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
							pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
							Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
							Camera.getNumberOfCameras() >0 ;
		
		if(!hasACamera) {
			mPhotoButton.setEnabled(false);
		}
		
		return v;
	}
	
	/*
	 * 
	 */
		
	public static CrimeFragment newInstance(UUID crimeID) {
		Bundle args = new Bundle();
		
		args.putSerializable(EXTRA_CRIME_ID, crimeID);		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
		
	}
	
	// chapter 12 --------
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return ;
		
		if(requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setmDate(date);
			
			//mDateButton.setText(mCrime.getmDate().toString());
			updateDate();
		} else if(requestCode == REQUEST_PHOTO){
			//Challenge 02 erease older photo

			if(mCrime.getPhoto() != null) {

				File file = new File(mCrime.getPhoto().getPathname() +"/" +
									mCrime.getPhoto().getFilename());
				if(file.exists())
					file.delete();
			}

			//Create a new Photo object and attach it to the crime
			String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (filename != null) {
				//Chapter 20-section 2
				Log.i(TAG,"filename: " + filename);
				
				//Chapter 20-section 3
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				
				showPhoto();
				//Log.i(TAG,"Crime: " + mCrime.getmTitle() + " has a photo");
				
			}
		}
	}
	
	public void updateDate() {
		mDateButton.setText(mCrime.getmDate().toString());
	}
	//--------------------
	
	public void returnResult(int resultCode){
		//=======================================================
		
		//Intent i = new Intent(EXTRA_CRIME);
		Intent i = new Intent();
		Bundle args = new Bundle();
		args.putSerializable(SER_KEY,mCrime);
		i.putExtras(args);
		getActivity().setResult(resultCode, i);

		Log.i(TAG,"CrimeFragment return to CrimeListFragment.");

		getActivity().finish();

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.d("CrimeFragmet事件", "捕获到返回键OK");
            return false;
        }
        return true;
    }
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	//Chapter 20 Section 4
	 
	private void showPhoto() {
		// (Re)set the image button's image based on our photo
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if(p != null) {
			
			// Directory Changed by me ===
			//Environment.getExternalStorageDirectory() + "/"+
			//		CrimeLab.crimesPath(), filename);
			//============================
			
			//String path = getActivity()
			//		.getFileStreamPath(p.getFilename()).getAbsolutePath();

			
			String path = Environment.getExternalStorageDirectory() + "/"+
							CrimeLab.crimesPath() +"/" + p.getFilename();

			Log.i(TAG,"CrimeImage:" + path);

			File file = new File(path);
			if(!file.exists())
				return ;
			
			b = PictureUtils.getScaledDrawable(getActivity(), path);
			
		}
		
		mPhotoView.setImageDrawable(b);
		
	}
	
	@Override 
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

	// Chapter 20 challende section 20.7
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_photo_delete, menu);
	}
	
	// Chapter 20 challende section 20.7
	@Override 
	public boolean onContextItemSelected(MenuItem item) {
		deletePhoto();
		return true;

	}
	
	private void deletePhoto() {
		Photo p = mCrime.getPhoto();
		if(p == null) {
			Log.i(TAG,"There is not photo in this crime.");
			Toast.makeText(getActivity(),
					"The Photo not extist!", 
					Toast.LENGTH_LONG).show();
			return ;
		}

		Log.i(TAG,"Path:" + mCrime.getPhoto().getPathname() + "  File:"+ 
	             mCrime.getPhoto().getFilename());

		File file = new File(mCrime.getPhoto().getPathname() +"/" +
	             mCrime.getPhoto().getFilename());
		
		if(file.exists()) {
			mCrime.setPhoto(null);
			file.delete();
			Toast.makeText(getActivity(), 
					"The Photo has been deleted!", 
					Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getActivity(), 
					"The Photo not exist!", 
					Toast.LENGTH_LONG).show();
		}
		
		showPhoto();
	}
	
}

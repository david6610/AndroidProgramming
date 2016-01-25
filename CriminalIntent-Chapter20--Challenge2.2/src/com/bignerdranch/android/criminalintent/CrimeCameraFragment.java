package com.bignerdranch.android.criminalintent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	
	public static final String EXTRA_PHOTO_FILENAME = 
					"com.bignerdranch.android.criminalintent.photo_filename";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContainer;
	
	@Override 
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button takePictureButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//getActivity().finish();
				takepicture(v);
				
			}
		});
		
		mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		//setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated.
		//but are required for Camera preview to work on pre-3.0 devices.
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if(mCamera != null) {
					mCamera.stopPreview();
				}
				
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				//Tell the camera to use this surface as its preview area
				try {
					if (mCamera != null ){
						mCamera.setPreviewDisplay(holder);
						
					}
					
				}catch (IOException exception) {
					Log.e(TAG,"Error setting up preview display",exception);
					
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				// TODO Auto-generated method stub
				if(mCamera == null) return;
				
				//the Surface has changed size; update the camera preview size
				Camera.Parameters parameters = mCamera.getParameters();
				//Size s = null; //To be reset in the next section
				
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(),w,h);
				parameters.setPreviewSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
					
				}catch (Exception e) {
					Log.e(TAG, "Could not start preview",e);
					mCamera.release();
					mCamera = null;
				}
				
			}
		});
		
		
		
		return v;
	}
	
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
			
		}else {
			mCamera = Camera.open();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		
		if(mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	/*
	 * A Simple algorithm to get the largest size availzble , For a more
	 * robust version , see CameraPreview.java in the ApiDemos
	 * sample app from Android .
	 */
	private Size getBestSupportedSize(List<Size> sizes, int width, int height){
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for(Size s:sizes) {
			int area = s.width * s.height;
			if(area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}
    public void takepicture(View v){
        /*
         * shutter:快门被按下
         * raw:相机所捕获的原始数据
         * jpeg:相机处理的数据
         */
        mCamera.takePicture(mShutterCallback, null, mJpegCallback);
    }
    
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			// Display the progress indicator
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
    
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			//Create a filename
			String filename = UUID.randomUUID().toString() + ".jpg";
			
			//save the jpeg data to disk
			FileOutputStream os = null;
			boolean success = true;
			
			try {
				Log.e(TAG, "Try to create file" );

				File file = new File(Environment.getExternalStorageDirectory() + "/"+
								CrimeLab.crimesPath(), filename);
				os = new FileOutputStream(file);
				//os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
                //camera.startPreview();
				
			}catch (Exception e) {
				Log.e(TAG, "Error writing to file " + filename, e);
				success = false;
				
			}finally {
				try {
					if(os != null) 
						os.close();
					
				}catch (Exception e ) {
					Log.e(TAG, "Error closing file " + filename, e) ;
					success = false;
				}
			}
			if(success) {
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK, i);
				Log.e(TAG, "JPEG saved at " + filename);
			}else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			
			getActivity().finish();
		}
	};
	

}


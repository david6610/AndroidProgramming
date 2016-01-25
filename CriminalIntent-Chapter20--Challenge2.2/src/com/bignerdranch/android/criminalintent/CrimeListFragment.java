package com.bignerdranch.android.criminalintent;


import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.R.layout;


public class CrimeListFragment extends ListFragment {
	
	private ArrayList<Crime> mCrimes;
	private static final String TAG = "CrimeListFragment";
	private static final int REQUEST_NEW_CRIME = 1;
	private static final int REQUEST_MODIFIED_CRIME = 2;
	
	@Override 
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setHasOptionsMenu(true);
		
		
		getActivity().setTitle(R.string.crimes_title);
		
		
		/*
		 * 调用 CrimeLab的静态方法get
		 * 在get（）内部创建CrimeLab对象，（单例）。同时返回对象
		 * 在CrimeListFragment中使用此对象： mCrimes 进行工作
		 */
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		//--显示 圆圈圈 的 运行图标，资源布局使用系统却省的：
		//              android.R.layout.simple_list_item_1
		//ArrayAdapter<Crime>adapter = 
		//		new ArrayAdapter<Crime>(getActivity(),
		//				                android.R.layout.simple_list_item_1,
		//				                mCrimes);
		
		// 创建 控制器 对象 Adapter ----- 用于控制：模型与视图之间的交换控制
		/*
		 * （1）创建必要的视图对象
		 * （2）用模型层数据填充视图对象
		 * （3）将准备好的视图对象返回给ListView
		 */
		
		
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		
		setListAdapter(adapter);
		
	}
	
	//==================================================
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle saveInstanceState) {
		//method 1:
		View v = inflater.inflate(R.layout.fragment_list_view, parent,false);
	   
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			//Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onActionItemClicked(
						android.view.ActionMode mode, MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()){
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
						CrimeLab crimelab = CrimeLab.get(getActivity());
						
						for(int i=adapter.getCount()-1; i>=0; i--) {
							if(getListView().isItemChecked(i)){
								crimelab.deleteCrime(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default :
						return false;
					}
				}

				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode,
						Menu menu) {
					// TODO Auto-generated method stub
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(android.view.ActionMode arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onPrepareActionMode(
						android.view.ActionMode arg0, Menu arg1) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(
						android.view.ActionMode mode, int position, long id,
						boolean checked) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		}
	    
		return v;
	}
	
	
	//==================================================
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id){

		//getKistAdapter()是ListFragment类的便利方法，该方法可返回设置在ListFragment列表视图上的adapter。
		//调用adapter的getItem（int）方法，把结果转换成Crime对象
		Crime c = (Crime)(getListAdapter()).getItem(position);//必须类型转换
		
		//Log.d(TAG, c.getmTitle()+ " was clicked.");
		
		//start CrimeActivity
		//Intent i = new Intent(getActivity(), CrimeActivity.class);

		//start CrimePagerActivity 
		
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getmId());
		
		startActivityForResult(i,REQUEST_MODIFIED_CRIME);
		
	}

	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes ) {
			super(getActivity(), 0, crimes);			
		}

		/*
		 * 在getView（） 内部，
		 * （1）adapter使用数组列表中指定位置的列表项，创建一个视图对象
		 * （2）将该对象返回给ListView
		 * （3）ListView 将其设置为自己的子视图
		 * （4）ListView刷新，在屏幕上显示 
		 * 
		 * ListView与Adapter 之间会话：
		 * 调用 getCount（） 获得总数，然后再调用 getView() 循环调用多次完成所有项目的显示
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//if we weren't given a view , inflate on 
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_crime, null);//使用新设计的R.layout.list_item_crime布局 
			}
			
			
			//configure the view for this Crime 
			Crime c = getItem(position);  //可以不类型转换
						
			TextView titleTextView = 
					(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getmTitle());
			TextView dateTextView = 
					(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(c.getmDate().toString());
			
			CheckBox solvedCheckBox = 
					(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			return convertView;
		}
		
	}

	@Override 
	public void onResume(){
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			Intent i = new Intent(getActivity(),CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getmId());
			startActivityForResult(i, REQUEST_NEW_CRIME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true;
			
		}
		return super.onContextItemSelected(item);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//获取 crime
		Log.i(TAG,"Return from CrimeFragment.");

		Crime crime = (Crime)data.getSerializableExtra(CrimeFragment.SER_KEY);
		int pos = -1;

		
		// 找到 返回的 Crime 是 mCrimes 中的第几条记录来，后续用来决定是否修改 
		for(int i =0; i<mCrimes.size(); i++) {
			if(mCrimes.get(i).getmId().equals(crime.getmId())) {
				pos = i;
				break;
			}
		}
		//根据请求码决定是“新增”、“修订”哪个发出的请求
		switch (requestCode) {
			case REQUEST_NEW_CRIME:   //新增 记录
						
				if(resultCode == Activity.RESULT_OK)  //结果代码是 OK
					mCrimes.get(pos).setTo(crime);
				else {                                //结果代码是Cancel 
					Log.i(TAG,"_NEW_CRIME_Cancel");
					mCrimes.remove(pos);		// remove
				}
				break;
			case REQUEST_MODIFIED_CRIME: //修改记录
				if(resultCode == Activity.RESULT_OK) {  //结果代码是OK
					 Log.i(TAG,"_CRIME_MODIFIED_OK_");
					 mCrimes.get(pos).setTo(crime);   
				}else if(resultCode == Activity.RESULT_CANCELED) {//结果代码是Cancel
					 Log.i(TAG,"_CRIME_MODIFIED_CANCELED_");
				}
				else if(resultCode == CrimeFragment.DELETE_ITEM) {
					mCrimes.remove(pos);		// remove
				}
				break;
			default:
				break;
		}
	}
	
}


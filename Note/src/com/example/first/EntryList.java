package com.example.first;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EntryList extends Activity implements OnGestureListener {
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> mDataList;
	private MyDataBaseAdapter mDataBaseAdapter;
	private SimpleAdapter adapter;
	private GestureDetector detector;
	
	private void setData() {
		mDataList = new ArrayList<HashMap<String, Object>>();
		Cursor cursor = mDataBaseAdapter.queryAllFromEntry();
		cursor.moveToNext();
		String str = null;
		long id[] = new long[1];
		while(!cursor.isAfterLast()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				str = cursor.getString(2);
				id[0] = cursor.getLong(0);
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			if(str == null || str.equals(""))
				str = "非文本事件";
			
			map.put("id", id[0]);
			map.put("text", str);
			mDataList.add(map);
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_list);
        
        detector = new GestureDetector(this);
        
        mDataBaseAdapter = new MyDataBaseAdapter(this);
        mDataBaseAdapter.open();
        
        mListView = (ListView)findViewById(R.id.entry_list);
        
        setData();

        adapter = new SimpleAdapter(this,
        		mDataList,
        		R.layout.entry_item,
        		new String[] {"id", "text"},
        		new int[] {R.id.id, R.id.entry_text});
        
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(itemClickListener);
        mListView.setOnItemLongClickListener(itemLongClickListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			TextView id = (TextView)view.findViewById(R.id.id);
			// TODO Auto-generated method stub
			Intent intent = new Intent(EntryList.this, EntryInfo.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", id.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
		
	};
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				final int arg2, long arg3) {
			// TODO Auto-generated method stub
			final TextView id = (TextView)view.findViewById(R.id.id);
			AlertDialog.Builder dlg = new AlertDialog.Builder(EntryList.this)
			.setTitle("删除事件")
			.setMessage("确定删除吗?")
			.setCancelable(false)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					mDataBaseAdapter.deleteFromEntry(id.getText().toString());
					mDataList.remove(arg2);
					adapter.notifyDataSetChanged();
				}
			});
			dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			dlg.show();
			return false;
		}
	};
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent Ev1, MotionEvent Ev2, float velocityX,
			float velocityY) {
		if ( (Ev2.getX() - Ev1.getX() > 100 && Math.abs(velocityX) > 100
				&& Math.abs(velocityX)/Math.abs(velocityY) > 2) ) {  
			Intent intent = new Intent(EntryList.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			finish();
	    }
		return true;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){	
		this.detector.onTouchEvent(ev);//在这里先处理下你的手势左右滑动事件
	    return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent Ev1, MotionEvent Ev2, float distanceX,
			float distanceY) {
		if ( (Ev2.getX() - Ev1.getX() > 400
				&& Math.abs(distanceX)/Math.abs(distanceY) > 2) ) {  
			Intent intent = new Intent(EntryList.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			finish();
	    }
		return true;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}

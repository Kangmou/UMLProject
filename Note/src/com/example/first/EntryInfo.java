package com.example.first;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class EntryInfo extends Activity {
	private Button ensure;
	private Button cancel;
	private Spinner kind;
	private Spinner deadline;
	private EditText text;
	private EditText emergency;
	private MyDataBaseAdapter mDataBaseAdapter;
	private int ktID;
	private int dlID;
	private String vp = null;
	private String st = null;
	private int em = 1;
	private List<String> mkindList;
	private static final String[] mdeadlineItem = 
			{ "12小时内", "1天内", "2天内", "3天内" };
	private ArrayAdapter<String> adapter_kind;
	private ArrayAdapter<String> adapter_deadline;
	private ImageButton play;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_info);
        
        play = (ImageButton)findViewById(R.id.info_voice);
        
        ensure = (Button)this.findViewById(R.id.info_ensure);
        cancel = (Button)this.findViewById(R.id.info_cancel);
        
        kind = (Spinner)this.findViewById(R.id.info_kind);
        deadline = (Spinner)this.findViewById(R.id.info_deadline);
        
        text = (EditText)findViewById(R.id.info_entry);
        emergency = (EditText)findViewById(R.id.info_emergency);
        
        mDataBaseAdapter = new MyDataBaseAdapter(this);
        mDataBaseAdapter.open();
        
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");
        Log.e("errorr", id);

        mkindList = new ArrayList<String>();
        Cursor cursor_kind = mDataBaseAdapter.queryAllFromKind();
        while(!cursor_kind.isAfterLast()) {
        	try {
            	mkindList.add(cursor_kind.getString(1));
            }
            catch(Exception e) {
            	e.printStackTrace();
            }    
            cursor_kind.moveToNext();
        }
        cursor_kind.close();
        
        Cursor cursor = mDataBaseAdapter.queryFromEntry(id);
        while(!cursor.isAfterLast()) {
        	try {
            	st = cursor.getString(2);
            	em = cursor.getInt(3);
            	vp = cursor.getString(4);
            	String kt = cursor.getString(1);
            	String dl = cursor.getString(5);	
         
            	for( int i = 0; i < mkindList.size(); i++ ){
            		if( mkindList.get(i).equals(kt)){
            			ktID = i;
            			break;
            		}
            	}
            	for( int i = 0; i < mdeadlineItem.length; i++ ){
            		if( mdeadlineItem[i].equals(dl)){
            			dlID = i;
            			break;
            		}
            	}
            }
            catch(Exception e) {
            	e.printStackTrace();
            }    
            text.setText(st);
            emergency.setText("" + em);
            cursor.moveToNext();
        }
        cursor.close();
        
        adapter_kind = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
        		mkindList);
        adapter_kind.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kind.setAdapter(adapter_kind);
        kind.setSelection(ktID, true);
        
        adapter_deadline = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
        		mdeadlineItem);
        adapter_deadline.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadline.setAdapter(adapter_deadline);
        deadline.setSelection(dlID, true);
        
        play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(vp != null && (!vp.equals(""))) {
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(vp)), "audio");
					startActivity(intent);
				}
			}
        	
        });
        
        ensure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mDataBaseAdapter.updateFromEntry(Long.parseLong(id), text.getText().toString(),
						kind.getSelectedItem().toString(), deadline.getSelectedItem().toString(),
						vp, Integer.parseInt(emergency.getText().toString()));
				
				Intent intent = new Intent(EntryInfo.this,EntryList.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				finish();
			}
        	
        });
        
        cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(EntryInfo.this,EntryList.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				finish();
			}
        	
        });
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

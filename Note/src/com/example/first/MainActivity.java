package com.example.first;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity{
	
	private LinearLayout kind;
	private Button add;
	private ImageView touchPoint;
	private LinearLayout colorBar;
	private DisplayMetrics metric;
	private int screenWidth;
	private int screenHeight;
	private Button newDirectely;
	private Button newByVoice;
	private Button newByText;
	private File mRecAudioFile;
	private File mRecAudioPath;
	private MediaRecorder mMediaRecorder;
	private LinearLayout linearLayout;
	private String strTempFile = "recaudio_";
	private List<Button> mItemList = new ArrayList<Button>();
	private List<Button> mTimeList = new ArrayList<Button>();
	private boolean voiceFlag = true;
	private boolean directFlag = true;
	private boolean textFlag = true;
	private MyDataBaseAdapter mDataBaseAdapter;
	private String entryKind = null;
	private String voicePath = null;
	private String entryText = null;
	private String deadline  = null;
	private int emergency = 0;
	
	private void setData() {
		Button half, one, two, three;
		half = (Button)findViewById(R.id.half);
		one = (Button)this.findViewById(R.id.one);
		two = (Button)this.findViewById(R.id.two);
		three = (Button)this.findViewById(R.id.three);
		mTimeList.add(half);
		mTimeList.add(one);
		mTimeList.add(two);
		mTimeList.add(three);
		
		Cursor cursor = mDataBaseAdapter.queryAllFromKind();
		cursor.moveToNext();
		String str = null;
		while(!cursor.isAfterLast()) {
			final Button newButton = new Button(MainActivity.this);
			final long id[] = new long[1];
			try {
				str = cursor.getString(1);
				id[0] = cursor.getLong(0);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			newButton.setText(str);
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(5, 5, 5, 5);
			newButton.setLayoutParams(params);
			newButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_shape));
			newButton.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this)
					.setTitle("删除事件类型")
					.setMessage("确定删除?")
					.setCancelable(false)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							newButton.setVisibility(View.GONE);
							mDataBaseAdapter.deleteFromKind(id[0]);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
						}
					});
					d.show();
					return false;
				}
				
			});
			newButton.setOnTouchListener(myButtonTouchListener);
			kind.addView(newButton);
			mItemList.add(newButton);
			cursor.moveToNext();
		}
		cursor.close();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDataBaseAdapter = new MyDataBaseAdapter(this);
        mDataBaseAdapter.open();
        
        linearLayout = (LinearLayout)findViewById(R.id.container);
        linearLayout.setOnTouchListener(nmb);
        
        newDirectely = (Button)findViewById(R.id.new_directely);
        newByVoice = (Button)findViewById(R.id.new_by_voice);
        newByText = (Button)findViewById(R.id.new_by_text);

        kind = (LinearLayout)findViewById(R.id.kind);
        add = (Button)findViewById(R.id.add);
        
        setData();
        
        add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_dialog, null);
				
				AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this)
				.setView(view)
				.setTitle("添加事件类型")
				.setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						final Button newButton = new Button(MainActivity.this);
						EditText edt = (EditText)view.findViewById(R.id.new_kind);
						final String str = edt.getText().toString();
						final long id = mDataBaseAdapter.insertIntoKind(str);
						newButton.setText(str);
						LinearLayout.LayoutParams params = 
								new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.WRAP_CONTENT,
										LinearLayout.LayoutParams.MATCH_PARENT);
						newButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_shape));
						newButton.setLayoutParams(params);
						params.setMargins(5, 5, 5, 5);
						newButton.setOnLongClickListener(new OnLongClickListener() {

							@Override
							public boolean onLongClick(View arg0) {
								// TODO Auto-generated method stub
								//mDataBaseAdapter.deleteFromKind(str);
								AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this)
								.setTitle("删除事件类型")
								.setMessage("确定删除?")
								.setCancelable(false)
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										mDataBaseAdapter.deleteFromKind(id);
										newButton.setVisibility(View.GONE);
									}
								})
								.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										
									}
								});
								d.show();
								return false;
							}
							
						});
						newButton.setOnTouchListener(myButtonTouchListener);
						kind.addView(newButton);
						mItemList.add(newButton);
					}
					
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				dlg.show();
			}
        	
        });
        
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        
        touchPoint = (ImageView)findViewById(R.id.touchpoint);
        
        colorBar = (LinearLayout)findViewById(R.id.emergency);
        colorBar.setOnTouchListener(new OnTouchListener() {
        	int lastX, lastY;

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				int x, y, dx, dy;
				int left, top, right, bottom;
				int width = touchPoint.getWidth(), height = touchPoint.getHeight();
				// TODO Auto-generated method stub
				switch(motionEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int)motionEvent.getX();
					y = (int)motionEvent.getY();
					lastX = (int)motionEvent.getRawX();
					lastY = (int)motionEvent.getRawY();
				    touchPoint.layout((x - width  / 2),
							          (y - height / 2),
							          (x + width  / 2),
							          (y + height / 2));
				    touchPoint.setVisibility(View.VISIBLE);
				    emergency = (int)(motionEvent.getX() / screenWidth * 100);
					break;
				case MotionEvent.ACTION_MOVE:
					dx = (int)motionEvent.getRawX() - lastX;
					dy = (int)motionEvent.getRawY() - lastY;
					left = touchPoint.getLeft() + dx;
					top = touchPoint.getTop() + dy;
					right = touchPoint.getRight() + dx;
					bottom = touchPoint.getBottom() + dy;
					if(left < 0) {
						left = 0;
						right = left + width;
					}
					if(right > screenWidth) {
						right = screenWidth;
						left = right - width;
					}
					if(top < 0) {
						top = 0;
						bottom = top + height;
					}
					if(bottom > screenHeight) {
						bottom = screenHeight;
						top = bottom - height;
					}
					touchPoint.layout(left, top, right, bottom);
					lastX = (int)motionEvent.getRawX();
					lastY = (int)motionEvent.getRawY();
					kindLoop(lastX, lastY);
					deadlineLoop(lastX, lastY);
					_newDirectely(lastX, lastY);
					_newByVoice(lastX, lastY);
					_newByText(lastX, lastY);
					break;
				case MotionEvent.ACTION_UP:
					touchPoint.layout(0,  0, width, height);
					touchPoint.setVisibility(View.INVISIBLE);
					break;
				}
				return true;
			}
        	
        });
    }
    
    public void kindLoop(int lastX, int lastY) {
    	for(int i = 0; i < mItemList.size(); i++ ){
			Button temp = (Button)mItemList.get(i);
			int location[] = getLocationOfView(temp);
			if( lastX > location[0] && lastX < location[2] 
					&& lastY > location[1] && lastY < location[3] ){
				temp.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed));
				entryKind = temp.getText().toString();
			}
			else
				temp.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal));
		}
    }
    
    public void deadlineLoop(int lastX, int lastY) {
    	for(int i = 0; i < mTimeList.size(); i++ ){
			Button temp = (Button)mTimeList.get(i);
			int location[] = getLocationOfView(temp);
			if( lastX > location[0] && lastX < location[2] 
					&& lastY > location[1] && lastY < location[3] ){
				temp.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed));
				deadline = temp.getText().toString();
			}
			else
				temp.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal));
		}
    }
    
    public void _newDirectely(int lastX, int lastY) {
    	if(directFlag == false)
    		return;
    	
    	int location[] = getLocationOfView(newDirectely);
    	
		if( lastX > location[0] && lastX < location[2] 
				&& lastY > location[1] && lastY < location[3] ) {
			directFlag = false;
			mDataBaseAdapter.insertIntoEntry(entryKind,
					null, emergency, null, deadline);
	    	startActivity(new Intent(MainActivity.this,
	    			EntryList.class));
			finish();
		}
    }
    
    public void _newByText(int lastX, int lastY) {
    	if(textFlag == false)
    		return;
    	
    	int location[] = getLocationOfView(newByText);
    	
		if( lastX > location[0] && lastX < location[2] 
				&& lastY > location[1] && lastY < location[3] ) {
			textFlag = false;
			final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog, null);
			final EditText edt = (EditText)view.findViewById(R.id.new_entry);
			
			Dialog dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("新建事件")
			.setView(view)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					entryText = edt.getText().toString();
					mDataBaseAdapter.insertIntoEntry(entryKind, entryText,
							emergency, null, deadline);
			    	startActivity(new Intent(MainActivity.this, EntryList.class));
					finish();
				}
				
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			}).create();
			dialog.show();
		}
    }
    
    public void _newByVoice(int lastX, int lastY) {
    	
    	if(voiceFlag == false)
    		return;
    	
    	int location[] = getLocationOfView(newByVoice);
    	
		if( lastX > location[0] && lastX < location[2] 
				&& lastY > location[1] && lastY < location[3] ) {
			
			voiceFlag = false;
			
			touchPoint.setVisibility(View.INVISIBLE);
			
			mRecAudioPath = Environment.getExternalStorageDirectory();
    	
	    	Dialog dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("是否开始录音?")
			.setCancelable(false)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					try {
						mRecAudioFile = File.createTempFile(strTempFile,
								".amr", mRecAudioPath);
						mMediaRecorder = new MediaRecorder();
						mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
					    mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
					    voicePath = mRecAudioFile.getAbsolutePath();
						mMediaRecorder.prepare();
						mMediaRecorder.start();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("录音中")
					.setMessage("正在录音...")
					.setNegativeButton("完毕", new DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							if(mRecAudioFile != null) {
								mMediaRecorder.stop();
								mMediaRecorder.release();
								mMediaRecorder = null;
								mDataBaseAdapter.insertIntoEntry(entryKind, null, emergency,
										voicePath, deadline);
								startActivity(new Intent(MainActivity.this, EntryList.class));
								finish();
							}
							voiceFlag = true;
						}
					}).show();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					voiceFlag = true;
				}
			}).create();
			dialog.show();
		}
    }
    
    public int[] getLocationOfView(View view) {
    	int[] location = new int[2]; 
		int[] result = new int[4];
		
        view.getLocationOnScreen(location);
        
        result[0] = location[0];
        result[1] = location[1];
        
		result[2] = location[0] + view.getWidth();
		result[3] = location[1] + view.getHeight();
		
		return result;
    }
    
    private OnTouchListener myButtonTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    };

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	private OnTouchListener nmb = new OnTouchListener() {
		float lastX = 0, lastY = 0, curX, curY;
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			// TODO Auto-generated method stub
			switch(motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastX = motionEvent.getRawX();
				lastY = motionEvent.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				curX = motionEvent.getRawX();
				curY = motionEvent.getRawY();
				if(lastX - curX > 100 && Math.abs(lastY - curY) < 100) {
					Intent intent = new Intent(MainActivity.this, EntryList.class);
					startActivity(intent);
					finish();
				}
				break;
			}
			return true;
		}
	};
}

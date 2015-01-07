package com.aicfeng.denglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Getdaiban {

	private Context context;
	private PullToRefreshListView  listview;
	private ProgressDialog progressDialog;
	private View moreView;
	private SimpleAdapter	mSimpleAdapter;
	private JSONArray jsonArray;
	private int tiaoshu;
	private Map<String, String> map;
	private TextView listcount;
	static List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	static List<String> list_title = new ArrayList<String>();
	public  Getdaiban(Context context,PullToRefreshListView  listView,ProgressDialog progressDialog,View moreView,TextView listcount )
	{
		this.context = context;
		this.listview = listView;
		this.progressDialog = progressDialog;
		this.moreView = moreView;
		this.listcount = listcount;
	}
	
	public void  getDaiban (final String httpRequest,final int start,final String flag)
	{
		new Thread()
		{
			public void run()
			{
				String result = com.aicfeng.denglu.httpRequest.get(httpRequest);
				if (result.contains("timed out")||result.contains("请求失败"))
				{
					handler.sendEmptyMessage(1);
				}
				else
				{
				try {
					jsonArray = new JSONArray(result);
					list.clear();
					for(int i = 0;i<jsonArray.length();i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						String InstanceID = jsonObject.getString("InstanceID");
						String title = jsonObject.getString("title");
						String FlowID = jsonObject.getString("FlowID");
						String GroupID = jsonObject.getString("GroupID");
						String StepID = jsonObject.getString("StepID");
						String TaskID = jsonObject.getString("TaskID");
						map = new HashMap<String, String>();
						list_title.add(title);
						map.put("InstanceID", InstanceID);
						map.put("title", title);
						map.put("FlowID", FlowID);
						map.put("GroupID", GroupID);
						map.put("StepID", StepID);
						map.put("TaskID", TaskID);
						list.add(map);	
					}
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		}.start();
		
	}
	
	
	private  Handler handler = new Handler()
	{
		 @Override  
	        public void handleMessage(Message msg) {  
			 switch (msg.what)
	        	{
	        	case 0:      		
	        	mSimpleAdapter = new SimpleAdapter(context, list, R.layout.listitem,
	                        new String[] { "title", "InstanceID" ,"FlowID", "GroupID","StepID","TaskID"},
	                        new int[] { R.id.tv_title, R.id.tv_content ,R.id.item_FlowID, R.id.item_GroupID,R.id.item_StepID,R.id.item_TaskID});
	                // 加上底部View，注意要放在setAdapter方法前
	             //listview.addFooterView(moreView);
	             listview.setAdapter(mSimpleAdapter);
	             listcount.setText(mSimpleAdapter.getCount()+"");
	   			 //listview.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,list_title));
				 progressDialog.dismiss();
				 listview.onRefreshComplete(); 
	            break;
	        	case 1:
	        		progressDialog.dismiss();
	        		Common.showDialog(context,2);
	        		listview.onRefreshComplete(); 
	        		break;
	        	}
		 }
	};
	
	
	public void getMoreData(String httpString,int start) throws JSONException
	{
		
		 getDaiban (httpString,start,"more");
	}
	
}

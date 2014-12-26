package com.aicfeng.denglu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class NeiRong extends Activity {

	 private long mExitTime;
	 private View moreView;
	 private Button bt_moredata;
	 private ProgressBar pg;
	 private String account,Goden_id;
	 private static TextView textView;
	 private Button bt_down;
	 private LinearLayout yiban,daiban;
	 private ProgressBar progressBar;
	 private TextView textView_name,listType;
	 private PullToRefreshListView  list_daiban;
		private ProgressDialog progressDialog;
		private TextView listcount;
		private String httpRequest_getMisson,httpRequest_getyiban;
	private	List<Map<String,String>> list;
	private SimpleAdapter mSimpleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.neirong);

		slidingMenu();
        findviewbyid();
		Bundle bundle = this.getIntent().getExtras();
		account = bundle.getString("key1");		
		String requestUrlName = "http://"+Common.Date.getIp()+"/wcf/WuserInfo.svc/userInfo/GetByAccount/"+account;
		getName(requestUrlName);
		
		httpRequest_getMisson = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetTasks/"+Goden_id+"/0";
		daiban.setOnClickListener(new OnClicklistener_Misson());
		
		httpRequest_getyiban = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetTasks/"+Goden_id+"/1";
		yiban.setOnClickListener(new OnClicklistener_yiban());
		bt_down.setOnClickListener(bt_down_down);
		list_daiban.setMode(Mode.BOTH);
		list_daiban.setOnItemClickListener(listItemListener);
		list_daiban.setEmptyView(findViewById(R.id.empty));  //����б��ǿյľ���ʾ�յ���һ���ֵ����ݣ�
		list_daiban.setOnRefreshListener(new PullRefresh());

	}
    
	//�»�������ˢ��
	class PullRefresh implements OnRefreshListener2<ListView>
	{
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            //����������ˢ���Ѱ컹�Ǵ����б�ȡlisttype��ֵ�����жϡ�
			if(listType.getText().toString().equals("�����б�"))
			{
	        Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
    		getDaiben.getDaiban(httpRequest_getMisson,10000,"start");//��һ�μ��ش�0��ʼ��
			}
			else
			{
				Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
	    		getDaiben.getDaiban(httpRequest_getyiban,10000,"start");//��һ�μ��ش�0��ʼ��
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if(listType.getText().toString().equals("�����б�"))
			{
	        Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
    		getDaiben.getDaiban(httpRequest_getMisson,10000,"start");//��һ�μ��ش�0��ʼ��
			}
			else
			{
				Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
	    		getDaiben.getDaiban(httpRequest_getyiban,10000,"start");//��һ�μ��ش�0��ʼ��
			}
		}
	}
	
	private void slidingMenu()
	{
        // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu);
	}
	private void findviewbyid()
	{
		list_daiban = (PullToRefreshListView )findViewById(R.id.listdaiban);
        textView_name=(TextView)findViewById(R.id.textView_name);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        yiban = (LinearLayout)findViewById(R.id.yiban);
        listcount = (TextView)findViewById(R.id.listcount);
        daiban = (LinearLayout)findViewById(R.id.daiban);
        listType=(TextView)findViewById(R.id.listType);
     // ʵ�����ײ�����
		moreView = getLayoutInflater().inflate(R.layout.moredata, null);
		bt_moredata = (Button) moreView.findViewById(R.id.bt_load);
        pg = (ProgressBar) moreView.findViewById(R.id.pg);
		bt_down = (Button)findViewById(R.id.button1);
	}
	
	//����ļ����¼�
	class OnClicklistener_Misson implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			listType.setText("�����б�");
			String httpRequest = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetTasks/"+Goden_id+"/0";		
			progressDialog = ProgressDialog.show(NeiRong.this, "��������...", "���Ժ�...", true, false);
			Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
			getDaiben.getDaiban(httpRequest,10000,"start");//��һ�μ��ش�0��ʼ��
		}
		
	}
	
	//�Ѱ�ļ����¼�
	class OnClicklistener_yiban implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			listType.setText("�Ѱ��б�");
			String httpRequest = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetTasks/"+Goden_id+"/1";		
			progressDialog = ProgressDialog.show(NeiRong.this, "��������...", "���Ժ�...", true, false);
			Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
			getDaiben.getDaiban(httpRequest,10000,"start");//��һ�μ��ش�0��ʼ��
		}
		
	}
	
	//���ڴ�mingxi����ʱ�Ƿ�ˢ�¡�
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode==200)
		{
		//String httpRequest = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GETtasks/"+Goden_id+"/0";
		
		progressDialog = ProgressDialog.show(NeiRong.this, "��������...", "���Ժ�...", true, false);
		getDaibanRestart(httpRequest_getMisson,10000,"start");//��һ�μ��ش�0��ʼ��
		}
	}
	
	//����ϸ�������һ���Ժ󷵻����ݽ���ʱʹ������߳����¼��ش����б�ʵ��ˢ�¡�
	public void  getDaibanRestart (final String httpRequest,final int start,final String flag)
	{
		new Thread()
		{
			public void run()
			{
				String result = com.aicfeng.denglu.httpRequest.get(httpRequest);
				if (result.contains("timed out")||result.contains("����ʧ��"))
				{
					handler_list.sendEmptyMessage(1);
				}
				else
				{
				try {
				JSONArray jsonArray = new JSONArray(result);
					list = new ArrayList<Map<String,String>>();
					Map<String,String> map = new HashMap<String, String>();
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
						map.put("InstanceID", InstanceID);
						map.put("title", title);
						map.put("FlowID", FlowID);
						map.put("GroupID", GroupID);
						map.put("StepID", StepID);
						map.put("TaskID", TaskID);
						list.add(map);	
					}
					handler_list.sendEmptyMessage(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		}.start();
		
	}
	private  Handler handler_list = new Handler()
	{
		 @Override  
	        public void handleMessage(Message msg) {  
			 try
			 {
			 switch (msg.what)
	        	{
	        	case 0:      		
	         mSimpleAdapter = new SimpleAdapter(NeiRong.this, list, R.layout.listitem,
	                        new String[] { "title", "InstanceID" ,"FlowID", "GroupID","StepID","TaskID"},
	                        new int[] { R.id.tv_title, R.id.tv_content ,R.id.item_FlowID, R.id.item_GroupID,R.id.item_StepID,R.id.item_TaskID});
	             list_daiban.setAdapter(mSimpleAdapter);
				 progressDialog.dismiss();
	            break;
	        	case 1:
	        		progressDialog.dismiss();
	        		Common.showDialog(NeiRong.this,2);
	        		break;
	        	}
			 }
			 catch(Exception e)
			 {
				 progressDialog.dismiss();
	        		Common.showDialog(NeiRong.this,e.toString());
			 }
		 }
	};
	
	//����һ��������������listviewitem�ĵ���¼���ͨ�������ѡ����ı����ID���ݸ�minxi
	OnItemClickListener listItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Map<String, String> map = (Map<String, String>) arg0.getAdapter().getItem(arg2);//����ֱ��ʹ��adpte
			String title = map.get("title");
			String InstanceID = map.get("InstanceID");
			String FlowID = map.get("FlowID");
			String GroupID = map.get("GroupID");
			String StepID = map.get("StepID");
			String TaskID = map.get("TaskID");
			String listtype = listType.getText().toString();
			Intent intent=new Intent();   
			 
			//��ǰ��ActivityΪTest,Ŀ��ActivityΪName 
			intent.setClass(NeiRong.this,Mingxi.class);   
			
			//���������п�ʼ�ǽ����ݴ����µ�Activity,����������ݣ�ֻ�Ǽ򵥵���ת���⼸�д�����ע�͵�   
			Bundle bundle=new Bundle();   
			bundle.putString("title",title);   
			bundle.putString("InstanceID",InstanceID);
			bundle.putString("FlowID",FlowID);   
			bundle.putString("GroupID",GroupID); 
			bundle.putString("StepID",StepID);  
			bundle.putString("TaskID",TaskID); 
			bundle.putString("userid", Goden_id);
			bundle.putString("listtype", listtype);
			intent.putExtras(bundle);//�����ݽ���   
			startActivityForResult(intent,0);
		}
	};
	
	
	
	//���ذ�ť�ļ����¼�
	OnClickListener bt_down_down = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String url = "http://www.baidu.com/img/bdlogo.png";
			Down down = new Down(null,progressBar,NeiRong.this);
			down.execute(url);
		}
	};
	
	//�����˺�ȡ���û���ȫ����Ȼ��ͨ��ȫ����,ͨ��handlerˢ��listview
	private void getName(final String requestUrlName)
	{
		new Thread()
		{
			public void run()
			{
				String jsonData;
				jsonData=httpRequest.get(requestUrlName);
				try
				{
					JSONObject jsonObject=new JSONObject(jsonData);
					String name = jsonObject.getString("<Name>k__BackingField");
					Goden_id = jsonObject.getString("<ID>k__BackingField");
					Message message=new Message();
					message.obj = name;
					handler.sendMessage(message);
				}
				catch (Exception e)
				{
					String x = e.toString();
				}
			}
		}.start();
	}
	
	private Handler handler = new Handler(){  
		  
        @Override  
        public void handleMessage(Message msg) {  
              
        	textView_name.setText(msg.obj.toString());
        	
        	httpRequest_getMisson = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetTasks/"+Goden_id+"/0";
    		
    		progressDialog = ProgressDialog.show(NeiRong.this, "��������...", "���Ժ�...", true, false);
    		Getdaiban getDaiben = new Getdaiban(NeiRong.this,list_daiban,progressDialog,moreView,listcount);
    		getDaiben.getDaiban(httpRequest_getMisson,10000,"start");//��һ�μ��ش�0��ʼ��
    		listType.setText("�����б�");
        	}
        } ;
        
        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Object mHelperUtils;
                        Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                } else {
                        finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
}
}
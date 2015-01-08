package com.aicfeng.denglu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Mingxi_ZiJin extends Activity {
	private EditText edittext_yijian,editText_paytitle,editText_paydept,editText_paybank,editText_payaccount,
	editText_userdept,editText_paydate,editText_payreason,editText_paymoney,editText_contracId,editText_contractMoney,editText_note,
	editText_havepay,editText_payuser;
	private ProgressDialog progressDialog;
	private Button button_submit;
	private static List<Map<String, String>> list_liucheng = new ArrayList<Map<String,String>>();
	private static List<Map<String,String>> list_next = new ArrayList<Map<String,String>>();
	private SimpleAdapter	mSimpleAdapter;
	private ListView listLiucheng,listAttachment;
	private Map<String, String> map;
	private Spinner spinner_JueYi,Spinner_NextSteps;  
	private String postUrl;
	private TextView textView_sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zijin_mingxi);
		
		findViewbyid();
		
		Bundle bundle = this.getIntent().getExtras();
		bundle.getString("title");
		bundle.getString("InstanceID");
		bundle.getString("FlowID");
		bundle.getString("GroupID");
		bundle.getString("StepID");
		bundle.getString("TaskID");
		bundle.getString("userid");
		if(bundle.get("listtype").equals("已办列表"))
		{
			//已办就获得当前步奏人员并把部分控件变成不可见
			button_submit.setVisibility(View.GONE);
			edittext_yijian.setVisibility(View.GONE);
			spinner_JueYi.setVisibility(View.GONE);
			textView_sp.setVisibility(View.GONE);
		}
		else
		{
			//待办获得下一步人员
			String httprequest_getnextsteps = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/GetNextSteps/"+bundle.getString("FlowID")+"/"+bundle.getString("StepID")+"/"+bundle.getString("GroupID");
			GetNextSteps getnextsteps = new GetNextSteps(Mingxi_ZiJin.this,httprequest_getnextsteps,Spinner_NextSteps);
			getnextsteps.getNextSteps();
		}
		
		button_submit.setOnClickListener(new Button_submitListener(bundle));
		//获得array里面决议的内容放到list里面调用banDinSpinner方法绑定数据源。
		String[] mItems = getResources().getStringArray(R.array.spinner_yijian);
		List<String> list_JueYi = new ArrayList<String>(); 
		list_JueYi = Arrays.asList(mItems);  
		Common.banDinSpinner (list_JueYi,Mingxi_ZiJin.this,spinner_JueYi);
		
		
		String httprequest = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/getPayDetail/"+bundle.getString("InstanceID");
		progressDialog = ProgressDialog.show(Mingxi_ZiJin.this, "正在加载付款信息...", "请稍后...", true, false);
		getMingxi(httprequest);
		
		String httprequest_liucheng = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/getComment/"+bundle.getString("FlowID")+"/"+bundle.getString("GroupID")+"/"+bundle.getString("StepID");
		getLiucheng(httprequest_liucheng);
		

		
		listAttachment.setOnItemClickListener(onItemClickListener_attachmentList);
	}

	

	private void findViewbyid()
	{
		editText_paytitle = (EditText)findViewById(R.id.editText_paytitle);
		editText_paydept = (EditText)findViewById(R.id.editText_paydept);
		editText_paybank = (EditText)findViewById(R.id.editText_paybank);
		editText_payaccount = (EditText)findViewById(R.id.editText_payaccount);
		editText_userdept = (EditText)findViewById(R.id.editText_userdept);
		editText_paydate = (EditText)findViewById(R.id.editText_paydate);
		editText_payreason = (EditText)findViewById(R.id.editText_payreason);
		editText_paymoney = (EditText)findViewById(R.id.editText_paymoney);
		editText_contracId = (EditText)findViewById(R.id.editText_contracId);
		editText_contractMoney = (EditText)findViewById(R.id.editText_contractMoney);
		editText_note = (EditText)findViewById(R.id.editText_note);
		editText_havepay = (EditText)findViewById(R.id.editText_havepay);
		editText_payuser = (EditText)findViewById(R.id.editText_payuser);
		

		button_submit = (Button)findViewById(R.id.button1);
		textView_sp = (TextView)findViewById(R.id.textView_sp);
		listAttachment = (ListView)findViewById(R.id.listActtachment);
		Spinner_NextSteps = (Spinner)findViewById(R.id.Spinner_NextSteps);
		edittext_yijian = (EditText)findViewById(R.id.edittext_yijian);
		spinner_JueYi = (Spinner)findViewById(R.id.Spinner01);
		listLiucheng = (ListView)findViewById(R.id.listLiucheng);
	}
	
	
	private void getMingxi(final String httprequest) {
		new Thread()
		{
			public void run()
			{
				try
				{
					String jsonData ;
					jsonData=httpRequest.get(httprequest);
					Message message=new Message();
					message.obj = jsonData;
					handler_mingxi.sendMessage(message);
				}
				catch (Exception e)
				{
					String x = e.toString();
				}
			}
		}.start();
	}
	
	private Handler handler_mingxi = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String result = msg.obj.toString();
			if(result.contains("timed out"))
			{
				progressDialog.dismiss();
				Common.showDialog(Mingxi_ZiJin.this, 2);
			}
			else if (result.contains("请求失败"))
			{
				progressDialog.dismiss();
				Common.showDialog(Mingxi_ZiJin.this, 4);
			}
			else
			{
				try {
					JSONObject jsonObject=new JSONObject(result);
					//从接口取到的值赋值给控件。
					editText_contractMoney.setText(jsonObject.getString("<contractMoney>k__BackingField"));
					editText_havepay.setText(jsonObject.getString("<havepay>k__BackingField"));
					editText_note.setText(jsonObject.getString("<note>k__BackingField"));
					editText_payaccount.setText(jsonObject.getString("<payaccount>k__BackingField"));
					editText_paybank.setText(jsonObject.getString("<paybank>k__BackingField"));
					editText_paydate.setText(jsonObject.getString("<paydate>k__BackingField"));
					editText_paymoney.setText(jsonObject.getString("<paymoney>k__BackingField"));
					editText_payreason.setText(jsonObject.getString("<payreason>k__BackingField"));
					editText_paytitle.setText(jsonObject.getString("<paytitle>k__BackingField"));
					editText_payuser.setText(jsonObject.getString("<payuser>k__BackingField"));
					editText_userdept.setText(jsonObject.getString("<userdept>k__BackingField"));
					editText_paydept.setText(jsonObject.getString("<paydept>k__BackingField"));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	private void attachment(String attchment) throws UnsupportedEncodingException
	{
		List<Map<String,String>> attachment_list = new ArrayList<Map<String,String>>();
		Map<String,String> map;
		if(!attchment.isEmpty())//判断获取的地址是否为空如果为空则显示无附件
		{
			String[] Attachment_url =attchment.split("\\|");
			for(int i=0;i<Attachment_url.length;i++)
			{
				String Attachment_name = Attachment_url[i].substring(Attachment_url[i].lastIndexOf('/')+1);
				map = new HashMap<String, String>();
				
				String Urlpart = Attachment_url[i].substring(0,Attachment_url[i].lastIndexOf('/')+1);
				String ChineseName = Attachment_name.substring(0,Attachment_name.indexOf("."));
				String fileType = Attachment_name.substring(Attachment_name.indexOf("."));
				String utf_ChineseName = java.net.URLEncoder.encode(ChineseName,"UTF-8");
				String fileurl =Urlpart+utf_ChineseName+fileType;
				map.put("Attachment_name", Attachment_name);
				map.put("Attachment_url", "http://"+Common.Date.getIp()+fileurl);
				attachment_list.add(map);
			}
		}
		else
		{
			map = new HashMap<String, String>();
			map.put("Attachment_name", "无附件");
			map.put("Attachment_url", "");
			attachment_list.add(map);
		}
		SimpleAdapter simpleAdapter_attachment = new SimpleAdapter(Mingxi_ZiJin.this, attachment_list, R.layout.attachmentitme,
                new String[] { "Attachment_name", "Attachment_url"},
                new int[] { R.id.attchment_name, R.id.attchment_url}); 
		listAttachment.setAdapter(simpleAdapter_attachment);
		int x = simpleAdapter_attachment.getCount();
		Common.setListViewHeightBasedOnChildren(listAttachment);
	}
	//附件list点击下载
	OnItemClickListener onItemClickListener_attachmentList = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Map<String, String> map = (Map<String, String>) arg0.getAdapter().getItem(arg2);//不能直接使用adpte
			String url = map.get("Attachment_url");
			if(!url.isEmpty())//地址是否为空如果为空点击没有反应。主要应对无附件的情况
			{
			UpdateManager upm = new UpdateManager(Mingxi_ZiJin.this, url);
			upm.showDownloadDialog(url,"正在下载");
			}
			else
			{
				
			}
		}
		
	};
	
	//获取流程信息的线程
	private void getLiucheng(final String httprequest_liucheng)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					String jsonData;
					jsonData=httpRequest.get(httprequest_liucheng);
					list_liucheng.clear();
					if(jsonData.contains("timed out"))
					{
						handler_liucheng.sendEmptyMessage(2);
					}
					else if (jsonData.contains("请求失败"))
					{
						handler_liucheng.sendEmptyMessage(4);
					}
					else
					{
						JSONArray jsonArray = new JSONArray(jsonData);
							
							for(int i = 0;i<jsonArray.length();i++)
							{
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								
								String CompletedTime1 = jsonObject.getString("CompletedTime1")+" ";
								String ReceiveName = jsonObject.getString("ReceiveName")+" ";
								String ReceiveTime = jsonObject.getString("ReceiveTime")+" ";
								String StepName = jsonObject.getString("StepName");
								String Comment1 = jsonObject.getString("Comment1");
								map = new HashMap<String, String>();
								map.put("CompletedTime1", CompletedTime1);
								map.put("ReceiveName", ReceiveName);
								map.put("ReceiveTime", ReceiveTime);
								map.put("StepName", StepName);
								map.put("Comment1"," "+Comment1);
								list_liucheng.add(map);	
							}
							handler_liucheng.sendEmptyMessage(1);
					}

				}
				catch (Exception e)
				{
					Message msg = new Message();
					msg.obj = e.toString();
					handler_liucheng.sendMessage(msg);
				}
			}
		}.start();
	}
	
	private Handler handler_liucheng = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == 1)
			{
				mSimpleAdapter = new SimpleAdapter(Mingxi_ZiJin.this, list_liucheng, R.layout.shenpilistitem,
                        new String[] { "StepName", "CompletedTime1" ,"ReceiveName", "ReceiveTime","Comment1"},
                        new int[] { R.id.StepName, R.id.CompletedTime1 ,R.id.ReceiveName, R.id.ReceiveTime,R.id.Comment1}); 
				listLiucheng.setAdapter(mSimpleAdapter);
				//int x = mSimpleAdapter.getCount();
				Common.setListViewHeightBasedOnChildren(listLiucheng);
   			 //listview.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,list_title));
			 progressDialog.dismiss();
			}
			else if (msg.what == 2)
			{
				progressDialog.dismiss();
        		Common.showDialog(Mingxi_ZiJin.this,2);
			}
			else if (msg.what == 4)
			{
				progressDialog.dismiss();
        		Common.showDialog(Mingxi_ZiJin.this,4);
			}
			else
			{
        		progressDialog.dismiss();
        		Common.showDialog(Mingxi_ZiJin.this,msg.obj.toString());
			}
		}
	};
	
	//保存按钮的触发事件
	class Button_submitListener implements OnClickListener{
		
		private Bundle bundle;
		public  Button_submitListener(Bundle bundle)
		{
			this.bundle = bundle;
		}
		@Override
		public void onClick(View v) {
			if(!TextUtils.isEmpty(edittext_yijian.getText()))
			{
				try {	
						postUrl = "http://"+Common.Date.getIp()+"/wcf/wpost.svc/Wpost/Execute";
						progressDialog = ProgressDialog.show(Mingxi_ZiJin.this, "正在上传审批信息...", "请稍后...", true, false);					
						//创建一个NameValuePair列表
						//List<NameValuePair> params = new ArrayList<NameValuePair>();
//						//将要POST的值加到列表里面来。
//						params.add(new BasicNameValuePair("StepID",bundle.getString("StepID")));
//						params.add(new BasicNameValuePair("FlowID",bundle.getString("FlowID")));
//						params.add(new BasicNameValuePair("GroupID",bundle.getString("GroupID")));
//						params.add(new BasicNameValuePair("Comment",edittext_yijian.getText().toString()));
//						params.add(new BasicNameValuePair("InstanceID",bundle.getString("InstanceID")));
//						params.add(new BasicNameValuePair("UserId",bundle.getString("userid")));
//						params.add(new BasicNameValuePair("Title",editText_contract_name.getText().toString()));
//						params.add(new BasicNameValuePair("TaskID",bundle.getString("TaskID")));
						JSONStringer vehicle;
						vehicle = new JSONStringer()
						.object()
						    .key("StepID").value(bundle.getString("StepID"))
						    .key("FlowID").value(bundle.getString("FlowID"))
						    .key("GroupID").value(bundle.getString("GroupID"))
						    .key("Comment").value(edittext_yijian.getText().toString())
						    .key("InstanceID").value(bundle.getString("InstanceID"))
						    .key("UserId").value(bundle.getString("userid"))
						    .key("Title").value(bundle.getString("title"))
						    .key("TaskID").value(bundle.getString("TaskID"))
						    .endObject();
						StationPost station = new StationPost(postUrl,vehicle.toString());//new一个对象
						station.stationPost();
					
				} catch (Exception e) {
					progressDialog.dismiss();
					Common.showDialog(Mingxi_ZiJin.this, e.toString());
				} 
			}
			else
			{
				progressDialog.dismiss();
				Common.showDialog(Mingxi_ZiJin.this, "审批意见不能为空");
			}
		}
	}
	
	//提交当前步奏处理结果成功则返回待办页否则提示错误信息
	class StationPost extends Thread
	{
		private String url;
		private String params;
		public StationPost (String url,String params)
		{
			this.url = url;
			this.params = params;
		}
		private void stationPost()
		{
			new Thread()
			{
				public void run()
				{
//					
//					//调用post方法提交数据获得返回值；
					String result = httpRequest.post(url, params);
					//String result = httpRequest.get(url);
					if(result.contains("true"))
					{
						handle_stationPost.sendEmptyMessage(1);
					}
					else
					{
						Message msg = new Message();
						msg.obj = result;
						handle_stationPost.sendMessage(msg);
					}
				}
			}.start();
		}
		
		Handler handle_stationPost = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				
				if(msg.what==1)
				{
					progressDialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(Mingxi_ZiJin.this, NeiRong.class);
				Bundle mBundle = new Bundle();
				intent.putExtras(mBundle);
				setResult(200,intent);	
				finish();
				}
				else
				{
					progressDialog.dismiss();
					Common.showDialog(Mingxi_ZiJin.this, msg.obj.toString());
				}
			}
		};
}
	
	
	//获得下一步可以选择的人员
	//调用banDingSpinner重写的方法，获得姓名将GUID隐藏起来。
	class GetNextSteps extends Thread
	{
		private Context context;
		private String httprequest;
		private Spinner spinner;
		public GetNextSteps( Context context,String httprequest,Spinner spinner)
		{
			this.context = context;
			this.httprequest = httprequest;
			this.spinner = spinner;
		}
		
		private void getNextSteps()
		{
			new Thread()
			{
				public void run()
				{
					try
					{
						String jsonData;
						jsonData=httpRequest.get(httprequest);
						list_next.clear();
						if(jsonData.contains("timed out"))
						{
							handler_getnextsteps.sendEmptyMessage(2);
						}
						else if (jsonData.contains("请求失败"))
						{
							handler_getnextsteps.sendEmptyMessage(4);
						}
						else
						{
							JSONArray jsonArray = new JSONArray(jsonData);
								for(int i = 0;i<jsonArray.length();i++)
								{
									JSONObject jsonObject = jsonArray.getJSONObject(i);
									String NameGuid = jsonObject.getString("NameGuid");
									String MemberName = jsonObject.getString("MemberName");
									map = new HashMap<String, String>();
									map.put("NameGuid", NameGuid);
									map.put("MemberName", MemberName);
									list_next.add(map);	
								}
								handler_getnextsteps.sendEmptyMessage(1);
						}

					}
					catch (Exception e)
					{
						Message msg = new Message();
						msg.obj = e.toString();
						handler_getnextsteps.sendMessage(msg);
					}
				}
			}.start();
		}
		
		Handler handler_getnextsteps = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 2)
				{
					progressDialog.dismiss();
	        		Common.showDialog(Mingxi_ZiJin.this,2);
				}
				else if (msg.what == 4)
				{
					progressDialog.dismiss();
	        		Common.showDialog(Mingxi_ZiJin.this,"获取下一步人员失败！");
				}
				else if (msg.what == 1)
				{			
						Common.banDingSpinner(list_next, context, spinner);
		        		progressDialog.dismiss();
				}
				else
				{
					progressDialog.dismiss();
					Common.showDialog(context, msg.obj.toString());
				}
			}
		};
	}
}

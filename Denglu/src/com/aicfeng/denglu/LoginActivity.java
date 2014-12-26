package com.aicfeng.denglu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private Button signin_button;
	private EditText editText_account;
	private EditText editText_pw;
	private ProgressDialog progressDialog; 
	private CheckBox checkBox_rmbUserPs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		//自动更新。
		String chackurl = "http://"+Common.Date.getIp()+"/wcf/wWorkFlowTask.svc/WorkFlowTask/cupdate";
		UpdateManager updateManager = new UpdateManager(LoginActivity.this, chackurl);
		updateManager.checkUpdateInfo();
		
		checkBox_rmbUserPs = (CheckBox)findViewById(R.id.checkBox_rmbUserPs);
		signin_button = (Button)findViewById(R.id.signin_button);
		editText_account = (EditText)findViewById(R.id.username_edit);
		editText_pw = (EditText)findViewById(R.id.password_edit);
		signin_button.setOnClickListener(new Signin_buttonListener());
		
		RemanberUserName remanberUserName = new RemanberUserName(checkBox_rmbUserPs, LoginActivity.this, editText_pw, editText_account);
    	remanberUserName.getUserNameAndPassword();
	}
	
	 private class Signin_buttonListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(LoginActivity.this, "正在登陆...", "请稍后...", true, false); 
				

				if(!TextUtils.isEmpty(editText_account.getText())&&!TextUtils.isEmpty(editText_pw.getText()))
				{
					//boolean x = Common.teshuzifu(editText_account.getText().toString());
					if(!Common.teshuzifu(editText_account.getText().toString())&&!Common.teshuzifu(editText_pw.getText().toString()))
					{
				   //新建线程  
             new Thread(){  

                 @Override  
                 public void run() {  
                     //需要花时间计算的方法  
                 	String result,requestUrl;  
                 	requestUrl="http://"+Common.Date.getIp()+"/wcf/wlogin.svc/Login/login/"+editText_account.getText()+"/"+editText_pw.getText();
                 	String account = editText_account.getText().toString();
                 	result=httpRequest.get(requestUrl);
                 	//analysis.filterHtml(result);
                 	String x ="\"登陆成功\"";
                 	Message msg = new Message();
                 	msg.obj= account;
                 	if(result.contains("登陆成功"))
                 	{
                 		handler.sendMessage(msg);
                 		handler.sendEmptyMessage(0);
                 		signin(account);
                 	}
                 	else if (result.contains("timed out"))
                 	{
                 		handler.sendEmptyMessage(2);
                 	}
                 	else if (result.contains("帐号或密码错误"))
                 	{
                 		handler.sendEmptyMessage(1);
                 	}
                 	else if (result.contains("帐号已被冻结"))
                 	{
                 		handler.sendEmptyMessage(3);
                 	}
                 	else
                 	{
                 		handler.sendEmptyMessage(4);
                 	}
                     //向handler发消息  
                       
                     
                 }}.start();  
					}
					else
					{
						progressDialog.dismiss();
						Common.showDialog(LoginActivity.this,"不能包含特殊字符！");
					}
				}
				else
				{
					progressDialog.dismiss();
					Common.showDialog(LoginActivity.this, 0);
				}
				
			}
	    }


	
	
	/** 
     * 用Handler来更新UI 
     */  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
              
        	switch (msg.what)
        	{
        	case 0:
            //关闭ProgressDialog  
                RemanberUserName remanberUserName = new RemanberUserName(checkBox_rmbUserPs, LoginActivity.this, editText_pw, editText_account);
                if(checkBox_rmbUserPs.isChecked())
                {
                	remanberUserName.setUserNameAndPassword();
                }
                else
                {
                	remanberUserName.delUserNameAndPassword();
                }
            progressDialog.dismiss();
            break;
        	case 1:
        		progressDialog.dismiss(); 
        		Common.showDialog(LoginActivity.this,1);
        		break;
        	case 2:
        		progressDialog.dismiss(); 
        		Common.showDialog(LoginActivity.this,2);
        		break;
        	case 3:
        		progressDialog.dismiss(); 
        		Common.showDialog(LoginActivity.this,3);
        		break;
        	case 4:
        		progressDialog.dismiss(); 
        		Common.showDialog(LoginActivity.this,4);
        		break;
        	}
        }};  
        
 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void signin( String key1)
	{
		Intent intent=new Intent();   
		 
		//当前的Activity为Test,目标Activity为Name 
		intent.setClass(LoginActivity.this,NeiRong.class);   
		
		//从下面这行开始是将数据传给新的Activity,如果不传数据，只是简单的跳转，这几行代码请注释掉   
		Bundle bundle=new Bundle();   
		bundle.putString("key1",key1);//key1为名，value1为值   
		bundle.putString("key2","value2");   
		intent.putExtras(bundle);//传数据结束   
		startActivity(intent);
		finish();
		
	}
	
}

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
		
		//�Զ����¡�
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
				progressDialog = ProgressDialog.show(LoginActivity.this, "���ڵ�½...", "���Ժ�...", true, false); 
				

				if(!TextUtils.isEmpty(editText_account.getText())&&!TextUtils.isEmpty(editText_pw.getText()))
				{
					//boolean x = Common.teshuzifu(editText_account.getText().toString());
					if(!Common.teshuzifu(editText_account.getText().toString())&&!Common.teshuzifu(editText_pw.getText().toString()))
					{
				   //�½��߳�  
             new Thread(){  

                 @Override  
                 public void run() {  
                     //��Ҫ��ʱ�����ķ���  
                 	String result,requestUrl;  
                 	requestUrl="http://"+Common.Date.getIp()+"/wcf/wlogin.svc/Login/login/"+editText_account.getText()+"/"+editText_pw.getText();
                 	String account = editText_account.getText().toString();
                 	result=httpRequest.get(requestUrl);
                 	//analysis.filterHtml(result);
                 	String x ="\"��½�ɹ�\"";
                 	Message msg = new Message();
                 	msg.obj= account;
                 	if(result.contains("��½�ɹ�"))
                 	{
                 		handler.sendMessage(msg);
                 		handler.sendEmptyMessage(0);
                 		signin(account);
                 	}
                 	else if (result.contains("timed out"))
                 	{
                 		handler.sendEmptyMessage(2);
                 	}
                 	else if (result.contains("�ʺŻ��������"))
                 	{
                 		handler.sendEmptyMessage(1);
                 	}
                 	else if (result.contains("�ʺ��ѱ�����"))
                 	{
                 		handler.sendEmptyMessage(3);
                 	}
                 	else
                 	{
                 		handler.sendEmptyMessage(4);
                 	}
                     //��handler����Ϣ  
                       
                     
                 }}.start();  
					}
					else
					{
						progressDialog.dismiss();
						Common.showDialog(LoginActivity.this,"���ܰ��������ַ���");
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
     * ��Handler������UI 
     */  
    private Handler handler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
              
        	switch (msg.what)
        	{
        	case 0:
            //�ر�ProgressDialog  
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
		 
		//��ǰ��ActivityΪTest,Ŀ��ActivityΪName 
		intent.setClass(LoginActivity.this,NeiRong.class);   
		
		//���������п�ʼ�ǽ����ݴ����µ�Activity,����������ݣ�ֻ�Ǽ򵥵���ת���⼸�д�����ע�͵�   
		Bundle bundle=new Bundle();   
		bundle.putString("key1",key1);//key1Ϊ����value1Ϊֵ   
		bundle.putString("key2","value2");   
		intent.putExtras(bundle);//�����ݽ���   
		startActivity(intent);
		finish();
		
	}
	
}

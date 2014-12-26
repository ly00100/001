package com.aicfeng.denglu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

public class RemanberUserName {

	private Context context;
	private EditText edittext_password,edittext_username;
	private CheckBox checkBox_rmbUserPs;
	public RemanberUserName(CheckBox checkBox_rmbUserPs,Context context,EditText edittext_password,EditText edittext_username)
	{
		this.context = context;
		this.edittext_password = edittext_password;
		this.edittext_username = edittext_username;
		this.checkBox_rmbUserPs = checkBox_rmbUserPs;
	}
	
	public void setUserNameAndPassword()
	{       
		 SharedPreferences sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		//存入数据
		Editor editor = sp.edit();
		editor.putString("username", edittext_username.getText().toString());
		editor.putString("password", edittext_password.getText().toString());
		editor.putBoolean("BOOLEAN_KEY", checkBox_rmbUserPs.isChecked());
		editor.commit();        
	}
	
	public void getUserNameAndPassword()
	{
		 SharedPreferences sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		 
		 //取得数据
		edittext_username.setText(sp.getString("username", null));
		edittext_password.setText(sp.getString("password", null));
		checkBox_rmbUserPs.setChecked(sp.getBoolean("BOOLEAN_KEY", false));
	}
	
	public void delUserNameAndPassword()
	{
		 SharedPreferences sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		//存入数据
		Editor editor = sp.edit();
		editor.putString("username", "");
		editor.putString("password", "");
		editor.putBoolean("BOOLEAN_KEY", false);
		editor.commit(); 
	}
}

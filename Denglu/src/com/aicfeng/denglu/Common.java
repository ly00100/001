package com.aicfeng.denglu;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Common {
	
	static class Date
	{
		private static final String IP = "10.2.100.15";

		public static String getIp() {
			return IP;
		}

	}
	
	public static void showDialog(Context context,int flag){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.a);
        builder.setTitle("提示");
        builder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener(){
        	 
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
             
        });
        if(flag == 0)
        {
        builder.setMessage("用户名密码不能为空！");

        }
        else if (flag == 1)
        {
            builder.setMessage("用户名密码不正确！");
        }
        else if (flag == 2)
        {
            builder.setMessage("网络连接超时！");
        }
        else if (flag == 3)
        {
        	 builder.setMessage("用户被锁定");
        }
        else if (flag == 4)
        {
        	 builder.setMessage("请求失败！");
        }
        builder.show();
    }
	
	public static void showDialog(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.a);
        builder.setTitle("提示");
        builder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener(){
        	 
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
             
        });
        builder.setMessage(message);
        builder.show();
    }
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);   
    }  
	
	//对下拉绑定数据
	public static void banDinSpinner (List<String> list_spiner,Context context,Spinner spinner)
	{
		ArrayAdapter<String> adapterSpinner; 
		//第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项    
		
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。    
        adapterSpinner = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,list_spiner);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        spinner.setAdapter(adapterSpinner);    
	}
	
	
	public static void banDingSpinner (List<Map<String,String>> list_spiner,Context context,Spinner spinner)
	{
		SimpleAdapter mSipleAdapter = new SimpleAdapter(context, list_spiner, R.layout.spinner_nextsteps,
                new String[] { "MemberName", "NameGuid"},
                new int[] { R.id.MemberName, R.id.NameGuid});
		mSipleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(mSipleAdapter);
	}
	
	public static boolean teshuzifu(String zifu)
	{
	String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]\\s*|\t|\r|\n"; 
    Pattern p = Pattern.compile(regEx); 
    Matcher m = p.matcher(zifu);                 
    return m.find();
	}
}
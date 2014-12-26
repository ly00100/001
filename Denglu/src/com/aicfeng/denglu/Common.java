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
        builder.setTitle("��ʾ");
        builder.setPositiveButton("��", new android.content.DialogInterface.OnClickListener(){
        	 
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
             
        });
        if(flag == 0)
        {
        builder.setMessage("�û������벻��Ϊ�գ�");

        }
        else if (flag == 1)
        {
            builder.setMessage("�û������벻��ȷ��");
        }
        else if (flag == 2)
        {
            builder.setMessage("�������ӳ�ʱ��");
        }
        else if (flag == 3)
        {
        	 builder.setMessage("�û�������");
        }
        else if (flag == 4)
        {
        	 builder.setMessage("����ʧ�ܣ�");
        }
        builder.show();
    }
	
	public static void showDialog(Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.a);
        builder.setTitle("��ʾ");
        builder.setPositiveButton("��", new android.content.DialogInterface.OnClickListener(){
        	 
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
             
        });
        builder.setMessage(message);
        builder.show();
    }
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {   
        // ��ȡListView��Ӧ��Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()�������������Ŀ   
            View listItem = listAdapter.getView(i, null, listView);   
            // ��������View �Ŀ��   
            listItem.measure(0, 0);    
            // ͳ������������ܸ߶�   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�   
        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�   
        listView.setLayoutParams(params);   
    }  
	
	//������������
	public static void banDinSpinner (List<String> list_spiner,Context context,Spinner spinner)
	{
		ArrayAdapter<String> adapterSpinner; 
		//��һ�������һ�������б����list��������ӵ�����������б�Ĳ˵���    
		
        //�ڶ�����Ϊ�����б���һ����������������õ���ǰ�涨���list��    
        adapterSpinner = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,list_spiner);    
        //��������Ϊ���������������б�����ʱ�Ĳ˵���ʽ��    
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //���Ĳ�������������ӵ������б���    
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
	String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]\\s*|\t|\r|\n"; 
    Pattern p = Pattern.compile(regEx); 
    Matcher m = p.matcher(zifu);                 
    return m.find();
	}
}
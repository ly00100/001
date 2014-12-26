package com.aicfeng.denglu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
  
public class UpdateManager {  
  
    private Context mContext;       
    //提示语  
    private String downurl;
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";  
    private Dialog noticeDialog;     
    private Dialog downloadDialog;  
    private ProgressBar mProgress;  
    private String chackurl;
    private String versionname;

    public UpdateManager(Context context,String chackurl) {  
        this.mContext = context;  
        this.chackurl = chackurl;
    }  
      
    //外部接口让主Activity调用  
    public void checkUpdateInfo(){  
    	new Thread()
    	{
    		public void run()
    		{
    			String result = httpRequest.get(chackurl);
    			Message msg = new Message();
    			if(result.contains("timed out"))
				{
    				handler.sendEmptyMessage(2);
				}
				else if (result.contains("请求失败"))
				{
					handler.sendEmptyMessage(4);
				}
				else
    			{
    			try {
					JSONArray jsonArray = new JSONArray(result);
					JSONObject jsonVersion = jsonArray.getJSONObject(0);
				versionname =	jsonVersion.get("Ver").toString();
				downurl = "http://"+Common.Date.getIp()+jsonVersion.get("Url").toString();
					
				Bundle bundle = new Bundle();
				bundle.putString("versionname", jsonVersion.get("Ver").toString());
				bundle.putString("downurl", "http://"+Common.Date.getIp()+jsonVersion.get("Url").toString());
				msg.setData(bundle);
				handler .sendMessage(msg);
    			} catch (Exception e) {
    				msg.obj = e.toString();
    				handler.sendEmptyMessage(6);
    				handler.sendMessage(msg);
				}
    			}
    		}
    	}.start();
    }  
    //JSOn取得版本号和下载地址。
    Handler handler = new Handler(){
    	public void handleMessage(Message msg)
		{
    	if (msg.what == 2)
		{
    		Common.showDialog(mContext,"获得更新信息超时！");
		}
		else if (msg.what == 4)
		{
    		Common.showDialog(mContext,"获得更新信息失败！");
		}
		else if (msg.what == 6)
		{
			Common.showDialog(mContext,msg.obj.toString());
		}
		else
		{
			Bundle bundle = new Bundle();
			bundle = msg.getData();
			versionname = bundle.get("versionname").toString();
			downurl = bundle.getString("downurl");
			String version = null;
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info;
			try {
				info = manager.getPackageInfo(mContext.getPackageName(), 0);
				version = info.versionName;
				if(!versionname.equals(version))
				{
					showNoticeDialog(bundle.getString("downurl")); 
				}
				else
				{
					
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
    };
      
      
    private void showNoticeDialog(final String downurl){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("软件版本更新");  
        builder.setMessage(updateMsg);  
        builder.setPositiveButton("下载", new OnClickListener() {           
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                showDownloadDialog(downurl,"软件版本更新");             
            }  
        });  
        builder.setNegativeButton("以后再说", new OnClickListener() {             
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();                 
            }  
        });  
        noticeDialog = builder.create();  
        noticeDialog.show();  
    }  
      
    public void showDownloadDialog(String downurl,String title){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle(title);  
          
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.progress, null);  
        mProgress = (ProgressBar)v.findViewById(R.id.progress);  
          
        builder.setView(v);  
        downloadDialog = builder.create();  
        downloadDialog.show();  
          
        Down down = new Down(downloadDialog, mProgress, mContext);
        down.execute(downurl);
    }  
}  
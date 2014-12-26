package com.aicfeng.denglu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Down extends AsyncTask<String, Integer, String> {

	private Dialog dialog = null;
	private ProgressBar progressBar = null;
	private TextView textView = null;
	private Context context = null;
	
	public Down(Dialog dialog,ProgressBar progressBar,Context context) {
		this.progressBar = progressBar;
		this.context = context;
		this.dialog = dialog;
	}
	
	@Override
	protected String doInBackground(String... urlDownload) {
		String dirName = "";
		dirName = Environment.getExternalStorageDirectory()+"/MyDownload/";
		File f = new File(dirName);
		if(!f.exists())
		{
		    f.mkdir();
		}
		
		//׼��ƴ���µ��ļ����������ڴ洢������ļ�����
		String newFilename = urlDownload[0].substring(urlDownload[0].lastIndexOf("/")+1);
		newFilename = dirName + newFilename;
		File file = new File(newFilename);
		//���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
		if(file.exists())
		{
		    file.delete();
		}
		try {
		         // ����URL   
		         URL url = new URL(urlDownload[0]);   
		         // ������   
		         URLConnection con = url.openConnection();
		         //����ļ��ĳ���
		         int contentLength = con.getContentLength();
		         System.out.println("���� :"+contentLength);
		         // ������   
		         InputStream is = con.getInputStream();  
		         // 1K�����ݻ���   
		         byte[] bs = new byte[1024];   
		         // ÿ�ζ�ȡ�������ݳ���   
		         int len; 
		         //Ϊ�˽�����
		         int downlen = 0;
		         // ������ļ���   
		         OutputStream os = new FileOutputStream(newFilename);   
		         // ��ʼ��ȡ   
		         while ((len = is.read(bs)) != -1) {   
		             os.write(bs, 0, len);
		             downlen = downlen+len;
		             publishProgress((int)(downlen*100/contentLength));//���س�һ��ֵ���½�����������onProgressUpdate
		             
		         }  
		         // ��ϣ��ر���������   
		         os.close();
		         //if(is!=null)
		         is.close();
		         return newFilename;
		            
		} catch (IOException e) {
			e.toString();
			return e.toString();
		}
	}
	
	protected void onPostExecute(String result)
	{
		try{
			dialog.dismiss();
			context.startActivity(openFile(result));
		}
		catch(Exception e)
		{
			Toast.makeText(context, "sorry�������ܴ򿪣���������������", 500).show(); 
		}
		progressBar.setProgress(0);
	}
	
	
	public static Intent openFile(String filePath){
		File file = new File(filePath);  
        if(file.exists()) 
        {
        	/* ȡ����չ�� */  
        	String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(); 
            /* ����չ�������;���MimeType */
           if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                    end.equals("jpeg")||end.equals("bmp")){
                return getImageFileIntent(filePath);
            }else if(end.equals("apk")){
                return getApkFileIntent(filePath);
            }else if(end.equals("ppt")){
                return getPptFileIntent(filePath);
            }else if(end.equals("xls")){
                return getExcelFileIntent(filePath);
            }else if(end.equals("doc")){
                return getWordFileIntent(filePath);
            }else if(end.equals("pdf")){
                return getPdfFileIntent(filePath);
            }else if(end.equals("chm")){
                return getChmFileIntent(filePath);
            }else if(end.equals("txt")){
                return getTextFileIntent(filePath,false);
            }else{
                return getAllIntent(filePath);
            }
        }
        else
        {
        	return null;
        }
	}
	
	   public static Intent getAllIntent( String param ) {
		   
	        Intent intent = new Intent();  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        intent.setAction(android.content.Intent.ACTION_VIEW);  
	        Uri uri = Uri.fromFile(new File(param ));
	        intent.setDataAndType(uri,"*/*"); 
	        return intent;
	    }
	 //Android��ȡһ�����ڴ�APK�ļ���intent
    public static Intent getApkFileIntent( String param ) {
 
        Intent intent = new Intent();  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setAction(android.content.Intent.ACTION_VIEW);  
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
        return intent;
    }
    
	//Android��ȡһ�����ڴ�ͼƬ�ļ���intent  
    public static Intent getImageFileIntent( String param ) {  
  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(new File(param ));  
        intent.setDataAndType(uri, "image/*");  
        return intent;  
    }  
    
  //Android��ȡһ�����ڴ�PDF�ļ���intent
    public static Intent getPdfFileIntent( String param )  
    
    {  
    
      Intent intent = new Intent("android.intent.action.VIEW");  
    
      intent.addCategory("android.intent.category.DEFAULT");  
    
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
    
      Uri uri = Uri.fromFile(new File(param ));  
    
      intent.setDataAndType(uri, "application/pdf");  
    
      return intent;  
    
    }  
    
    //android��ȡһ�����ڴ�Word�ļ���intent  
    
    public static Intent getWordFileIntent( String param )  
   
   {  
   
      Intent intent = new Intent("android.intent.action.VIEW");  
   
      intent.addCategory("android.intent.category.DEFAULT");  
   
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
   
      Uri uri = Uri.fromFile(new File(param ));  
   
      intent.setDataAndType(uri, "application/msword");  
   
      return intent;  
   
    }  
   
  //android��ȡһ�����ڴ�Excel�ļ���intent  
   
    public static Intent getExcelFileIntent( String param )  
   
    {  
   
      Intent intent = new Intent("android.intent.action.VIEW");  
   
      intent.addCategory("android.intent.category.DEFAULT");  
   
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
   
      Uri uri = Uri.fromFile(new File(param ));  
   
      intent.setDataAndType(uri, "application/vnd.ms-excel");  
   
      return intent;  
   
    }  
   
  //android��ȡһ�����ڴ�PPT�ļ���intent  
   
    public static Intent getPptFileIntent( String param )  
   
    {  
   
      Intent intent = new Intent("android.intent.action.VIEW");  
   
      intent.addCategory("android.intent.category.DEFAULT");  
   
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
   
      Uri uri = Uri.fromFile(new File(param ));  
   
      intent.setDataAndType(uri, "application/vnd.ms-powerpoint");  
   
      return intent;  
   
    }  
    
    //Android��ȡһ�����ڴ�CHM�ļ���intent   
    public static Intent getChmFileIntent( String param ){   
 
        Intent intent = new Intent("android.intent.action.VIEW");   
        intent.addCategory("android.intent.category.DEFAULT");   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        Uri uri = Uri.fromFile(new File(param ));   
        intent.setDataAndType(uri, "application/x-chm");   
        return intent;   
    }   
 
    //Android��ȡһ�����ڴ��ı��ļ���intent   
    public static Intent getTextFileIntent( String param, boolean paramBoolean){   
 
        Intent intent = new Intent("android.intent.action.VIEW");   
        intent.addCategory("android.intent.category.DEFAULT");   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        if (paramBoolean){   
            Uri uri1 = Uri.parse(param );   
            intent.setDataAndType(uri1, "text/plain");   
        }else{   
            Uri uri2 = Uri.fromFile(new File(param ));   
            intent.setDataAndType(uri2, "text/plain");   
        }   
        return intent;   
    }  
    
	protected void onProgressUpdate(Integer... progresses)
	{
	     progressBar.setProgress(progresses[0]);
	}
}







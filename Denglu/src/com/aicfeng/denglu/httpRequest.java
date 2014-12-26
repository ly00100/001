package com.aicfeng.denglu;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONStringer;

public class httpRequest {

	public static String post(String requestUrl,String JSONparams)
	{
		// ����������URL  
        // ����HttpClientʵ��  
        HttpClient client = new DefaultHttpClient();  
        // ����URL����HttpPostʵ��  
        HttpPost post = new HttpPost(requestUrl);  
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        
        
		try {
			StringEntity entity = new StringEntity(JSONparams,HTTP.UTF_8);
			post.setEntity(entity);
        
            // ����URL����  
            //post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // �������󲢻�ȡ����  
            HttpResponse response = client.execute(post);  
  
            int x =response.getStatusLine().getStatusCode();
            // �ж������Ƿ�ɹ�����  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                // �������ص�����  
                String result = EntityUtils.toString(response.getEntity());  
                // ����ѯ���������������ʾ��TextView��  
                return result;
            }
            else
            {
            	return "û�еõ�����";
            }
        } catch (Exception e) {  
            return(e.toString()); 
            
        }  
	}
	
	public static String get(String requestUrl) {
		try {
			HttpGet request = new HttpGet(requestUrl);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		//����ͻ���
		HttpClient httpClient = new DefaultHttpClient();
		 // ����ʱ
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // ��ȡ��ʱ
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 100000    );


		// Send GET request to <service>
		//ʹ�ÿͻ��˷������󲢻�÷���������Ӧ
		HttpResponse response = httpClient.execute(request); 
		//���ܵ�����Ϣ
		int x =response.getStatusLine().getStatusCode();
		if(response.getStatusLine().getStatusCode()==200) 
		{
			HttpEntity responseEntity = response.getEntity();
			String result; 
			result = retrieveInputStream(responseEntity);
			return result;
		}
		else
		{
			return "����ʧ��";
		}
		}
		catch (Exception e) 
		{
			return e.toString();
		}

		}

		 


		protected static String retrieveInputStream(HttpEntity httpEntity) {
			int length = (int) httpEntity.getContentLength();
			if (length < 0)
				length = 10000;
			StringBuffer stringBuffer = new StringBuffer(length);
			try {
					InputStreamReader inputStreamReader = new InputStreamReader(
					httpEntity.getContent(), HTTP.UTF_8);
					char buffer[] = new char[length];
					int count;
					while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
							stringBuffer.append(buffer, 0, count);
						}
		} catch (UnsupportedEncodingException e) {

		 

		} catch (IllegalStateException e) {

		 

		} catch (IOException e) {
		}

		return stringBuffer.toString();

		}
}

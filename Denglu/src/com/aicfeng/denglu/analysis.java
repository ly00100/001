package com.aicfeng.denglu;

public class analysis {

	/** 
     * ʹ��������ʽ����HTML��� 
     *  
     * @param source ���������� 
     * @return 
     */  
    public static String filterHtml(String source) {  
        if(null == source){  
            return "";  
        }  
        return source.replaceAll("</?[^>]+>","").trim();  
    } 
}

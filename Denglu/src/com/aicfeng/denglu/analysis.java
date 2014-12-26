package com.aicfeng.denglu;

public class analysis {

	/** 
     * 使用正则表达式过滤HTML标记 
     *  
     * @param source 待过滤内容 
     * @return 
     */  
    public static String filterHtml(String source) {  
        if(null == source){  
            return "";  
        }  
        return source.replaceAll("</?[^>]+>","").trim();  
    } 
}

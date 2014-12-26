package com.aicfeng.denglu;

import java.util.Date;

public class Gouzao {
	
	private static long id;
	private static String attachment, comment,cbacktype,signed,userid,operator1,supplier,fulfilway,massindex,Ccount,Ctype,station,arrive,categories,buyer, receiver, seller, contract_name,csalebuy,deptname,contract_address,contract_number,contract_type,contract_content;
	private static String date,contract_date,execdate1,execdate2;
	private static double price,amount; 
	private static int iscomplete;
//	   public  Gouzao(String attachment, String comment,String cbacktype,String signed,
//			   String userid,String operator1,String supplier,String fulfilway,String massindex,
//			   String Ccount,String Ctype,String station,String arrive,String categories,String buyer, 
//			   String receiver,String seller,String contract_name,String csalebuy,
//			   String deptname,String contract_address,String contract_number,String contract_type,
//			   String contract_content,Date date,Date contract_date,Date execdate1,Date execdate2,
//			   double price,double amount,int iscomplete,long id) { 
//	        Gouzao.id = id; 
//	        Gouzao.attachment = attachment; 
//	        Gouzao.comment = comment; 
//	        Gouzao.cbacktype = cbacktype;
//	        Gouzao.signed = signed;
//	        Gouzao.userid = userid;
//	        Gouzao.operator1 = operator1;
//	        Gouzao.supplier = supplier;
//	        Gouzao.fulfilway = fulfilway;
//	        Gouzao.massindex = massindex;
//	        Gouzao.Ccount = Ccount;
//	        Gouzao.Ctype = Ctype;
//	        Gouzao.station = station;
//	        Gouzao.arrive = arrive; 
//	        Gouzao.categories = categories;
//	        Gouzao.buyer = buyer;
//	        Gouzao.receiver = receiver;
//	        Gouzao.seller = seller;
//	        Gouzao.contract_name = contract_name;
//	        Gouzao.csalebuy = csalebuy;
//	        Gouzao.deptname = deptname;
//	        Gouzao.contract_address = contract_address;
//	        Gouzao.contract_number = contract_number;
//	        Gouzao.contract_type = contract_type;
//	        Gouzao.contract_content = contract_content;
//	        Gouzao.date = date;
//	        Gouzao.contract_date = contract_date;
//	        Gouzao.execdate1 = execdate1;
//	        Gouzao.execdate2 = execdate2;
//	        Gouzao.price = price;
//	        Gouzao.amount = amount;
//	        Gouzao.iscomplete = iscomplete;
//	        Gouzao.id = id;
//	    }
	
	public static String getAttachment() {
		return attachment;
	}
	public static void setAttachment(String attachment) {
		Gouzao.attachment = attachment;
	}
	
	public static String getComment() {
		return comment;
	}
	public static void setComment(String comment) {
		Gouzao.comment = comment;
	}
	
	public static String getCbacktype() {
		return cbacktype;
	}
	public static void setCbacktype(String cbacktype) {
		Gouzao.cbacktype = cbacktype;
	}
	
	public static String getSigned() {
		return signed;
	}
	public static void setSigned(String signed) {
		Gouzao.signed = signed;
	}
	
	public static String getUserid() {
		return userid;
	}
	public static void setUserid(String userid) {
		Gouzao.userid = userid;
	}
	
	public static String getOperator1() {
		return operator1;
	}
	public static void setOperator1(String operator1) {
		Gouzao.operator1 = operator1;
	}
	
	public static String getFulfilway() {
		return fulfilway;
	}
	public static void setFulfilway(String fulfilway) {
		Gouzao.fulfilway = fulfilway;
	}
	
	public static String getMassindex() {
		return massindex;
	}
	public static void setMassindex(String massindex) {
		Gouzao.massindex = massindex;
	}
	
	public static String getCcount() {
		return Ccount;
	}
	public static void setCcount(String Ccount) {
		Gouzao.Ccount = Ccount;
	}
	
	public static String getStation() {
		return station;
	}
	public static void setStation(String station) {
		Gouzao.station = station;
	}
	
	public static String getArrive() {
		return arrive;
	}
	public static void setArrive(String arrive) {
		Gouzao.arrive = arrive;
	}
	
	public static String getCategories() {
		return categories;
	}
	public static void setCategories(String categories) {
		Gouzao.categories = categories;
	}
	
	public static String getBuyer() {
		return buyer;
	}
	public static void setBuyer(String buyer) {
		Gouzao.buyer = buyer;
	}
	
	public static String getReceiver() {
		return receiver;
	}
	public static void setReceiver(String receiver) {
		Gouzao.receiver = receiver;
	}
	
	public static String getSeller() {
		return seller;
	}
	public static void setSeller(String seller) {
		Gouzao.seller = seller;
	}
	
	public static String getContract_name() {
		return contract_name;
	}
	public static void setContract_name(String contract_name) {
		Gouzao.contract_name = contract_name;
	}
	
	public static String getDeptname() {
		return deptname;
	}
	public static void setDeptname(String deptname) {
		Gouzao.deptname = deptname;
	}
	
	public static String getContract_address() {
		return contract_address;
	}
	public static void setContract_address(String contract_address) {
		Gouzao.contract_address = contract_address;
	}
	
	public static  String getContract_number() {
		return contract_number;
	}
	public static void setContract_number(String contract_number) {
		Gouzao.contract_number = contract_number;
	}
	
	public static String getContract_content() {
		return contract_content;
	}
	public static void setContract_content(String contract_content) {
		Gouzao.contract_content = contract_content;
	}
	
	public static String getContract_date() {
		return contract_date;
	}
	public static void setContract_date(String contract_date) {
		Gouzao.contract_date = contract_date;
	}
	
	public static String getExecdate1() {
		return execdate1;
	}
	public static void setExecdate1(String execdate1) {
		Gouzao.execdate1 = execdate1;
	}
	
	public static String getExecdate2() {
		return execdate2;
	}
	public static void setExecdate2(String execdate2) {
		Gouzao.execdate2 = execdate2;
	}
	
	public static double getPrice() {
		return price;
	}
	public static void setPrice(double price) {
		Gouzao.price = price;
	}
	
	public static double getAmount() {
		return amount;
	}
	public static void setAmount(double amount) {
		Gouzao.amount = amount;
	}
	
	public static String getsupplier() {
		return supplier;
	}
	public static void setsupplier(String supplier) {
		Gouzao.supplier = supplier;
	}
}

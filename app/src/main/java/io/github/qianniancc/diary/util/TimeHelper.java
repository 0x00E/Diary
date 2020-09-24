package io.github.qianniancc.diary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper{

	public static String dateToStamp(String s){
		String res;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			date=simpleDateFormat.parse(s);
		}catch (ParseException e) {
			e.printStackTrace();
		}
		long ts=date.getTime();
		res=String.valueOf(ts);
		return res;
	}
	
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt=new Long(s);
		Date date=new Date(lt);
		res=simpleDateFormat.format(date);
		return res;
	}



}
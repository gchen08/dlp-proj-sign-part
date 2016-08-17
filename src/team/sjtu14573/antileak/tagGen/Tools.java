package team.sjtu14573.antileak.tagGen;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Tools
{
	/**
	 * 读取文件创建时间
	 * @param filePath
	 * @return getCreatTime
	 */
	public static long getCreateTime(String filePath)
	{
		File file = new File(filePath);
		Long time = file.lastModified();
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(time);
//		System.out.println(time / 1000);
		return (time / 1000);
	}

	/**
	 * 获取当前时间
	 */
	public static long nowtime()
	{
		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		System.out.println(date.getTime()/1000);
		return (date.getTime() / 1000);
	}

	/**
	 * SHA-1算法实现
	 */
	public static void Sha(String value)
	{
		try
		{
			String myinfo = value;
			java.security.MessageDigest alg = java.security.MessageDigest.getInstance("SHA-1");
			alg.update(myinfo.getBytes());
			byte[] digest = alg.digest();
			System.out.println(byte2hex(digest));
//			通过某种方式传给其他人你的信息(myinfo)和摘要(digest)对方可以判断是否更改或传输正常
		}
		catch (java.security.NoSuchAlgorithmException ex)
		{
//			System.out.println("非法摘要算法");
		}
	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 获取CPU序列号
	 */
	public static String CpuNum() throws IOException
	{

//		long start = System.currentTimeMillis();
		Process process = Runtime.getRuntime().exec(new String[]
		{ "wmic", "cpu", "get", "ProcessorId" });
		process.getOutputStream().close();
		Scanner sc = new Scanner(process.getInputStream());
		String serial = sc.next();
		serial = sc.next();
		sc.close();
//		System.out.println(serial);
		return serial;
//		System.out.println("time:" + (System.currentTimeMillis() - start));

	}

}

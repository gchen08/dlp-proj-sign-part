package team.sjtu14573.antileak.tagGen;

import java.util.regex.Pattern;

import team.sjtu14573.antileak.injection.*;

public class TagGen
{
	private static long cre_time;
	private static long now_time;
	private static String cpu_0;
	private static String cpu_1;
	private static String tag = "";
	private static int _ver = 1;

	/**
	 * 判断是否为标签
	 * 
	 * @param tag
	 * @param version
	 * @return isTag
	 */
	public static boolean isTag(String tag, int version)
	{
		int len = Common.tagLength(version);
		if (tag.length() != len)
		{
			return (false);
		}
		if (tag.charAt(len - 1) != (char) (version + 48))
		{
			return (false);
		}
		String patternNum = "[0-9]{20}";
		if (!Pattern.matches(patternNum, tag.substring(0, 20)))
		{
			return (false);
		}
		String patternHex = "[0-9A-F]{32}";
		if (!Pattern.matches(patternHex, tag.substring(20, 52)))
		{
			return (false);
		}
		return (true);
	}

	/**
	 * 标签更新
	 * 
	 * @param oldTag
	 * @return updateTag
	 */
	public static String updateTag(String oldTag)
	{
		try
		{
			now_time = Tools.nowtime();
			cpu_1 = Tools.CpuNum();
			cpu_0 = oldTag.substring(21, 37);
			String newTag = oldTag.substring(0, 10) + String.valueOf(now_time) + cpu_0 + cpu_1 + String.valueOf(_ver);
			return (newTag);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (oldTag);
	}

	/**
	 * 生成标签
	 * 
	 * @param fileStr
	 * @return newTag
	 */
	public static String newTag(String fileStr)
	{
		try
		{
			cre_time = Tools.getCreateTime(fileStr);
			now_time = Tools.nowtime();
			String cretime = String.valueOf(cre_time);
			String nowtime = String.valueOf(now_time);
			String ver = String.valueOf(_ver);
			cpu_0 = Tools.CpuNum();
			cpu_1 = Tools.CpuNum();
			tag = cretime + nowtime + cpu_0 + cpu_1 + ver;
//			System.out.println(tag);
			return (tag);
//			Tools.Sha(tag);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (tag);
	}
}

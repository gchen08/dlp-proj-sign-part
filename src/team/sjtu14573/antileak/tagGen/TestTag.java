package team.sjtu14573.antileak.tagGen;

import java.io.IOException;

public class TestTag
{
	static long cre_time;
	static long now_time;
	static String cpu_0;
	static String cpu_1;
	static String tag;
	static int _ver;

	/**
	 * ��ǩ���ɲ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		_ver = 1;
		String file = "C:\\Documents\\ʵ����\\��й��\\�����ļ�\\����.xls";
		file = file.replaceAll("\\\\", "/");
		cre_time = Tools.getCreateTime(file);
		now_time = Tools.nowtime();
		String cretime = "" + cre_time;
		String nowtime = "" + now_time;
		String ver = "" + _ver;
		cpu_1 = Tools.CpuNum();
		cpu_0 = Tools.CpuNum();
		tag = cretime + nowtime + cpu_0 + cpu_1 + ver;
//		System.out.println(cre_time);
//		System.out.println(now_time);
		System.out.println(tag);
//		Tools.Sha(tag);

	}

}

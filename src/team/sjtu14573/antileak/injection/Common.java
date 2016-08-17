/**   
* @Title: BinaryFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: 标签嵌入提取操作通用类
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class Common
{

	/** 
	* @Title: getFilePath
	* @Description: 获取文件路径
	* @param file 文件地址 
	* @return getFilePath 文件路径
	* @throws 
	*/
	public static String getFilePath(String file)
	{
		file = file.replaceAll("\\\\", "/");
		int i = file.lastIndexOf("/");
		if (i != -1)
		{
			return (file.substring(0, i + 1));
		}
//		System.out.println("非法文件路径!");
		return ("");
	}

	/** 
	* @Title: getFileName
	* @Description: 获取文件名
	* @param file 文件地址 
	* @return getFileName 文件名
	* @throws 
	*/
	public static String getFileName(String file)
	{
		file = file.replaceAll("\\\\", "/");
		int i = file.lastIndexOf("/");
		if (i != -1)
		{
			return (file.substring(i + 1, file.length()));
		}
//		System.out.println("非法文件路径!");
		return ("");
	}

	/** 
	* @Title: getFileSuffix
	* @Description: 获取文件后缀名
	* @param filePath 文件地址 
	* @return getFileSuffix 后缀名
	* @throws 
	*/
	public static String getFileSuffix(String filePath)
	{
		int i = filePath.lastIndexOf(".");
		if (i != -1)
		{
			return (filePath.substring(i + 1, filePath.length()));
		}
//		System.out.println("非法文件路径!");
		return ("");
	}
	
	/** 
	* @Title: checkSuffix
	* @Description: 检测文件后缀名可嵌入
	* @param fileSuffix 文件后缀名 
	* @return checkSuffix 后缀名是否可嵌入
	* @throws 
	*/
	public static boolean checkSuffix(String fileSuffix)
	{
		String[] suffix = new String[]
		{ "doc", "xls", "ppt", "docx", "xlsx", "pptx", "pdf" };
		for (int i = 0; i < suffix.length; i++)
		{
			if (fileSuffix.equals(suffix[i]))
			{
				return (true);
			}
		}
		return (false);
	}

	/** 
	* @Title: isValid
	* @Description: 检查文件是否可嵌入
	* @param dir 文件路径
	* @return isValid 文件是否可嵌入
	* @throws 
	*/
	public static boolean isValid(String dir)
	{
		File file = new File(dir);
		if (!file.isDirectory() && checkSuffix(getFileSuffix(dir)))
		{
			return(true);
		}
		return(false);
	}
	
	/** 
	* @Title: transferCopy
	* @Description: 复制文件
	* @param source 源文件, target 目标文件
	* @return 
	* @throws 
	*/
	public static void transferCopy(File source, File target)
	{
		try
		{
			if (!target.exists())
			{
				FileInputStream inStream = new FileInputStream(source);
				FileOutputStream outStream = new FileOutputStream(target);
				FileChannel in = inStream.getChannel();
				FileChannel out = outStream.getChannel();
				in.transferTo(0, in.size(), out);
				inStream.close();
				outStream.close();
				in.close();
				out.close();
			}
			else
			{
//				System.out.println("�ļ��Ѵ��ڣ��޷�����!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: deleteDir
	* @Description: 递归删除文件夹
	* @param dir 文件或文件夹
	* @return deleteDir 删除是否成功
	* @throws 
	*/
	public static boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] nList = dir.list();
			for (int i = 0; i < nList.length; i++)
			{
				boolean del = deleteDir(new File(dir, nList[i]));
				if (!del)
				{
					return (false);
				}
			}
		}
		return (dir.delete());
	}

	/** 
	* @Title: deleteDir
	* @Description: 删除文件
	* @param filePath 文件路径
	* @return 
	* @throws 
	*/
	public static void deleteDir(String filePath)
	{
		try
		{
			File dir = new File(filePath);
			if (Common.deleteDir(dir))
			{
//				System.out.println("删除成功!");
			}
			else
			{
//				System.out.println("目录" + filePath + "删除失败！");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: tagLength
	* @Description: 获取标签长度
	* @param version 标签版本
	* @return tagLength 标签长度
	* @throws 
	*/
	public static int tagLength(int version)
	{
		if (version == 1)
		{
			return (53);
		}
		return (0);
	}
}
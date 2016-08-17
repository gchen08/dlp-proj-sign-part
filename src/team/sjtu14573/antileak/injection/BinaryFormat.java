/**   
* @Title: BinaryFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: Office二进制文件嵌入/提取程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class BinaryFormat
{

	/** 
	* @Title: embedProcess
	* @Description: 嵌入过程
	* @param filePath 文件路径, fileName 文件名, suffix 文件后缀名, tag 标签 
	* @return embedProcess 嵌入是否成功
	* @throws 
	*/
	public static boolean embedProcess(String filePath, String fileName, String suffix, String tag)
	{
		try
		{
			File biFile = new File(filePath + fileName);
			InputStream in = new FileInputStream(biFile);
			String stegoDirStr = filePath + "Stego/";
			File stegoDir = new File(stegoDirStr);
			if (!stegoDir.exists())
			{
				stegoDir.mkdirs();
			}
			File stegoFile = new File(stegoDirStr + fileName);
			Common.transferCopy(biFile, stegoFile);
			FileOutputStream out = new FileOutputStream(stegoFile, true);
			byte[] buffer = new byte[tag.length()];
			for (int i = 0; i < tag.length(); i++)
			{
				buffer[i] = (byte) Integer.parseInt(tag.substring(i, i + 1), 16);
			}
			out.write(buffer);
			in.close();
			out.close();
			return (true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (false);
	}

	/** 
	* @Title: extractProcess
	* @Description: 提取过程
	* @param filePath 文件路径, fileName 文件名, suffix 文件后缀名, version 标签版本 
	* @return extractProcess 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractProcess(String filePath, String fileName, String suffix, int version)
	{
		try
		{
			File biFile = new File(filePath + fileName);
			InputStream in = new FileInputStream(biFile);
			long fileLen = biFile.length();
			int tagLen = Common.tagLength(version);
			long count = 0;
			while (count < (fileLen - tagLen))
			{
				in.read();
				count++;
			}
			String tag = "";
			for (int i = 0; i < tagLen; i++)
			{
				tag += Integer.toHexString(in.read());
			}
			in.close();
			return (tag.toUpperCase());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ("");
	}

}

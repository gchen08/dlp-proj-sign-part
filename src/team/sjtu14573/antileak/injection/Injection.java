/**   
* @Title: Injection.java
* @Package  team.sjtu14573.antileak.injection
* @Description: 标签嵌入提取操作通用过程
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.io.File;

public class Injection
{

	/** 
	* @Title: embedProcess
	* @Description: 嵌入过程
	* @param filePath 文件路径, tag 标签 
	* @return embedProcess 嵌入是否成功
	* @throws 
	*/
	public static boolean embedProcess(String filePath, String tag)
	{
		try
		{
			String fileStr = filePath.replaceAll("\\\\", "/");
			File file = new File(fileStr);
			if (file.exists())
			{
				String path = Common.getFilePath(fileStr);
				String name = Common.getFileName(fileStr);
				String suffix = Common.getFileSuffix(name);
				boolean embed = false;
				if ((suffix.toLowerCase().equals("docx")) || (suffix.toLowerCase().equals("pptx"))
						|| (suffix.toLowerCase().equals("xlsx")))
				{
					embed = XMLFormat.embedProcess(path, name, suffix, tag);
				}
				if ((suffix.toLowerCase().equals("doc")) || (suffix.toLowerCase().equals("ppt"))
						|| (suffix.toLowerCase().equals("xls")))
				{
					embed = BinaryFormat.embedProcess(path, name, suffix, tag);
				}
				if (suffix.toLowerCase().equals("pdf"))
				{
					embed = PdfFormat.embedProcess(path, name, tag);
				}
				if (embed)
				{
					if (file.delete())
					{
						File src = new File(path + "Stego/" + name);
						Common.transferCopy(src, file);
						Common.deleteDir(path + "Stego/");
						return(true);
					}
					else
					{
//						System.out.println("删除失败！");
					}
//					System.out.println("嵌入完成！");
				}
				else
				{
					Common.deleteDir(path + "Stego/");
//					System.out.println("嵌入失败！");
				}
				return(false);
			}
			else
			{
//				System.out.println(fileStr + "文件不存在！");
				return(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return(false);
	}

	/** 
	* @Title: extractProcess
	* @Description: 提取过程
	* @param filePath 文件路径, version 标签版本 
	* @return extractProcess 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractProcess(String filePath, int version)
	{
		try
		{
			String fileStr = filePath.replaceAll("\\\\", "/");
			File file = new File(fileStr);
			if (file.exists())
			{
				String path = Common.getFilePath(fileStr);
				String name = Common.getFileName(fileStr);
				String suffix = Common.getFileSuffix(name);
				String extract = "";
				if ((suffix.toLowerCase().equals("docx")) || (suffix.toLowerCase().equals("pptx"))
						|| (suffix.toLowerCase().equals("xlsx")))
				{
					extract = XMLFormat.extractProcess(path, name, suffix.toLowerCase(), version);
				}
				if ((suffix.toLowerCase().equals("doc")) || (suffix.toLowerCase().equals("ppt"))
						|| (suffix.toLowerCase().equals("xls")))
				{
					extract = BinaryFormat.extractProcess(path, name, suffix.toLowerCase(), version);
				}
				if (suffix.toLowerCase().equals("pdf"))
				{
					extract = PdfFormat.extractProcess(path, name, version);
				}
				if (extract != "")
				{
					// System.out.println("提取成功！");
					return (extract);
				}
			}
			else
			{
				// System.out.println("文件不存在！");
				return ("");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ("");
	}

}

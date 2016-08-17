/**   
* @Title: XMLFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: OfficeXML文件嵌入/提取程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class XMLFormat
{
	
	private final static String encoding = "GBK";

	/** 
	* @Title: renameToZip
	* @Description: 重命名为zip文件
	* @param filePath 文件目录, fileName 文件名 
	* @return 
	* @throws 
	*/
	public static void renameToZip(String filePath, String fileName)
	{
		String zipName = fileName + ".zip";
		File srcFile = new File(filePath + fileName);
		File zipFile = new File(filePath + zipName);
		if (zipFile.exists() || (!srcFile.renameTo(zipFile)))
		{
			// System.out.println("无法重命名为zip文件！");
		}
	}

	/** 
	* @Title: renameToXML
	* @Description: 重命名为XML文件
	* @param filePath 文件目录, fileName 文件名, suffix 文件后缀名
	* @return 
	* @throws 
	*/
	public static void renameToXML(String filePath, String fileName, String suffix)
	{
		String srcName = fileName.replaceAll(".zip", "");
		File zipFile = new File(filePath + fileName);
		File srcFile = new File(filePath + srcName);
		if (srcFile.exists() || (!zipFile.renameTo(srcFile)))
		{
			// System.out.println("无法重命名为" + suffix + "文件！");
		}
	}

	/** 
	* @Title: zipFiles
	* @Description: 压缩文件
	* @param src 待压缩文件目录, dest 压缩文件
	* @return 
	* @throws 
	*/
	public static void zipFiles(File src, File dest)
	{
		try
		{
			Project proj = new Project();
			Zip zip = new Zip();
			zip.setProject(proj);
			zip.setDestFile(dest);
			FileSet fileSet = new FileSet();
			if (src.isFile())
			{
				fileSet.setFile(src);
			}
			else
			{
				fileSet.setDir(src);
			}
			zip.addFileset(fileSet);
			zip.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: unZipFiles
	* @Description: 解压缩文件
	* @param src 待解压缩文件, dest 解压后文件
	* @return 
	* @throws 
	*/
	public static void unZipFiles(File src, File dest)
	{
		try
		{
			Project proj = new Project();
			Expand expand = new Expand();
			expand.setProject(proj);
			expand.setTaskType("unzip");
			expand.setSrc(src);
			expand.setDest(dest);
			expand.setEncoding(encoding);
			expand.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: embedProcess
	* @Description: XML文件嵌入过程
	* @param filePath 文件路径, fileName 文件名, suffix 文件后缀名, tag 标签
	* @return embedProcess 嵌入是否成功
	* @throws 
	*/
	public static boolean embedProcess(String filePath, String fileName, String suffix, String tag)
	{
//		File xmlFile = new File(filePath + fileName);
//		String copyDirStr = filePath + "Orig/";
		String copyDirStr = filePath;
		String compDirStr = filePath + "Components/";
		String stegoDirStr = filePath + "Stego/";
//		File copyDir = new File(copyDirStr);
//		if (!copyDir.exists())
//		{
//			copyDir.mkdirs();
//		}
//		PreTreatment.transferCopy(xmlFile, new File(copyDirStr + fileName));
		renameToZip(copyDirStr, fileName);
		File zipFile = new File(copyDirStr + fileName + ".zip");
		File compDir = new File(compDirStr);
		if (!compDir.exists())
		{
			compDir.mkdirs();
		}
		unZipFiles(zipFile, compDir);
		File stegoDir = new File(stegoDirStr);
		if (!stegoDir.exists())
		{
			stegoDir.mkdirs();
		}
		if (((suffix.toLowerCase().equals("docx")) && (DocxFormat.embedProcess(compDirStr, tag)))
				|| ((suffix.toLowerCase().equals("xlsx")) && (XlsxFormat.embedProcess(compDirStr, tag)))
				|| ((suffix.toLowerCase().equals("pptx")) && (PptxFormat.embedProcess(compDirStr, tag))))
		{
			File stegoFile = new File(stegoDirStr + fileName + ".zip");
			zipFiles(compDir, stegoFile);
			renameToXML(copyDirStr, fileName + ".zip", suffix);
			renameToXML(stegoDirStr, fileName + ".zip", suffix);
			Common.deleteDir(compDirStr);
			return (true);
		}
		return (false);
	}

	/** 
	* @Title: extractProcess
	* @Description: XML文件提取过程
	* @param filePath 文件路径, fileName 文件名, suffix 文件后缀, version 标签版本号 
	* @return extractProcess 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractProcess(String filePath, String fileName, String suffix, int version)
	{
		renameToZip(filePath, fileName);
		String compDirStr = filePath + "Components/";
		File zipFile = new File(filePath + fileName + ".zip");
		File compDir = new File(compDirStr);
		if (!compDir.exists())
		{
			compDir.mkdirs();
		}
		unZipFiles(zipFile, compDir);
		String tag = "";
		if (suffix.toLowerCase().equals("docx"))
		{
			tag = DocxFormat.extractProcess(compDirStr, Common.tagLength(version));
			renameToXML(filePath, fileName + ".zip", suffix);
			Common.deleteDir(compDirStr);
		}
		if (suffix.toLowerCase().equals("xlsx"))
		{
			tag = XlsxFormat.extractProcess(compDirStr, Common.tagLength(version));
			renameToXML(filePath, fileName + ".zip", suffix);
			Common.deleteDir(compDirStr);
		}
		if (suffix.toLowerCase().equals("pptx"))
		{
			tag = PptxFormat.extractProcess(compDirStr, Common.tagLength(version));
			renameToXML(filePath, fileName + ".zip", suffix);
			Common.deleteDir(compDirStr);
		}
		return (tag);
	}

}

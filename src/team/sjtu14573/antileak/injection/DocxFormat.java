/**   
* @Title: DocxFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: Docx格式文件嵌入/提取程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import team.sjtu14573.antileak.constant.Constant;

public class DocxFormat
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
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmlDocument = db.parse(new File(filePath + "word/document.xml"));
			Node node = xmlDocument.getElementsByTagName("w:p").item(0);
			Element elmt = (Element) node;
			elmt.setAttribute(Constant.TAG_NAME, tag);
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			DOMSource dmS = new DOMSource(xmlDocument);
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			FileOutputStream fos = new FileOutputStream(filePath + "word/document.xml");
			StreamResult result = new StreamResult(fos);
			tf.transform(dmS, result);
			fos.close();
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
	* @param filePath 文件路径, len 标签长度 
	* @return extractProcess 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractProcess(String filePath, int len)
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document xmlDocument = db.parse(new File(filePath + "word/document.xml"));
			NodeList nList = xmlDocument.getElementsByTagName("w:p");
			for (int i = 0; i < nList.getLength(); i++)
			{
				Element elmt = (Element) nList.item(i);
				String tmpStr = elmt.getAttribute(Constant.TAG_NAME);
				if (tmpStr.length() == len)
				{
					return (tmpStr);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ("");
	}

}

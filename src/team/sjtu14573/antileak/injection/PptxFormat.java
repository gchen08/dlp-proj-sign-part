/**   
* @Title: PptxFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: Pptx格式文件嵌入/提取程序
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

public class PptxFormat
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
			Document xmlDocument = db.parse(new File(filePath + "ppt/tableStyles.xml"));
			Node node = xmlDocument.getElementsByTagName("a:tblStyleLst").item(0);
			String tmpStr = tag;
			while ((tmpStr.length() % 32) != 0)
			{
				tmpStr += "0";
			}
			int len = tmpStr.length();
			for (int i = 0; i < (len / 32); i++)
			{
				Element elmt = xmlDocument.createElement("a:tblStyle");
				String attr = "{" + tmpStr.substring(0, 8) + "-" + tmpStr.substring(8, 12) + "-"
						+ tmpStr.substring(12, 16) + "-" + tmpStr.substring(16, 20) + "-" + tmpStr.substring(20, 32)
						+ "}";
				elmt.setAttribute("styleId", attr);
				elmt.setAttribute("styleName", Constant.TAG_NAME);
				tmpStr = tmpStr.substring(32, tmpStr.length());
				node.appendChild(elmt);
			}
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			DOMSource dmS = new DOMSource(xmlDocument);
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			FileOutputStream fos = new FileOutputStream(filePath + "ppt/tableStyles.xml");
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
			Document xmlDocument = db.parse(new File(filePath + "ppt/tableStyles.xml"));
			NodeList nList = xmlDocument.getElementsByTagName("a:tblStyleLst");
			String tmpStr = "";
			loot: for (int i = 0; i < nList.getLength(); i++)
			{
				Element fElmt = (Element) nList.item(i);
				NodeList cList = fElmt.getChildNodes();
				for (int j = 0; j < cList.getLength(); j++)
				{
					Element cElmt = (Element) cList.item(j);
					String styleName = cElmt.getAttribute("styleName");
					if (styleName.equals(Constant.TAG_NAME))
					{
						String styleId = cElmt.getAttribute("styleId");
						styleId = styleId.replaceAll("-", "");
						tmpStr += styleId.substring(1, styleId.length() - 1);
					}
					if (tmpStr.length() >= len)
					{
						break loot;
					}
				}
			}
			if (tmpStr.length() >= len)
			{
				tmpStr = tmpStr.substring(0, len);
				return (tmpStr);
			}
			else
			{
				return("");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ("");
	}

}

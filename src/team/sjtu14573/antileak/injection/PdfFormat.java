/**   
* @Title: PdfFormat.java
* @Package  team.sjtu14573.antileak.injection
* @Description: pdf格式文件嵌入/提取程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfFormat
{

	private static String imgPath = "C:/Users/Gong/workspace/AntiLeak/lib/";
	private static String imgName = "testPIC.bmp";
	private static String imgSuffix = "bmp";
	private static float imgWidth = 20f;
	private static float imgHeight = 20f;
	private static final int DATA_SIZE = 8;
	private static final int MAX_INT_LEN = 4;

	/** 
	* @Title: intToBytes
	* @Description: 数字转比特数组
	* @param int 待转换数字 
	* @return intToBytes 比特数组
	* @throws 
	*/
	public static byte[] intToBytes(int i)
	{
		try
		{
			byte[] integerBs = new byte[MAX_INT_LEN];
			integerBs[0] = (byte) ((i >>> 24) & 0xFF);
			integerBs[1] = (byte) ((i >>> 16) & 0xFF);
			integerBs[2] = (byte) ((i >>> 8) & 0xFF);
			integerBs[3] = (byte) (i & 0xFF);
			return (integerBs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/** 
	* @Title: accessBytes
	* @Description: 获取图像比特列
	* @param img 图像文件
	* @return accessBytes 图像比特列
	* @throws 
	*/
	public static byte[] accessBytes(BufferedImage img)
	{
		try
		{
			WritableRaster raster = img.getRaster();
			DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
			return buffer.getData();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (null);
	}

	/** 
	* @Title: buildStego
	* @Description: 将信息转化为比特列
	* @param sign 隐藏信息
	* @return buildStego 隐藏信息比特列
	* @throws 
	*/
	public static byte[] buildStego(String sign)
	{
		try
		{
			byte[] msgBytes = sign.getBytes();
			byte[] lenBs = intToBytes(msgBytes.length);
			int totalLen = lenBs.length + msgBytes.length;
			byte[] stego = new byte[totalLen];
			System.arraycopy(lenBs, 0, stego, 0, lenBs.length);
			System.arraycopy(msgBytes, 0, stego, lenBs.length, msgBytes.length);
			return stego;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (null);
	}

	/** 
	* @Title: embedTag
	* @Description: 嵌入标签过程
	* @param fileName 文件名, stegoImgPath 图像地址, tag 标签
	* @return embedTag 嵌入是否成功
	* @throws 
	*/
	public static boolean embedTag(String fileName, String stegoImgPath, String tag)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(imgPath + imgName));
			if (img == null)
			{
				return (false);
			}
			byte[] stego = buildStego(tag);
			int totalLen = stego.length;
			byte[] imBytes = accessBytes(img);
			int imLen = imBytes.length;
			if ((totalLen * DATA_SIZE) > imLen)
			{
				System.out.println("Ƕ��λ�ò���!");
				return false;
			}
			int offset = 0; // start position
			for (int i = 0; i < stego.length; i++)
			{
				int byteVal = stego[i];
				for (int j = 7; j >= 0; j--)
				{
					int bitVal = (byteVal >>> j) & 1;
					imBytes[offset] = (byte) ((imBytes[offset] & 0xFE) | bitVal);
					offset++;
				}
			}
			File imgFile = new File(stegoImgPath + fileName + "1." + imgSuffix);
			ImageIO.write(img, imgSuffix, imgFile);
			return (true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (false);
	}

	/**
	 * ���Ƕ�����
	 * 
	 * @param imBytes
	 * @param size
	 * @param offset
	 * @return extractHiddenBytes
	 */
	public static byte[] extractHiddenBytes(byte[] imBytes, int size, int offset)
	{
		try
		{
			int finalPosn = offset + (size * DATA_SIZE);
			if (finalPosn > imBytes.length)
			{
				return (null);
			}
			byte[] hiddenBytes = new byte[size];
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < DATA_SIZE; j++)
				{
					hiddenBytes[i] = (byte) ((hiddenBytes[i] << 1) | (imBytes[offset] & 1));
					offset++;
				}
			}
			return (hiddenBytes);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (null);
	}

	/** 
	* @Title: extractSign
	* @Description: 从图像上提取标签过程
	* @param imgPath 图像地址, imgName 图像名, len 标签长度
	* @return extractSign 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractSign(String imgPath, String imgName, int len)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(imgPath + imgName));
			if (img == null)
			{
				return ("");
			}
			byte[] imBytes = accessBytes(img);
			int offset = 0;
			byte[] lenBytes = extractHiddenBytes(imBytes, MAX_INT_LEN, offset);
			if (lenBytes == null)
			{
				return ("");
			}
			int msgLen = (((lenBytes[0] & 0xFF) << 24) | ((lenBytes[1] & 0xFF) << 16) | ((lenBytes[2] & 0xFF) << 8)
					| (lenBytes[3] & 0xFF));
			if (msgLen != len)
			{
				return ("");
			}
			byte[] msgBytes = extractHiddenBytes(imBytes, len, MAX_INT_LEN * DATA_SIZE);
			String sign = new String(msgBytes);
			return (sign);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ("");
	}

	/** 
	* @Title: addImage
	* @Description: 在pdf文件上添加图像
	* @param filePath 文件地址, fileName 文件名 , outPath, 输出目录, imgPath 图像地址
	* @return 
	* @throws 
	*/
	public static void addImage(String filePath, String fileName, String outPath, String imgPath)
	{
		try
		{
			PdfReader pdfReader = new PdfReader(filePath + fileName);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(outPath + fileName));
			int pageNum = pdfReader.getNumberOfPages();
			com.itextpdf.text.Rectangle lastPage = pdfReader.getPageSize(pageNum);
			// System.out.println(lastPage.getWidth());
			// System.out.println(lastPage.getHeight());
			Image img = Image.getInstance(imgPath + fileName + "1." + imgSuffix);
			PdfContentByte content = pdfStamper.getUnderContent(pageNum);
			img.setAbsolutePosition(lastPage.getWidth() - imgWidth / 2, 0f);
			img.scalePercent(50f);
			content.addImage(img);
			pdfStamper.close();
			pdfReader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: convertImage
	* @Description: 将pdf转成图像文件
	* @param filePath 文件地址, fileName 文件名 , imgDirStr 图像目录
	* @return convertImage 转化是否成功
	* @throws 
	*/
	public static boolean convertImage(String filePath, String fileName, String imgDirStr)
	{
		try
		{
			PDDocument pdfFile = PDDocument.load(new File(filePath + fileName));
			int pageNum = pdfFile.getNumberOfPages();
			// System.out.println(pageNum);
			PDPage lastPage = (PDPage) pdfFile.getDocumentCatalog().getAllPages().get(pageNum - 1);
			// System.out.println(lastPage.getMediaBox().getWidth());
			// System.out.println(lastPage.getMediaBox().getHeight());
			BufferedImage image = lastPage.convertToImage();
			String imgName = fileName + "2." + imgSuffix;
			File imgFile = new File(imgDirStr + imgName);
			ImageIO.write(image, imgSuffix, imgFile);
			pdfFile.close();
			return (true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (false);
	}

	/** 
	* @Title: cutImage
	* @Description: 从pdf文件上截取图像
	* @param filePath 文件地址, fileName
	* @return
	* @throws 
	*/
	public static void cutImage(String filePath, String fileName)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(filePath + fileName + "2." + imgSuffix));
			FileInputStream fis = new FileInputStream(filePath + fileName + "2." + imgSuffix);
			Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(imgSuffix);
			ImageReader imgReader = it.next();
			ImageInputStream iis = ImageIO.createImageInputStream(fis);
			imgReader.setInput(iis, true);
			ImageReadParam param = imgReader.getDefaultReadParam();
			int x = (int) (img.getWidth() - imgWidth);
			int y = (int) (img.getHeight() - imgHeight);
			int width = (int) imgWidth;
			int height = (int) imgHeight;
			Rectangle rect = new Rectangle(x, y, width, height);
			param.setSourceRegion(rect);
			BufferedImage bufImg = imgReader.read(0, param);
			ImageIO.write(bufImg, imgSuffix, new File(filePath + fileName + "3." + imgSuffix));
			fis.close();
			iis.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/** 
	* @Title: embedProcess
	* @Description: 嵌入过程
	* @param filePath 文件路径, fileName 文件名, sign 标签 
	* @return embedProcess 嵌入是否成功
	* @throws 
	*/
	public static boolean embedProcess(String filePath, String fileName, String sign)
	{
		try
		{
			// File origFile = new File(filePath + fileName);
			// String copyDirStr = filePath + "Orig/";
			String stegoDirStr = filePath + "Stego/";
			String imgDirStr = stegoDirStr + "images/";
			// File copyDir = new File(copyDirStr);
			// if (!copyDir.exists())
			// {
			// copyDir.mkdirs();
			// }
			// PreTreatment.transferCopy(origFile, new File(copyDirStr +
			// fileName));
			File stegoDir = new File(stegoDirStr);
			if (!stegoDir.exists())
			{
				stegoDir.mkdirs();
			}
			File imgDir = new File(imgDirStr);
			if (!imgDir.exists())
			{
				imgDir.mkdirs();
			}
			if (embedTag(fileName, imgDirStr, sign))
			{
				addImage(filePath, fileName, stegoDirStr, imgDirStr);
				// PreTreatment.deleteDir(imgDirStr);
				return (true);
			}
			else
			{
//				System.out.println("嵌入失败!");
			}
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
	* @param filePath 文件路径, fileName 文件名, version 标签版本 
	* @return extractProcess 提取标签（失败则为""）
	* @throws 
	*/
	public static String extractProcess(String filePath, String fileName, int version)
	{
		try
		{
			String imgDirStr = filePath + "images/";
			File imgDir = new File(imgDirStr);
			if (!imgDir.exists())
			{
				imgDir.mkdirs();
			}
			if (convertImage(filePath, fileName, imgDirStr))
			{
				cutImage(imgDirStr, fileName);
				String tag = extractSign(imgDirStr, fileName + "3." + imgSuffix, Common.tagLength(version));
				if (tag != "")
				{
					Common.deleteDir(imgDirStr);
					return (tag);
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

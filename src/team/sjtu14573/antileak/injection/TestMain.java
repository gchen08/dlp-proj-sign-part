/**   
* @Title: TestMain.java
* @Package  team.sjtu14573.antileak.injection
* @Description: 标签嵌入提取操作测试程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.injection;

public class TestMain
{
	/** 
	* @Title: main
	* @Description: main函数
	* @param args 参数值
	* @return 
	* @throws 
	*/
	public static void main(String[] args)
	{
		String origFile = "C:/Documents/实验室/反泄密/测试文件/测试.docx";
		String tag = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1";
		System.out.println("待嵌入文件："+ origFile);
		System.out.println("待嵌入标签：" + tag);
		Injection.embedProcess(origFile, tag);

		String stegoFile = "C:/Documents/实验室/反泄密/测试文件/测试1.docx";
		int version = 1;
		System.out.println("待提取文件：" + stegoFile);
		System.out.println("标签版本号：" + version);
		System.out.println("提取标签为：" + Injection.extractProcess(stegoFile, version));
	}

}
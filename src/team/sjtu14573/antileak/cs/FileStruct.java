/**   
* @Title: FileStruct.java
* @Package  team.sjtu14573.antileak.cs
* @Description: 文件结构类
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.cs;

import team.sjtu14573.antileak.injection.Common;

public class FileStruct
{
	private String name; // 文件名
	private String suffix; // 后缀名
	private String path; // 文件路径
	private int state;  // 文件状态标记（更改，创建，删除等）
	private String tag; // 嵌入标签

	public FileStruct(String dir)
	{
		this.path = dir;
		this.name = Common.getFileName(dir);
		this.suffix = Common.getFileSuffix(dir);
		this.state = -1;
		this.tag = "";
	}

	public String getName()
	{
		return (this.name);
	}

	public String getSuffix()
	{
		return (this.suffix);
	}

	public String getPath()
	{
		return (this.path);
	}

	public int getState()
	{
		return (this.state);
	}
	
	public void updateState(int state)
	{
		this.state = state;
	}
	
	public String getTag()
	{
		return (this.tag);
	}

	public void updateTag(String tag)
	{
		this.tag = tag;
	}
}

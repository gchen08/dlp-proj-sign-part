/**   
* @Title: Client.java
* @Package  team.sjtu14573.antileak.cs
* @Description: 客户端程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.cs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedList;
import team.sjtu14573.antileak.injection.Injection;
import team.sjtu14573.antileak.injection.Common;
import team.sjtu14573.antileak.tagGen.TagGen;
import team.sjtu14573.antileak.constant.*;

public class Client
{
	private static ArrayList<FileStruct> fsList = new ArrayList<FileStruct>(); // 文件列表
	private static String dir = "C:\\Documents\\实验室\\反泄密\\测试文件\\Client\\"; // 测试目录
	private static String logDir = "C:\\Documents\\实验室\\反泄密\\测试文件\\Client\\log.txt"; // 日志文件
	private static boolean monRun = false;

	/** 
	* @Title: getFiles 
	* @Description: 获取目录文件
	* @param dir 目录地址 
	* @return 
	* @throws 
	*/
	public static void getFiles(String dir)
	{
		File root = new File(dir);
		File[] files = root.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				getFiles(file.getAbsolutePath());
			}
			else
			{
				String filePath = file.getAbsolutePath();
				if (Common.isValid(filePath))
				{
					FileStruct fs = new FileStruct(filePath);
					fsList.add(fs);
				}
			}
		}
	}

	/** 
	* @Title: init
	* @Description: 客户端初始操作，为目录下文件进行标签嵌入
	* @param
	* @return 
	* @throws 
	*/
	public static void init()
	{
		try
		{
			getFiles(dir);
			int count = 0;
			int oldf = 0;
			int newf = 0;
			for (FileStruct fs : fsList)
			{
				count++;
				String tag = Injection.extractProcess(fs.getPath(), Constant._VER);
				if (TagGen.isTag(tag, Constant._VER))
				{
					tag = TagGen.updateTag(tag);
					oldf++;
				}
				else
				{
					tag = TagGen.newTag(fs.getPath());
					newf++;
				}
				if (Injection.embedProcess(fs.getPath(), tag))
				{
					fs.updateTag(tag);
					fs.updateState(0);
				}
			}
			System.out.println("共" + count + "文件，其中" + oldf + "文件更新标签，" + newf + "文件嵌入标签");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: fileMon
	* @Description: 监视目录线程
	* @param
	* @return 
	* @throws 
	*/
	public static class fileMon implements Runnable
	{
		private String filePath;

		public fileMon()
		{
			this.filePath = dir;
		}

		@Override
		public void run()
		{
			try
			{
				WatchService watchService = FileSystems.getDefault().newWatchService();
				Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
						StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
				File rootFile = new File(filePath);
				LinkedList<File> fileList = new LinkedList<File>();
				fileList.addLast(rootFile);
				while (fileList.size() > 0)
				{
					File file = fileList.removeFirst();
					if (file.listFiles() == null)
					{
						continue;
					}
					for (File leafFile : file.listFiles())
					{
						if (leafFile.isDirectory())
						{
							fileList.addLast(leafFile);
							Paths.get(leafFile.getAbsolutePath()).register(watchService,
									StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
									StandardWatchEventKinds.ENTRY_MODIFY);
						}
					}
				}
				while (monRun)
				{
					WatchKey watchKey = watchService.take();
					for (WatchEvent<?> event : watchKey.pollEvents())
					{
						Path dir = (Path) watchKey.watchable();
						String fullPath = dir.resolve(event.context().toString()).toString();
						// System.out.println(fullPath + "->" + event.kind());
						if (Common.isValid(fullPath))
						{
							System.out.println(fullPath + "->" + event.kind());
//							FileStruct fs = new FileStruct(fullPath);
//							if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE))
//							{
//								fsList.remove(fs);
//							}
//							else
//							{
//								fsList.add(fs);
//							}
						}
					}
					boolean valid = watchKey.reset();
					if (!valid)
					{
						break;
					}
				}
				watchService.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/** 
	* @Title: createLog
	* @Description: 提取标签，生成日志
	* @param log 目录地址
	* @return 
	* @throws 
	*/
	public static void createLog(String log)
	{
		try
		{
			String logStr = log.replaceAll("\\\\", "/");
			File logFile = new File(logStr);
			FileWriter fw = new FileWriter(logFile);
			BufferedWriter bw = new BufferedWriter(fw);
			for (FileStruct fs : fsList)
			{
				bw.write(fs.getTag());
				bw.newLine();
			}
			bw.close();
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** 
	* @Title: clientThread
	* @Description: 客户端线程
	* @param
	* @return 
	* @throws 
	*/
	private static class clientThread implements Runnable
	{
		Socket socket = null;
		
		public clientThread(Socket socket)
		{
			this.socket = socket;
		}

		@Override
		public void run()
		{
			InputStream in = null;
			OutputStream out = null;
			try
			{
				in = socket.getInputStream();
				out = socket.getOutputStream();
				String str = "";
				Thread fileMonitor = new Thread(new fileMon());
				String localIP = InetAddress.getLocalHost().getHostAddress();
				doWrite(out, "IP" + localIP);
				while (true)
				{
					Thread.sleep(1000);
					str = doRead(in).trim();
					if (str.equals("Hello"))
					{
						System.out.println("已连接服务器");
					}
					else if (str.startsWith("Command"))
					{
						String comStr = str.replaceAll("Command", "");
						if (comStr.equals("1"))
						{
							System.out.println("收到命令：Check");
							init();
						}
						if (comStr.equals("2") && !fileMonitor.isAlive())
						{
							System.out.println("收到命令：Mark");
							monRun = true;
							fileMonitor.start();
							System.out.println("监视目录中");
						}
						if (comStr.equals("3") && fileMonitor.isAlive())
						{
							System.out.println("收到命令：Stop");
							fileMonitor.interrupt();
							monRun = false;
							System.out.println("已停止监视");
						}
						if (comStr.equals("4"))
						{
							System.out.println("收到命令：Submit");
							createLog(logDir);
							// submitLog();
							System.out.println("日志生成完毕");
						}
						if (comStr.equals("5"))
						{
							System.out.println("收到命令：Exit");
							doWrite(out, "Bye!");
							break;
						}
						doWrite(out, "Done!");
					}
				}
				System.out.println("连接已关闭");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/** 
		* @Title: doRead
		* @Description: Socket读取操作
		* @param in 输入流
		* @return String 读取出的字符串
		* @throws 
		*/
		private static String doRead(InputStream in)
		{
			try
			{
				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				return (new String(bytes).trim());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return ("");
		}

		/** 
		* @Title: doWrite
		* @Description: Socket写入操作
		* @param out 输出流, str 要写入的字符串
		* @return boolean 写入是否成功
		* @throws 
		*/
		private static boolean doWrite(OutputStream out, String str)
		{
			try
			{
				out.write(str.getBytes());
				out.flush();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return (true);
		}
	}

	/** 
	* @Title: main
	* @Description: main函数
	* @param args 参数值
	* @return 
	* @throws 
	*/
	public static void main(String[] args)
	{
		try
		{
			System.out.println("客户端开启");
			Socket socket = new Socket(Constant.SERVER_IP, Constant.PORT);
			socket.setSoTimeout(50000);
			new Thread(new clientThread(socket)).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

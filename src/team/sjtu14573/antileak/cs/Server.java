/**   
* @Title: Server.java
* @Package  team.sjtu14573.antileak.cs
* @Description: 客户端程序
* @author gchen08   
* @date 2016/7/28
* @version V1.0   
*/
package team.sjtu14573.antileak.cs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import team.sjtu14573.antileak.constant.*;

public class Server
{

	/** 
	* @Title: ServerThread
	* @Description: 服务器线程
	* @param
	* @return 
	* @throws 
	*/
	private static class ServerThread implements Runnable
	{
		private Socket socket = null;

		public ServerThread(Socket socket)
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
				String clientIP = null;
				boolean clientConnected = false;
				boolean sendCommand = false;
				while (true)
				{
					Thread.sleep(1000);
					String str = doRead(in);
					if (str.startsWith("IP") && !clientConnected)
					{
						doWrite(out, "Hello");
						clientIP = str.substring(3, str.length());
						clientConnected = true;
						System.out.println("客户端: " + clientIP + "已连接");
					}
					else
					{
						if (!sendCommand)
						{
							System.out.println("请键入命令：1.Check 2.Mark 3.Stop 4.Submit 5.Exit");
							InputStreamReader isr = new InputStreamReader(System.in);
							BufferedReader br = new BufferedReader(isr);
							String strCommand = br.readLine().trim();
							boolean isCommand = false;
							while (!isCommand)
							{
								if (strCommand.equals("1") || strCommand.equals("2") || strCommand.equals("3")
										|| strCommand.equals("4") || strCommand.equals("5"))
								{
									isCommand = true;
								}
								else
								{
									strCommand = br.readLine();
								}
							}
							doWrite(out, "Command" + strCommand);
							sendCommand = true;
						}
						else
						{
							if (str.startsWith("Done"))
							{
								sendCommand = false;
							}
							if (str.startsWith("Bye"))
							{

							}
						}
					}
//					if (!getResult && str.startsWith("file"))
//					{
//						str = str.replaceAll("file", "");
//						String logPath = logDir.replaceAll("\\\\", "/");
//						File log = new File(logPath);
//						FileWriter fw = new FileWriter(log);
//						BufferedWriter bw = new BufferedWriter(fw);
//						int line = 0;
//						while ((line = str.indexOf("\n")) != -1)
//						{
//							bw.append(str.substring(0, line));
//							bw.newLine();
//						}
//						bw.close();
//						fw.close();
//						System.out.println("日志接受完毕");
//						getResult = true;
//					}
				}
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
		* @return doRead 读取出的字符串
		* @throws 
		*/
		private static String doRead(InputStream in)
		{
			try
			{
				byte[] bytes = new byte[in.available()];
				in.read(bytes);
				// System.out.println(new String(bytes).trim());
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
		* @return doWrite 写入是否成功
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
	public static void main(String args[])
	{
		ServerSocket serverSocket = null;
		System.out.println("服务器开启");
		try
		{
			serverSocket = new ServerSocket(Constant.PORT);
			while (true)
			{
				Socket socket = serverSocket.accept();
				new Thread(new ServerThread(socket)).start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				serverSocket.close();
				System.out.println("服务器关闭");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

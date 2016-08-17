# AntiLeak/system

## Table of Contents

1. [Description](#description)
2. [Setup](#setup)
3. [Usage](#usage)
4. [Reference](#reference)
5. [Limitations](#limitations)

## Description

**System** module of AntiLeak project implements the functionalities of both server and client, according to demands given by [README.md](http://202.120.7.35:8090/jacksonstarry/antileakcs/blob/master/README.md"). A server is capable of sending commands to and receiving requests or files from any client connected. Functions provided by [AntiLeak/injection](http://202.120.7.35:8090/AntiLeak/injection) and [AntiLeak/tag_gen](http://202.120.7.35:8090/AntiLeak/tag_gen) are utilized and integrated in this module to maintain all the documents in a certain client folder is tag-embedded.

## Setup

- Keep **Server.java** running in a server. A message will be displayed whenever a socket is accepted.

> 服务器开启<br>
> 客户端: 92.168.1.112已连接

- Start **Client.java** in any client. If the sent socket is accepted, a confirmation will be shown as:

> 客户端开启<br>
> 已连接服务器

## Usage

**Server:** Whenever a socket is accepted or a command is accomplished, the following message is displayed.

> 请键入命令：1.Check 2.Mark 3.Stop 4.Submit 5.Exit

Type number 1 to 5 and the client would execute the corresponding command.

- Check: scan and mark all documents within a certain folder
- Mark: monitor the folder with WatchService API
- Stop: stop monitoring
- Submit: submit a log to the server
- Exit: stop the socket

**Client:**

Performa certain functions according to the received command. Whenever a task is done, an acknowledgement message is sent to the server. An examples is presented here to introduce the usage.

Exp 1

> 收到命令：Check<br>
> 共8文件，其中8文件更新标签，0文件嵌入标签

Exp 2

> 收到命令：Mark<br>
> 监视目录中<br>
> C:\Documents\实验室\反泄密\测试文件\Client\信安学院2015-2016%281%29研究生课程表.xls->ENTRY_DELETE<br>
> C:\Documents\实验室\反泄密\测试文件\Client~$息安全技术基础和安全策略》.doc->ENTRY_CREATE<br>
> C:\Documents\实验室\反泄密\测试文件\Client~$息安全技术基础和安全策略》.doc->ENTRY_MODIFY<br>
> C:\Documents\实验室\反泄密\测试文件\Client~$息安全技术基础和安全策略》.doc->ENTRY_DELETE

Exp 3

> 收到命令：Stop<br>
> 已停止监视

Exp 4

> 收到命令：Submit<br>
> 日志生成完毕

Exp 5

> 收到命令：Exit<br>
> 连接已关闭

## Reference

Please the following command in cmd window

```javadoc
    javadoc *.java -encoding UTF-8 -charset UTF-8
```

Our codes are well-commented.

## Limitations

- All file information is stored as an ArrayList of FileStruct, a customized structure to describe a file. Database would be a better alternate.
- WatchService API needs to scan and register certain directories or files beforehand, which results in the incompetency to watch any files created in a new sub-directory.
- A list of sockets is needed to maintain all active clients.
- Function to generate a trace string/diagram to depict the transmission of a given file is not implemented yet.

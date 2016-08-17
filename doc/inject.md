# AntiLeak/injection

## Table of Contents

1. [Description](#description)
2. [Setup](#setup)

  - [What [AntiLeak/injection] affects](#what-[AntiLeak/injection]-affects)
  - [Setup requirements](#setup-requirements)
  - [Beginning with [AntiLeak/injection]](#beginning-with-[AntiLeak/injection])

3. [Usage](#usage)

4. [Reference](#reference)
5. [Limitations](#limitations)

## Description

**Injection** module of AntiLeak project implements the embedment of a give tag into files of certain types, including office files and pdf file. **Injection** module also accomplishes the functionality to provide a tag extracted from a valid-type file.

## Setup

We provide a test program [TestMain.java](http://202.120.7.35:8090/AntiLeak/injection/blob/master/src/team/sjtu14573/antileak/injection/TestMain.java) to evaluate and present the performance of this module.

### What [AntiLeak/injection] affects

- The file to be embedded/extracted will be overwritten if not secured, so please be prepared with proper backup.
- Failure of embedding or extracting might result in the creation of a **Stego** folder, please delete it before next running.

### Setup requirements

- Our module works the file of which the type is Office binary files(_doc, xls, ppt_), Office XML files(_docx, xlsx, pptx_) and PDF file(_pdf_). Make sure you have a valid file.
- A valid tag is necessary for the tag embedment. You can get one from module [AntiLeak/tag_gen](http://202.120.7.35:8090/AntiLeak/tag_gen).

### Beginning with AntiLeak/injection

Provide us with the full path of a file. To embed a tag, add the tag into input; to extract a tag, add the tag version instead.

## Usage

By changing the value of variables in [TestMain.java](http://202.120.7.35:8090/AntiLeak/injection/blob/master/src/team/sjtu14573/antileak/injection/TestMain.java), the functions of our module can be implemented. Two examples is presented to illustrate the usage of embedment and extraction functions of our module respectively.

Exp1 - embedment

**Input**

variables  |         meaning
---------- | :----------------------:
_origFile_ | file path of a test file
_tag_      |    tag to be embeded

```java
String origFile = "C:/Documents/实验室/反泄密/测试文件/测试.docx";
String tag = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1";
```

**Output**

> 待嵌入文件：C:/Documents/实验室/反泄密/测试文件/测试.docx<br>
> 待嵌入标签：FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1

Exp2 - extraction

**Input**

variables   |              meaning
----------- | :--------------------------------:
_stegoFile_ |      file path of a test file
_version_   | version of the tag to be extracted

```java
String stegoFile = "C:/Documents/实验室/反泄密/测试文件/测试.docx";
int version = 1;
```

**Output**

> 待提取文件：C:/Documents/实验室/反泄密/测试文件/测试.docx<br>
> 标签版本号：1<br>
> 提取标签为：FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1

## Reference

Please the following command in cmd window

```javadoc
    javadoc *.java -encoding UTF-8 -charset UTF-8
```

Our codes are well-commented.

## Limitations

- The time complexity of tag extraction from Office binary files is *O(n)*, where *n* is the length of file, which indicates it takes quite long do deal with a large Office file.
- According to the preliminary experiment, our module only works on parts of PDF files. Failures result from conditions like diverse pdf encoding, incompatibility between [pdfbox](https://pdfbox.apache.org/) and jdk, etc.

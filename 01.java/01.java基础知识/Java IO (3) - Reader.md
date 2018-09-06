#Java IO (3) - Reader

@(编程)

##前言
JavaIO一共包括两种，一种是stream，一种是reader/writer，每种又包括in/out，所以一共是四种包。Java 流在处理上分为字符流和字节流。字符流处理的单元为 2 个字节的 Unicode 字符，分别操作字符、字符数组或字符串，而字节流处理单元为 1 个字节，操作字节和字节数组。
Java 内用 Unicode 编码存储字符，字符流处理类负责将外部的其他编码的字符流和 java 内 Unicode 字符流之间的转换。而类 InputStreamReader 和 OutputStreamWriter 处理字符流和字节流的转换。字符流（一次可以处理一个缓冲区）一次操作比字节流（一次一个字节）效率高。
##0. 目录
1. Reader
2. BufferedReader
3. InputStreamReader
4. FileReader
5. 总结


##1. Reader

![reader](http://images.cnblogs.com/cnblogs_com/wardensky/645143/o_reader.png)
Reader是一个抽象类，其介绍如下：
>Abstract class for reading character streams. The only methods that a subclass must implement are read(char[], int, int) and close(). Most subclasses, however, will override some of the methods defined here in order to provide higher efficiency, additional functionality, or both.

###1.1 主要方法
```
abstract void	close()
Closes the stream and releases any system resources associated with it.

void	mark(int readAheadLimit)
Marks the present position in the stream.

boolean	markSupported()
Tells whether this stream supports the mark() operation.

int	read()
Reads a single character.

int	read(char[] cbuf)
Reads characters into an array.

abstract int	read(char[] cbuf, int off, int len)
Reads characters into a portion of an array.

int	read(CharBuffer target)
Attempts to read characters into the specified character buffer.

boolean	ready()
Tells whether this stream is ready to be read.

void	reset()
Resets the stream.

long	skip(long n)
Skips characters.
```
注意，不同于stream的是reader读的是char[]。
其常见的子类包括BufferedReader和InputStreamReader，InputStreamReader的子类FileReader也很常见，下面简单介绍一下。
##2. BufferedReader
BufferedReader继承Reader，本身的方法非常简单，其官方解释如下：
>Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
>The buffer size may be specified, or the default size may be used. The default is large enough for most purposes.

简单翻译一下：
>从流里面读取文本，通过缓存的方式提高效率，读取的内容包括字符、数组和行。
>缓存的大小可以指定，也可以用默认的大小。大部分情况下，默认大小就够了。


###2.1. 构造函数
BufferedReader有两个构造函数，其声明如下：
```
BufferedReader(Reader in)
Creates a buffering character-input stream that uses a default-sized input buffer.

BufferedReader(Reader in, int sz)
Creates a buffering character-input stream that uses an input buffer
 of the specified size.
```
一个是传一个Reader，另外一个增加了缓存的大小。

常见的初始化方法
```
BufferedReader br = new BufferedReader(new FileReader("d:/123.txt"));
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
```
第一个方法是读取一个文件；第二个方法是从标准输入读。
###2.2. 主要方法
```
void	close()
Closes the stream and releases any system resources associated with it.

void	mark(int readAheadLimit)
Marks the present position in the stream.

boolean	markSupported()
Tells whether this stream supports the mark() operation, which it does.

int	read()
Reads a single character.

int	read(char[] cbuf, int off, int len)
Reads characters into a portion of an array.

String	readLine()
Reads a line of text.

boolean	ready()
Tells whether this stream is ready to be read.

void	reset()
Resets the stream to the most recent mark.

long	skip(long n)
Skips characters.
```
提供了三种读数据的方法read、read(char[] cbuf, int off, int len)、readLine()，其中常用的是readLine。

##3. InputStreamReader
InputStreamReader的介绍如下：
>An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset. The charset that it uses may be specified by name or may be given explicitly, or the platform's default charset may be accepted.
Each invocation of one of an InputStreamReader's read() methods may cause one or more bytes to be read from the underlying byte-input stream. To enable the efficient conversion of bytes to characters, more bytes may be read ahead from the underlying stream than are necessary to satisfy the current read operation.
For top efficiency, consider wrapping an InputStreamReader within a BufferedReader. For example:
```
 BufferedReader in
   = new BufferedReader(new InputStreamReader(System.in));
```
也就是说，InputStreamReader是把字节翻译成字符的。

###3.1构造函数
```
InputStreamReader(InputStream in)
Creates an InputStreamReader that uses the default charset.

InputStreamReader(InputStream in, Charset cs)
Creates an InputStreamReader that uses the given charset.

InputStreamReader(InputStream in, CharsetDecoder dec)
Creates an InputStreamReader that uses the given charset decoder.

InputStreamReader(InputStream in, String charsetName)
Creates an InputStreamReader that uses the named charset.

```
可以看到，InputStreamReader的构造函数会传入一个字符编码，通常用InputStreamReader来解决乱码问题。

###3.2. 主要方法
```
void	close()
Closes the stream and releases any system resources associated with it.

String	getEncoding()
Returns the name of the character encoding being used by this stream.

int	read()
Reads a single character.

int	read(char[] cbuf, int offset, int length)
Reads characters into a portion of an array.

boolean	ready()
Tells whether this stream is ready to be read.
```
###3.3. 示例代码
经常用InputStreamReader解决乱码问题，示例代码如下：
```
	private void test() throws Throwable {
		BufferedReader in = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					"d:/123.txt"), "UTF-8");
			in = new BufferedReader(isr);
			while (true) {
				String lineMsg = in.readLine();
				if (lineMsg == null || lineMsg.equals("")) {
					break;
				}
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Throwable t) {
				throw t;
			}
		}
	}

```
编码集见本文末尾。
##4. FileReader
其介绍如下：
>Convenience class for reading character files. The constructors of this class assume that the default character encoding and the default byte-buffer size are appropriate. To specify these values yourself, construct an InputStreamReader on a FileInputStream.
FileReader is meant for reading streams of characters. For reading streams of raw bytes, consider using a FileInputStream.
FileReader是方便读取字符文件的。

###4.1. 构造函数
```
FileReader(File file)
Creates a new FileReader, given the File to read from.

FileReader(FileDescriptor fd)
Creates a new FileReader, given the FileDescriptor to read from.

FileReader(String fileName)
Creates a new FileReader, given the name of the file to read from.
```
可以看到，FileReader的构造函数主要是读取文件。

##5. 总结一下：
####1. BufferedReader可以更高效的读取文件
####2. InputStreamReader可以处理乱码问题
####3. FileReader可以直接读取文件，方便
####4. 常见编码
- *7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块*
 "US-ASCII"
- *ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 *
 "ISO-8859-1"
- *8 位 UCS 转换格式 *
  "UTF-8"
- *16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 *
 "UTF-16BE"
- *16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 *
 "UTF-16LE"
- *16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 *
 "UTF-16"
- *中文超大字符集 *
  "GBK"

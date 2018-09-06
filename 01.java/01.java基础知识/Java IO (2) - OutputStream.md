#Java IO (2) - OutputStream

@(编程)

##前言
JavaIO一共包括两种，一种是stream，一种是reader/writer，每种又包括in/out，所以一共是四种包。Java 流在处理上分为字符流和字节流。字符流处理的单元为 2 个字节的 Unicode 字符，分别操作字符、字符数组或字符串，而字节流处理单元为 1 个字节，操作字节和字节数组。
Java 内用 Unicode 编码存储字符，字符流处理类负责将外部的其他编码的字符流和 java 内 Unicode 字符流之间的转换。而类 InputStreamReader 和 OutputStreamWriter 处理字符流和字节流的转换。字符流（一次可以处理一个缓冲区）一次操作比字节流（一次一个字节）效率高。
##0. 目录
1. OutputStream
2. FileOutputStream

##1. OutputStream
![outstream](http://images.cnblogs.com/cnblogs_com/wardensky/645143/o_outputstream.png)
OutputStream与InputStream对应，也是一个抽象类，其主要方法如下：
```
void	close()
Closes this output stream and releases any system resources associated
 with this stream.

void	flush()
Flushes this output stream and forces any buffered output bytes to be written out.

void	write(byte[] b)
Writes b.length bytes from the specified byte array to this output stream.
void	write(byte[] b, int off, int len)

Writes len bytes from the specified byte array starting at offset off
to this output stream.

abstract void	write(int b)
Writes the specified byte to this output stream.
```
主要方法就是write方法，注意，只write字节。

##2. FileOutputStream
FileOutputStream是OutputStream的一个子类，实现对文件的写入。
>A file output stream is an output stream for writing data to a File or to a FileDescriptor. Whether or not a file is available or may be created depends upon the underlying platform. Some platforms, in particular, allow a file to be opened for writing by only one FileOutputStream (or other file-writing object) at a time. In such situations the constructors in this class will fail if the file involved is already open.
FileOutputStream is meant for writing streams of raw bytes such as image data. For writing streams of characters, consider using FileWriter.

简单翻译
>FileOutputStream是用来写文件的，但这个很依赖操作系统，有的操作系统只能操作一个FileOutputStream。FileOutputStream是用来写二进制的，如果要写字符，请使用FileWriter。

###2.1. 构造函数

```
FileOutputStream(File file)
Creates a file output stream to write to the file represented by the
specified File object.

FileOutputStream(File file, boolean append)
Creates a file output stream to write to the file represented by the
specified File object.

FileOutputStream(FileDescriptor fdObj)
Creates a file output stream to write to the specified file descriptor,
which represents an existing connection to an actual file in the file system.

FileOutputStream(String name)
Creates a file output stream to write to the file with the specified name.

FileOutputStream(String name, boolean append)
Creates a file output stream to write to the file with the specified name.
```
如上，有五个构造函数，如果append为true，则追加写文件
```
FileOutputStream fos = new FileOutputStream("d:/123.txt", true);
```

###2.2. 主要方法
```
void	close()
Closes this file output stream and releases any system resources associated
 with this stream.

protected void	finalize()
Cleans up the connection to the file, and ensures that the close method of
this file output stream is called when there are no more references to this stream.

FileChannel	getChannel()
Returns the unique FileChannel object associated with this file output stream.

FileDescriptor	getFD()
Returns the file descriptor associated with this stream.

void	write(byte[] b)
Writes b.length bytes from the specified byte array to this file output stream.

void	write(byte[] b, int off, int len)
Writes len bytes from the specified byte array starting at offset off to
 this file output stream.

void	write(int b)
Writes the specified byte to this file output stream.
```

###2.4. 示例代码
```
	private void test() {
		try {
			FileOutputStream fos = new FileOutputStream("d:/123.txt", true);
			String s = "礼拜天";
			fos.write(s.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
*注意：流要close；不处理乱码。为什么不处理乱码呢？因为这个是写二进制的，二进制可没有编码的问题。*

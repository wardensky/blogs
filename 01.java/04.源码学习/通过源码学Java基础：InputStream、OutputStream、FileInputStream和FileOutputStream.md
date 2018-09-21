###通过源码学Java基础：InputStream、OutputStream、FileInputStream和FileOutputStream



####1. InputStream
#####1.1 说明
InputStream是一个抽象类，具体来讲：
>This abstract class is the superclass of all classes representing an input stream of bytes.

其主要子类包括：
>AudioInputStream, ByteArrayInputStream, FileInputStream, FilterInputStream, InputStream, ObjectInputStream, PipedInputStream, SequenceInputStream, StringBufferInputStream


#####1.2 构造函数
其构造函数非常简单，只有一个无参构造函数
```
  public InputStream() {}
```
#####1.3 主要方法
其主要方法与上一篇介绍的BufferedReader相同，不做详细介绍。
```
int	available()
Returns an estimate of the number of bytes that can be read (or skipped over) from this input stream without blocking by the next invocation of a method for this input stream.
void	close()
Closes this input stream and releases any system resources associated with the stream.
void	mark(int readlimit)
Marks the current position in this input stream.
boolean	markSupported()
Tests if this input stream supports the mark and reset methods.
abstract int	read()
Reads the next byte of data from the input stream.
int	read(byte[] b)
Reads some number of bytes from the input stream and stores them into the buffer array b.
int	read(byte[] b, int off, int len)
Reads up to len bytes of data from the input stream into an array of bytes.
void	reset()
Repositions this stream to the position at the time the mark method was last called on this input stream.
long	skip(long n)
Skips over and discards n bytes of data from this input stream.
```
####2. OutputStream
#####2.1 说明
同InputStream一样，OutputStream也是一个抽象类。
>This abstract class is the superclass of all classes representing an output stream of bytes. An output stream accepts output bytes and sends them to some sink.

#####2.2 构造函数
只有一个默认无参构造函数：
```
OutputStream()
```
#####2.3 主要方法
```
void	close()
Closes this output stream and releases any system resources associated with this stream.
void	flush()
Flushes this output stream and forces any buffered output bytes to be written out.
void	write(byte[] b)
Writes b.length bytes from the specified byte array to this output stream.
void	write(byte[] b, int off, int len)
Writes len bytes from the specified byte array starting at offset off to this output stream.
abstract void	write(int b)
Writes the specified byte to this output stream.
```
####3. FileInputStream
#####3.1 说明
FileInputStream是InputStream的一个子类，实现了对文件的读。
>A FileInputStream obtains input bytes from a file in a file system. What files are available depends on the host environment.
FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

简单翻译一下：

>FileInputStream从文件中读取字节，比较适合读取二进制数据，如果要读取字符文件，最好用FileReader。

FileReader下一篇再讲。

#####3.2 构造函数
```
FileInputStream(File file)
Creates a FileInputStream by opening a connection to an actual file, the file named by the File object file in the file system.
FileInputStream(FileDescriptor fdObj)
Creates a FileInputStream by using the file descriptor fdObj, which represents an existing connection to an actual file in the file system.
FileInputStream(String name)
Creates a FileInputStream by opening a connection to an actual file, the file named by the path name name in the file system.
```
有三个构造函数，其参数分别是文件、文件描述符、字符串
其主要用法如下：
```
FileInputStream fis = new FileInputStream("d:/123.txt");
FileInputStream fis1 = new FileInputStream(new File("d:/123.txt"));
```
文件描述符那个不常用，我也不会。
通过看源码，发现字符串那个会默认构造成文件
```
  public FileInputStream(String paramString)
    throws FileNotFoundException
  {
    this(paramString != null ? new File(paramString) : null);
  }

  public FileInputStream(File paramFile)
    throws FileNotFoundException
  {
    String str = paramFile != null ? paramFile.getPath() : null;
    SecurityManager localSecurityManager = System.getSecurityManager();
    if (localSecurityManager != null) {
      localSecurityManager.checkRead(str);
    }
    if (str == null) {
      throw new NullPointerException();
    }
    if (paramFile.isInvalid()) {
      throw new FileNotFoundException("Invalid file path");
    }
    this.fd = new FileDescriptor();
    this.fd.incrementAndGetUseCount();
    this.path = str;
    open(str);
  }
```
注意，这两个构造函数都会抛出FileNotFoundException，但如果传的字符串是null，也会抛出空指针异常。
#####3.3 主要方法
```
int	available()
Returns an estimate of the number of remaining bytes that can be read (or skipped over) from this input stream without blocking by the next invocation of a method for this input stream.
void	close()
Closes this file input stream and releases any system resources associated with the stream.
protected void	finalize()
Ensures that the close method of this file input stream is called when there are no more references to it.
FileChannel	getChannel()
Returns the unique FileChannel object associated with this file input stream.
FileDescriptor	getFD()
Returns the FileDescriptor object that represents the connection to the actual file in the file system being used by this FileInputStream.
int	read()
Reads a byte of data from this input stream.
int	read(byte[] b)
Reads up to b.length bytes of data from this input stream into an array of bytes.
int	read(byte[] b, int off, int len)
Reads up to len bytes of data from this input stream into an array of bytes.
long	skip(long n)
Skips over and discards n bytes of data from the input stream.
```
主要方法与BufferedReader相同。
下面是一个简单的代码示例：
```
	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream("d:/123.txt");
			byte[] b = new byte[1000 * 20];
			fis.read(b);
			System.out.println(new String(b));
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
通常情况下，很少有人直接通过FileInputStream读取文本文件，会通过BufferedReader封装一下。
另外，FileInputStream也会遇到乱码问题，这个下一篇讲InputStreamReader会讲到。
####4. FileOutputStream
#####4.1 说明
FileOutputStream是OutputStream的一个子类，实现对文件的写入。
>A file output stream is an output stream for writing data to a File or to a FileDescriptor. Whether or not a file is available or may be created depends upon the underlying platform. Some platforms, in particular, allow a file to be opened for writing by only one FileOutputStream (or other file-writing object) at a time. In such situations the constructors in this class will fail if the file involved is already open.
FileOutputStream is meant for writing streams of raw bytes such as image data. For writing streams of characters, consider using FileWriter.

简单翻译
>FileOutputStream是用来写文件的，但这个很依赖操作系统，有的操作系统只能操作一个FileOutputStream。FileOutputStream是用来写二进制的，如果要写字符，请使用FileWriter。
#####4.2 构造函数

```
FileOutputStream(File file)
Creates a file output stream to write to the file represented by the specified File object.
FileOutputStream(File file, boolean append)
Creates a file output stream to write to the file represented by the specified File object.
FileOutputStream(FileDescriptor fdObj)
Creates a file output stream to write to the specified file descriptor, which represents an existing connection to an actual file in the file system.
FileOutputStream(String name)
Creates a file output stream to write to the file with the specified name.
FileOutputStream(String name, boolean append)
Creates a file output stream to write to the file with the specified name.
```
如上，有五个构造函数，如果append为true，则追加写文件
```
FileOutputStream fos = new FileOutputStream("d:/123.txt", true);
```

#####4.3 主要方法
```
void	close()
Closes this file output stream and releases any system resources associated with this stream.
protected void	finalize()
Cleans up the connection to the file, and ensures that the close method of this file output stream is called when there are no more references to this stream.
FileChannel	getChannel()
Returns the unique FileChannel object associated with this file output stream.
FileDescriptor	getFD()
Returns the file descriptor associated with this stream.
void	write(byte[] b)
Writes b.length bytes from the specified byte array to this file output stream.
void	write(byte[] b, int off, int len)
Writes len bytes from the specified byte array starting at offset off to this file output stream.
void	write(int b)
Writes the specified byte to this file output stream.
```
主要方法也与BufferedWriter相同，其简单用法：
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
注意：流要close；不处理乱码。为什么不处理乱码呢？因为这个是读取二进制的，二进制可没有编码的问题。

**参考：**
Java源码
Java Platform Standard Edition 7 Documentation

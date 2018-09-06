#Java IO (4) - Writer

@(编程)

##前言
JavaIO一共包括两种，一种是stream，一种是reader/writer，每种又包括in/out，所以一共是四种包。Java 流在处理上分为字符流和字节流。字符流处理的单元为 2 个字节的 Unicode 字符，分别操作字符、字符数组或字符串，而字节流处理单元为 1 个字节，操作字节和字节数组。
Java 内用 Unicode 编码存储字符，字符流处理类负责将外部的其他编码的字符流和 java 内 Unicode 字符流之间的转换。而类 InputStreamReader 和 OutputStreamWriter 处理字符流和字节流的转换。字符流（一次可以处理一个缓冲区）一次操作比字节流（一次一个字节）效率高。
##0. 目录
1. Writer
2. BufferedWriter
3. OutputStreamWriter
4. FileWriter

##1. Writer
![writer](http://images.cnblogs.com/cnblogs_com/wardensky/645143/o_writer.png)
>Abstract class for writing to character streams. The only methods that a subclass must implement are write(char[], int, int), flush(), and close(). Most subclasses, however, will override some of the methods defined here in order to provide higher efficiency, additional functionality, or both.

**1.1. 主要方法**
```
Writer	append(char c)
Appends the specified character to this writer.

Writer	append(CharSequence csq)
Appends the specified character sequence to this writer.

Writer	append(CharSequence csq, int start, int end)
Appends a subsequence of the specified character sequence to this writer.

abstract void	close()
Closes the stream, flushing it first.

abstract void	flush()
Flushes the stream.

void	write(char[] cbuf)
Writes an array of characters.

abstract void	write(char[] cbuf, int off, int len)
Writes a portion of an array of characters.

void	write(int c)
Writes a single character.

void	write(String str)
Writes a string.

void	write(String str, int off, int len)
Writes a portion of a string.
```

同Reader类似，本文介绍一下BufferedWriter和OutputStreamWriter，FileWriter。
##2. BufferedWriter
>Writes text to a character-output stream, buffering characters so as to provide for the efficient writing of single characters, arrays, and strings.
>The buffer size may be specified, or the default size may be accepted. The default is large enough for most purposes.

###2.1. 构造函数
```
BufferedWriter(Writer out)
Creates a buffered character-output stream that uses a default-sized output buffer.

BufferedWriter(Writer out, int sz)
Creates a new buffered character-output stream that uses an output buffer
of the given size.
```
###2.2. 主要方法
```
void	close()
Closes the stream, flushing it first.

void	flush()
Flushes the stream.

void	newLine()
Writes a line separator.

void	write(char[] cbuf, int off, int len)
Writes a portion of an array of characters.

void	write(int c)
Writes a single character.

void	write(String s, int off, int len)
Writes a portion of a String.
```
###2.3. 需要注意的地方
####2.3.1. flush方法
需要注意里面的flush方法
flush方法的说明是这样的
>Flushes the stream.

也就是说会把内容flush进去，冲进去。
那什么时候用这个呢？答案就是，基本上不用。
先看看flush干了什么事情。
```
  public void flush()
    throws IOException
  {
    synchronized (this.lock)
    {
      flushBuffer();
      this.out.flush();
    }
  }
```
就是调用了flushBuffer()方法。
那么close的时候干了什么事情呢？
```
  public void close()
    throws IOException
  {
    synchronized (this.lock)
    {
      if (this.out == null) {
        return;
      }
      try
      {
        flushBuffer();
      }
      finally
      {
        this.out.close();
        this.out = null;
        this.cb = null;
      }
    }
  }
```
注意，也调用了flushBuffer()。也就是说，只要你close了，就不用flush了。
而且，write也会flush，其代码如下：
```
  public void write(int paramInt)
    throws IOException
  {
    synchronized (this.lock)
    {
      ensureOpen();
      if (this.nextChar >= this.nChars) {
        flushBuffer();
      }
      this.cb[(this.nextChar++)] = ((char)paramInt);
    }
  }
```
那close和write都flush了，flush还有啥用呢？有人这么说：
>In other words, relax - just write, write, write and close :) The only time you normally need to call flush manually is if you really, really need the data to be on disk now. (For instance, if you have a perpetual logger, you might want to flush it every so often so that whoever's reading the logs doesn't need to wait until the buffer's full before they can see new log entries!)

不逐字翻译了，大意就是除非你**非常非常**要确定数据已经写出了。
####2.3.2 append

在BufferedWriter还可以敲出来一个方法append，那么，这个方法跟write有什么区别呢？是不是追加的时候用append，不追加的时候用write呢。
各位可以试一下，但我现在告诉你答案，不是这样的。
看append的源码：
```
  public Writer append(CharSequence paramCharSequence)
    throws IOException
  {
    if (paramCharSequence == null) {
      write("null");
    } else {
      write(paramCharSequence.toString());
    }
    return this;
  }
```
原来就是调用write，只不过处理了空的情况。如果用write，传入一个空的会抛出空指针异常；如果用append，会写入null，就是这个区别而已。
那么，如何追加文件呢？追加文件在FileWriter的构造函数中
```
BufferedWriter bw = new BufferedWriter(new FileWriter("d:/1234.txt", true));
```
后面加一个true就可以了。
一层层向上看，最后会追踪到一个native方法
```
  private native void open(String paramString, boolean paramBoolean)
    throws FileNotFoundException;
```
即打开文件的方式。熟悉c语言或者c++的同学看到这里就明白了，不展开解释。
##3. OutputStreamWriter
>An OutputStreamWriter is a bridge from character streams to byte streams: Characters written to it are encoded into bytes using a specified charset. The charset that it uses may be specified by name or may be given explicitly, or the platform's default charset may be accepted.
Each invocation of a write() method causes the encoding converter to be invoked on the given character(s). The resulting bytes are accumulated in a buffer before being written to the underlying output stream. The size of this buffer may be specified, but by default it is large enough for most purposes. Note that the characters passed to the write() methods are not buffered.
For top efficiency, consider wrapping an OutputStreamWriter within a BufferedWriter so as to avoid frequent converter invocations. For example:
``` Writer out
   = new BufferedWriter(new OutputStreamWriter(System.out));
 ```
A surrogate pair is a character represented by a sequence of two char values: A high surrogate in the range '\uD800' to '\uDBFF' followed by a low surrogate in the range '\uDC00' to '\uDFFF'.
A malformed surrogate element is a high surrogate that is not followed by a low surrogate or a low surrogate that is not preceded by a high surrogate.
This class always replaces malformed surrogate elements and unmappable character sequences with the charset's default substitution sequence. The CharsetEncoder class should be used when more control over the encoding process is required.

###3.1. 构造函数
```
OutputStreamWriter(OutputStream out)
Creates an OutputStreamWriter that uses the default character encoding.

OutputStreamWriter(OutputStream out, Charset cs)
Creates an OutputStreamWriter that uses the given charset.

OutputStreamWriter(OutputStream out, CharsetEncoder enc)
Creates an OutputStreamWriter that uses the given charset encoder.

OutputStreamWriter(OutputStream out, String charsetName)
Creates an OutputStreamWriter that uses the named charset.
```

###3.2. 主要方法
```
void	close()
Closes the stream, flushing it first.

void	flush()
Flushes the stream.

String	getEncoding()
Returns the name of the character encoding being used by this stream.

void	write(char[] cbuf, int off, int len)
Writes a portion of an array of characters.

void	write(int c)
Writes a single character.

void	write(String str, int off, int len)
Writes a portion of a string.
```
OutputStreamWriter写入字符，同时可以设置编码集。
OutputStreamWriter一般不单独使用，作用翻译器。

##4. FileWriter
>Convenience class for writing character files. The constructors of this class assume that the default character encoding and the default byte-buffer size are acceptable. To specify these values yourself, construct an OutputStreamWriter on a FileOutputStream.
Whether or not a file is available or may be created depends upon the underlying platform. Some platforms, in particular, allow a file to be opened for writing by only one FileWriter (or other file-writing object) at a time. In such situations the constructors in this class will fail if the file involved is already open.
>
FileWriter is meant for writing streams of characters. For writing streams of raw bytes, consider using a FileOutputStream.

主要内容：
>FileWriter是用来写字符的，如果要读取二进制文件，用FileOutputStream。

###4.1. 构造函数
```
FileWriter(File file)
Constructs a FileWriter object given a File object.

FileWriter(File file, boolean append)
Constructs a FileWriter object given a File object.

FileWriter(FileDescriptor fd)
Constructs a FileWriter object associated with a file descriptor.

FileWriter(String fileName)
Constructs a FileWriter object given a file name.

FileWriter(String fileName, boolean append)
Constructs a FileWriter object given a file name with a boolean
indicating whether or not to append the data written.
```
注意，在Writer中，只有FileWriter能够追加写，别的都不可以。
###4.2. 主要方法
FileWriter没有自己的方法，主要继承OutputStreamWriter和Writer。
###4.3. 代码例子
```
public class FileWriterTest {
	public static void main(String[] args) throws IOException {
		FileWriter fw = new FileWriter("d:/1234.txt", true);
		fw.append("你好");
		fw.write("world");
		fw.close();
	}
}
```

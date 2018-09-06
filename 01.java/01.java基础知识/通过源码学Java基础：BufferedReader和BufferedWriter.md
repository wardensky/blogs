###通过源码学Java基础：BufferedReader和BufferedWriter

@(编程)

准备写一系列Java基础文章，先拿Java.io下手，今天聊一聊BufferedReader和BufferedWriter

####BufferedReader
BufferedReader继承Reader，本身的方法非常简单，其官方解释如下：
>Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
>The buffer size may be specified, or the default size may be used. The default is large enough for most purposes.

简单翻译一下：
>从流里面读取文本，通过缓存的方式提高效率，读取的内容包括字符、数组和行。
>缓存的大小可以指定，也可以用默认的大小。大部分情况下，默认大小就够了。


#####1. 构造函数
BufferedReader有两个构造函数，其声明如下：
```
BufferedReader(Reader in)
Creates a buffering character-input stream that uses a default-sized input buffer.
BufferedReader(Reader in, int sz)
Creates a buffering character-input stream that uses an input buffer of the specified size.
```
一个是传一个Reader，另外一个增加了缓存的大小。

```
常见的初始化方法
```
BufferedReader br = new BufferedReader(new FileReader("d:/123.txt"));
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
```
第一个方法是读取一个文件；第二个方法是从标准输入读。
#####2. 主要方法
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

######a. read方法
>Reads a single character.

这个方法从reader里面读一个字符，并向下移一位。如果读完了，会返回-1.
```
	public void readTest() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("d:/123.txt"));
			for (;;) {
				int i = br.read();
				if (i == -1) {
					break;
				}
				System.out.print((char) i);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```

######b. read(char[] cbuf, int off, int len)方法
```
	public void readCharTest() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("d:/123.txt"));
			char[] buf = new char[100];
			br.read(buf);
			System.out.print(String.valueOf(buf));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
如上，先定义一个数组，然后读进数组就可以了。如果数组比内容少，则数组满了就不读了。当然，也可以指定数据的off和len，但一般不用。
######c. readline方法
```
	public void readline() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("d:/123.txt"));
			while (true) {
				String str = br.readLine();
				if (str == null) {
					break;
				}
				System.out.println(str);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
用null判断是否读完。
######d. close方法
>Closes the stream and releases any system resources associated with it. Once the stream has been closed, further read(), ready(), mark(), reset(), or skip() invocations will throw an IOException. Closing a previously closed stream has no effect.

用完流之后，一定要close掉。当然，放在finally里面更保险。

#####3. 其他
BufferedReader的子类包括FileReader, InputStreamReader。因此，这两个子类也有如上方法。
####BufferedWriter
BufferedWriter继承自java.io.Writer。
>Writes text to a character-output stream, buffering characters so as to provide for the efficient writing of single characters, arrays, and strings.
>The buffer size may be specified, or the default size may be accepted. The default is large enough for most purposes.

#####1. 构造函数
```
BufferedWriter(Writer out)
Creates a buffered character-output stream that uses a default-sized output buffer.
BufferedWriter(Writer out, int sz)
Creates a new buffered character-output stream that uses an output buffer of the given size.
```
其源码如下：
```
  public BufferedWriter(Writer paramWriter)
  {
    this(paramWriter, defaultCharBufferSize);
  }

  public BufferedWriter(Writer paramWriter, int paramInt)
  {
    super(paramWriter);
    if (paramInt <= 0) {
      throw new IllegalArgumentException("Buffer size <= 0");
    }
    this.out = paramWriter;
    this.cb = new char[paramInt];
    this.nChars = paramInt;
    this.nextChar = 0;
    this.lineSeparator = ((String)AccessController.doPrivileged(new GetPropertyAction("line.separator")));
  }
```
用法
```
BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
BufferedWriter bw = new BufferedWriter(new FileWriter("d:/1234.txt", true));
```
输出到标准输出或者输出到一个文件。
#####2. 主要方法
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
既然是writer，那主要是写数据。
######a. write方法
```
	private void writeTest() {
		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("d:/1234.txt"));
			bw.write("writeTest");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
注意，BufferedWriter没有write(String s)或者write(char[] s)的定义，为什么还能直接bw.write("writeTest")呢。因为BufferedWriter的父类Writer实现了这个方法。不过，writer实际上也是调用了BufferedWriter的方法实现的。
```
  public void write(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    synchronized (this.lock)
    {
      char[] arrayOfChar;
      if (paramInt2 <= 1024)
      {
        if (this.writeBuffer == null) {
          this.writeBuffer = new char['?'];
        }
        arrayOfChar = this.writeBuffer;
      }
      else
      {
        arrayOfChar = new char[paramInt2];
      }
      paramString.getChars(paramInt1, paramInt1 + paramInt2, arrayOfChar, 0);
      write(arrayOfChar, 0, paramInt2);
    }
  }
```
其中write(arrayOfChar, 0, paramInt2)在Writer中是抽象方法，定义如下：
```
  public abstract void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
	 throws IOException;
```
这个是标准的模板模式啊。

######b. newLine方法
newLine方法实现了换行，其源码如下：
```
  public void newLine()
    throws IOException
  {
    write(this.lineSeparator);
  }

```
代码很简单，只是写了一个换行符，那么换行符是什么呢？
```
this.lineSeparator = ((String)AccessController.doPrivileged(new GetPropertyAction("line.separator")));
```
不过，也有人说换行符有的时候不靠谱，不过我没遇到过。
在Java7之后，也可以这么写，当然，实际上是一样的。
```
System.lineSeparator()
```
######c. flush方法
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

#####3. 特殊说明
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

参考：
Java源码
Java Platform Standard Edition 7 Documentation
[When to flush a BufferedWriter?](http://stackoverflow.com/questions/908168/when-to-flush-a-bufferedwriter)

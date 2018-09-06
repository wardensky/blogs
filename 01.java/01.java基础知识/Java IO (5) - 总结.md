#Java IO (5) - 总结

@(编程)

##0. 目录
1. FileInputStream
2. FileOutputStream
3. FileReader
4. FileWriter
5. BufferedReader
6. BufferedWriter
7. InputStreamReader
8. OutStreamWriter

##1. FileInputStream
FileInputStream主要用来读取二进制数据，当然，也能读取文本文件。在读取文本文件时，通常用做InputStreamReader的输入。

```
	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("d:/1234.txt");
		byte[] b = new byte[100];
		fis.read(b);
		System.out.println(new String(b));
		fis.close();
	}
```
##2. FileOutputStream
同FileInputStream一样，通常用来写二进制文件。

```
	public static void main(String[] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("d:/1234.txt", true);
		String s = "今天是周五";
		fos.write(s.getBytes());
		fos.close();
	}
```
##3. FileReader
FileReader用来直接读取文件，其编码集是系统默认编码集，会出现乱码问题。

```
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("d://1234.txt");
		char[] cbuf = new char[100];
		fr.read(cbuf);
		System.out.println(new String(cbuf));
		fr.close();
	}
```
##4. FileWriter
FileWriter和FileReader对应，也会出现乱码问题。FileWriter可以追加写。
```
	public static void main(String[] args) throws IOException {
		FileWriter fw = new FileWriter("d:/1234.txt", true);
		fw.append("你好");
		fw.write("world");
		fw.close();
	}
```
##5. BufferedReader
BufferedReader能够更有效的读文件，提供了readline这个方法，别的reader是没有的。不过BufferedReader需要传入一个Reader。
```
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("d:/1234.txt"));
		for (;;) {
			String s = br.readLine();
			if (s == null || s.equals("")) {
				break;
			}
			System.out.println(s);
		}
		br.close();
	}
```
##6. BufferedWriter
BufferedWriter与BufferedReader对应，提供了writeline方法。BufferedWriter如果要追加写，需要用FileWriter。
```
	public static void main(String[] args) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("d:/1234.txt",true));
		bw.newLine();
		bw.write("hahaha");
		bw.close();
	}
```
##7. InputStreamReader
InputStreamReader用来处理编码问题,也是stream转为reader的桥梁。需要传入一个stream。
```
	public static void main(String[] args) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream("d:/1234.txt"),"GBK");//UTF-8是乱码
		char[] cbuf = new char[1024];
		isr.read(cbuf);
		System.out.println(new String(cbuf));
		isr.close();
	}
```
##8. OutStreamWriter
OutStreamWriter同样处理写文件的乱码，与InputStreamReader对应。
```
	public static void main(String[] args) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				"d:/1234.txt", true), "GBK");
		osw.write("今天放假");
		osw.close();
	}
```

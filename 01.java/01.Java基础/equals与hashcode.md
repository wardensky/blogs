# equals与hashcode



## ==和equals

**java中的==是比较两个对象在JVM中的地址。**

equals是根类Obeject中的方法。
源代码如下：

```
public boolean equals(Object obj) {
    return (this == obj);
}
```

可见默认的equals方法，直接调用==，比较对象地址。不同的子类，可以重写此方法，进行两个对象的equals的判断。

String的equals源码。

```
public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String) anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                            return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```

## hashcode
下面是String的hashcode源代码。

```

    /**
     * Returns a hash code for this string. The hash code for a
     * {@code String} object is computed as
     * <blockquote><pre>
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * </pre></blockquote>
     * using {@code int} arithmetic, where {@code s[i]} is the
     * <i>i</i>th character of the string, {@code n} is the length of
     * the string, and {@code ^} indicates exponentiation.
     * (The hash value of the empty string is zero.)
     *
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

```


hashCode() 方法用于返回字符串的哈希码。
字符串对象的哈希码根据以下公式计算：
```
s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
```
使用 int 算法，这里 s[i] 是字符串的第 i 个字符，n 是字符串的长度，^ 表示求幂。空字符串的哈希值为 0。

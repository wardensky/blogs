有时候会遇到有些文件curl下载不下来，导致brew不能正常工作，但是文件通过浏览器是可以下载的。
以下是解决方案：
1. 手动下载压缩文件（一定要下载brew提示下载失败的文件）；
2. 执行brew --cache获取brew缓存路径，将下载的文件放入缓存目录；
3. 重新执行安装命令，brew会发现缓存中有了文件，就不去下载了，OK。
 

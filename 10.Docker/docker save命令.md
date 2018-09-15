# docker save命令
 

```
Usage:	docker save [OPTIONS] IMAGE [IMAGE...]

Save one or more images to a tar archive (streamed to STDOUT by default)

Options:
  -o, --output string   Write to a file, instead of STDOUT
```
例子

```
docker save -o jenkins_wisdombud.tar jenkins:wisdombud
```

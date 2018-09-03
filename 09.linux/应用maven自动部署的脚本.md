# 应用maven自动部署的脚本



最近写了一个自动部署的脚本，可以一键部署到测试服务器或者生产服务器上，包括一个函数脚本和一个调用脚本，比较简单，记录如下。
特点如下：
- 部署前自动备份
- 可以部署tomcat项目和java项目
- 支持参数，可以部署测试环境和生产环境

function.sh

```
#!/bin/bash
##author:zch
##date:20171116

host=192.168.163.50

if [ "$1"x = "online"x ]; then
host=111.111.111.111
echo "host is 111.111.111.111"
else
host=192.168.163.50
echo "host is 192.168.163.50"
fi
##远程调用，传入参数
remote_call(){
ssh -tt hhtd@$host << remotessh
$1
exit
remotessh
}

##更新、编译代码，参数是名称，如admin或者app_server
build_code(){
git checkout $1_online
git pull
mvn clean install dependency:copy-dependencies
echo "build code success"
}

##启动远程tomcat，参数是名称，如admin或者app_server
start_remote_process(){
remote_call "/home/hhtd/bin/start_$1.sh"
}

##杀掉远程tomcat，参数是名称，如admin或者app_server
kill_remote_process(){
remote_call "/home/hhtd/bin/kill_$1.sh"
}

##备份远程tomcat，参数是名称，如admin或者app_server
backup_remote(){
remote_call "/home/hhtd/bin/backup_$1.sh"
}

##删除远程tomcat日志，参数是全路径，如/home/hhtd/online/admin-tomcat-8082/
rm_tomcat_logs(){
remote_call "rm -rf $1/logs/*.*"
}

##删除远程的release目录下的root，参数是名称，如admin或者app_server
rm_remote_release_root(){
remote_call "rm -rf /home/hhtd/release/hhtd_$1/ROOT"
}

##把本地ROOT复制到远程release目录下，参数是名称，如admin或者app_server
scp_root_remote(){
scp -r ./hhtd_$1/target/ROOT hhtd@$host:/home/hhtd/release/hhtd_$1/ROOT
}

##把本地文件复制到远程，适用于非web项目，包括两个参数，第一个是名称，第二个是全路径
scp_release_remote(){
remote_call "mkdir $2"
remote_call "mkdir $2/bin"
remote_call "mkdir $2/lib"
remote_call "mkdir $2/config"
remote_call "mkdir $2/logs"
scp -r ./release/hhtd_$1/bin/*.* hhtd@$host:$2/bin/
scp -r ./release/hhtd_$1/lib/*.* hhtd@$host:$2/lib/
}

##删除远程服务器上的tomcat下的root，参数是全路径，如/home/hhtd/online/admin-tomcat-8082/
rm_tomcat_root(){
remote_call "rm -rf $1/webapps/ROOT"
}

##备份远程服务器上tomcat下面的attach（attache）文件夹，参数是全路径，如/home/hhtd/online/admin-tomcat-8082/
backup_tomcat_attach(){
remote_call "mv $1/webapps/ROOT/attache $1/webapps/attache"
}
##把release目录下的root复制到online目录下，包括两个参数，第一个是名称，第二个是全路径
cp_tomcat_release_online(){
remote_call "cp -R /home/hhtd/release/hhtd_$1/ROOT $2/webapps/ROOT"
}

##把备份的tomcat下的attach目录恢复回去
recover_tomcat_attach(){
remote_call "mv $1/webapps/attache $1/webapps/ROOT/attache"
}

##备份tomcat配置文件，参数是全路径，如/home/hhtd/online/admin-tomcat-8082/
backup_tomcat_config(){
remote_call "cp $1/webapps/ROOT/WEB-INF/classes/ $1/*.properties "
}

##把配置文件复制回去，参数是全路径，如/home/hhtd/online/admin-tomcat-8082/
recover_tomcat_config(){
remote_call "cp $1/*.properties $1/webapps/ROOT/WEB-INF/classes/"
}

##复制依赖jar文件到release目录
copy_dependencies_jar(){
rm -f ./release/hhtd_$1/lib/*.jar
cp ./hhtd_$1/target/dependency/*.jar ./release/hhtd_$1/lib/
cp ./hhtd_$1/target/*hhtd_$1*.jar ./release/hhtd_$1/lib/
}

##给sh执行权限，参数是名称，适用于非web项目
chmod_x(){
remote_call "chmod +x /home/hhtd/online/hhtd_$1/bin/start.sh"
}

##适用于非web项目，参数是名称
backup_remote_config(){
remote_call "rm -rf /home/hhtd/tmp/"
remote_call "mkdir /home/hhtd/tmp/"
remote_call "cp /home/hhtd/online/hhtd_$1/config/*.* /home/hhtd/tmp/"
}

##适用于非web项目，参数是路径
rm_remote_folder(){
remote_call "rm -rf $1"
}
##适用于非web项目，参数是路径
recover_config(){
remote_call "cp /home/hhtd/tmp/*.* $1/config/"
}

##适用于web项目，参数是路径
rm_tomcat_config(){
remote_call "rm $1/webapps/ROOT/WEB-INF/classes/*.properties"
}
```

admin.sh

```
#!/bin/bash
##author:zch
##date:20171116

time1=`date +"%s"`

name=admin
folder=admin-tomcat-8082
fullPath=/home/hhtd/online/$folder

source function.sh

## build source code
build_code $name

## remote rm files scp to online
rm_remote_release_root $name
scp_root_remote $name

##kill tomcat
kill_remote_process $name

## backup tomcat
backup_remote $name

## rm tomcat logs
rm_tomcat_logs $fullPath

##rm ROOT
backup_tomcat_attach $fullPath
rm_tomcat_root $fullPath
echo "root deleted"

##cp ROOT
cp_tomcat_release_online $name $fullPath
recover_tomcat_attach $fullPath
echo "cp root finished"

##rm tomcat config
rm_tomcat_config $fullPath

##cp config
recover_tomcat_config $fullPath

##start tomcat
start_remote_process $name

time2=`date +"%s"`
((total=$time2-$time1))
echo "deploy finished total time is "$total seconds

```

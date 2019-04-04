# install-conf-server 设计与实现



主要功能：

1. 客户端提交安装新kvm虚拟机请求时，为其生成配置文件
2. 提供FTP下载服务，以便装机时可下载配置文件
3. eureka服务注册中心
4. 提供KVM安装配置所需信息


# 1. 服务器配置



1. 安装ftp服务

   ```bash
   yum install  vsftpd  -y
   systemctl enable vsftpd.service
   systemctl start vsftpd.service
   ```
   >  不需要做任何其他任何配置修改

2. 在 `/var/ftp/` 目录会存放以下文件

   ```
   1. ip : 服务生成， 内容为新的可用ip，对外提供
   2. hostname： 服务生成，内容为新的主机名， 对外提供
   3. ifcfg： 原网卡配置文件,其中内容会被动态替换
   4. jdk8.tar.gz： jdk压缩包，对外提供下载
   5. maven3.tar.gz： maven压缩包， 对外提供下载
   6. profile： 环境变量配置文件，对外提供下载
   7. CentOS-Base.repo： 使用阿里云源的repo文件，对外提供下载
   8. ifcfg-eth0: 服务生成，新的主机网卡配置文件，对外提供下载
   9. ks.cfg: 装机时读取的kickstart文件，对外提供下载
   ```

3. java运行环境

# 2. 服务代码编写

涉及技术：

Spring Boot、Spring Cloud、 Shell、Redis、MySQL

# 3. 启动服务



保证服务后台运行， 并且将启动日志输出到日志文件

```bash
nohub java -jar /opt/app/install-conf-server/install-conf-server.jar >> `date +%Y%m%d`.log &
```


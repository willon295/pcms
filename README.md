# PCMS（Project Changes  Management System）
项目变更管理系统


版本定义

|软件|版本|
|---|---|
|JDK|1.8.0_212|
|spring boot|2.0.9.RELEASE|
|Spring Cloud|Finchley.SR2|

# 1. 项目架构

## 1.1 gitlab-api

### 1.1.1 主要功能

1. 提供gitlab api访问
2. 创建分支，配置用户权限
3. pre 分之唯一，代表预发环境
4. master 为线上环境分支


## 1.2 kvm-api

运行在 `项目环境` 宿主机上

1. 提供宿主机kvm管理的api

### 1.2.1 权限配置

1. 仅 root 用户可操作 master 分支
2. 仅 root 用户可操作 pre 分支

## 1.3 install-conf-server

### 1.3.1 主要功能

一、生成KVM自动化安装前需要的配置文件：

1. 新的可用  ip （自增） 
2. hostname 主机名
3. ifcfg-eth0 网卡配置文件
4. /etc/profile 文件

二、提供httpd文件下载服务

1. ip : 文本文件， 内容为新的可用ip
2. hostname： 文本文件 ，内容为新的主机名
3. ifcfg-eth0： 网卡配置文件
4. jdk8.tar.gz： jdk压缩包
5. maven3.tar.gz： maven压缩包
6. profile： 文本文件，配置过环境变量后的
7. CentOS-Base.repo： 使用阿里云源的repo文件

### 1.3.2 工作流程

1. 接收来自 `pcms-web` 的生成新的kvm配置文件请求
2. install-conf-server 生成新的配置文件
   - 使用redis加锁：kv对应为   `INSTALL_LOCK  ：  主机名`， 锁有效时间 `600s` 
   - 从mysql获取最新的ip信息， 自增+1，新的ip 写入文件 `/var/www/html/ip`, 同时写入 `mysql` 
   - 生成新的 `hostname` ， `ifcfg-eth0` 文件， 写入文件到 `/var/www/html/` 目录
   - 一切就绪之后返回 `success`
3. `pcms-web` 接收到返回success，在各宿主机开始安装Kvm
4. 接收来自KVM的完成请求
5. install-conf-server 释放redis锁

### 1.3.3 涉及技术

后端技术： Spring Boot 、Mybatis、Mysql、Redis、Maven、Git
运维： Shell、Kvm、Kickstart、镜像文件ISO定制

## 1.4 pcms-web

### 1.4.1 主要功能

1. 输入变更的信息：涉及工程， 分支名，参与人员， 项目上线时间
   - 涉及工程：此次变更涉及改动的工程，前提在 `gitlab` 上有此工程
   - 分支名： 所有涉及工程会统一创建此分支
   - 参与人员： 对此分支拥有操作权限，对此项kvm拥有操作权限
   - 项目上线时间： 时间+3D是项目环境kvm的销毁时间
   - kvm的hostname为：  工程名+分支名
2. 项目打包：发送打包请求，  拉取对应环境分支代码，进行打包
3. 项目部署： 发送部署请求， 运行对应的项目环境工程
4. 项目环境测试通过: 拥有进入预发环境权限
5. 预发审核：  进行权限检查， 是否其他项目占用预发， 如果是， 需要审核，审核通过后拥有 `pre` 分之操作权限 
6. 预发部署： 合并 `项目环境分支` 代码到 `pre`  分支， 对pre分支打包， 部署

### 1.4.2 涉及技术

后端技术： Spring Boot、Thymleaf、Mybatis、Mysql、Redis

前端技术： Html、Css、JavaScript、Vue、Bootstrap





##  1.5 pack-deploy

### 1.5.1 主要功能

1. 获取各个工程对应的分支代码，负责maven项目打包
2. 将打包后的 `{project-name}.tar.gz` 拷贝到对应的服务器
3. 在对应的服务器上，解压 `tar.gz` 文件， 并且运行项目

### 1.5.2 涉及技术

1.  maven多环境配置， 打包，部署
2. shell

# 2. 服务器,KVM相关

### 2.1 主机分配策略 

项目环境：  1工程 --> 1 主机 --> 多 kvm
预发环境： 1工程 --> 1主机 
线上环境： 1工程 --> 1主机|多主机

### 2.2 KVM自动化安装流程

1. pcms-web 获取输入信息之后，尝试向 `install-conf-server`  获取生成配置的锁：

   ```bash
   GET  http://10.0.0.10:8080/tryLock
   
   # true: 代表成功获得锁，可继续下一步
   # false: 其他应用正在安装，当前不可获取锁
   ```
   成功获得锁之后， 向`install-conf-server` 发起生成新配置请求
   ```bash
   GET http://10.0.0.10:8080/generate/{hostname}
   #hostname = projectName + branchName
   ```
2. install-conf-server 生成新的配置文件， 并加配置锁
3. pcms-web 接收到返回 `success` 执行宿主机上的kvm安装脚本
4. kvm 从 `install-conf-server` 获取所有需要的7个文件，完成装机
5. kvm装机完成之后向 `install-conf-server`  发送完成请求

   ```bash
   curl http://10.0.0.10:8080/finsh
   ```
6. install-conf-server释放配置锁

# 3. 项目规范



## 3.1.1 文件目录规范

1. `JAVA_HOME` 为 `/opt/lib/jdk8/`
2. `M2_HOME` 为 `/opt/lib/maven3`
3. HTTP根目录 ： `/var/www/html/`
4. KVM相关脚本文件： `/opt/kvm/bin/`
5. KVM虚拟磁盘存放目录： `/opt/kvm/disk/`
6. KVM需要的ISO文件存放目录： `/opt/kvm/iso/`
7. app运行目录： `/opt/app/{project-name}/`

## 3.1.2 服务器环境配置

统一密码： `sdfsdf` 

### 3. 2.1 项目环境主机配置

1. os： centos7.6-minimal.x86_64
2. 支持虚拟化
3. 安装kvm
4. 自动扫描关闭的kvm，并启动

### 3.2.2 KVM 配置

1. os:  centos7.6-minimal.x86_64
2. cpu: 1G/1H
3. disk:  10G
4. swap: 2G
5. password:  sdfsdf
6. 软件: java1.8 

### 3.2.3 基础服务IP分配

工程对应的kvm统一从 `10.0.0.100` 开始分配

预发环境主机 `10.0.1.100` 开始分配

线上环境主机从 ` 10.1.0.100` 开始分配



| 服务器              | ip        | 备注                |
| ------------------- | --------- | ------------------- |
| pcms-eureka	| 10.0.0.9 | 服务注册中心 |
| install-conf-server | 10.0.0.10 | kvm安装前配置服务 |
| pcms-gitlab         | 10.0.0.11 | gitlab服务器：root/sdfsdfsdf      |
| gitlab-api          | 10.0.0.12 |  gitlab-api服务  |
| pcms-mysql     | 10.0.0.13 |  mysql 服务器: root/root  |
| pack-deploy         | 10.0.0.14 | 打包部署服务      |
| pcms-web            | 10.0.0.15 | 管理前台        |
| kvm-api | 不定 | kvm访问控制服务，运行在各个工程宿主机上 |

# 部署规范

部署使用命令如下：
```bash
nohup java -server -Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar  XXXX.jar  >> `date +%Y%m%d`.log &
```
1. 开启远程调试,端口5005
2. 将输出信息输出日志，日志每天一份

# 启动流程

1. 注册中心 `pcms-eureka (10.0.0.9)`
```
nohub java -jar /opt/app/pcms-eureka/*.jar >> `date +%Y%m%d`.log &
```
2. kvm安装配置服务器 `install-conf-server (10.0.0.10)`, 启动 `redis-server`, `vsftpd`,`java` 
```
systemctl start vsftpd
redis-server &
nohup java -server -Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar  /opt/app/工程目录/*.jar  >> `date +%Y%m%d`.log &
```
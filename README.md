# PCMS（Project Changes  Management System）
项目变更管理系统

# 项目架构

## gitlab-api

### 主要功能

1. 提供gitlab api访问
2. 创建分支，配置用户权限
3. pre 分之唯一，代表预发环境
4. master 为线上环境分支


## kvm-api

运行在 `项目环境` 宿主机上

1. 提供宿主机kvm管理的api

### 权限配置

1. 仅 root 用户可操作 master 分支
2. 仅 root 用户可操作 pre 分支

## pcms-web

### 主要功能

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

### 涉及技术

后端技术： Spring Boot、Thymleaf、Mybatis、Mysql、Redis

前端技术： Html、Css、JavaScript、Vue、Bootstrap

## install-conf-server

### 主要功能

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

### 工作流程

1. 接收来自 `pcms-web` 的生成新的kvm配置文件请求
2. install-conf-server 生成新的配置文件
   - 使用redis加锁：kv对应为   `INSTALL_LOCK  ：  主机名`， 锁有效时间 `600s` 
   - 从mysql获取最新的ip信息， 自增+1，新的ip 写入文件 `/var/www/html/ip`, 同时写入 `mysql` 
   - 生成新的 `hostname` ， `ifcfg-eth0` 文件， 写入文件到 `/var/www/html/` 目录
   - 一切就绪之后返回 `success`
3. `pcms-web` 接收到返回success，在各宿主机开始安装Kvm
4. 接收来自KVM的完成请求
5. install-conf-server 释放redis锁

### 涉及技术

后端技术： Spring Boot 、Mybatis、Mysql、Redis、Maven、Git
运维： Shell、Kvm、Kickstart、镜像文件ISO定制

## 服务器、KVM自动化相关

### 主机分配策略 

项目环境：  1工程 --> 1 主机 --> 多 kvm
预发环境： 1工程 --> 1主机 
线上环境： 1工程 --> 1主机|多主机

### KVM自动化安装流程

1. pcms-web 获取输入信息之后， 向 `install-conf-server` 发送生成新kvm配置的请求

   ```bash
   curl  http://10.0.0.10:8080/generate/{hostname}
   
   #hostname = 工程名 + 分支名
   ```
2. install-conf-server 生成新的配置文件， 并加redis锁
3. pcms-web 接收到返回 `success` 执行宿主机上的kvm安装脚本
4. kvm 从 `install-conf-server` 获取所有需要的7个文件，完成装机
5. kvm装机完成之后向 `install-conf-server`  发送完成请求

   ```bash
   curl http://10.0.0.10:8080/done/{hostname}
   ```
6. install-conf-server释放redis锁

# 项目规范



## 文件目录

1. `JAVA_HOME` 为 `/opt/lib/jdk8/`
2. `M2_HOME` 为 `/opt/lib/maven3`
3. HTTP根目录 ： `/var/www/html/`
4. KVM相关脚本文件： `/opt/kvm/bin/`
5. KVM虚拟磁盘存放目录： `/opt/kvm/disk/`
6. KVM需要的ISO文件存放目录： `/opt/kvm/iso/`
7. app运行目录： `/opt/app/{project-name}/`

## 服务器环境配置

统一密码： sdfsdf

### 项目环境主机配置

1. os： centos7.6-minimal.x86_64
2. 支持虚拟化
3. 安装kvm
4. 自动扫描关闭的kvm，并启动

### KVM 配置

1. os:  centos7.6-minimal.x86_64
2. cpu: 1G/1H
3. disk:  10G
4. swap: 2G
5. password:  sdfsdf
6. 软件: java1.8 

### IP分配

工程对应的kvm统一从 `10.0.0.100` 开始分配

预发环境主机 `10.0.1.100` 开始分配

线上环境主机从 ` 10.1.0.100` 开始分配



| 服务器              | ip        | 备注                |
| ------------------- | --------- | ------------------- |
| pcms-gitlab         | 10.0.0.11 | root/sdfsdfsdf      |
| install-conf-server | 10.0.0.10 | kvm安装前配置服务器 |
| pack-deploy         | 10.0.0.12 | 打包部署服务器      |
| pcms-web            | 10.0.0.13 | 管理后台            |
|                     |           |                     |
|                     |           |                     |
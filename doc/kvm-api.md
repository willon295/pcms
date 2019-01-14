# Kvm-api 设计与实现

提供管理kvm虚拟机api接口

1. 安装新的虚拟机
   客户端提交新安装请求，自动化安装新的虚拟机， 并自动化配置环境
2. 重启虚拟机
   重启指定主机名的虚拟机
3. 删除虚拟机
   分为两种情况：
   - 用户手动删除
   - 项目已发布上线，虚拟机已到期

# 1. 代码实现

使用的技术：

Spring Boot、Spring Cloud、 Shell



# 2. 启动服务



保证服务后台运行， 并且将启动日志输出到日志文件

```bash
nohub java -jar /opt/app/kvm-api/kvm-api.jar >> `date +%Y%m%d`.log &
```
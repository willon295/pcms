# KVM自动化管理设计与实现

本系统主要实现以下功能：

1. 客户端提交主机名（项目名称-分支名）
2. 宿主机自动化创建kvm
3. 为kvm分配固定ip，配置网络，主机名，java运行环境

# 1. 文件目录结构说明

```bash
├── bin
│   └── kvm.sh              ==> kvm管理脚本
├── disk
│   ├── gitlab-api.qcow2    ==> 系统磁盘文件
│   └── pcms-web.qcow2
└── iso
      └── centos.iso  ==> 基于centos7制作的镜像文件，用于安装系统
```

# 2. kvm软件安装

宿主机操作系统为： `Centos7.6` 



1. 检查cpu是否支持虚拟化

   ```bash
   grep vmx /proc/cpuinfo
   ```

   若有信息输出，说明支持

2. 确保BIOS开启虚拟化功能

   ```bash
   modprobe  kvm
   modprobe kvm_intel
   lsmod | grep kvm
   ```

3. 安装kvm必要软件

   ```bash
    yum -y install  qemu-kvm.x86_64 qemu-kvm-tools.x86_64  libvirt.x86_64 bridge-utils  virt-install.noarch
   ```

4. 启动`libvirtd.service`

   ```bash
   systemctl start libvirtd.service
   systemctl enable libvirtd.service
   ```

# 3. kvm管理脚本

此脚本内容如下

```bash
#/bin/bash

#=================================================#
#    kvm.sh  install 名称 ==> 创建新的KVM
#    kvm.sh  delete  名称 ==> 删除指定KVM
#    kvm.sh  ip      名称 ==> 查看对应的IP
#    kvm.sh  start   名称 ==> 启动KVM
#    kvm.sh  check   名称 ==> 检查KVM
#    kvm.sh  restart 名称 ==> 重启KVM
#=================================================#
DISKPATH="/opt/kvm/disk"
ISOPATH="/opt/kvm/iso/centos.iso"
#检查KVM状态
function checkKvm(){
    STATUS=`virsh  list --all | grep  -e  $1`
    if [ -z "$STATUS" ];then
        echo "虚拟机不存在"
        exit 1
    fi
}
#启动KVM
function startKvm(){
    checkKvm $1
    if [ $? -eq 0 ]; then
        virsh  start $1
    fi
}

#启动所有关闭的kvm
function startAllKvm(){
	NOT_RUN_KVM=`virsh  list --all | grep  " - " | awk '{print $2}'`
	if [ -z "$NOT_RUN_KVM"  ]; then
			exit 0
	fi
	for i in $NOT_RUN_KVM
	do
        virsh  start $i
	done		
		
}

#重启KVM
function restartKvm(){
    checkKvm $1
    virsh  reboot $1
}
#关闭所有
function stopAllKvm(){
    RUN_KVM=` virsh   list  --all  | grep  "running" | awk '{print $2}'`
	if [ -z "$RUN_KVM" ];then
			exit 0
	fi
	for i in $RUN_KVM
	do
	    virsh shutdown $i
    done
}
#删除KVM
function destoryKvm(){
    virsh shutdown $1
    virsh destroy $1
    virsh undefine $1
    rm -f $DISKPATH/$1.qcow2
}
#获取KVM 的IP
function getKvmIp(){
    checkKvm $1
	RUNNING_KVM=`virsh  list --all | grep  $1`
	MAC=`virsh  dumpxml    $1 | grep "mac address" | awk -F"'" '{print $2}'` 
	IP=`arp -ne |grep "$MAC" |awk '{printf $1}'` 
	echo "$IP"
}
#检查目录是否存在
function checkAndmkDir(){
    if [ ! -d $DISKPATH ]; then
        mkdir -p $DISKPATH
    fi
}
#创建虚拟硬盘
function  createDisk(){
   checkAndmkDir
	qemu-img  create  -f qcow2 $DISKPATH/$1.qcow2 10G
	if [ -f $DISKPATH/$1.qcow2 ];then
		echo "Create Disk  $1.qcow2 SUCCESS !"
	fi
}
#安装系统
function installKvm(){
    echo "INSTALLING  $1"
	virt-install   --name $1  --ram 1024  --vcpus=1   --cdrom=$ISOPATH  --disk=$DISKPATH/$1.qcow2 --network network=default,model=virtio --os-type linux --os-variant rhel7.6
}

#第一个参数是功能参数
if [ -z $1 ];then
    echo "参数不合法"
elif [ "install" = $1 ];then
    createDisk $2
    if [ $? -eq 0 ]; then
        installKvm $2
    fi
elif [ "check" = $1 ];then
    checkKvm $2
elif [ "start" = $1 ];then
    startKvm $2
elif [ "restart" = $1 ];then
    restartKvm $2
elif [ "ip" = $1 ];then
    getKvmIp $2
elif [ "delete" = $1 ];then
    destoryKvm $2
elif [ "start-all" = $1 ];then
	startAllKvm
elif [ "stop-all" = $1 ];then
	stopAllKvm
else
    echo "命令不合法"
fi
```

# 4. KVM网络配置

创建虚拟网卡， 虚拟机的网络信息会被定义

1. 创建一个 `default.xml` 文件

   ```xml
   <network connections='2'>
     <name>default</name>
     <uuid>b024a020-6590-479f-b893-3b7451247936</uuid>
     <forward mode='nat'>
       <nat>
         <port start='1024' end='65535'/>
       </nat>
     </forward>
     <bridge name='virbr0' stp='on' delay='0'/>
     <mac address='52:54:00:18:04:95'/>
     <ip address='10.0.0.1' netmask='255.0.0.0'>
       <dhcp>
         <range start='10.0.0.10' end='10.0.0.254'/>
       </dhcp>
     </ip>
   </network>
   ```

2. 使用此配置

   ```bash
   virsh  net-define default.xml
   ```

3. 启动此网络

   ```bash
   virsh net-start  default
   virsh net-autostart default
   ```

# 5. 制作自动化安装ISO镜像

制作过程繁琐， 可参考本人博客： 

https://github.com/willon295/centos-kickstart-kvm/blob/master/centos7/readme.md

将生成iso文件重命名为 `centos.iso` , 移动到 `/opt/kvm/iso/` 目录

## 5.1 ks.cfg 文件

最终编写的 `ks.cfg` 文件内容如下

```bash
firewall --disabled
reboot
install
cdrom
text
rootpw --plaintext sdfsdf
auth --useshadow --passalgo=sha512
keyboard us
lang en_US
selinux --disabled
network --onboot=yes --device=eth0 --bootproto=dhcp --noipv6
skipx
logging --level=info
timezone --isUtc Asia/Shanghai
bootloader --location=mbr
zerombr
clearpart --all --initlabel
part /boot --fstype="ext4" --size=500
part swap --fstype="swap" --size=2048
part / --fstype="ext4" --grow --size=1
%packages --nobase
@core
%end
%post
HTTP_URL=ftp://10.0.0.10
mkdir -p /opt/lib /opt/app
#fast boot
sed  -i "s/timeout=5/timeout=0/g"  /boot/grub2/grub.cfg 
yum makecache
yum install  bash-completion net-tools -y
#config network,hostname,java
curl  $HTTP_URL/ifcfg-eth0  -o /etc/sysconfig/network-scripts/ifcfg-eth0
curl  $HTTP_URL/hostname  -o /etc/hostname
curl  $HTTP_URL/profile  -o /etc/profile
curl  $HTTP_URL/jdk8.tar.gz  -o /opt/lib/jdk8.tar.gz
tar  -zxf /opt/lib/jdk8.tar.gz -C /opt/lib/
rm -f /opt/lib/*.tar.gz
#use aliyun mirror
mv  /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.back
curl  $HTTP_URL/CentOS-Base.repo  -o  /etc/yum.repos.d/CentOS-Base.repo
#send finish signal
curl  $HTTP_URL:8080/finish
%end
```

## 5.2 isolinux.cfg 文件

主要修改内容：

```bash
label linux
  menu label ^Install CentOS 7
  kernel vmlinuz
  append ks=ftp://10.0.0.10/ks.cfg  initrd=initrd.img

label check
  menu label Test this ^media & install CentOS 7
  menu default
  kernel vmlinuz
  append ks=ftp://10.0.0.10/ks.cfg  initrd=initrd.img

```
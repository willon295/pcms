# static-web

前端页面： 主要负责页面的展示， 逻辑交互

# 1. 登录页面

实现用户的登录功能

1.  获取用户输入的：  `用户名` ， `密码` 
2. 发送Ajax请求， 登录
3. 登录失败提示失败，成功则跳转后台首页

# 2. 后台首页

默认展示我的变更：

## 2.1. 基本的统计数据
 变更总数、进行中的变更数、即将到期的变更数、已过期的变更数。 点击相应数字跳转至查看详情页面

##  2.2 变更列表

   分页显示以下信息： 

   - 变更名称： 
   - 变更状态： 环境创建中：0，进行中：1，即将到期:2，已到期:3
   - 到期日期：
   - 操作： 详情，操作项目环境，操作预发环境、操作线上环境



# 3. 变更详情

显示

- 变更id
- 变更名称：
- 分支名称：
- 到期时间
- 工程列表：
  - 工程名称：
  - 参与者：
  - 项目环境KVM的IP
- 审核信息



# 4. 操作项目环境页

显示 `变更名称` ，工程列表

## 4.1 工程列表

显示列表内容如下：

- 工程名： 工程名称

- 工程状态：-1:环境创建中， 0:待操作，1:打包中，2:部署中，3:运行中，4:打包失败，5:部署失败，6:测试通过，7:master代码有更新

- 操作按钮：

  master代码无需更新：

  1. 部署： 打包并且部署项目
  2. 测试通过： 测试通过，具有预发操作权限
  3. 回滚： 回滚项目至上一个版本
  4. 查看部署日志： 查看部署日志

  master代码需要更新：

  - 合并代码

# 5. 操作预发环境页

显示列表内容如下：

- 工程名： 工程名称

- 工程预发状态： -3:其他项目占用（不可操作）， -2：审核中，-1：审核拒绝， 0:待操作，1:打包中，2:部署中，3:运行中，4:打包失败，5:部署失败，6:测试通过

- 操作按钮：

  拥有预发操作权限：

  1. 部署： 打包并且部署项目
  2. 测试通过： 测试通过，具有预发操作权限
  3. 回滚： 回滚项目至上一个版本
  4. 查看部署日志： 查看部署日志

  无预发操作权限：

  - 无操作权限







# 6. 操作线上环境



显示列表内容如下：

- 工程名： 工程名称

- 工程预发状态： 0:待操作，1:打包中，2:部署中，3:运行中，4:打包失败，5:部署失败

- 操作按钮：

  拥有预发操作权限：

  1. 部署： 打包并且部署项目
  2. 测试通过： 测试通过，具有预发操作权限
  3. 回滚： 回滚项目至上一个版本
  4. 查看部署日志： 查看部署日志

  无预发操作权限：

  - 无操作权限

# 7. 添加变更

输入： 

- 变更名称：
- 创建人：变更owner 

- 分支名称:
- 到期日期：
- 工程列表：
  1. 工程名称
  2. 参与者

# 8. 修改变更

变更名称： 不可修改

分支名称： 不可修改

到期日期： 可修改

参与人员： 可赠删

工程：可添加删除



# 9. kvm管理

环境： 0：dev ,1:pre , 2:publish

显示三个tab， 分别为三个环境， 每个tab内容显示如下

自己项目的 kvm， 预发的服务器 ip
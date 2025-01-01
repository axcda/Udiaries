# UDiaries - 日记本应用

一个简洁的Android日记本应用，支持用户登录注册，可以创建、编辑和管理个人日记。

## 功能介绍

### 用户功能
- 用户注册
- 用户登录
- 账号管理

### 日记功能
- 查看日记列表
- 创建新日记
- 编辑日记内容
- 删除日记
- 日记时间记录
- 笔记搜索功能

## 技术实现

### 前端
- Material Design UI组件
- RecyclerView实现列表展示
- Retrofit2处理网络请求
- 实时搜索过滤

### 后端
- JSON Server提供RESTful API
- 文件持久化存储

## 开始使用

### 环境要求
- Android Studio
- JDK 17
- Node.js (运行JSON Server)
- Android 8.1+(API 27)以上设备

### 安装步骤

1. 克隆项目
```bash
git clone https://github.com/yourusername/UDiaries.git
```

2. 启动后端服务
```bash
# 安装json-server
npm install -g json-server

# 启动服务器
json-server --watch db.json
```

3. 配置Android Studio
- 打开项目
- 等待Gradle同步完成
- 确保SDK版本正确

4. 修改API地址
- 模拟器测试：使用`10.0.2.2:3000`
- 真机测试：使用电脑的IP地址

5. 运行应用

### 测试账号
```
用户名：admin
密码：123456
```

## API接口说明

### 用户接口
```
GET /users - 用户登录
POST /users - 用户注册
```

### 日记接口
```
GET /notes - 获取日记列表
GET /notes/{id} - 获取单个日记
POST /notes - 创建新日记
PUT /notes/{id} - 更新日记
DELETE /notes/{id} - 删除日记
```

## 数据结构

### 用户数据
```json
{
  "id": "1",
  "username": "admin",
  "password": "123456",
  "email": "test@example.com",
  "created_at": "2024-03-24T10:00:00Z"
}
```

### 笔记数据
```json
{
  "id": "1",
  "userId": 1,
  "title": "欢迎使用",
  "content": "这是您的第一条笔记",
  "created_at": "2024-03-24T10:00:00Z"
}
```

## 项目结构

```
app/
├── src/main/
│   ├── java/com/ruble/udiaries/
│   │   ├── api/
│   │   │   ├── ApiService.java
│   │   │   └── RetrofitClient.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── Note.java
│   │   ├── adapter/
│   │   │   └── NoteAdapter.java
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── NoteListActivity.java
│   │   └── NoteEditActivity.java
│   └── res/
│       ├── layout/
│       │   ├── activity_login.xml
│       │   ├── activity_register.xml
│       │   ├── activity_note_list.xml
│       │   ├── activity_note_edit.xml
│       │   └── item_note.xml
│       └── values/
│           ├── colors.xml
│           ├── strings.xml
│           └── themes.xml
```

## 最新更新

- 修改Note模型中id字段为String类型，适配json-server
- 更新API接口以支持String类型的id
- 添加笔记搜索功能
- 优化错误处理和日志记录
- 完善API响应处理

## 待开发功能

- [ ] 笔记分类
- [ ] 图片上传
- [ ] 数据导出
- [ ] 主题切换
- [ ] 密码修改
- [ ] 笔记标签功能

## 开发者

- [@yourusername](https://github.com/yourusername)

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件
```

这个README.md包含了：
1. 项目简介和主要功能
2. 技术栈说明
3. 详细的安装步骤
4. API接口文档
5. 完整的项目结构
6. 数据库结构说明
7. 待开发功能列表

你可以根据需要修改：
- 项目名称和描述
- GitHub用户名和链接
- 待开发功能列表
- 许可证类型


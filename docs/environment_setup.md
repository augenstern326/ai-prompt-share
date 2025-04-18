# AI提示词分享网站环境配置说明

本文档提供了AI提示词分享网站的环境配置说明，包括所有必要的软件和依赖项安装指南。

## 目录

1. [系统要求](#系统要求)
2. [前端环境配置](#前端环境配置)
3. [后端环境配置](#后端环境配置)
4. [数据库配置](#数据库配置)
5. [MinIO配置](#minio配置)
6. [项目配置](#项目配置)

## 系统要求

- 操作系统：Linux、Windows或macOS
- 内存：至少4GB RAM
- 存储空间：至少10GB可用空间
- 网络：稳定的互联网连接

## 前端环境配置

### 安装Node.js和npm

前端开发和构建需要Node.js环境，推荐使用Node.js 14.x或更高版本。

**Linux (Ubuntu/Debian):**

```bash
# 更新包管理器
sudo apt update

# 安装Node.js和npm
sudo apt install -y nodejs npm

# 安装n模块来管理Node.js版本
sudo npm install -g n

# 安装稳定版Node.js
sudo n stable

# 验证安装
node -v
npm -v
```

**Windows:**

1. 访问[Node.js官网](https://nodejs.org/)
2. 下载并安装LTS版本
3. 按照安装向导完成安装
4. 打开命令提示符验证安装：`node -v` 和 `npm -v`

**macOS:**

```bash
# 使用Homebrew安装
brew install node

# 验证安装
node -v
npm -v
```

### 安装Vue CLI

```bash
# 安装Vue CLI
npm install -g @vue/cli

# 验证安装
vue --version
```

## 后端环境配置

### 安装Java 8

**Linux (Ubuntu/Debian):**

```bash
# 更新包管理器
sudo apt update

# 安装OpenJDK 8
sudo apt install -y openjdk-8-jdk

# 验证安装
java -version
javac -version
```

**Windows:**

1. 访问[Oracle Java下载页面](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
2. 下载并安装JDK 8
3. 设置JAVA_HOME环境变量
4. 将%JAVA_HOME%\bin添加到PATH环境变量
5. 打开命令提示符验证安装：`java -version` 和 `javac -version`

**macOS:**

```bash
# 使用Homebrew安装
brew tap adoptopenjdk/openjdk
brew install --cask adoptopenjdk8

# 验证安装
java -version
javac -version
```

### 安装Maven

**Linux (Ubuntu/Debian):**

```bash
# 安装Maven
sudo apt install -y maven

# 验证安装
mvn -version
```

**Windows:**

1. 访问[Apache Maven下载页面](https://maven.apache.org/download.cgi)
2. 下载二进制zip文件
3. 解压到合适的目录（如C:\Program Files\Apache\maven）
4. 设置M2_HOME环境变量
5. 将%M2_HOME%\bin添加到PATH环境变量
6. 打开命令提示符验证安装：`mvn -version`

**macOS:**

```bash
# 使用Homebrew安装
brew install maven

# 验证安装
mvn -version
```

## 数据库配置

### 安装MySQL

**Linux (Ubuntu/Debian):**

```bash
# 更新包管理器
sudo apt update

# 安装MySQL
sudo apt install -y mysql-server

# 启动MySQL服务
sudo systemctl start mysql

# 设置开机自启
sudo systemctl enable mysql

# 配置MySQL安全设置
sudo mysql_secure_installation
```

**Windows:**

1. 访问[MySQL下载页面](https://dev.mysql.com/downloads/installer/)
2. 下载并运行MySQL安装程序
3. 选择"Developer Default"安装类型
4. 按照安装向导完成安装和配置

**macOS:**

```bash
# 使用Homebrew安装
brew install mysql

# 启动MySQL服务
brew services start mysql

# 配置MySQL安全设置
mysql_secure_installation
```

### 创建数据库和用户

连接到MySQL后，执行以下SQL命令：

```sql
-- 创建数据库
CREATE DATABASE ai_prompt_sharing CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权
CREATE USER 'promptuser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ai_prompt_sharing.* TO 'promptuser'@'localhost';
FLUSH PRIVILEGES;
```

## MinIO配置

### 安装MinIO

**Linux (Ubuntu/Debian):**

```bash
# 下载MinIO
wget https://dl.min.io/server/minio/release/linux-amd64/minio

# 添加执行权限
chmod +x minio

# 移动到bin目录
sudo mv minio /usr/local/bin/

# 创建存储目录
sudo mkdir -p /data/minio

# 创建MinIO用户
sudo useradd -r minio-user -s /sbin/nologin

# 设置目录权限
sudo chown minio-user:minio-user /data/minio

# 创建systemd服务文件
sudo nano /etc/systemd/system/minio.service
```

将以下内容添加到服务文件中：

```
[Unit]
Description=MinIO
Documentation=https://docs.min.io
Wants=network-online.target
After=network-online.target

[Service]
User=minio-user
Group=minio-user
Environment="MINIO_ROOT_USER=minioadmin"
Environment="MINIO_ROOT_PASSWORD=minioadmin"
ExecStart=/usr/local/bin/minio server /data/minio --console-address :9001
Restart=always
LimitNOFILE=65536

[Install]
WantedBy=multi-user.target
```

然后启动服务：

```bash
# 重新加载systemd配置
sudo systemctl daemon-reload

# 启动MinIO服务
sudo systemctl start minio

# 设置开机自启
sudo systemctl enable minio
```

**Windows:**

1. 访问[MinIO下载页面](https://min.io/download)
2. 下载Windows版本
3. 创建存储目录（如D:\minio\data）
4. 打开命令提示符，运行：
   ```
   minio.exe server D:\minio\data --console-address :9001
   ```

**macOS:**

```bash
# 使用Homebrew安装
brew install minio/stable/minio

# 创建存储目录
mkdir -p ~/minio/data

# 启动MinIO服务
minio server ~/minio/data --console-address :9001
```

### 配置MinIO

1. 访问MinIO控制台：http://localhost:9001
2. 使用默认凭据登录（用户名：minioadmin，密码：minioadmin）
3. 创建一个名为"ai-prompt-sharing"的存储桶

## 项目配置

### 前端配置

1. 修改API基础URL

编辑`/src/utils/request.js`文件，将baseURL修改为后端服务地址：

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api', // 修改为实际后端地址
  timeout: 15000
});
```

### 后端配置

1. 修改数据库连接信息

编辑`/src/main/resources/application.yml`文件：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_prompt_sharing?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: promptuser
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

2. 修改MinIO配置

编辑`/src/main/resources/application.yml`文件：

```yaml
minio:
  endpoint: http://localhost:9000
  accessKey: minioadmin
  secretKey: minioadmin
  bucketName: ai-prompt-sharing
```

## 启动项目

### 启动后端

```bash
# 进入后端项目目录
cd ai-prompt-sharing/backend

# 使用Maven打包
mvn clean package -DskipTests

# 运行Spring Boot应用
java -jar target/sharing-0.0.1-SNAPSHOT.jar
```

### 启动前端（开发模式）

```bash
# 进入前端项目目录
cd ai-prompt-sharing/frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

### 部署前端（生产模式）

```bash
# 进入前端项目目录
cd ai-prompt-sharing/frontend

# 构建生产版本
npm run build

# 将dist目录部署到Web服务器
```

现在，您已经完成了AI提示词分享网站的环境配置。前端应用将在http://localhost:8080访问，后端API将在http://localhost:8080/api提供服务。

# 技术交底书智能撰写系统

## 项目说明

这是一个基于Vue.js 3开发的技术交底书智能撰写系统前端项目。项目采用标准Vue项目结构，便于团队协作和维护。

## 项目结构

```
tech_prompt/
├── public/             # 静态资源
│   ├── index.html      # Vue应用HTML模板
│   └── legacy-index.html # 原有的HTML文件(作为iframe内容)
├── src/                # 源代码
│   ├── assets/         # 静态资源
│   ├── components/     # 组件
│   ├── views/          # 页面
│   ├── router/         # 路由
│   ├── store/          # 状态管理
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── Dockerfile          # Docker构建文件
├── babel.config.js     # Babel配置
├── package.json        # 项目依赖
├── vue.config.js       # Vue配置
└── nginx.conf          # Nginx配置
```

## 特别说明

本项目使用了嵌套方式，将原有的前端页面通过iframe集成在Vue应用中：
1. 原始的index.html文件被复制为legacy-index.html
2. Vue应用创建自己的index.html作为入口
3. App.vue组件通过iframe加载legacy-index.html

这种方式保留了原有功能的同时，提供了更现代的项目结构。

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run serve
```

### 构建生产版本

```bash
npm run build
```

### Docker构建

```bash
docker build -t tech-prompt .
```

## 接口说明

项目使用Axios进行API调用，所有接口请求都会自动添加身份验证Token。

API基础URL:
- 开发环境: http://localhost:9000
- 生产环境: 相对路径，由Nginx代理

## 部署说明

项目可以通过Docker进行部署，构建后的静态文件会被复制到Nginx容器中，并通过Nginx提供服务。

```bash
# 构建Docker镜像
docker build -t tech-prompt .

# 运行容器
docker run -d -p 80:80 tech-prompt
``` 
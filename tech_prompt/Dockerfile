# 使用nginx镜像作为基础
FROM nginx:stable-alpine

# 创建工作目录
WORKDIR /usr/share/nginx/html

# 直接复制所有静态文件到nginx目录
COPY . /usr/share/nginx/html

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露80端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]
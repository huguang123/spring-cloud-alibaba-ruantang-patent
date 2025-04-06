#!/bin/bash

# 创建必要的目录
echo "创建必要的目录..."
mkdir -p nginx/conf nginx/logs nginx/html nginx/ssl

# 复制nginx配置
echo "复制nginx配置文件..."
mkdir -p nginx/conf
cp tech_prompt/nginx.conf nginx/conf/default.conf

# 手动构建前端
echo "开始构建前端项目..."
bash build-frontend.sh

# 启动环境服务
echo "启动环境服务..."
docker-compose -f docker-compose.env.yml up -d

# 等待环境服务启动完成
echo "等待环境服务启动完成..."
sleep 10

# 启动应用服务（但不包括frontend服务，因为我们已经手动构建了前端）
echo "启动应用服务..."
# 通过--scale frontend=0禁用frontend服务的启动
docker-compose -f docker-compose.service.yml up -d --scale frontend=0

echo "部署完成，服务已启动。"
echo "请通过 http://localhost 访问应用。" 
#!/bin/bash

echo "开始构建前端项目..."

# 进入前端项目目录
cd tech_prompt

# 安装依赖
echo "安装依赖..."
npm install

# 构建项目
echo "构建项目..."
NODE_ENV=production npm run build

# 创建nginx静态文件目录
echo "创建nginx静态文件目录..."
mkdir -p ../nginx/html

# 清空原有文件
echo "清空原有文件..."
rm -rf ../nginx/html/*

# 复制构建结果到nginx目录
echo "复制构建结果到nginx目录..."
cp -r dist/* ../nginx/html/

echo "前端项目构建完成！" 
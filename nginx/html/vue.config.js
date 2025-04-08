const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  lintOnSave: false,
  productionSourceMap: false,
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  transpileDependencies: true,
  
  // 将原有的index.html文件复制到public目录，但使用不同的文件名
  chainWebpack: config => {
    config.plugin('copy').tap(([options]) => {
      options.patterns.push({
        from: 'index.html',
        to: 'legacy-index.html',  // 改为legacy-index.html避免冲突
        toType: 'file'
      });
      return [options];
    });
  },
  
  // 开发服务器配置
  devServer: {
    port: 8080,
    open: true,
    proxy: {
      '/api': {
        target: process.env.API_URL || 'http://gateway:9000',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/ums': {
        target: process.env.API_URL || 'http://gateway:9000',
        changeOrigin: true
      },
      '/prom': {
        target: process.env.API_URL || 'http://gateway:9000',
        changeOrigin: true
      }
    }
  }
}); 
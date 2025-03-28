spring:
  application:
    name: service-user  # 这个值决定了从Nacos加载的dataId
  config:
    import: optional:nacos:${spring.application.name}.yaml
  cloud:
    nacos:
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      discovery:
        namespace: ""
      config:
        namespace: ""
        group: DEFAULT_GROUP
        file-extension: yaml  # 关键配置，指定加载yaml格式
        refresh-enabled: true  # 启用配置动态刷新

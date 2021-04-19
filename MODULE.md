# 基础服务 garden-service
https://github.com/geektcp/garden-service.git

 模块名称                 | 开发人员  | 新端口   | 说明
---                      | ---       | ---      | --- 
garden-dist              |           |          | 打包服务
garden-common-core       |           |          | 核心公共模块  主要是高频广泛使用的工具包
garden-common-nacos      |           |          | 配置公共模块  负责配置中心的连接
garden-common-spring     |           |          | 框架公共模块  主要是spring的框架代码和依赖包
garden-zuul              |           | 10000    | 网关
garden-gateway           |           | 10100    | 网关
garden-auth              | xxx       | 10200    | 前台鉴权  
garden-log               | xxx       | 10400    | 统一日志服务

# 前端项目 garden-front
https://github.com/geektcp/garden-front.git

 模块名称               | 开发人员   | 端口       | 说明
---                    | ---        |  ---       | ---
garden-front           | xxx        | 9000       | 博客花园前端 

# 前端项目 garden-back
https://github.com/geektcp/garden-back.git

 模块名称               | 开发人员   | 端口      | 说明
---                    | ---        |  ---     | ---
garden-back            | xxx        | 6000    | 博客花园后台

# nacos配置中心
通过环境变量DEPLOY_ENV决定使用哪一组配置

环境变量                 | 说明 
---                     | --- 
DEPLOY_ENV=dev          | 开发环境
DEPLOY_ENV=release      | 发布环境

# 环境列表
环境名称 | 前端           | 微服务
---     | ---            | --- 
开发环境 | x.x.x.x        | x.x.x.x 
演示环境 | x.x.x.x        | x.x.x.x 

# 当前分支
dev-1.0

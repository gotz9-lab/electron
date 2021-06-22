# Electron-chat

基于 Netty 与 protobuf 序列化的 socket 服务器. 借助 javax 提供的 compiler 套件实现逻辑代码编译, 通过自定义 ClassLoader 加载类, 实现逻辑代码热更新.

## module
模块说明:

| 模块                | 说明                                                                                        |
| :------------------ | :------------------------------------------------------------------------------------------ |
| `electron-message`  | protobuf 协议, 通过 maven 插件编译生成 Java 类. 编译需要环境 PATH 变量中提供 protoc 编译器. |
| `electron-core`     | 提供核心的编译器模块, handler 加载模块, service 容器访问.                                   |
| `electron-protocol` | 提供 socket 通信协议编解码以及其他支持.                                                     |
| `electron-service`  | 服务器承载的核心业务, 基于 Spring context 容器加载和管理.                                   |
| `electron-handler`  | socket 消息处理, 接收相关 protocol 消息, 调用核心业务库进行处理.                            |

## build

环境依赖:

|  项目  |  版本  |                                           说明                                           |
| :----: | :----: | :--------------------------------------------------------------------------------------: |
| protoc | 3.15.8 | protobuf 编译器, 需要在 PATH 中配置添加可执行文件路径, 用以支持 `electron-message` 编译. |


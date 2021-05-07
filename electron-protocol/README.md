# Electron 编码

编码分为 2 层: 长度前缀分片, protobuf 编解码.

protobuf 消息体内部利用 type 字段说明 oneof 字段中具体的消息类型. 
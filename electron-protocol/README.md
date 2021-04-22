# Electron 协议

协议功能暂定为单聊, 群聊.

内部涉及登录认证, 在线状态同步, 消息传输

## 序列化

为了便于前期快速实现, 使用 JSON 格式传输, 通过 GSON 库进行解析.

## 消息体

以下是伪代码描述, 用形似 protobuf 的格式进行描述, 使用数据类型暂定都是 `string`.

```proto
// 用户信息
struct basicUserInfo {
    string  nickname,
}

struct userStateInfo extends basicUserInfo {
    state   state,  // 在线状态
}

// 群组信息
struct groupInfo {
    string  groupName,          // 群组名
    list<basicUserInfo> users,  // 群组用户列表
}

// 消息内容
struct message { 
    string  content,    // 消息内容
}

enum notificationType {
    CONNECTION_RESET,
    AUTHENTICATE_FAILED,
}

// 全局错误信息
response notify {
    tips    type,       // 类型
    string  message,    // 消息内容
}

// 认证登录:
request authenticate {
    string  username,   // 账户
    string  password,   // 密码
}

// 信息同步:

// 用户在线状态
enum userState {
    ONLINE,
    OFFLINE,
}

// 请求同步数据
request sync {}

// 好友列表
response friendList {
    list<userInfo>  users;    
}

// 群组列表
response groupList {
    list<groupInfo> users;    
}

// 状态更新:

struct newState {
    string  user,
    state   state,
}

response updateState {
    list<newState> states, // 状态变化列表
}

// 单聊消息:

// 发送单聊
request sendMessage {
    string  target,     // 消息目标
    message message,    // 消息内容
}

// 接收单聊
response receiveMessage {
    string  source,     // 消息来源
    message message,    // 消息内容
} 

// 群聊消息:

// 发送群聊
request sendGroupMessage {
    string  target,     // 消息目标
    message message,    // 消息内容
}

// 接收群聊
response receiveGroupMessage {
    string  source,     // 消息来源
    string  group,      // 来源组
    message message,    // 消息内容
}

```
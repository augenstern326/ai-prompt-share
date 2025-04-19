# 数据库设计文档

## 概述
本文档描述了AI提示词分享网站的数据库设计，包括表结构、字段说明和关系模型。

## 数据库表设计

### 1. 用户表 (user)
存储系统用户信息，包括普通用户和管理员。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键，用户ID |
| username | varchar(50) | 用户名，唯一 |
| password | varchar(100) | 密码（加密存储） |
| email | varchar(100) | 邮箱，唯一 |
| avatar | varchar(255) | 头像URL |
| role | varchar(20) | 角色：user-普通用户，admin-管理员 |
| status | tinyint(1) | 状态：0-禁用，1-启用 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint(1) | 是否删除：0-未删除，1-已删除 |

### 2. 标签表 (tag)
存储提示词标签，支持系统预设和用户自定义。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键，标签ID |
| name | varchar(50) | 标签名称，唯一 |
| type | tinyint(1) | 标签类型：0-系统预设，1-用户创建 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint(1) | 是否删除：0-未删除，1-已删除 |

### 3. 提示词表 (prompt)
存储用户上传的AI提示词。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键，提示词ID |
| title | varchar(100) | 标题 |
| content | text | 提示词内容 |
| image_url | varchar(255) | 效果图片URL |
| user_id | varchar(32) | 创建用户ID |
| like_count | int(11) | 点赞数 |
| dislike_count | int(11) | 点踩数 |
| favorite_count | int(11) | 收藏数 |
| view_count | int(11) | 浏览数 |
| status | tinyint(1) | 状态：0-禁用，1-启用 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |
| deleted | tinyint(1) | 是否删除：0-未删除，1-已删除 |

### 4. 提示词标签关联表 (prompt_tag)
存储提示词和标签的多对多关系。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键 |
| prompt_id | varchar(32) | 提示词ID |
| tag_id | varchar(32) | 标签ID |
| create_time | datetime | 创建时间 |

### 5. 收藏表 (favorite)
存储用户收藏的提示词。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键 |
| user_id | varchar(32) | 用户ID |
| prompt_id | varchar(32) | 提示词ID |
| create_time | datetime | 创建时间 |

### 6. 点赞/点踩表 (vote)
存储用户对提示词的点赞或点踩记录。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键 |
| user_id | varchar(32) | 用户ID |
| prompt_id | varchar(32) | 提示词ID |
| type | tinyint(1) | 类型：1-点赞，-1-点踩 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

### 7. 举报表 (report)
存储用户举报提示词的记录。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | varchar(32) | 主键 |
| user_id | varchar(32) | 举报用户ID |
| prompt_id | varchar(32) | 被举报提示词ID |
| reason | varchar(255) | 举报理由 |
| status | tinyint(1) | 状态：0-未处理，1-已处理 |
| handle_result | varchar(255) | 处理结果 |
| handle_time | datetime | 处理时间 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

## 关系模型

1. 用户(1) - 提示词(N)：一个用户可以创建多个提示词
2. 提示词(N) - 标签(M)：一个提示词可以有多个标签，一个标签可以属于多个提示词
3. 用户(1) - 收藏(N)：一个用户可以收藏多个提示词
4. 用户(1) - 点赞/点踩(N)：一个用户可以对多个提示词点赞或点踩
5. 用户(1) - 举报(N)：一个用户可以举报多个提示词

## 索引设计

为了提高查询效率，数据库设计了以下索引：

1. 用户表：用户名和邮箱唯一索引
2. 标签表：标签名称唯一索引
3. 提示词表：用户ID索引、创建时间索引、点赞点踩复合索引、收藏数索引
4. 提示词标签关联表：提示词ID和标签ID的唯一复合索引
5. 收藏表：用户ID和提示词ID的唯一复合索引
6. 点赞/点踩表：用户ID和提示词ID的唯一复合索引
7. 举报表：用户ID索引、提示词ID索引、状态索引

## 设计考虑

1. 支持未登录用户浏览提示词广场，但无操作权限
2. 标签系统支持预设标签和用户自由创建
3. 提示词可以按照点赞数减去点踩数进行排序或按收藏数排序
4. 支持提示词搜索和标签筛选
5. 用户只能给一个提示词点一次赞或踩（通过唯一索引约束）
6. 使用逻辑删除（deleted字段）而非物理删除，便于数据恢复和审计

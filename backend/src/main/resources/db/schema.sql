-- 用户表
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色：user-普通用户，admin-管理员',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 标签表
CREATE TABLE `tag` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT '标签ID',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `user_id` varchar(32) NOT NULL COMMENT '创建人ID',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '标签类型：0-系统预设，1-用户创建',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 提示词表
CREATE TABLE `prompt` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT '提示词ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '提示词内容',
  `image_url` varchar(255) DEFAULT NULL COMMENT '效果图片URL',
  `user_id` varchar(32) NOT NULL COMMENT '创建用户ID',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `dislike_count` int(11) NOT NULL DEFAULT 0 COMMENT '点踩数',
  `favorite_count` int(11) NOT NULL DEFAULT 0 COMMENT '收藏数',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览数',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_score` (`like_count`, `dislike_count`),
  KEY `idx_favorite_count` (`favorite_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提示词表';

-- 提示词标签关联表
CREATE TABLE `prompt_tag` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT 'ID',
  `prompt_id` varchar(32) NOT NULL COMMENT '提示词ID',
  `tag_id` varchar(32) NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_prompt_tag` (`prompt_id`, `tag_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提示词标签关联表';

-- 收藏表
CREATE TABLE `favorite` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT 'ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `prompt_id` varchar(32) NOT NULL COMMENT '提示词ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_prompt` (`user_id`, `prompt_id`),
  KEY `idx_prompt_id` (`prompt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 点赞/点踩表
CREATE TABLE `vote` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT 'ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `prompt_id` varchar(32) NOT NULL COMMENT '提示词ID',
  `type` tinyint(1) NOT NULL COMMENT '类型：1-点赞，-1-点踩',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_prompt` (`user_id`, `prompt_id`),
  KEY `idx_prompt_id` (`prompt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞/点踩表';

-- 举报表
CREATE TABLE `report` (
  `id` varchar(32) NOT NULL DEFAULT (REPLACE(UUID(), '-', '')) COMMENT 'ID',
  `user_id` varchar(32) NOT NULL COMMENT '举报用户ID',
  `prompt_id` varchar(32) NOT NULL COMMENT '被举报提示词ID',
  `reason` varchar(255) NOT NULL COMMENT '举报理由',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-未处理，1-已处理',
  `handle_result` varchar(255) DEFAULT NULL COMMENT '处理结果',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prompt_id` (`prompt_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报表';

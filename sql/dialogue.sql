/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : localhost:3306
 Source Schema         : dialogue

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 14/06/2024 14:00:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_user` int(0) UNSIGNED NOT NULL COMMENT '作者id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者用户名',
  `user_photo` int(0) UNSIGNED NULL DEFAULT 1,
  `gmt_create` datetime(0) NOT NULL COMMENT '文章创建时间 ',
  `gmt_modify` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '文章修改时间',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `subtitle` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '文章副标题',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '摘要',
  `article_photo` int(0) UNSIGNED NULL DEFAULT NULL,
  `article_view` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '文章浏览量',
  `article_like` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '文章点赞量',
  `article_collect` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '文章收藏量',
  `article_share` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '文章分享量',
  `is_check` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否审核',
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `total_mark` int(0) UNSIGNED NULL DEFAULT 0,
  `mark_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `idx_block` int(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`, `idx_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_activity
-- ----------------------------
DROP TABLE IF EXISTS `article_activity`;
CREATE TABLE `article_activity`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_article` int(0) UNSIGNED NULL DEFAULT NULL,
  `idx_activity` int(0) UNSIGNED NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论唯一id',
  `idx_article` bigint(0) UNSIGNED NOT NULL COMMENT '文章id',
  `idx_comment` bigint(0) UNSIGNED NOT NULL COMMENT '非0代表子评论父id，0代表不是子评论',
  `idx_user` int(0) UNSIGNED NOT NULL COMMENT '作者id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者用户名',
  `user_photo` int(0) UNSIGNED NULL DEFAULT 1,
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未知',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论内容',
  `comment_like` int(0) UNSIGNED NULL DEFAULT 0 COMMENT '评论点赞量',
  `gmt_create` datetime(0) NOT NULL COMMENT '评论创建时间',
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '评论修改时间',
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否已删除',
  `idx_span` int(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`, `idx_article`, `idx_comment`, `idx_user`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_detail
-- ----------------------------
DROP TABLE IF EXISTS `article_detail`;
CREATE TABLE `article_detail`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_create` datetime(0) NOT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `edit_type` tinyint(0) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_photo
-- ----------------------------
DROP TABLE IF EXISTS `article_photo`;
CREATE TABLE `article_photo`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_article` bigint(0) UNSIGNED NOT NULL,
  `tag` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`, `idx_article`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `block_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `is_login` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `idx_photo` int(0) UNSIGNED NULL DEFAULT 0,
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '小可爱用户',
  `user_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '小可爱用户',
  `user_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '系统原装签名',
  `user_age` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `user_city` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `user_level` tinyint(0) UNSIGNED NULL DEFAULT 1,
  `user_tel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `user_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未知',
  `total_article_num` int(0) NULL DEFAULT 0,
  `total_article_view` int(0) NULL DEFAULT 0,
  `total_article_like` int(0) NULL DEFAULT 0,
  `total_article_collect` int(0) NULL DEFAULT 0,
  `total_follower_num` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_collect
-- ----------------------------
DROP TABLE IF EXISTS `user_collect`;
CREATE TABLE `user_collect`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_user` int(0) UNSIGNED NOT NULL DEFAULT 0,
  `idx_article` bigint(0) UNSIGNED NOT NULL DEFAULT 0,
  `gmt_create` datetime(0) NOT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`, `idx_user`, `idx_article`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_comment_history
-- ----------------------------
DROP TABLE IF EXISTS `user_comment_history`;
CREATE TABLE `user_comment_history`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_user` int(0) UNSIGNED NULL DEFAULT NULL,
  `idx_comment` int(0) UNSIGNED NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL,
  `is_like` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_contact
-- ----------------------------
DROP TABLE IF EXISTS `user_contact`;
CREATE TABLE `user_contact`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_from` int(0) UNSIGNED NOT NULL,
  `idx_to` int(0) UNSIGNED NOT NULL,
  `name_from` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name_to` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `photo_from` int(0) UNSIGNED NULL DEFAULT 0,
  `photo_to` int(0) UNSIGNED NULL DEFAULT 0,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_create` datetime(0) NOT NULL,
  `gmt_modify` datetime(0) NOT NULL,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_check` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `type` tinyint(0) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`, `idx_to`, `gmt_modify`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_follower
-- ----------------------------
DROP TABLE IF EXISTS `user_follower`;
CREATE TABLE `user_follower`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_user` int(0) UNSIGNED NOT NULL DEFAULT 0,
  `idx_follower` int(0) UNSIGNED NOT NULL DEFAULT 0,
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `follower_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`, `idx_user`, `idx_follower`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_history
-- ----------------------------
DROP TABLE IF EXISTS `user_history`;
CREATE TABLE `user_history`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_user` bigint(0) UNSIGNED NOT NULL,
  `idx_article` bigint(0) UNSIGNED NOT NULL,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '未知',
  `is_like` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_collect` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_share` tinyint(0) NULL DEFAULT 0,
  `is_comment` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `mark_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`, `idx_user`, `gmt_modify`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `idx_to` int(0) UNSIGNED NOT NULL COMMENT '消息接受用户id',
  `idx_from` int(0) UNSIGNED NOT NULL COMMENT '消息发送用户id',
  `photo_from` int(0) UNSIGNED NULL DEFAULT 0,
  `name_to` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息接受用户名',
  `name_from` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息发送用户名',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `is_delete` tinyint(0) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `is_check` tinyint(0) UNSIGNED NULL DEFAULT 0,
  `type` tinyint(0) UNSIGNED NULL DEFAULT 1,
  PRIMARY KEY (`id`, `idx_to`, `idx_from`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_photo
-- ----------------------------
DROP TABLE IF EXISTS `user_photo`;
CREATE TABLE `user_photo`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_modify` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

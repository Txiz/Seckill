/*
 Navicat Premium Data Transfer

 Source Server         : MySQL-Aliyun
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 121.196.198.1:3306
 Source Schema         : txiz-seckill

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 09/07/2021 22:08:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `oid` int NOT NULL AUTO_INCREMENT COMMENT '订单表主键ID',
  `sid` int NULL DEFAULT NULL COMMENT '库存ID',
  `uid` int NULL DEFAULT NULL COMMENT '用户ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库存名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 332 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (332, 1, 1, 'phone', '2021-07-09 21:55:10');
INSERT INTO `tb_order` VALUES (333, 1, 1, 'phone', '2021-07-09 21:55:10');
INSERT INTO `tb_order` VALUES (334, 1, 1, 'phone', '2021-07-09 21:55:10');
INSERT INTO `tb_order` VALUES (335, 1, 1, 'phone', '2021-07-09 21:55:11');
INSERT INTO `tb_order` VALUES (336, 1, 1, 'phone', '2021-07-09 21:55:11');
INSERT INTO `tb_order` VALUES (337, 1, 1, 'phone', '2021-07-09 21:55:11');
INSERT INTO `tb_order` VALUES (338, 1, 1, 'phone', '2021-07-09 21:55:11');
INSERT INTO `tb_order` VALUES (339, 1, 1, 'phone', '2021-07-09 21:55:11');
INSERT INTO `tb_order` VALUES (340, 1, 1, 'phone', '2021-07-09 21:55:12');
INSERT INTO `tb_order` VALUES (341, 1, 1, 'phone', '2021-07-09 21:55:12');
INSERT INTO `tb_order` VALUES (342, 1, 1, 'phone', '2021-07-09 21:55:12');

-- ----------------------------
-- Table structure for tb_stock
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock`;
CREATE TABLE `tb_stock`  (
  `sid` int NOT NULL AUTO_INCREMENT COMMENT '库存表主键ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `count` int NULL DEFAULT NULL COMMENT '库存',
  `sale` int NULL DEFAULT NULL COMMENT '已售',
  `version` int NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_stock
-- ----------------------------
INSERT INTO `tb_stock` VALUES (1, 'phone', 100, 11, 11);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `uid` int NOT NULL COMMENT '用户表主键ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

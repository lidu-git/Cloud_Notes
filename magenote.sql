/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : magenote

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-02-22 15:31:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_note
-- ----------------------------
DROP TABLE IF EXISTS `tb_note`;
CREATE TABLE `tb_note` (
  `noteId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `typeId` int(11) DEFAULT NULL COMMENT '外键，从属tb_note_type',
  `pubtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`noteId`),
  KEY `fk_note_ref_type` (`typeId`),
  CONSTRAINT `fk_note_ref_type` FOREIGN KEY (`typeId`) REFERENCES `tb_note_type` (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=396 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_note
-- ----------------------------
INSERT INTO `tb_note` VALUES ('392', 'oop', '<p>oop都完了。。。。change</p>', '8', '2017-05-01 14:28:44');
INSERT INTO `tb_note` VALUES ('393', '数据库666', '<p>666666</p>', '24', '2017-09-15 19:55:26');
INSERT INTO `tb_note` VALUES ('394', 'php666', '<p>666666</p>', '23', '2017-09-15 20:19:58');
INSERT INTO `tb_note` VALUES ('395', 'abc', '<p>abc</p>', '6', '2019-02-20 17:52:41');

-- ----------------------------
-- Table structure for tb_note_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_note_type`;
CREATE TABLE `tb_note_type` (
  `typeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长',
  `typeName` varchar(50) NOT NULL COMMENT '类别名，在同一个用户下唯一',
  `userId` int(11) DEFAULT NULL COMMENT '从属用户',
  PRIMARY KEY (`typeId`),
  KEY `fk_type_ref_user` (`userId`),
  CONSTRAINT `fk_type_ref_user` FOREIGN KEY (`userId`) REFERENCES `tb_user` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_note_type
-- ----------------------------
INSERT INTO `tb_note_type` VALUES ('6', '1', '2');
INSERT INTO `tb_note_type` VALUES ('7', '2', '2');
INSERT INTO `tb_note_type` VALUES ('8', '五一长假', '1');
INSERT INTO `tb_note_type` VALUES ('22', 'java', '1');
INSERT INTO `tb_note_type` VALUES ('23', 'php', '1');
INSERT INTO `tb_note_type` VALUES ('24', '数据库', '1');
INSERT INTO `tb_note_type` VALUES ('25', '3', '2');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自动增长',
  `uname` varchar(50) NOT NULL COMMENT '用户名',
  `upwd` varchar(50) DEFAULT NULL COMMENT '密码',
  `nick` varchar(50) DEFAULT NULL COMMENT '昵称',
  `head` varchar(100) DEFAULT NULL COMMENT '头像',
  `mood` varchar(500) DEFAULT NULL COMMENT '心情',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'admin', '4QrcOUm6Wau+VuBX8g+IPg==', 'mage', 'h2.jpg', '我爱编程');
INSERT INTO `tb_user` VALUES ('2', 'abc', '4QrcOUm6Wau+VuBX8g+IPg==', 'test', 'h2.jpg', 'test的心情');

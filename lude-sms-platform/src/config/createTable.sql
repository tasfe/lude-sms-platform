set charset GBK;
drop database smsdb;
create database smsdb charset=utf8;
use smsdb;


CREATE TABLE `agent` (
  `agent_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `status` tinyint(3) NOT NULL default '1',
  `ack_level` int(10) NOT NULL default '0',
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `organization` (
  `organization_id` int(10) unsigned NOT NULL auto_increment,
  `parent_org_id` int(10) unsigned NOT NULL default '0',
  `org_no` char(50) NOT NULL default '0', 
  `name` varchar(100) NOT NULL,
  `status` tinyint(3) NOT NULL default '1',  
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `channel` (
  `channel_id` int(10) unsigned NOT NULL auto_increment,
  `channel_no` char(50) NOT NULL default '0', 
  `name` varchar(100) NOT NULL,
  `status` tinyint(3) NOT NULL default '1',  
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sms_server` (
  `sms_server_id` int(10) unsigned NOT NULL default '0',  
  `name` varchar(100) NOT NULL,
  `status` tinyint(3) NOT NULL default '1',
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`sms_server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `operator` (
  `operator_id` int(10) unsigned NOT NULL auto_increment,
  `login_name` varchar(80) NOT NULL default '',
  `password` varchar(80) NOT NULL default '',
  `name` varchar(80) NOT NULL default '',  
  `status` tinyint(3) NOT NULL default '1',
  `msisdn` bigint(20) unsigned NOT NULL default '0',
  `organization_id` int(10) unsigned NOT NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',  
  PRIMARY KEY  (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `role_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL default '1',  
  `description` varchar(200) NOT NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `operator_role` (
  `user_role_id` int(10) unsigned NOT NULL auto_increment,
  `operator_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `status` tinyint(3) NOT NULL default '1',  
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permission_sort` (
  `permission_sort_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `index` smallint(6) NOT NULL default '1',
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`permission_sort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permission` (
  `permission_id` int(10) unsigned NOT NULL auto_increment,
  `key_name` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `index` smallint(6) NOT NULL default '1',
  `permission_sort_id` int(10) unsigned NOT NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_permission` (
  `role_permission_id` int(10) unsigned NOT NULL auto_increment,
  `role_id` int(10) unsigned NOT NULL,
  `permission_id` int(10) unsigned NOT NULL,
  `status` tinyint(3) NOT NULL default '1',  
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`role_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mt_queue` (
  `mt_id` bigint(20) unsigned NOT NULL auto_increment,
  `org_no` char(50) NOT NULL default '0',
  `channel_no` char(50) NOT NULL default '0', 
  `bank_no` char(50) NOT NULL default '0',
  `cust_no` char(50) NOT NULL default '0',
  `msisdn` bigint(20) NOT NULL default '0',
  `mt_type` tinyint(3) unsigned NOT NULL default '1',  
  `mt_template_id` int(10) unsigned NOT NULL default '0',  
  `content` varchar(1100) NOT NULL, 
  `sms_num` tinyint(3) NOT NULL default '1',
  `agent_id` int(10) unsigned default NULL,  
  `send_type` tinyint(3) unsigned NOT NULL default '1',
  `immedflag` tinyint(3) unsigned NOT NULL default '1', 
  `order_datetime` datetime NOT NULL,   
  `priority` tinyint(3) NOT NULL default '0',
  `sms_server_id` int(10) unsigned NOT NULL,   
  `trx_id` bigint(20) unsigned default NULL,
  `mt_status` smallint(6) NOT NULL default '0',
  `sp_status` smallint(6) NOT NULL default '0',
  `smsc_status` smallint(6) NOT NULL default '0',
  `dn_status` smallint(6) NOT NULL default '0',
  `final_status` smallint(6) NOT NULL default '0',  
  `error_msg` varchar(500) default NULL, 
  `commit_counter` smallint(6) NOT NULL default '0',
  `commit_succ_counter` smallint(6) NOT NULL default '0',
  `done_date` date default NULL,
  `operator_id` int(10) unsigned default NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',  
  PRIMARY KEY  (`mt_id`),  
  KEY `agent` (`agent_id`),
  key transaction(`trx_id`),
  KEY `send_type` (`send_type`), 
  KEY `immedflag` (`immedflag`), 
  KEY `sms_server` (`sms_server_id`),   
  KEY `order_datetime` (`order_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mt_template` (
  `mt_template_id` int(10) unsigned NOT NULL auto_increment,
  `template_type` tinyint(3) unsigned NOT NULL default '1',
  `language` varchar(6) default NULL,
  `template` varchar(255) NOT NULL,
  `operator_id` int(10) unsigned default NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',    
  PRIMARY KEY  (`mt_template_id`),
  KEY `language` (`language`(2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `menu_sort` (
  `menu_sort_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `index` smallint(6) NOT NULL default '1',
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`menu_sort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `menu` (
  `menu_id` int(10) unsigned NOT NULL auto_increment,
  `url` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `index` smallint(6) NOT NULL default '1',
  `menu_sort_id` int(10) unsigned NOT NULL,
  `permission`  varchar(100) default NULL,
  `create_date` date NOT NULL default '0000-00-00',
  `create_time` time NOT NULL default '00:00:00',
  `last_modify_date` date NOT NULL default '0000-00-00',
  `last_modify_time` time NOT NULL default '00:00:00',
  PRIMARY KEY  (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--------------操作员与角色------------------;
insert into organization values(1, 0, 'company', '公司', 1, now(), now(), now(), now());
insert into operator values(1, 'lude', 'lude', 'lude', 1, 11111111111, 1, now(), now(), now(), now());
insert into role values(1, '管理员', 1, '可以进行所有操作', now(), now(), now(),now());
insert into operator_role values(1, 1, 1, 1, now(), now(), now(), now());


--------------菜单------------------;
delete from menu;
delete from menu_sort;
insert into menu_sort values(1, '用户管理', 1, now(), now(), now(), now());
insert into menu_sort values(2, '系统管理', 2, now(), now(), now(), now());
insert into menu_sort values(3, '发送短信', 3, now(), now(), now(), now());
insert into menu_sort values(4, '查询短信', 4, now(), now(), now(), now());
insert into menu values(1, 'organizationQuery.do', '机构管理', 1, 1, 'organizationQuery', now(), now(), now(), now());
insert into menu values(2, 'roleQuery.do', '角色管理', 2, 1, 'roleQuery', now(), now(), now(), now());
insert into menu values(3, 'operatorQuery.do', '操作员管理', 3, 1, 'operatorQuery', now(), now(), now(), now());
insert into menu values(4, 'agentQuery.do', '运营商管理', 1, 2, 'agentQuery', now(), now(), now(), now());
insert into menu values(5, 'channelQuery.do', '渠道管理', 2, 2, 'channelQuery', now(), now(), now(), now());
insert into menu values(6, 'smsServerQuery.do', '服务器管理', 3, 2, 'smsServerQuery', now(), now(), now(), now());
insert into menu values(7, 'simpleMt.do', '简单短信', 1, 3, 'simpleMt', now(), now(), now(), now());
insert into menu values(8, 'mtQuery.do', '简单查询', 1, 4, 'mtQuery', now(), now(), now(), now());



--------------权限------------------;
delete from permission;
delete from permission_sort;
insert into permission_sort values(1, '角色管理', 1, now(), now(), now(), now());
insert into permission_sort values(2, '操作员管理', 2, now(), now(), now(), now());
insert into permission_sort values(3, '运营商管理', 3, now(), now(), now(), now());
insert into permission_sort values(4, '渠道管理', 4, now(), now(), now(), now());
insert into permission_sort values(5, '服务器管理', 5, now(), now(), now(), now());
insert into permission_sort values(6, '机构管理', 6, now(), now(), now(), now());
insert into permission_sort values(7, '发送短信', 7, now(), now(), now(), now());
insert into permission_sort values(8, '查询短信', 8, now(), now(), now(), now());


--------------角色------------------;
insert into permission values(1, 'roleAdd', '添加角色', 1, 1, now(), now(), now(), now());
insert into permission values(2, 'roleRemove', '删除角色', 2, 1, now(), now(), now(), now());
insert into permission values(3, 'roleQuery', '查询角色', 3, 1, now(), now(), now(), now());
insert into permission values(4, 'roleEdit', '编辑角色', 4, 1, now(), now(), now(), now());
insert into permission values(5, 'rolePermissionEdit', '编辑角色权限', 5, 1, now(), now(), now(), now());

insert into role_permission values(1001, 1, 1, 1, now(), now(), now(), now());
insert into role_permission values(1002, 1, 2, 1, now(), now(), now(), now());
insert into role_permission values(1003, 1, 3, 1, now(), now(), now(), now());
insert into role_permission values(1004, 1, 4, 1, now(), now(), now(), now());
insert into role_permission values(1005, 1, 5, 1, now(), now(), now(), now());


--------------操作员------------------;
insert into permission values(11, 'operatorAdd', '添加操作员', 1, 2, now(), now(), now(), now());
insert into permission values(12, 'operatorRemove', '删除操作员', 2, 2, now(), now(), now(), now());
insert into permission values(13, 'operatorQuery', '查询操作员', 3, 2, now(), now(), now(), now());
insert into permission values(14, 'operatorEdit', '编辑操作员', 4, 2, now(), now(), now(), now());
insert into permission values(15, 'operatorRoleEdit', '编辑操作员角色', 5, 2, now(), now(), now(), now());

insert into role_permission values(1011, 1, 11, 1, now(), now(), now(), now());
insert into role_permission values(1012, 1, 12, 1, now(), now(), now(), now());
insert into role_permission values(1013, 1, 13, 1, now(), now(), now(), now());
insert into role_permission values(1014, 1, 14, 1, now(), now(), now(), now());
insert into role_permission values(1015, 1, 15, 1, now(), now(), now(), now());


--------------运营商------------------;
insert into permission values(21, 'agentAdd', '添加运营商', 1, 3, now(), now(), now(), now());
insert into permission values(22, 'agentRemove', '删除运营商', 2, 3, now(), now(), now(), now());
insert into permission values(23, 'agentQuery', '查询运营商', 3, 3, now(), now(), now(), now());
insert into permission values(24, 'agentEdit', '编辑运营商', 4, 3, now(), now(), now(), now());

insert into role_permission values(1021, 1, 21, 1, now(), now(), now(), now());
insert into role_permission values(1022, 1, 22, 1, now(), now(), now(), now());
insert into role_permission values(1023, 1, 23, 1, now(), now(), now(), now());
insert into role_permission values(1024, 1, 24, 1, now(), now(), now(), now());


--------------渠道------------------;
insert into permission values(31, 'channelAdd', '添加渠道', 1, 4, now(), now(), now(), now());
insert into permission values(32, 'channelRemove', '删除渠道', 2, 4, now(), now(), now(), now());
insert into permission values(33, 'channelQuery', '查询渠道', 3, 4, now(), now(), now(), now());
insert into permission values(34, 'channelEdit', '编辑渠道', 4, 4, now(), now(), now(), now());

insert into role_permission values(1031, 1, 31, 1, now(), now(), now(), now());
insert into role_permission values(1032, 1, 32, 1, now(), now(), now(), now());
insert into role_permission values(1033, 1, 33, 1, now(), now(), now(), now());
insert into role_permission values(1034, 1, 34, 1, now(), now(), now(), now());


--------------服务器------------------;
insert into permission values(41, 'smsServerAdd', '添加渠道', 1, 5, now(), now(), now(), now());
insert into permission values(42, 'smsServerRemove', '删除渠道', 2, 5, now(), now(), now(), now());
insert into permission values(43, 'smsServerQuery', '查询渠道', 3, 5, now(), now(), now(), now());

insert into role_permission values(1041, 1, 41, 1, now(), now(), now(), now());
insert into role_permission values(1042, 1, 42, 1, now(), now(), now(), now());
insert into role_permission values(1043, 1, 43, 1, now(), now(), now(), now());


--------------机构------------------;
insert into permission values(51, 'organizationAdd', '添加机构', 1, 6, now(), now(), now(), now());
insert into permission values(52, 'organizationRemove', '删除机构', 2, 6, now(), now(), now(), now());
insert into permission values(53, 'organizationQuery', '查询机构', 3, 6, now(), now(), now(), now());
insert into permission values(54, 'organizationEdit', '编辑机构', 4, 6, now(), now(), now(), now());

insert into role_permission values(1051, 1, 51, 1, now(), now(), now(), now());
insert into role_permission values(1052, 1, 52, 1, now(), now(), now(), now());
insert into role_permission values(1053, 1, 53, 1, now(), now(), now(), now());
insert into role_permission values(1054, 1, 54, 1, now(), now(), now(), now());


--------------发送短信------------------;
insert into permission values(61, 'simpleMt', '简单短信', 1, 7, now(), now(), now(), now());
insert into role_permission values(1061, 1, 61, 1, now(), now(), now(), now());


--------------查询短信------------------;
insert into permission values(71, 'mtQuery', '简单查询', 1, 8, now(), now(), now(), now());
insert into role_permission values(1071, 1, 71, 1, now(), now(), now(), now());





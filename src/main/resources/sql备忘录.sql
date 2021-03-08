CREATE TABLE `user` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	username VARCHAR(20) NOT NULL COMMENT '用户名',
	password VARCHAR(100) NOT NULL COMMENT '密码',
	salt VARCHAR(30) NOT NULL COMMENT '盐值',
	register_id INT(10) DEFAULT NULL COMMENT '花名册id',
	remark VARCHAR(20) DEFAULT NULL COMMENT '备注',
	is_enable TINYINT(2) DEFAULT 1 COMMENT '是否开启：0-否，1-是',
	is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录用户表';

CREATE TABLE `user_role` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	user_id INT(10) NOT NULL COMMENT '登录用户id',
	role_id INT(10) NOT NULL COMMENT '角色id',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';

CREATE TABLE `role` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	role_name VARCHAR(20) NOT NULL COMMENT '角色名称',
	remark VARCHAR(20) DEFAULT NULL COMMENT '备注',
	is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

CREATE TABLE `role_permission` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	role_id INT(10) NOT NULL COMMENT '角色id',
	permission_id INT(10) NOT NULL COMMENT '权限id',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

CREATE TABLE `permission` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	permission_name VARCHAR(20) NOT NULL COMMENT '权限名称',
	permission_name_en  VARCHAR(50) NOT NULL COMMENT '权限名称英文',
	is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
	remark VARCAHR(20) DEFAULT NULL COMMENT '备注',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

CREATE TABLE `insider_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(10) NOT NULL COMMENT '姓名',
    id_card VARCHAR(20) NOT NULL COMMENT '身份证号码',
    phone VARCHAR(20) NOT NULL COMMENT '手机号码',
    group_name VARCHAR(20) NOT NULL COMMENT '所属连队',
    position VARCHAR(20) NOT NULL COMMENT '职位',
    superior VARCHAR(20) NOT NULL COMMENT '直属上级',
    image_uri VARCHAR(100) NOT NULL COMMENT '个人照片',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营区内部人员表';

CREATE TABLE `dependant_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    couple_name VARCHAR(20) NOT NULL COMMENT '配偶姓名',
    institution VARCHAR(20) NOT NULL COMMENT '工作单位',
    image_uri VARCHAR(100) NOT NULL COMMENT '个人照片',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营区家属人员表';

CREATE TABLE `user` (
	id INT(10) AUTO_INCREMENT COMMENT '自增id',
	username VARCHAR(20) NOT NULL COMMENT '用户名',
	password VARCHAR(100) NOT NULL COMMENT '密码',
	salt VARCHAR(30) NOT NULL COMMENT '盐值',
	register_id INT(10) DEFAULT NULL COMMENT '花名册id',
	remark VARCHAR(20) DEFAULT NULL COMMENT '备注',
	is_enable TINYINT(2) DEFAULT 0 COMMENT '是否开启：0-否，1-是',
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
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    id_card VARCHAR(20) NOT NULL COMMENT '身份证号码',
    phone VARCHAR(20) NOT NULL COMMENT '手机号码',
    group_name VARCHAR(20) NOT NULL COMMENT '所属连队',
    position VARCHAR(20) NOT NULL COMMENT '职位',
    superior VARCHAR(20) NOT NULL COMMENT '直属上级',
    image_uri VARCHAR(100) NOT NULL COMMENT '个人照片',
    is_account TINYINT(2) DEFAULT 0 COMMENT '是否拥有后台账号：0-否，1-是',
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

CREATE TABLE `duty_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    group_name VARCHAR(20) NOT NULL COMMENT '所属连队',
    position VARCHAR(20) NOT NULL COMMENT '职位',
    start_time DATETIME NOT NULL COMMENT '值班开始时间',
    end_time DATETIME NOT NULL COMMENT '值班结束时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='值班信息表';

CREATE TABLE `message_polling_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    title VARCHAR(20) NOT NULL COMMENT '标题',
    content TEXT DEFAULT NULL COMMENT '内容',
    is_polling TINYINT(2) DEFAULT 0 COMMENT '是否轮询：0-否，1-是',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息轮询信息表';

CREATE TABLE `attachment_info` (
    id INT(10) AUTO_INCREMENT COMMENT '附件id',
    original_name VARCHAR(100) NOT NULL COMMENT '附件原名',
    encode_name VARCHAR(100) NOT NULL COMMENT '加密名字',
    mapping_path VARCHAR(100) NOT NULL COMMENT '映射路径',
    remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '上传时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上传附件信息表';

CREATE TABLE `emergency_solution_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    title VARCHAR(20) NOT NULL COMMENT '标题',
    description VARCHAR(200) DEFAULT NULL COMMENT '概述',
    solution TEXT DEFAULT NULL COMMENT '处理办法',
    attachment_ids VARCHAR(200) DEFAULT NULL COMMENT '附件id',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急预案信息表';

CREATE TABLE `vacation_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    reason VARCHAR(200) DEFAULT NULL COMMENT '请假原因',
    leave_status TINYINT(2) DEFAULT 0 COMMENT '离营状态：0-未离营，1-已离营',
    cancel_vacation_status TINYINT(2) DEFAULT 0 COMMENT '销假状态：0-未销假，1-已销假',
    cancel_vacation_time DATETIME DEFAULT NULL COMMENT '销假时间',
    approver VARCHAR(20) DEFAULT NULL COMMENT '批准人',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假信息表';

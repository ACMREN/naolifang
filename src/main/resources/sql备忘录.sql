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
	permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
	permission_name_en  VARCHAR(50) NOT NULL COMMENT '权限名称英文',
	is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
	remark VARCHAR(20) DEFAULT NULL COMMENT '备注',
	create_time DATETIME NOT NULL COMMENT '创建时间',
	update_time DATETIME NOT NULL COMMENT '更新时间',
	PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

CREATE TABLE `insider_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(10) NOT NULL COMMENT '姓名',
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    id_card VARCHAR(20) DEFAULT NULL COMMENT '身份证号码',
    rank_num VARCHAR(20) DEFAULT NULL COMMENT '军衔号码',
    phone VARCHAR(20) NOT NULL COMMENT '手机号码',
    department VARCHAR(20) NOT NULL COMMENT '部门',
    nick_name VARCHAR(20) NOT NULL COMMENT '称谓',
    rank_name VARCHAR(20) DEFAULT NULL COMMENT '军衔',
    address VARCHAR(200) DEFAULT NULL COMMENT '住址',
    image_uri VARCHAR(100) DEFAULT NULL COMMENT '个人照片',
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
    relation VARCHAR(20) NOT NULL COMMENT '关联人员',
    relationship VARCHAR(20) NOT NULL COMMENT '关联关系',
    institution VARCHAR(20) NOT NULL COMMENT '工作单位',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    address VARCHAR(200) DEFAULT NULL COMMENT '住址',
    image_uri VARCHAR(100) NOT NULL COMMENT '个人照片',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='营区家属人员表';

CREATE TABLE `duty_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    insider_id INT(20) NOT NULL COMMENT '内部人员id',
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender TINYINT(2) DEFAULT 0 COMMENT '性别',
    group_name VARCHAR(20) DEFAULT NULL COMMENT '所属连队',
    position VARCHAR(20) DEFAULT NULL COMMENT '职位',
    nick_name VARCHAR(20) DEFAULT NULL COMMENT '称谓',
    start_time DATETIME NOT NULL COMMENT '值班开始时间',
    end_time DATETIME NOT NULL COMMENT '值班结束时间',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
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

CREATE TABLE `device_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    name VARCHAR(20) DEFAULT NULL COMMENT '设备名字',
    type TINYINT(5) DEFAULT 0 COMMENT '设备类型',
    index_code VARCHAR(50) NOT NULL COMMENT '设备编号',
    ip VARCHAR(20) NOT NULL COMMENT 'ip地址',
    manufacturer VARCHAR(20) DEFAULT NULL COMMENT '厂商',
    position VARCHAR(20) DEFAULT NULL COMMENT '位置',
    position_info VARCHAR(200) DEFAULT NULL COMMENT '位置信息',
    region TINYINT(2) DEFAULT NULL COMMENT '区域',
    maintain_person VARCHAR(20) DEFAULT NULL COMMENT '维护人员',
    status TINYINT(2) DEFAULT 0 COMMENT '状态：0-离线，1-在线，2-已禁用，3-未激活',
    door_status TINYINT(2) DEFAULT 0 COMMENT '门禁开关状态：0-正常开关，1-常关，2-常闭',
    connect_time DATETIME DEFAULT NULL COMMENT '上线时间',
    live_time BIGINT(20) DEFAULT NULL COMMENT '生命周期',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

CREATE TABLE `alarm_event_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    alarm_type TINYINT(2) DEFAULT 0 COMMENT '告警类型：0-设备告警，1-事件告警',
    content VARCHAR(20) NOT NULL COMMENT '告警内容',
    alarm_level TINYINT(5) DEFAULT NULL COMMENT '告警级别',
    event_id VARCHAR(30) NOT NULL COMMENT '事件标识',
    device_id INT(10) NOT NULL COMMENT '设备id',
    fix_person VARCHAR(10) DEFAULT NULL COMMENT '维修人员',
    status TINYINT(2) NOT NULL COMMENT '处理状态',
    handle_person VARCHAR(10) DEFAULT NULL COMMENT '处理人员',
    alarm_time DATETIME DEFAULT NULL COMMENT '告警时间',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handle_content TEXT DEFAULT NULL COMMENT '处理内容',
    happen_start_time VARCHAR(30) DEFAULT NULL COMMENT '事件发生开始时间',
    happen_end_time VARCHAR(30) DEFAULT NULL COMMENT '事件发生结束时间',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件告警信息表';

CREATE TABLE `alarm_level_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    alarm_level TINYINT(2) DEFAULT 0 COMMENT '告警级别：0-一般。1-严重，2-非常严重',
    region VARCHAR(20) DEFAULT NULL COMMENT '区域：0-防护区，1-监控区，2-限制区',
    alarm_type TINYINT(2) DEFAULT 0 COMMENT '告警类型：0-设备告警，1-事件告警',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警等级信息表';

CREATE TABLE `alarm_malfunction_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    alarm_level_id INT(10) NOT NULL COMMENT '告警级别id',
    malfunction_type BIGINT(20) DEFAULT 0 COMMENT '故障类型：0-离线，其它参照海康',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警级别和类型关系表';

CREATE TABLE `visitor_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    order_id VARCHAR(50) DEFAULT NULL COMMENT '访客唯一id',
    name VARCHAR(20) NOT NULL COMMENT '访客姓名',
    id_card VARCHAR(20) NOT NULL COMMENT '身份证号码',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    visit_person VARCHAR(20) NOT NULL COMMENT '访问对象',
    reason VARCHAR(200) DEFAULT NULL COMMENT '访问理由',
    visit_start_time DATETIME NOT NULL COMMENT '访问开始时间',
    visit_end_time DATETIME NOT NULL COMMENT '访问结束时间',
    leave_time DATETIME DEFAULT NULL COMMENT '签离时间',
    original_place VARCHAR(20) DEFAULT NULL COMMENT '出发地',
    status TINYINT(2) DEFAULT 0 COMMENT '到访状态',
    image_uri TEXT DEFAULT NULL COMMENT 'base64图片流',
    is_delete TINYINT(2) DEFAULT 0 COMMENT '是否已经删除：0-否，1-是',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客信息表';

CREATE TABLE `sign_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    visitor_id INT(10) NOT NULL COMMENT '访客id',
    temperature VARCHAR(10) DEFAULT NULL COMMENT '体温',
    health_code VARCHAR(10) DEFAULT NULL COMMENT '健康码颜色',
    original_place VARCHAR(20) DEFAULT NULL COMMENT '出发地',
    check_time DATE DEFAULT NULL COMMENT '核酸检测时间',
    check_hospital VARCHAR(100) DEFAULT NULL COMMENT '检测医院',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客签入信息表';

CREATE TABLE `face_info` (
    id INT(10) AUTO_INCREMENT COMMENT '自增id',
    photo_url VARCHAR(500) NOT NULL COMMENT '人脸图片',
    name VARCHAR(10) DEFAULT NULL COMMENT '姓名',
    id_card VARCHAR(20) DEFAULT NULL COMMENT '身份证号码',
    PRIMARY KEY(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人脸图库信息表';
INSERT INTO DS_COMMON_SYSTEM (ID, NAME, ALIAS, PASSWORD, DOMAINURL, ROOTURL, MENUURL, STATUS, SEQ) VALUES 
(0, '测试单机系统', NULL, NULL, NULL, NULL, NULL, 1, 0),
(1, '统一认证平台', 'DsCommon', '1', '', '/DsCommon', '/DsCommon/menu.jsp', 1, 1),
(2, '门户', 'portal', '1', '', '/portal', '/portal/menu.jsp', 1, 2);

INSERT INTO DS_COMMON_ROLE (ID, PID, SYSTEMID, NAME, SEQ, MEMO) VALUES 
(10, NULL, 0, '超级管理员', 0, ''),
(21, NULL, 1, '超级管理员', 0, ''),
(22, NULL, 1, '门户管理', 0, ''),
(23, NULL, 1, '业务管理', 0, ''),
(24, NULL, 1, '用户管理', 0, '');

INSERT INTO DS_COMMON_ORG (ID, PID, NAME, STATUS, SEQ, DUTYSCOPE, MEMO) VALUES 
(31, NULL, '内置管理单位', 2, 0, '', ''),
(32, 31, '内置管理部门', 1, 0, '', ''),
(33, 32, '内置管理岗位', 0, 0, '', '');

INSERT INTO DS_COMMON_ORGROLE (ID, ORGID, ROLEID) VALUES 
(38, 33, 21);

INSERT INTO DS_COMMON_USERTYPE (ID, NAME, ALIAS, STATUS, SEQ, MEMO, RESOURCES) VALUES
(1, '管理用户', LOWER('0'), 1, 1, '', ''),
(2, '个人用户', LOWER('1'), 1, 2, '', ''),
(3, '企业用户', LOWER('2'), 1, 3, '', '1|企业管理员
0|普通用户');

INSERT INTO DS_COMMON_USER (ID, ACCOUNT, PASSWORD, NAME, STATUS, WORKCARD, CREATETIME, ORGPID, ORGID, TYPE, TYPENAME) VALUES 
(-100000, 'root',  '670B14728AD9902AECBA32E22FA4F6BD', '超级管理员', 1,      '', '0000-00-00 00:00:00', 31, 32, '0', '管理用户'),
(-1,     'admin',  '670B14728AD9902AECBA32E22FA4F6BD', '系统管理员', 1, 'admin', '0000-00-00 00:00:00', 31, 32, '0', '管理用户'),
(2,   'useradmin', '670B14728AD9902AECBA32E22FA4F6BD', '用户管理员', 1,      '', '0000-00-00 00:00:00', 31, 32, '0', '管理用户');

INSERT INTO DS_COMMON_USERORG (ID, ORGID, USERID) VALUES 
(1, 33, -100000),
(2, 33, -1),
(3, 33, 2);



SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS DS_FLOW_PI_DATA;
DROP TABLE IF EXISTS DS_FLOW_PI_WAITING;
DROP TABLE IF EXISTS DS_FLOW_PI;
DROP TABLE IF EXISTS DS_FLOW_TASK;
DROP TABLE IF EXISTS DS_FLOW;
DROP TABLE IF EXISTS DS_FLOW_CATEGORY;
-- DROP TABLE IF EXISTS DS_FLOW_PI_DATA;
-- DROP TABLE IF EXISTS DS_FLOW_PI_WAITING;
-- DROP TABLE IF EXISTS DS_FLOW_PI;
-- DROP TABLE IF EXISTS DS_FLOW_TASK;
-- DROP TABLE IF EXISTS DS_FLOW;
-- DROP TABLE IF EXISTS DS_FLOW_CATEGORY;



CREATE TABLE IF NOT EXISTS DS_FLOW_CATEGORY
(
   ID                   BIGINT(18) NOT NULL COMMENT '分类ID',
   NAME                 VARCHAR(300) COMMENT '分类名称',
   PID                  BIGINT(18) COMMENT '父类',
   SEQ                  BIGINT(18) COMMENT '排序',
   PRIMARY KEY (ID)
) COMMENT '流程分类';
CREATE TABLE IF NOT EXISTS DS_FLOW
(
   ID                   BIGINT(18) NOT NULL COMMENT '主键',
   CATEGORYID           BIGINT(18) COMMENT '分类',
   ALIAS                VARCHAR(64) COMMENT '流程标识',
   VNUM                 INT COMMENT '内部版本0为编辑版本',
   DEPLOYID             VARCHAR(128) COMMENT '流程发布ID，VNUM为0的放最新版本',
   NAME                 VARCHAR(30) COMMENT '名字',
   STATUS               INT(1) COMMENT '状态(1启用,0禁用)',
   DATATABLE            VARCHAR(8000) COMMENT '数据结构',
   FLOWXML              TEXT COMMENT '流程图配置XML',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_FLOW FOREIGN KEY (CATEGORYID)
      REFERENCES DS_FLOW_CATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '流程';
CREATE TABLE IF NOT EXISTS DS_FLOW_TASK
(
   ID                   BIGINT(18) NOT NULL COMMENT '主键',
   FLOWID               BIGINT(18) COMMENT '流程ID',
   DEPLOYID             VARCHAR(128) COMMENT '流程发布ID，当前版本此值为空',
   TALIAS               VARCHAR(64) COMMENT '节点标识(start开始，end结束)',
   TNAME                VARCHAR(30) COMMENT '节点名称',
   TCOUNT               INT(4) COMMENT '合并任务个数(只有一个任务时等于1，其余大于1)',
   TNEXT                VARCHAR(1000) COMMENT '下级任务(以逗号分隔节点标识，以|线分隔分支任务)',
   TUSERS               VARCHAR(1000) COMMENT '当前任务的用户ID(以逗号分隔可选用户，以|线分隔分支任务用户)',
   SUBCOUNT             INT(4) COMMENT '至少合并会签个数(不需要会签时值为-1)',
   SUBUSERS             VARCHAR(1000) COMMENT '当前会签的用户ID(以逗号分隔可选用户，以|线分隔并行用户)',
   TMEMO                VARCHAR(1000) COMMENT '参数',
   DATATABLE            VARCHAR(10000) COMMENT '数据结构',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_FLOW_TASK FOREIGN KEY (FLOWID)
      REFERENCES DS_FLOW (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '流程任务';



CREATE TABLE IF NOT EXISTS DS_FLOW_PI
(
   ID                   BIGINT(18) NOT NULL COMMENT '主键ID(流程实例ID)',
   YWLSH                VARCHAR(128) COMMENT '业务流水号',
   SBLSH                VARCHAR(128) COMMENT '申办流水号',
   ALIAS                VARCHAR(64) COMMENT '流程标识',
   FLOWID               BIGINT(18) COMMENT '流程ID(对应deployid)',
   DEPLOYID             VARCHAR(128) COMMENT '流程发布ID',
   PIDAY                INT COMMENT '时限天数',
   PIDAYTYPE            INT(1) COMMENT '时限天数类型(0日历日,1工作日)',
   PISTART              VARCHAR(19) COMMENT '开始时间',
   PIEND                VARCHAR(19) COMMENT '结束时间',
   PIUPSTART            VARCHAR(19) COMMENT '挂起开始时间',
   PIUPEND              VARCHAR(19) COMMENT '挂起结束时间',
   STATUS               INT(1) COMMENT '流程状态(1申请,2运行,3挂起,0结束)',
   CACCOUNT             VARCHAR(300) COMMENT '承办人账号',
   CNAME                VARCHAR(300) COMMENT '承办人',
   PIALIAS              VARCHAR(1000) COMMENT '当前任务标识，以逗号分隔',
   DATATABLE            TEXT COMMENT '数据结构',
   PRIMARY KEY (ID)
) COMMENT '流程实例';
CREATE TABLE IF NOT EXISTS DS_FLOW_PI_DATA
(
   ID                   BIGINT(18) NOT NULL COMMENT '主键ID',
   PIID                 BIGINT(18) COMMENT '流程实例ID',
   TPREV                VARCHAR(64) COMMENT '上级任务(从哪过来的)',
   TALIAS               VARCHAR(64) COMMENT '任务标识',
   TNAME                VARCHAR(30) COMMENT '任务名称',
   STATUS               INT(1) COMMENT '状态(0已处理,1代办,2挂起,3取消挂起)',
   PACCOUNT             VARCHAR(300) COMMENT '经办人ID',
   PNAME                VARCHAR(300) COMMENT '经办人姓名',
   PTIME                VARCHAR(19) COMMENT '经办时间',
   PTYPE                VARCHAR(300) COMMENT '经办类型(0拒绝,1同意等等)',
   MEMO                 VARCHAR(4000) COMMENT '意见',
   DATATABLE            TEXT COMMENT '数据结构',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_FLOW_PI_DATA FOREIGN KEY (PIID)
      REFERENCES DS_FLOW_PI (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '流程执行明细';
CREATE TABLE IF NOT EXISTS DS_FLOW_PI_WAITING
(
   ID                   BIGINT(18) NOT NULL COMMENT '主键ID',
   PID                  BIGINT(18) NULL COMMENT '外键ID',
   PIID                 BIGINT(18) COMMENT '实例ID',
   YWLSH                VARCHAR(128) COMMENT '业务流水号',
   SBLSH                VARCHAR(128) BINARY COMMENT '申办流水号',
   FLOWID               BIGINT(18) COMMENT '流程ID',
   FLOWNAME             VARCHAR(30) COMMENT '流程名称',
   TALIAS               VARCHAR(64) COMMENT '任务标识',
   TNAME                VARCHAR(300) COMMENT '任务名称',
   TCOUNT               INT COMMENT '合并任务个数(只有一个任务时等于1，其余大于1)',
   TPREV                VARCHAR(64) COMMENT '上级任务(从哪过来的)',
   TNEXT                VARCHAR(1000) COMMENT '下级任务(以逗号分隔节点标识，以|线分隔分支任务)',
   TSTART               VARCHAR(19) COMMENT '任务开始时间',
   TUSER                VARCHAR(1000) COMMENT '经办人',
   TUSERS               VARCHAR(1000) COMMENT '候选经办人',
   SUBCOUNT             INT(4) COMMENT '至少合并会签个数(不需要会签时值为-1)',
   SUBUSERS             VARCHAR(1000) COMMENT '当前会签的用户ID(以逗号分隔可选用户，以|线分隔并行用户)',
   TMEMO                VARCHAR(1000) COMMENT '参数',
   DATATABLE            TEXT COMMENT '数据结构',
   PRIMARY KEY (ID),
   CONSTRAINT FK_DS_FLOW_PI_DOING FOREIGN KEY (PIID)
      REFERENCES DS_FLOW_PI (ID) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_DS_FLOW_PI_WAITING FOREIGN KEY (PID)
      REFERENCES DS_FLOW_PI_WAITING (ID) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '流程待办事项';



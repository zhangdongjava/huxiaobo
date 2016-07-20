set @GenealogyTables = 10;
set @GenealogyMapTables = 10;
set @MemberTables = 100;
set @MemberExtendTables = 10;
set @MemberRightTables = 10;
set @FamilyTables = 100;
set @WealthTables = 10;
set @BillTables = 12;
set @TopicTables = 100;
set @SubjectTables = 100;
set @PostTables = 100;
set @FriendTables = 100;
set @FriendApplyTables = 12;
set @PartnerTables = 10;
set @OperationLogTables = 12;

drop procedure if exists pr_create_table;
delimiter //
create procedure pr_create_table(in name char(20), in num int, in fields varchar(1000))
begin
  declare v_name varchar(30);
  declare v_sql varchar(1100);
  declare v_i int;
  set v_i = 0;
  while v_i < num do
    set v_name = convert(v_i, char);
    set v_name = lpad(v_name, 2, '0');
    set v_name = concat(name, '_', v_name);

    set v_sql = concat('drop table if exists ', v_name);
    set @v_sql = v_sql;
    prepare stmt from @v_sql;
    execute stmt;
    deallocate prepare stmt;
    
    set v_sql = concat('create table ', v_name, fields);
    set @v_sql = v_sql;
    prepare stmt from @v_sql;
    execute stmt;
    deallocate prepare stmt;
    set v_i = v_i + 1;
  end while;
end;
//
delimiter ;

drop procedure if exists pr_create_index;
delimiter //
create procedure pr_create_index(in tablename varchar(30), in idxname varchar(30), in ukd bool, in num int, in fields varchar(255))
begin
  declare v_table_name varchar(40);
  declare v_idx_name varchar(50);
  declare v_sql varchar(300);
  declare v_i int;
  set v_i = 0;
  while v_i < num do
    set v_table_name = convert(v_i, char);
    set v_table_name = lpad(v_table_name, 2, '0');
    set v_idx_name = concat(idxname, '_', v_table_name);
    set v_table_name = concat(tablename, '_', v_table_name);

    if ukd then
        set v_sql = concat('create unique index ', v_idx_name, ' on ', v_table_name, fields);
    else
        set v_sql = concat('create index ', v_idx_name, ' on ', v_table_name, fields);
    end if;
    set @v_sql = v_sql;
    prepare stmt from @v_sql;
    execute stmt;
    deallocate prepare stmt;
    set v_i = v_i + 1;
  end while;
end;
//
delimiter ;

drop table if exists t_user;
CREATE TABLE `t_user`
(
  id              BIGINT        PRIMARY KEY,
  account         VARCHAR(30)   NULL,
  password        VARCHAR(50)   NULL,
  payPassword     VARCHAR(50)   NULL,
  areacode        VARCHAR(10)   NULL,
  phone           VARCHAR(20)   NULL,
  surname         VARCHAR(10)   NULL,
  name            VARCHAR(10)   NULL,
  nickname        varchar(50)   NULL,
  thumbnail       varchar(200)  NULL,
  generation      VARCHAR(20)   NULL,
  sexy            INT           NULL,
  rank            INT           NULL,
  sign			  VARCHAR(150)  NULL,
  address         VARCHAR(100)  NULL,
  qq              VARCHAR(30)   NULL,
  weixin          VARCHAR(50)   NULL,
  lastLoginTime   DATETIME      NULL,
  createTime      DATETIME      NOT NULL
);

CREATE UNIQUE INDEX t_user_idx1 ON t_user
(
  account
);

CREATE UNIQUE INDEX t_user_idx2 ON t_user
(
  phone
);

call pr_create_table('t_partner', @PartnerTables, '(
  id              BIGINT        PRIMARY KEY,
  userId          BIGINT        NOT NULL,     -- split table
  partnerId       VARCHAR(50)   NOT NULL,
  partnerPass	  VARCHAR(50)   NULL,
  type            INT           NOT NULL,
  description     VARCHAR(100)  NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_partner', 't_partner_idx1', true, @PartnerTables, '(
  userId,
  type
)');

call pr_create_table('t_genealogy', @GenealogyTables, '(
  id              BIGINT        PRIMARY KEY,
  creatorId       BIGINT        NOT NULL,     -- split table
  centerId        BIGINT        NULL,
  name            VARCHAR(50)   NULL,
  surname         VARCHAR(10)   NULL,
  thumbnail       VARCHAR(200)  NULL,
  introduce       VARCHAR(200)  NULL,
  generationName  varchar(20)   NULL,
  type            INT           NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_genealogy', 't_genealogy_idx1', false, @GenealogyTables, '(
  creatorId
)');

call pr_create_table('t_genealogy_map', @GenealogyMapTables, '(
  userId          BIGINT        NOT NULL,     -- split table
  genealogyId     BIGINT        NOT NULL,
  creatorId       BIGINT        NOT NULL,
  centerId        BIGINT        NULL,
  type            INT           NOT NULL,
  PRIMARY KEY(userId, genealogyId)
)');

call pr_create_table('t_member', @MemberTables, '(
  id              BIGINT        PRIMARY KEY,
  creatorId       BIGINT        NOT NULL,     -- split table
  userId          BIGINT        NULL,  
  surname         VARCHAR(10)   NULL,
  name            VARCHAR(10)   NULL,
  nickname        varchar(50)   NULL,
  thumbnail       varchar(200)  NULL,
  generation      varchar(20)   NULL,
  generationName  varchar(20)   NULL,
  sexy            INT           NULL,
  seniority		  INT			NULL,
  phone	          VARCHAR(30)   NULL,
  birthday        VARCHAR(30)   NULL,
  memorialDay     VARCHAR(30)   NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_member', 't_member_idx1', false, @MemberTables, '(
  creatorId
)');

call pr_create_index('t_member', 't_member_idx2', false, @MemberTables, '(
  userId
)');

call pr_create_table('t_member_extend', @MemberExtendTables, '(
  id              BIGINT        PRIMARY KEY,
  memberId        BIGINT        NOT NULL,     -- split table
  groupId         BIGINT        NULL,
  groupPosition   INT           NULL,
  name            VARCHAR(20)   NULL,
  value           VARCHAR(100)  NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_member_extend', 't_member_extend_idx1', false, @MemberExtendTables, '(
  memberId
)');

call pr_create_index('t_member_extend', 't_member_extend_idx2', false, @MemberExtendTables, '(
  groupId
)');

call pr_create_table('t_member_right', @MemberRightTables, '(
  id              BIGINT        PRIMARY KEY,
  memberId        BIGINT        NOT NULL,     -- split table
  rightType       INT           NOT NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_member_right', 't_member_right_idx1', false, @MemberRightTables, '(
  memberId
)');

call pr_create_table('t_family', @FamilyTables, '(
  id              BIGINT        PRIMARY KEY,
  genealogyId     BIGINT        NOT NULL,     -- split table
  genealogyCreatorId BIGINT     NOT NULL,
  fatherId        BIGINT        NULL,
  fatherCreatorId BIGINT        NULL,
  fatherType      INT           NULL,
  motherId        BIGINT        NULL,
  motherCreatorId BIGINT        NULL,
  motherType      INT           NULL,
  householder	  BIGINT        NULL,
  childId         BIGINT        NULL,
  childCreatorId  BIGINT        NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_family', 't_family_idx1', false, @FamilyTables, '(
  genealogyId
)');

call pr_create_index('t_family', 't_family_idx2', false, @FamilyTables, '(
  fatherId
)');

call pr_create_index('t_family', 't_family_idx3', false, @FamilyTables, '(
  motherId
)');

call pr_create_index('t_family', 't_family_idx4', false, @FamilyTables, '(
  childId
)');

call pr_create_index('t_family', 't_family_idx5', true, @FamilyTables, '(
  fatherId,
  fatherType,
  motherId,
  motherType,
  childId
)');

call pr_create_table('t_wealth', @WealthTables, '(
  id              BIGINT        PRIMARY KEY,
  userId          BIGINT        NOT NULL,     -- split table
  type            INT           NOT NULL,
  identity        VARCHAR(30)   NULL,
  value           BIGINT        NOT NULL,
  validateTime    DATETIME      NULL,
  description     VARCHAR(100)  NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_wealth', 't_wealth_idx1', true, @WealthTables, '(
  userId,
  type,
  identity
)');

call pr_create_table('t_bill', @BillTables, '(
  id              BIGINT        PRIMARY KEY,
  srcUserId       BIGINT        NOT NULL,
  srcWealthId     BIGINT        NULL,
  dstUserId       BIGINT        NOT NULL,
  dstWealthId     BIGINT        NULL,
  type            INT           NOT NULL,
  value           BIGINT        NULL,
  description     VARCHAR(100)  NULL,
  createTime      DATETIME      NOT NULL      -- split table
)');

call pr_create_index('t_bill', 't_bill_idx1', false, @BillTables, '(
  srcUserId,
  type
)');

call pr_create_index('t_bill', 't_bill_idx2', false, @BillTables, '(
  srcWealthId
)');

call pr_create_index('t_bill', 't_bill_idx3', false, @BillTables, '(
  dstUserId,
  type
)');

call pr_create_index('t_bill', 't_bill_idx4', false, @BillTables, '(
  dstWealthId
)');

call pr_create_table('t_topic', @TopicTables, '(
  id              BIGINT        PRIMARY KEY,
  refId           BIGINT        NOT NULL,     -- split table
  refTableSeq     BIGINT        NULL,
  type            INT           NOT NULL,
  superiorId      BIGINT        NULL,
  title           VARCHAR(100)  NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_topic', 't_topic_idx1', false, @TopicTables, '(
  refId,
  type
)');

call pr_create_index('t_topic', 't_topic_idx2', false, @TopicTables, '(
  superiorId
)');

call pr_create_table('t_subject', @SubjectTables, '(
  id              BIGINT        PRIMARY KEY,
  topicId         BIGINT        NOT NULL,
  refId           BIGINT        NOT NULL,     -- split table
  userId          BIGINT        NOT NULL,
  title           VARCHAR(100)  NULL,
  contentType     INT           NOT NULL,
  content         VARCHAR(300)  NOT NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_subject', 't_subject_idx1', false, @SubjectTables, '(
  topicId
)');

call pr_create_index('t_subject', 't_subject_idx2', false, @SubjectTables, '(
  refId
)');

call pr_create_index('t_subject', 't_subject_idx3', false, @SubjectTables, '(
  userId
)');

call pr_create_table('t_post', @PostTables, '(
  id              BIGINT        PRIMARY KEY,
  topicId         BIGINT        NOT NULL,
  refId           BIGINT        NOT NULL,     -- split table
  userId          BIGINT        NOT NULL,
  subjectId       BIGINT        NOT NULL,
  contentType     INT           NOT NULL,
  content         VARCHAR(300)  NOT NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_post', 't_post_idx1', false, @PostTables, '(
  topicId
)');

call pr_create_index('t_post', 't_post_idx2', false, @PostTables, '(
  refId
)');

call pr_create_index('t_post', 't_post_idx3', false, @PostTables, '(
  userId
)');

call pr_create_index('t_post', 't_post_idx4', false, @PostTables, '(
  subjectId
)');

call pr_create_table('t_friend', @FriendTables, '(
  id              BIGINT        PRIMARY KEY,
  userId          BIGINT        NOT NULL,     -- split table
  friendId        BIGINT        NOT NULL,
  type            INT           NULL,
  nickname        VARCHAR(50)   NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_friend', 't_friend_idx1', true, @FriendTables, '(
  userId,
  friendId
)');
call pr_create_table('t_friend_apply', @FriendApplyTables, '(
  id              BIGINT        PRIMARY KEY,
  userId          BIGINT        NOT NULL,     -- split table
  friendId        BIGINT        NOT NULL,
  type            INT           NULL,
  state           INT           NULL,
  content         VARCHAR(50)   NULL,
  reply           VARCHAR(50)   NULL,
  replyTime       DATETIME      NULL,
  createTime      DATETIME      NOT NULL
)');
call pr_create_index('t_friend_apply', 't_friend_apply_idx1', true, @FriendApplyTables, '(
  userId,
  friendId
)');
call pr_create_table('t_operation_log', @OperationLogTables, '(
  id              BIGINT        PRIMARY KEY,
  operId          BIGINT        NOT NULL,
  firstId         BIGINT        NULL,     
  secondId        BIGINT        NULL,
  thirdId         BIGINT        NULL,
  logText         VARCHAR(100)  NULL,
  type            INT           NULL,
  result          INT           NULL,
  createTime      DATETIME      NOT NULL
)');

call pr_create_index('t_operation_log', 't_operation_log_idx1', false, @OperationLogTables, '(
  operId
)');

call pr_create_index('t_operation_log', 't_operation_log_idx2', false, @OperationLogTables, '(
  firstId,
  secondId,
  thirdId
)');

drop table if exists t_type_value;
create table t_type_value
(
  id              INT           PRIMARY KEY,
  typeName        VARCHAR(20)   NOT NULL,
  typeTitle       VARCHAR(50)   NULL,
  typeId          INT           NOT NULL,
  typeValue       VARCHAR(30)   NOT NULL,
  description     VARCHAR(100)  NULL
);

CREATE UNIQUE INDEX t_type_value_idx1 ON t_type_value
(
  typeName,
  typeId
);

drop table if exists t_seqno;
create table t_seqno
(
  id              INT           PRIMARY KEY AUTO_INCREMENT,
  tablename       VARCHAR(30)   NOT NULL,
  seqno           BIGINT        NOT NULL
);

CREATE UNIQUE INDEX t_seqno_idx1 ON t_seqno
(
  tablename
);

drop table if exists t_system_param;
create table t_system_param
(
  id             INT           PRIMARY KEY AUTO_INCREMENT,
  name           VARCHAR(20)   NOT NULL,
  value          VARCHAR(100)  NOT NULL,
  description    VARCHAR(200)  NULL
);

create unique index t_system_param_idx1 on t_system_param
(
  name
);

drop table if exists t_error_code;
create table t_error_code
(
	id			int				primary key AUTO_INCREMENT,
	code		int				not null,
	reason		varchar(400)	not null,
	description	varchar(200)	null
);

/**
 * 添加表字段
 */
drop procedure if exists add_table_column;
delimiter //
create procedure add_table_column(in t_name char(20), in num int)
begin
  declare v_name varchar(30);
  declare v_sql varchar(1100);
  declare v_i int;
  set v_i = 0;
  while v_i < num do
    set v_name = convert(v_i, char);
    set v_name = lpad(v_name, 2, '0');
    set v_name = concat(t_name, '_', v_name);

    set v_sql = concat('ALTER TABLE ',v_name,' ADD COLUMN `clanFamily`  bit NOT NULL AFTER `motherType`');
    set @v_sql = v_sql;
    prepare stmt from @v_sql;
    execute stmt;
    set v_i = v_i + 1;
  end while;
end
//
delimiter ;

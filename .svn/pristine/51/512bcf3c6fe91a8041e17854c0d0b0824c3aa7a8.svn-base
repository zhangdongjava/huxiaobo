-- 家谱数据迁移存储过程
drop procedure if exists pr_t_genealogy_data_ransfer;
delimiter //
create procedure pr_t_genealogy_data_ransfer()
begin

	DECLARE creator_id BIGINT;
	DECLARE g_name varchar(100);
	DECLARE g_type int;
	DECLARE create_time varchar(40);
	DECLARE g_id_1 varchar(50);
	DECLARE centerId bigint;
	DECLARE g_centerId varchar(40);
	
	DECLARE v_sql varchar(1100);
	declare v_name varchar(30);
	DECLARE v_i int;
	DECLARE v_g_id BIGINT;

	-- 遍历数据结束标志
  DECLARE done INT DEFAULT TRUE;
	-- 用户家谱数据
	DECLARE genealogyCur CURSOR FOR SELECT
										tf.fu_familytree_id,
										tf.fu_familytree_name,
										1 AS type,
										tu.fu_id AS creatorId,
										tf.fu_familytree_applytime,
										tf.fu_familytree_centermenber_id
									FROM
										findu_database.fu_familytree_table tf,
										findu_database.fu_userfamilytree_table tuf,
										findu_database.fu_user_table tu
									WHERE
										tf.fu_familytree_id = tuf.fu_familytree_id
									AND tuf.fu_user_id = tu.fu_user_id ;

  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;
	-- 设置事务不收到提交

	OPEN genealogyCur;
		-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH genealogyCur INTO g_id_1,g_name,g_type,creator_id,create_time,g_centerId;

		-- 开始循环
		WHILE done do
			-- 根据分表规则，查找相应的表
			set v_i = creator_id % 10;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_genealogy', '_', v_name);
			-- 获取id
			CALL pr_auto_increment_id(v_name,v_g_id);

			call pr_t_member_data_ransfer(g_id_1,v_g_id,creator_id,centerId,g_centerId);

			call pr_t_genealogy_logs_data_ransfer(v_g_id,g_id_1);

			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (id,creatorId,centerId,name,type,createTime) 
				values(',IFNULL(v_g_id,'null'),',',IFNULL(creator_id,'null'),',',IFNULL(centerId,'null'),',',IFNULL(concat('"',g_name,'"'),'null'),',',g_type,',"',create_time,'");');
			
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;

			-- 根据分表规则，查找相应的表
			set v_i = creator_id % 10;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_genealogy_map', '_', v_name);
			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (userId,genealogyId,creatorId,centerId,type) values(',creator_id,',',v_g_id,',',creator_id,',',IFNULL(centerId,'null'),',1);');
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;
			
			-- 导入家庭关系数据
			call pr_t_family_data_transfer(v_g_id,g_id_1);

			-- 提取游标里的数据，这里只有一个，多个	的话也一样；
			FETCH genealogyCur INTO g_id_1,g_name,g_type,creator_id,create_time,g_centerId;
		END WHILE;
  -- 关闭游标
  CLOSE genealogyCur;
end
//
delimiter ;


-- 家谱日志数据迁移存储过程
drop procedure if exists pr_t_genealogy_logs_data_ransfer;
delimiter //
create procedure pr_t_genealogy_logs_data_ransfer(in genealogyId bigint,in genealogyId_1 varchar(50))
begin

	DECLARE logType int;
	DECLARE logText varchar(1000);
	DECLARE result int;
	DECLARE createTime varchar(40);
	DECLARE userId bigint;

	DECLARE v_sql varchar(1100);
	declare v_name varchar(30);
	DECLARE v_i int;
	DECLARE v_logId BIGINT;
	DECLARE v_month int;

	-- 遍历数据结束标志
  DECLARE done INT DEFAULT TRUE;
	-- 用户家谱数据
	declare genealogyLogsCur CURSOR FOR select t.fu_log_type,t.fu_log_comment,t.fu_log_result,t.fu_log_time,tu.fu_id from findu_database.fu_familytree_log_table t
												inner join findu_database.fu_user_table  tu on t.fu_user_id = tu.fu_user_id
												where t.fu_familytree_id = genealogyId_1;

  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;
	-- 设置事务不收到提交

	OPEN genealogyLogsCur;
		-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH genealogyLogsCur INTO logType,logText,result,createTime,userId;

		-- 开始循环
		WHILE done do
			select CAST(date_format(createTime,'%m')  AS UNSIGNED) into v_month;
			-- 根据分表规则，查找相应的表
			set v_i = (v_month - 1) % 12;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_genealogy_logs', '_', v_name);
			-- 获取id
			CALL pr_auto_increment_id(v_name,v_logId);

			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (id,genealogyId,userId,logText,type,result,createTime) 
									values(',v_logId,',',genealogyId,',',userId,',',IFNULL(CONCAT('"',logText,'"'),'null'),',',logType,',',result,',"',createTime,'");');
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;

			-- 提取游标里的数据，这里只有一个，多个	的话也一样；
			FETCH genealogyLogsCur INTO logType,logText,result,createTime,userId;
		END WHILE;
  -- 关闭游标
  CLOSE genealogyLogsCur;
end
//
delimiter ;

-- 成员家庭数据迁移存储过程
drop procedure if exists pr_t_member_data_ransfer;
delimiter //
create procedure pr_t_member_data_ransfer(in genealogy_1_id varchar(50),in genealogy_id bigint,in creator_id bigint,out t_centerId bigint,in g_centerId varchar(40))
begin

	DECLARE userId BIGINT;
	DECLARE surname varchar(100);
	DECLARE name varchar(100);
	DECLARE nickname varchar(100);
	DECLARE imageUrl varchar(200);
	DECLARE generation int;
	DECLARE sex int;
	DECLARE bir varchar(40);
	DECLARE right_role int;
	DECLARE createTime varchar(40);
	DECLARE fu_menber_id varchar(40);
	
	DECLARE v_sql varchar(1100);
	declare v_name varchar(30);
	DECLARE v_i int;
	DECLARE v_memberId BIGINT;
	DECLARE v_memberRightId BIGINT;

	-- 遍历数据结束标志
  DECLARE done INT DEFAULT TRUE;
	-- 用户家谱数据
	declare memberCur CURSOR FOR SELECT
																t1.fu_id,
																t.fu_menber_surname,
																t.fu_menber_name,
																t.fu_menber_lastname,
																t.fu_menber_headiamge,
																t.fu_menber_generation_number,
																t.fu_menber_sex,
																t.fu_menber_birthday,
																(t.fu_menber_role + 1) as rightRole,
																t.fu_menber_createtime,
																t.fu_menber_id
															from findu_database.fu_familytree_menber_table t
															LEFT JOIN findu_database.fu_user_table t1 ON t.fu_menber_fuuserid = t1.fu_user_id
															WHERE
																t.fu_menber_familytreeid = genealogy_1_id;
  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;
	-- 设置事务不收到提交
	OPEN memberCur;
		-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH memberCur INTO userId,surname,name,nickname,imageUrl,generation,sex,bir,right_role,createTime,fu_menber_id;

		-- 开始循环
		WHILE done do
			-- 根据分表规则，查找相应的表
			set v_i = creator_id % 100;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_member', '_', v_name);
			-- 获取id
			CALL pr_auto_increment_id(v_name,v_memberId);
			-- 获取centerId
			if g_centerId = fu_menber_id THEN
				set t_centerId = v_memberId;
			end if;

			-- 这里做你想做的循环的事件，更换表需要做相应更改
			SET v_sql = concat(
				'insert into ',
				v_name,
				' (id,creatorId,userId,surname,name,nickname,imageUrl,generation,sexy,birthday,memorialDay,createTime) 
																values(',
				v_memberId,
				',',
				creator_id,
				',',
				IFNULL(userId, 'null'),
				',',
				IFNULL(
					CONCAT('"', surname, '"'),
					'null'
				),
				',',
				IFNULL(
					CONCAT('"', NAME, '"'),
					'null'
				),
				',',
				IFNULL(
					CONCAT('"', nickname, '"'),
					'null'
				),
				',',
				IFNULL(
					CONCAT('"', imageUrl, '"'),
					'null'
				),
				',',
				IFNULL(generation, 'null'),
				',',
				IFNULL(sex, 'null'),
				',',
				IFNULL(CONCAT('"', bir, '"'), 'null'),
				',',
				IFNULL(CONCAT('"', bir, '"'), 'null'),
				',"',
				createTime,
				'");'
			) ;
			SET @v_sql = v_sql ;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;

			-- 根据分表规则，查找相应的表
			set v_i = v_memberId % 10;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_member_right', '_', v_name);
			-- 获取id
			CALL pr_auto_increment_id(v_name,v_memberRightId);
			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (id,memberId,rightType,createTime) 
									values(',v_memberRightId,',',v_memberId,',',right_role,',"',createTime,'");');
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;

			set v_sql = CONCAT('insert into t_temp_re(member_id,member_1_id) values(',v_memberId,',"',fu_menber_id,'");');
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;
			
			-- 提取游标里的数据，这里只有一个，多个	的话也一样；
			FETCH memberCur INTO userId,surname,name,nickname,imageUrl,generation,sex,bir,right_role,createTime,fu_menber_id;
		END WHILE;
  -- 关闭游标
  CLOSE memberCur;
end
//
delimiter ;

drop procedure if exists pr_t_family_data_transfer;
delimiter //
create procedure pr_t_family_data_transfer(in genealogyId bigint,in genealogy_1_id varchar(50))
begin
	DECLARE childId_1 varchar(50);
	DECLARE childId BIGINT;
	DECLARE fatherId_1 varchar(50);
	DECLARE fatherId BIGINT;
	DECLARE motherId_1 varchar(50);
	DECLARE motherId	BIGINT;
	DECLARE createTime varchar(50);
	DECLARE famliyId BIGINT;

	DECLARE v_sql varchar(1100);
	declare v_name varchar(30);
	DECLARE v_i int;

	-- 遍历数据结束标志
  DECLARE done INT DEFAULT TRUE;
	-- 用户家谱数据

	DECLARE familyCur CURSOR FOR SELECT
									tr.fu_relation_centermenber_id,
									tr.fu_relation_addtime
								FROM findu_database.fu_familytree_menber_relation_table tr
								WHERE tr.fu_relation_relationmenber_id IN (
										SELECT t.fu_menber_id
										FROM findu_database.fu_familytree_menber_table t
										WHERE t.fu_menber_familytreeid = genealogy_1_id )
								AND ( tr.fu_relation = 2 OR tr.fu_relation = 1 )
								GROUP BY tr.fu_relation_centermenber_id
								ORDER BY tr.fu_relation;
  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;
	
	-- 设置事务不收到提交
	OPEN familyCur;
		-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH familyCur INTO childId_1,createTime;

		-- 开始循环
		WHILE done do
			-- 根据分表规则，查找相应的表
			set v_i = genealogyId % 100;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_family', '_', v_name);
			-- 获取id
			CALL pr_auto_increment_id(v_name,famliyId);
		
			-- 插叙出当前孩子的父母亲
			select t.fu_relation_relationmenber_id into fatherId_1 from findu_database.fu_familytree_menber_relation_table t
			where t.fu_relation_centermenber_id = childId_1 and t.fu_relation = 1 limit 1;
			
			select t.fu_relation_relationmenber_id into motherId_1 from findu_database.fu_familytree_menber_relation_table t
			where t.fu_relation_centermenber_id = childId_1 and t.fu_relation = 2 limit 1;
			
			select t.member_id into childId from t_temp_re t where t.member_1_id =  childId_1;
			select t.member_id into fatherId from t_temp_re t where t.member_1_id =  fatherId_1;
			select t.member_id into motherId from t_temp_re t where t.member_1_id =  motherId_1;

			if createTime is null THEN
				set createTime = SYSDATE();
			end if;
			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (id,genealogyId,fatherId,motherId,childId,childCreatorId,clanFamily,fatherType,motherType,fatherCreatorId,motherCreatorId,createTime) 
									values(',famliyId,',',genealogyId,',',IFNULL(fatherId,'null'),',',IFNULL(motherId,'null'),',',IFNULL(childId,'null'),',',IFNULL(childId,'null'),',true,1,1,null,null,"',createTime,'");');

			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;

			-- 提取游标里的数据，这里只有一个，多个	的话也一样；
			FETCH familyCur INTO childId_1,createTime;
		END WHILE;
		
  -- 关闭游标
  CLOSE familyCur;
	-- 清空零时表的数据
	delete from t_temp_re;
end
//
delimiter ;




-- 用户以及好友数据迁移存储过程
drop procedure if exists pr_t_user_friend_data_ransfer;
delimiter //
create procedure pr_t_user_friend_data_ransfer()

begin

	DECLARE user_id BIGINT;
	DECLARE friend_id BIGINT;
	DECLARE createTime varchar(40);

	DECLARE v_sql varchar(1100);
	declare v_name varchar(30);
	DECLARE v_i int;
	DECLARE v_id BIGINT;

	-- 遍历数据结束标志
  DECLARE done INT DEFAULT TRUE;

	declare cur CURSOR FOR select tu1.fu_id as user_id,tu2.fu_id as friend_id,tf.fu_addtime from( 
											select * from (
											select t1.fu_userid,t1.fu_friendid,t1.fu_addtime,1  as f_type from findu_database.fu_user_friend_table t1
											UNION
											select t2.fu_user_id,t2.fu_friend_id,t2.fu_apply_time,case when t2.fu_apply_repay_type = 0 then 1 else t2.fu_apply_repay_type end as f_type  
											from findu_database.fu_user_friend_apply_table t2) tmp
											where tmp.fu_friendid <> tmp.fu_userid 
											group by tmp.fu_friendid,tmp.fu_userid) tf
											inner join findu_database.fu_user_table tu1 on tu1.fu_user_id = tf.fu_userid
											inner join findu_database.fu_user_table tu2 on tu2.fu_user_id = tf.fu_friendid;

  -- 将结束标志绑定到游标
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = FALSE;

	OPEN cur;
		-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH cur INTO user_id,friend_id,createTime;

		-- 开始循环
		WHILE done do
			-- 根据分表规则，查找相应的表
			set v_i = user_id % 100;
			set v_name = convert(v_i, char);
			set v_name = lpad(v_name, 2, '0');
			-- 更换表需要做相应更改
			set v_name = concat('t_friend', '_', v_name);

			-- 获取id
			CALL pr_auto_increment_id(v_name,v_id);

			-- 这里做你想做的循环的事件，更换表需要做相应更改
			set v_sql = concat('insert into ',v_name,' (id,userId,friendId,type,createTime) values(',v_id,',',user_id,',',friend_id,',1,"',createTime,'");');
			
			set @v_sql = v_sql;
			prepare stmt from @v_sql;
			execute stmt;
			deallocate prepare stmt;
		
			-- 提取游标里的数据，这里只有一个，多个	的话也一样；
		FETCH cur INTO user_id,friend_id,createTime;
		END WHILE;
  -- 关闭游标
  CLOSE cur;
end
//
delimiter ;

drop procedure if exists pr_auto_increment_id;
delimiter //
create procedure pr_auto_increment_id(in tb_name varchar(50),out t_id bigint)
begin

	DECLARE v_sql varchar(1100);
  
	select t.seqno into t_id from t_seqno t where t.tablename = tb_name;

	if t_id is NULL then
		set t_id = 1;
		set v_sql = CONCAT('insert into t_seqno(seqno,tablename) values(',t_id,',','\'',tb_name,'\'',');');
	ELSE
		set t_id = t_id + 1;
		set v_sql = CONCAT('update t_seqno t set t.seqno = ',t_id,' where t.tablename = ','\'',tb_name,'\'');
  end if;

	set @v_sql = v_sql;
	prepare stmt from @v_sql;
	execute stmt;
	deallocate prepare stmt;
end
//
delimiter ;
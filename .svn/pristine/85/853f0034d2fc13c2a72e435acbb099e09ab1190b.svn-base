	INSERT INTO t_user (
		id,
		account,
		PASSWORD,
		phone,
		surname,
		NAME,
		nickname,
		imageUrl,
		generation,
		sexy,
		rank,
		address,
		qq,
		weixin,
		createTime
	) SELECT
		t.fu_id,
		t.fu_user_phone,
		t.fu_user_pass,
		t.fu_user_phone,
		t.fu_user_surname,
		t.fu_user_real_surname,
		t.fu_user_name,
		t.fu_user_headimage,
		null,
		null,
		null,
		t.fu_user_address,
		t.fu_user_qq,
		t.fu_user_weixin,
		t.fu_user_applytime
	FROM
		findu_database.fu_user_table t;
package com.findu.model;
// Generated 2016-3-12 23:27:55 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;

import com.findu.utils.SysParamUtils;

/**
 * 用户表
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "t_user")
public class TUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3816727248708261817L;
	private long userId = 0;
	/**帐号*/
	private String account = null;
	/**密码*/
	private String password = null;
	/**支付密码*/
	private String payPassword = null;
	/**手机区号*/
	private String areacode = null;
	/**联系电话*/
	private String phone = null;
	/**姓*/
	private String surname = null;
	/**名字*/
	private String name = null;
	/**昵称*/
	private String nickname = null;
	/**头像*/
	private String thumbnail = null;
	/**第几代*/
	private String generation = null;
	/**性别 1男，2女*/
	private Integer sexy = null;
	/**用户等级 1普通用户，2vip用户*/
	private Integer rank = null;
	/**用户签名*/
	private String sign = null;
	/**用户地址*/
	private String address = null;
	private String qq = null;
	private String weixin = null;
	
	private Date lastLoginTime = null;
	private Date createTime = null;

	public TUser() {
	}

	public TUser(long id, Date createTime) {
		this.userId = id;
		this.createTime = createTime;
	}

	public TUser(long id, String account, String password, String phone, String surname, String name, String nickname,
			String thumbnail, String generation, Integer sexy, Integer rank, String address, String qq, String weixin,
			Date lastLoginTime, Date createTime) {
		this.userId = id;
		this.account = account;
		this.password = password;
		this.phone = phone;
		this.surname = surname;
		this.name = name;
		this.nickname = nickname;
		this.thumbnail = thumbnail;
		this.generation = generation;
		this.sexy = sexy;
		this.rank = rank;
		this.address = address;
		this.qq = qq;
		this.weixin = weixin;
		this.lastLoginTime = lastLoginTime;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator="customer_gen")
	@TableGenerator(name = "customer_gen",
				    table="t_seqno",
				    pkColumnName="tablename",
				    valueColumnName="seqno",
				    pkColumnValue="t_user",
				    allocationSize=1)
	@Column(name = "id", unique = true, nullable = false)
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 用户帐号，必须包含字母，且不能包含@。
	 */
	@Column(name = "account", length = 30)
	@Pattern(regexp = "[^@]*\\p{Alpha}+[^@]*", message = "帐号必须包含字母，不能包含@")
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "password", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "payPassword", length = 50)
	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	/**
	 * 电话号码，必须是全数字。
	 */
	@Column(name = "phone", length = 20)
	@Pattern(regexp = "\\d+", message = "电话号码必须是全数字")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "surname", length = 10)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "name", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 头像url，可以是相对地址，则必须以 / 作为第一个字符。
	 */
	@Column(name = "thumbnail", length = 100)
	@Pattern(regexp = "(^/.+)|(^http://.+)", message = "头像url地址，必须以 http:// 或 / 开始")
	public String getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		if(thumbnail == null)
			this.thumbnail = null;
		else if(thumbnail.indexOf(SysParamUtils.THUMNAIL_BASE_URL) == 0)
			this.thumbnail = thumbnail.substring(SysParamUtils.THUMNAIL_BASE_URL.length());
		else
			this.thumbnail = thumbnail;
	}

	@Column(name = "generation", length = 20)
	public String getGeneration() {
		return this.generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	/**
	 * 性别，1 男，2 女。
	 */
	@Column(name = "sexy")
	@Range(min=1, max=2, message="性别：1 男，2 女")
	public Integer getSexy() {
		return this.sexy;
	}

	public void setSexy(Integer sexy) {
		this.sexy = sexy;
	}

	@Column(name = "rank")
	public Integer getRank() {
		return this.rank;
	}
	@Column(name = "sign")
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "qq", length = 30)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "weixin", length = 50)
	public String getWeixin() {
		return this.weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastLoginTime", length = 19)
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**手机区号*/
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
}

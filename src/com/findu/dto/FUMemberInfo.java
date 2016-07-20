package com.findu.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.findu.model.TMember;
import com.findu.model.TMemberExtend;
import com.findu.utils.DataUtils;

public class FUMemberInfo{
	private long memberId = 0;
	/**成员添加人*/
	private long creatorId = 0;
	/**用户id,可以不存在*/
	private Long userId = null;
	/**姓*/
	private String surname = null;
	/**名字*/
	private String name = null;
	/**昵称*/
	private String nickname = null;
	/**头像*/
	private String thumbnail = null;
	/**第几代人*/
	private String generation = null;
	/**字辈*/
	private String generationName = null;
	/**手机号*/
	private String phone = null;
	/**成员兄弟姐妹排行*/
	private int seniority = 0;
	/**性别1男，2女*/
	private Integer sexy = null;
	/**生日*/
	private String birthday = null;
	/**纪念日*/
	private String memorialDay = null;
	/**创建时间*/
	private Date createTime = null;
	
	//////////////////////////////////////扩展属性
	/**居住地址*/
	private String address = null;
	/**户籍地址*/
	private String registry = null;
	
	private String qq = null;
	
	private String weixin = null;
	/**成员职业*/
	private String occupation = null;
	/**是否结婚*/
	private boolean marriage = false;
	/**学历*/
	private String education = null;
	/**成员个人介绍*/
	private String introduce = null;
	
	

	
	
	public FUMemberInfo() {
		// TODO Auto-generated constructor stub
	}
	public static FUMemberInfo fromObjects(TMember member,List<TMemberExtend> extendlist) {
		// TODO Auto-generated constructor stub
		
		JSONObject objc = JSONObject.fromObject(member);
//		objc.put("memberId", objc.getLong("id"));
		JSONObject ctime = objc.getJSONObject("createTime");
		objc.put("createTime", ctime.getLong("time"));
		if (extendlist!=null) {
			for (TMemberExtend tMemberExtend : extendlist) {
				objc.put(tMemberExtend.getName(), tMemberExtend.getValue());
			}
		}
		
		FUMemberInfo info = DataUtils.Json2Object(objc.toString(), FUMemberInfo.class);
		return info;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getGeneration() {
		return generation;
	}
	public void setGeneration(String generation) {
		this.generation = generation;
	}
	/**用户的字辈*/
	public String getGenerationName() {
		return generationName;
	}
	public void setGenerationName(String generationName) {
		this.generationName = generationName;
	}
	/**成员的手机号*/
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/** 用户的排行*/
	public int getSeniority() {
		return seniority;
	}
	public void setSeniority(int seniority) {
		this.seniority = seniority;
	}
	public Integer getSexy() {
		return sexy;
	}
	public void setSexy(Integer sexy) {
		this.sexy = sexy;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**纪念日*/
	public String getMemorialDay() {
		return memorialDay;
	}
	public void setMemorialDay(String memorialDay) {
		this.memorialDay = memorialDay;
	}
	/**现居住地址*/
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	/**户籍地址*/
	public String getRegistry() {
		return registry;
	}
	public void setRegistry(String registry) {
		this.registry = registry;
	}
	
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	/**学历*/
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	/**职业*/
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	/**是否结婚🎎*/
	public boolean getMarriage() {
		return marriage;
	}
	public void setMarriage(boolean marriage) {
		this.marriage = marriage;
	}
	/**成员个人介绍*/
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	/**类方法：获取Member的扩展对象 数组*/
	public static List<TMemberExtend> memberExtends(FUMemberInfo info){
		List<TMemberExtend> mExtends = new ArrayList<TMemberExtend>();
		if (info.address!=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("address");
			e.setValue(info.address);
			mExtends.add(e);
		}
		if (info.registry!=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("registry");
			e.setValue(info.registry);
			mExtends.add(e);
		}
		if (info.qq!=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("qq");
			e.setValue(info.qq);
			mExtends.add(e);
		}
		if (info.weixin!=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("weixin");
			e.setValue(info.weixin);
			mExtends.add(e);
		}
		if (info.education!=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("education");
			e.setValue(info.education);
			mExtends.add(e);
		}
		if (info.occupation !=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("occupation");
			e.setValue(info.occupation);
			mExtends.add(e);
		}
		if (info.introduce !=null) {
			TMemberExtend e = new TMemberExtend();
			e.setMemberId(info.memberId);
			e.setName("introduce");
			e.setValue(info.introduce);
			mExtends.add(e);
		}
		TMemberExtend e = new TMemberExtend();
		e.setMemberId(info.memberId);
		e.setName("marriage");
		e.setValue(info.marriage==true?"true":"false");
		mExtends.add(e);
		
		return mExtends;
	}
}



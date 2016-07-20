package com.findu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.findu.dao.TFamilyDao;
import com.findu.dao.TGenealogyDao;
import com.findu.dao.TGenealogyMapDao;
import com.findu.dao.TMemberDao;
import com.findu.dao.TMemberExtendDao;
import com.findu.dao.TMemberRightDao;
import com.findu.dao.TPartnerDao;
import com.findu.dao.TSubjectDao;
import com.findu.dao.TUserDao;
import com.findu.dto.FUChild;
import com.findu.dto.FUDaughter;
import com.findu.dto.FUFamilyMember;
import com.findu.dto.FUFamilyTree;
import com.findu.dto.FUFather;
import com.findu.dto.FUMember;
import com.findu.dto.FUMemberAdd;
import com.findu.dto.FUMother;
import com.findu.dto.FUResult;
import com.findu.dto.FUSearchPage;
import com.findu.dto.FUSon;
import com.findu.dto.FUUser;
import com.findu.model.TFamily;
import com.findu.model.TGenealogy;
import com.findu.model.TGenealogyMap;
import com.findu.model.TGenealogyMapId;
import com.findu.model.TMember;
import com.findu.model.TMemberExtend;
import com.findu.model.TMemberRight;
import com.findu.model.TPartner;
import com.findu.model.TSubject;
import com.findu.model.TUser;
import com.findu.utils.DataUtils;
import com.findu.utils.ErrorCodeUtil;
import com.findu.utils.SysParamUtils;
import com.findu.utils.TypeUtil;

@Service
public class GenealogyService {
	private static Logger logger = Logger.getLogger(GenealogyService.class);
	
	@Autowired
	private TMemberDao memberDao;
	
	@Autowired
	private TMemberExtendDao memberExtDao;
	
	@Autowired
	private TGenealogyDao genealogyDao;
	
	@Autowired
	private TGenealogyMapDao genealogyMapDao;
	
	@Autowired
	private TFamilyDao familyDao;
	
	@Autowired
	private TMemberRightDao memberRightDao; 
	
	@Autowired
	private TUserDao userDao;
	
	@Autowired
	private TPartnerDao partnerDao;
	
	@Autowired
	private TSubjectDao subjectDao;
	
	public TMember getMember(long id, long creatorId){
		TMember member = new TMember();
		member.setMemberId(id);
		member.setCreatorId(creatorId);
		return memberDao.get(member,id);
	}
	
	public int updateMember(TMember member) {
		if(!memberDao.update(member)){
			logger.warn("update member error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int insertMember(TMember member) {
		if(member.getCreateTime() == null)
			member.setCreateTime(new Date());
		if(memberDao.insert(member) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int deleteMember(long id, long creatorId){
		TMember member = new TMember();
		member.setMemberId(id);
		member.setCreatorId(creatorId);
		member.setCreateTime(new Date());
		if(!memberDao.delete(member)){
			logger.warn("delete member error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	/**
	 * 获取成员的扩展属性
	 * @param memberId 成员ID
	 * @param name     扩展名称
	 * @return
	 */
	public TMemberExtend getMemberExt(long memberId,String name){
		TMemberExtend memberExt = new TMemberExtend();
		memberExt.setName(name);
		memberExt.setMemberId(memberId);
		return memberExtDao.find(memberExt, true);
	}
	public TMemberExtend getMemberExt(long id, long memberId){
		TMemberExtend memberExt = new TMemberExtend();
		memberExt.setId(id);
		memberExt.setMemberId(memberId);
		return memberExtDao.get(memberExt, id);
	}
	
	public int updateMemberExt(TMemberExtend memberExt) {
		if(!memberExtDao.update(memberExt)){
			logger.warn("update member extend error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int insertMemberExt(TMemberExtend memberExt) {
		if(memberExt.getCreateTime() == null)
			memberExt.setCreateTime(new Date());
		if(memberExtDao.insert(memberExt) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int deleteMemberExt(long id, long memberId){
		TMemberExtend memberExt = new TMemberExtend();
		memberExt.setId(id);
		memberExt.setMemberId(memberId);
		if(!memberExtDao.delete(memberExt)){
			logger.warn("delete member extend error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TGenealogy getGenealogy(long genealogyId, long creatorId) {
		TGenealogy genealogy = new TGenealogy();
		genealogy.setGenealogyId(genealogyId);
		genealogy.setCreatorId(creatorId);
		genealogy = genealogyDao.get(genealogy, genealogyId);
		if(genealogy.getFamilyCount() <= 0){
			int count = familyDao.getFamilyCount(genealogy);
			if(count >= 0)
				genealogy.setFamilyCount(count);
			else
				logger.warn(String.format("get family count in genealogy %d error", genealogyId));
		}
		return genealogy;
	}
	
	public int updateGenealogy(TGenealogy genealogy) {
		if(!genealogyDao.update(genealogy)){
			logger.error("update genealogy error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int insertGenealogy(TGenealogy genealogy) {
		if(genealogy.getCenterId() <= 0)
			return ErrorCodeUtil.ERROR_NEED_CENTER;
		if(genealogy.getCreatorId() <= 0)
			return ErrorCodeUtil.ERROR_NEED_CREATOR;
		if(genealogy.getCreateTime() == null)
			genealogy.setCreateTime(new Date());
		if(genealogyDao.insert(genealogy) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int deleteGenealogy(long genealogyId, long creatorId){
		TGenealogy genealogy = new TGenealogy();
		genealogy.setGenealogyId(genealogyId);
		genealogy.setCreatorId(creatorId);
		genealogy.setCreateTime(new Date());
		if(!genealogyDao.delete(genealogy)){
			logger.warn("delete genealogy error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TGenealogyMap getGenealogyMap(long userId, long genealogyId) {
		TGenealogyMapId genealogyMapId = new TGenealogyMapId(userId, genealogyId);
		TGenealogyMap genealogyMap = new TGenealogyMap();
		genealogyMap.setId(genealogyMapId);
		return genealogyMapDao.get(genealogyMap,genealogyMapId);
	}

	public int updateGenealogyMap(TGenealogyMap genealogyMap) {
		if(!genealogyMapDao.update(genealogyMap)){
			logger.warn("update genealogy map error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteGenealogyMap(long userId, long genealogyId) {
		TGenealogyMapId genealogyMapId = new TGenealogyMapId(userId, genealogyId);
		TGenealogyMap genealogyMap = new TGenealogyMap();
		genealogyMap.setId(genealogyMapId);
		if(!genealogyMapDao.delete(genealogyMap)){
			logger.warn("delete genealogy map error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertGenealogyMap(TGenealogyMap genealogyMap) {
		if(genealogyMap.getCenterId() <= 0){
			TGenealogy genealogy = getGenealogy(genealogyMap.getId().getGenealogyId(), genealogyMap.getCreatorId());
			genealogyMap.setCenterId(genealogy.getCenterId());
		}
		if(genealogyMapDao.insert(genealogyMap) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TFamily getFamily(long familyId, long genealogyId) {
		TFamily family = new TFamily();
		family.setFamilyId(familyId);
		family.setGenealogyId(genealogyId);
		return familyDao.get(family,familyId);
	}

	public int updateFamily(TFamily family) {
		if(!familyDao.update(family)){
			logger.warn("update family error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertFamily(TFamily family) {
		TGenealogy genealogy = getGenealogy(family.getGenealogyId(), family.getGenealogyCreatorId());
		switch(genealogy.getType()){
		case TGenealogy.TYPE_PRIVATE:
			if(genealogy.getFamilyCount() >= Integer.parseInt(SysParamUtils.PRIVATE_G_F_N)){
				logger.warn(String.format("family in private genealogy %d is full", family.getGenealogyId()));
				return ErrorCodeUtil.ERROR_GENEALOGY_FULL;
			}
			break;
		case TGenealogy.TYPE_PUBLIC:
			if(genealogy.getFamilyCount() >= Integer.parseInt(SysParamUtils.PUBLIC_G_F_N)){
				logger.warn(String.format("family in public genealogy %d is full", family.getGenealogyId()));
				return ErrorCodeUtil.ERROR_GENEALOGY_FULL;
			}
			break;
		default:
			return ErrorCodeUtil.ERROR_GENEALOGY_INVALID;
		}
		if(family.getCreateTime() == null)
			family.setCreateTime(new Date());
		
		familyDao.insert(family);
		genealogy.setFamilyCount(genealogy.getFamilyCount() + 1);
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteFamily(long familyId, long genealogyId) {
		TFamily family = new TFamily();
		family.setFamilyId(familyId);
		family.setGenealogyId(genealogyId);
		family.setCreateTime(new Date());
		if(!familyDao.delete(family)){
			logger.warn("delete family error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		
		TGenealogy genealogy = getGenealogy(family.getGenealogyId(), family.getGenealogyCreatorId());
		genealogy.setFamilyCount(genealogy.getFamilyCount() - 1);
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public TMemberRight getMemberRight(long id, long memberId) {
		TMemberRight memberRight = new TMemberRight();
		memberRight.setId(id);
		memberRight.setMemberId(memberId);
		return memberRightDao.get(memberRight,id);
	}
	
	public int updateMemberRight(TMemberRight memberRight) {
		if(!memberRightDao.update(memberRight)){
			logger.warn("update member right error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int insertMemberRight(TMemberRight memberRight) {
		if(memberRight.getCreateTime() == null)
			memberRight.setCreateTime(new Date());
		
		if(memberRightDao.insert(memberRight) == null)
			return ErrorCodeUtil.ERROR_SYSTEM;
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	public int deleteMemberRight(long id, long memberId) {
		TMemberRight memberRight = new TMemberRight();
		memberRight.setId(id);
		memberRight.setMemberId(memberId);
		memberRight.setCreateTime(new Date());
		if(!memberRightDao.delete(memberRight)){
			logger.warn("delete member right error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	/**
	 * 根据家谱映射获取家谱列表，由于删除公有家谱时可能产生无效的家谱映射，所以对于无效的家谱映射直接删除。
	 * @param genealogyMaps 家谱映射列表。
	 * @return 家谱列表。
	 */
	public ArrayList<TGenealogy> listGenealogy(ArrayList<TGenealogyMap> genealogyMaps) {
		ArrayList<TGenealogy> list = new ArrayList<TGenealogy>();
		for (TGenealogyMap genealogyMap : genealogyMaps) {
			TGenealogy genealogy;
			try {
				genealogy = this.getGenealogy(genealogyMap.getId().getGenealogyId(), genealogyMap.getCreatorId());
			} catch (Exception e) {
				// TODO: handle exception
				genealogy = null;
			}
			if(genealogy == null){
				if(!genealogyMapDao.delete(genealogyMap)){
					logger.warn("delete genealogy map error");
				}
			}else{
				list.add(genealogy);	
			}			
		}
		return list;
	}
	public ArrayList<TMemberExtend> listMemberExt(long memberId){
		TMemberExtend ext = new TMemberExtend();
		ext.setMemberId(memberId);
		return memberExtDao.search(ext, true, null, false, null);
	}
	public ArrayList<TGenealogyMap> listGenealogyMap(long userId) {
		TGenealogyMap genealogyMap = new TGenealogyMap();
		TGenealogyMapId genealogyMapId  = new TGenealogyMapId();
		genealogyMapId.setUserId(userId);
		genealogyMap.setId(genealogyMapId);
		return genealogyMapDao.search(genealogyMap, true, null, false, null);
	}
	
	public FUFamilyTree getMemberTree(long genealogyId, long userId, long centerId, int up, int down, FUResult result) {
		TGenealogyMap genealogyMap = this.getGenealogyMap(userId, genealogyId);
		if(genealogyMap == null){
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
			return null;
		}
		TGenealogy genealogy = this.getGenealogy(genealogyMap.getId().getGenealogyId(), genealogyMap.getCreatorId());
		if(genealogy == null){
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
			return null;
		}
		
		FUFamilyTree fuFamily = null;
		switch(genealogy.getType()){
		case TGenealogy.TYPE_PRIVATE:
			fuFamily = this.findPrivateGenealogy(genealogy, centerId);
			break;
		case TGenealogy.TYPE_PUBLIC:
			fuFamily = this.findPublicGenealogy(genealogy, centerId, up, down);
			break;
		default:
			logger.warn(String.format("genealogy type %d is invalid", genealogy.getType()));
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
			return null;
		}
		if(fuFamily == null){
			result.setCode(ErrorCodeUtil.ERROR_GENEALOGY_INVALID);
		}
		
		logger.info("get member tree finish");
		return fuFamily;
	}
	
	/** 一次查询所有的家庭，并生成家族树 */
	private FUFamilyTree findPrivateGenealogy(TGenealogy genealogy, long centerId){
		TFamily cond = new TFamily();
		cond.setGenealogyId(genealogy.getGenealogyId());
		cond.setGenealogyCreatorId(genealogy.getCreatorId());
		ArrayList<TFamily> families = familyDao.search(cond, true
				, new ArrayList<String>(){{add("createTime");}}, false, null);
		
		FUFamilyTree tree = new FUFamilyTree();
		tree.setId(genealogy.getGenealogyId());
		tree.setCreatorId(genealogy.getCreatorId());
		tree.setCenterId(centerId);
		tree.setSurname(genealogy.getSurname());
		if(families == null || families.size() <= 0){
			logger.warn(String.format("private genealogy %d has no family", genealogy.getGenealogyId()));
			return tree;
		}
		logger.info(String.format("private genealogy %d has %d families", genealogy.getGenealogyId(), families.size()));
		
		if(!addFamiliesInTree(tree, families)){
			logger.warn("can not construct family tree of genealogy " + genealogy.getGenealogyId());
			return null;
		}
		
		if(!buildSiblings(tree)){
			logger.warn(String.format("build siblings in genealogy %d error", genealogy.getGenealogyId()));
			return null;
		}
		return tree;
	}
	
	/** 生成同辈列表时，所有同辈先公用一个列表对象，检查自己是否在列表中，如果不在则添加，后续再拆分列表*/
	private boolean addFamiliesInTree(FUFamilyTree tree, ArrayList<TFamily> families){
		int maxTier = 0, minTier = 0;
		HashMap<Integer, ArrayList<Long>> maps = new HashMap<>();
		for(TFamily family : families){
			logger.info(String.format("build family %d of family tree %d", family.getFamilyId(), tree.getId()));
			FUMember father = getFuMember(tree, family.getFatherId(), family.getFatherCreatorId());
			FUMember mother = getFuMember(tree, family.getMotherId(), family.getMotherCreatorId());
			FUMember child = getFuMember(tree, family.getChildId(), family.getChildCreatorId());
			LinkedList<FUFather> asFather = null;
			LinkedList<FUMother> asMother = null;
			LinkedList<FUSon> asSon = null;
			LinkedList<FUDaughter> asDaughter = null;
			LinkedList<Long> list = null;
			FUFather ff = null;
			FUMother fm = null;
			FUChild fc = null;
			
			if(father == null && mother == null && child == null){
				logger.warn(String.format("family %d of creator %d of user %d has no member"
						, family.getFamilyId(), family.getGenealogyId(), family.getGenealogyCreatorId()));
				continue;
			}
			
			// 设置妻子和孩子
			if(father != null){
				if(tree.getTiers() == null){
					father.setTier(-1);
					minTier = -1;
				}
				asFather = father.getAsFather();
				if(asFather == null){
					asFather = new LinkedList<FUFather>();
					father.setAsFather(asFather);
				}
				
				if(mother != null){
					ff = null;
					for(FUFather f : asFather){
						if(f.getWifeId() != null && f.getWifeId() == mother.getMemberId()){
							ff = f;
							break;
						}
					}
					if(ff == null){
						ff = new FUFather(family);
						asFather.add(ff);
					}
					
					if(mother.getTier() != null
							&& (father.getTier() == null || !ff.isHouseholder()))
						father.setTier(mother.getTier());
					else if(father.getTier() != null
							&& (mother.getTier() == null || ff.isHouseholder()))
						mother.setTier(father.getTier());
				}
				
				if(child != null){
					if(ff == null){
						for(FUFather f : asFather){
							if(f.getWifeId() == null || f.getWifeId() <= 0){
								ff = f;
								break;
							}
						}
					}
					if(ff == null){
						ff = new FUFather(family);
						asFather.add(ff);
					}
					list = ff.getChildIds();
					if(list == null){
						list = new LinkedList<Long>();
						ff.setChildIds(list);
					}
					if(list.indexOf(child.getMemberId()) < 0)
						list.add(child.getMemberId());
					
					if(father.getTier() != null && child.getTier() == null)
						child.setTier(father.getTier() + 1);
						
				}
			}
			
			// 设置丈夫和孩子
			if(mother != null){
				if(tree.getTiers() == null){
					mother.setTier(-1);
					minTier = -1;
				}
				asMother = mother.getAsMother();
				if(asMother == null){
					asMother = new LinkedList<FUMother>();
					mother.setAsMother(asMother);
				}
				
				if(father != null){
					fm = null;
					for(FUMother m : asMother){
						if (m.getHusbandId() != null && m.getHusbandId() == father.getMemberId()) {
							fm = m;
							break;
						}
					}
					if(fm == null){
						fm = new FUMother(family);
						asMother.add(fm);
					}
					
					if(father.getTier() != null
							&& (mother.getTier() == null || !fm.isHouseholder()))
						mother.setTier(father.getTier());
					else if(mother.getTier() != null
							&& (father.getTier() == null || fm.isHouseholder()))
						father.setTier(mother.getTier());
				}
				
				if(child != null){
					if(fm == null){
						for(FUMother m : asMother){
							if(m.getHusbandId() == null || m.getHusbandId() <= 0){
								fm = m;
								break;
							}
						}
					}
					if(fm == null){
						fm = new FUMother(family);
						asMother.add(fm);
					}
					list = fm.getChildIds();
					if(list == null){
						list = new LinkedList<Long>();
						fm.setChildIds(list);
					}
					if(list.indexOf(child.getMemberId()) < 0)
						list.add(child.getMemberId());
					
					if(mother.getTier() != null && child.getTier() == null)
						child.setTier(mother.getTier() + 1);
				}			
			}
			
			// 设置父母和兄弟姐妹
			if(child != null){
				if(tree.getTiers() == null){
					child.setTier(0);
					minTier = 0;
				}
				LinkedList<? extends FUChild> childList;
				if(child.getSexy() == null || child.getSexy() == TypeUtil.SEX_MAN){
					asSon = child.getAsSon();
					if(asSon == null){
						asSon = new LinkedList<FUSon>();
						child.setAsSon(asSon);
					}
					childList = asSon;
				}else{
					asDaughter = child.getAsDaughter();
					if(asDaughter == null){
						asDaughter = new LinkedList<FUDaughter>();
						child.setAsDaughter(asDaughter);
					}
					childList = asDaughter;
				}
				fc = null;
				if(father != null && mother != null){
					for(FUChild c : childList){
						if(c.getFatherId() != null && c.getFatherId() == father.getMemberId() 
								&& c.getMotherId() != null && c.getMotherId() == mother.getMemberId()){
							fc = c;
							break;
						}
					}
				}else if(father != null){
					for(FUChild c : childList){
						if(c.getFatherId() != null && c.getFatherId() == father.getMemberId() 
								&& (c.getMotherId() != null || c.getMotherId() <= 0)){
							fc = c;
							break;
						}
					}
				}else if(mother != null){
					for(FUChild c : childList){
						if(c.getMotherId() != null && c.getMotherId() == mother.getMemberId() 
								&& (c.getFatherId() == null || c.getFatherId() <= 0)){
							fc = c;
							break;
						}
					}
				}
				if(fc == null){
					if(child.getSexy() == null || child.getSexy() == TypeUtil.SEX_MAN){
						fc = new FUSon();
						asSon.add((FUSon)fc);
					}else{
						fc = new FUDaughter();
						asDaughter.add((FUDaughter)fc);
					}
					if(father != null)
						fc.setFatherId(father.getMemberId());
					if(mother != null)
						fc.setMotherId(mother.getMemberId());
				}
				if(child.getTier() != null){
					if(father != null)
						father.setTier(child.getTier() - 1);
					if(mother != null)
						mother.setTier(child.getTier() - 1);
				}
				
				// 更新同辈列表
				list = null;
				if(ff != null){
					list = ff.getChildIds();
				}else if(fm != null){
					list = fm.getChildIds();
				}
				if(list != null){
					if(list.get(0) != child.getMemberId()){
						FUMember m = tree.getMembers().get(list.get(0));
						list = m.getSiblingIds();
						if(list == null){
							list = new LinkedList<Long>();
							m.setSiblingIds(list);
						}
					}else{
						list = new LinkedList<Long>();
					}
					child.setSiblingIds(list);
					if(list.indexOf(child.getMemberId()) < 0)
						list.add(child.getMemberId());
				}
			}
			
			//设置辈份
			if(tree.getTiers() == null){
				tree.setTiers(new ArrayList<ArrayList<Long>>());
			}
			ArrayList<Long> tier;
			int idx;
			if(father != null && father.getTier() != null){
				if(minTier > father.getTier())
					minTier = father.getTier();
				idx = -1;
				tier = maps.get(father.getTier());
				if(tier == null){
					tier = new ArrayList<>();
					maps.put(father.getTier(), tier);
				}
				if(tier.indexOf(father.getMemberId()) < 0){
					if(mother != null)
						idx = tier.indexOf(mother.getMemberId());
					if(idx >= 0)
						tier.add(idx, father.getMemberId());
					else
						tier.add(father.getMemberId());
				}				
			}
			if(mother != null && mother.getTier() != null){
				if(minTier > mother.getTier())
					minTier = mother.getTier();
				idx = -1;
				tier = maps.get(mother.getTier());
				if(tier == null){
					tier = new ArrayList<>();
					maps.put(mother.getTier(), tier);
				}
				if(tier.indexOf(mother.getMemberId()) < 0){
					if(father != null)
						idx = tier.indexOf(father.getMemberId());
					if(idx >= 0)
						tier.add(idx + 1, mother.getMemberId());
					else
						tier.add(mother.getMemberId());
				}				
			}
			if(child != null && child.getTier() != null){
				if(maxTier < child.getTier())
					maxTier = child.getTier();
				idx = -1;
				tier = maps.get(child.getTier());
				if(tier == null){
					tier = new ArrayList<>();
					maps.put(child.getTier(), tier);
				}
				if(tier.indexOf(child.getMemberId()) < 0)
					tier.add(child.getMemberId());
			}
		}
		
		for(int i = minTier; i <= maxTier; i++){
			tree.getTiers().add(maps.get(i));
		}
		return true;
	}
	
	/** 拆分同辈列表，检查列表中是否包含自己，如果不包含则表示列表已经拆分，否则遍历列表成员，为每个成员单独生成列表*/
	private boolean buildSiblings(FUFamilyTree tree){
		HashMap<Long, FUMember> members = tree.getMembers();
		if(members == null || members.size() <= 0)
			return true;
		for(FUMember member : members.values()){
			LinkedList<Long> list = member.getSiblingIds();
			if(list == null || list.size() <= 0 || list.indexOf(member.getMemberId()) < 0)
				continue;
			logger.info(String.format("build siblings of member %d in family tree %d"
					, member.getMemberId(), tree.getId()));
			for(Long id : list){
				FUMember m = members.get(id);
				LinkedList<Long> siblings = new LinkedList<Long>(list);
				siblings.remove(id);
				if(siblings.size() <= 0)
					m.setSiblingIds(null);
				else
					m.setSiblingIds(siblings);
			}
		}
		return true;
	}
	
	private FUMember getFuMember(FUFamilyTree tree, Long id, Long creatorId){
		if(id == null || id <= 0 || creatorId == null || creatorId <= 0)
			return null;
		FUMember member = null;
		try {
			HashMap<Long, FUMember> members = tree.getMembers();
			if (members == null) {
				members = new HashMap<Long, FUMember>();
				tree.setMembers(members);
			}
			member = members.get(id);
			if (member == null) {
				TMember m = getMember(id, creatorId);
				if (m == null)
					return null;
				member = new FUMember();
				if(!DataUtils.CopyBean(m, member)){
					logger.warn("copy member bean error");
					return null;
				}
				members.put(id, member);
			} 
			logger.info(String.format("get member %d in family tree %d of creator %d"
					, id, tree.getId(), tree.getCreatorId()));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			return null;
		}
		return member;
	}
	
	/**
	 * 查询共有家谱树，从中心节点开始向上和向下遍历家庭。
	 * @param genealogy
	 * @param childId
	 * @param up
	 * @param down
	 * @return
	 */
	private FUFamilyTree findPublicGenealogy(TGenealogy genealogy, long centerId, int up, int down) {
		if(up < 0 || up > Integer.parseInt(SysParamUtils.PUBLIC_G_UP_MAX)
				|| down < 0 || down > Integer.parseInt(SysParamUtils.PUBLIC_G_DOWN_MAX)){
			logger.warn(String.format("up %d, down %d is invalid parameter for generate family tree", up, down));
			return null;
		}
		
		ArrayList<TFamily> families = new ArrayList<>();
		HashMap<Long, TFamily> familyMap = new HashMap<>();
		LinkedList<TFamily> queue = new LinkedList<>();
		
		TFamily cond = new TFamily();
		cond.setGenealogyId(genealogy.getGenealogyId());
		cond.setGenealogyCreatorId(genealogy.getCreatorId());
		cond.setChildId(centerId);
		families = familyDao.search(cond, false, null, false, null);
		for(TFamily family : families){
			family.setGeneration(0);
			familyMap.put(family.getFamilyId(), family);
			queue.add(family);
		}
		if(!searchFamilies(families, familyMap, queue, up, down)){
			logger.warn("search families error");
			return null;
		}
		
		FUFamilyTree tree = new FUFamilyTree();
		tree.setId(genealogy.getGenealogyId());
		tree.setCreatorId(genealogy.getCreatorId());
		tree.setCenterId(centerId);
		tree.setSurname(genealogy.getSurname());
		if(families == null || families.size() <= 0){
			logger.warn(String.format("no family of public genealogy %d match condition", genealogy.getGenealogyId()));
			return tree;
		}
		logger.info(String.format("build %d families of public genealogy %d"
				, families.size(), genealogy.getGenealogyId()));
		
		if(!addFamiliesInTree(tree, families)){
			logger.warn("can not construct family tree of genealogy " + genealogy.getGenealogyId());
			return null;
		}
		
		if(!buildSiblings(tree)){
			logger.warn(String.format("build siblings in genealogy %d error", genealogy.getGenealogyId()));
			return null;
		}
		return tree;
	}
	
	private boolean searchFamilies(ArrayList<TFamily> families, HashMap<Long, TFamily> familyMap, LinkedList<TFamily> queue, int up, int down){
		while(queue.size() > 0){
			TFamily family = queue.pollFirst();
		
			if(family.getFatherId() != null && family.getFatherId() > 0){
				if(!addPublicFamily(family.getGenealogyId(), family.getGenealogyCreatorId()
						, family.getFatherId(), family.getGeneration() - 1
						, families, familyMap, queue, up, down)){
					logger.warn(String.format("search families of member %d error", family.getFatherId().longValue()));
					return false;
				}
			}
			
			if(family.getMotherId() != null && family.getMotherId() > 0){
				if(!addPublicFamily(family.getGenealogyId(), family.getGenealogyCreatorId()
						, family.getMotherId(), family.getGeneration() - 1
						, families, familyMap, queue, up, down)){
					logger.warn(String.format("search families of member %d error", family.getFatherId().longValue()));
					return false;
				}
			}
			
			if(family.getChildId() != null && family.getChildId() > 0){
				if(!addPublicFamily(family.getGenealogyId(), family.getGenealogyCreatorId()
						, family.getChildId(), family.getGeneration()
						, families, familyMap, queue, up, down)){
					logger.warn(String.format("search families of member %d error", family.getFatherId().longValue()));
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean addPublicFamily(long genealogyId, long genealogyCreatorId, long memberId, int generation, ArrayList<TFamily> families, HashMap<Long, TFamily> familyMap, LinkedList<TFamily> queue, int up, int down){
		if(generation < -up || generation > down)
			return false;
		TFamily cond = new TFamily();
		ArrayList<TFamily> list;
		
		cond.setGenealogyId(genealogyId);
		cond.setGenealogyCreatorId(genealogyCreatorId);
		
		cond.setFatherId(memberId);
		cond.setMotherId(memberId);
		cond.setChildId(memberId);
		list = familyDao.search(cond, false, null, false, null);
		for(TFamily f : list){
			if(familyMap.containsKey(f.getFamilyId()))
				continue;
			if(f.getChildId() == memberId){
				if(generation < -up)
					continue;
				else
					f.setGeneration(generation - 1);
			}else{
				if(generation >= down)
					continue;
				else
					f.setGeneration(generation + 1);
			}
			families.add(f);
			familyMap.put(f.getFamilyId(), f);
			queue.add(f);
		}
		return true;
	}
	
	public int createGenealogy(TGenealogy genealogy) {
		Date now = new Date();
		if(genealogy.getCreatorId() <= 0)
			return ErrorCodeUtil.ERROR_NEED_CREATOR;
		if(genealogy.getCreateTime() == null)
			genealogy.setCreateTime(now);
		Session session = genealogyDao.getSession(genealogy);
		Transaction tran = session.beginTransaction();
		try {
			TMember member = new TMember();
			if(genealogy.getCenterId() <= 0){
				TUser user = new TUser();
				user.setUserId(genealogy.getCreatorId());
				user = userDao.get(session, user, user.getUserId());
				if(user == null){
					logger.warn(String.format("can not find user %d", genealogy.getCreatorId()));
					tran.rollback();
					return ErrorCodeUtil.ERROR_ACCOUNT;
				}
				
				member.setCreatorId(user.getUserId());
				member.setUserId(user.getUserId());
				ArrayList<TMember> list = memberDao.search(session, member, true, new ArrayList<String>(){{add("createTime");}}, false, null);
				if(list == null || list.size() <= 0){
					if(user.getSurname() == null || user.getSurname().trim().length() <= 0
							|| user.getName() == null || user.getName().trim().length() <= 0
							|| user.getSexy() == null){
						logger.warn("no center member");
						tran.rollback();
						return ErrorCodeUtil.ERROR_NEED_CENTER;
					}
					
					member = new TMember();
					member.setCreatorId(user.getUserId());
					member.setSurname(user.getSurname());
					member.setName(user.getName());
					member.setSexy(user.getSexy());
					list = memberDao.search(session, member, true, null, false, null);
					if(list == null || list.size() <= 0){
						member.setUserId(user.getUserId());
						member.setCreateTime(now);
						if(memberDao.insert(session, member) == null){
							logger.warn("create center member error");
							tran.rollback();
							return ErrorCodeUtil.ERROR_NEED_CENTER;
						}
					}
				}else{
					member = list.get(0);
				}
				genealogy.setCenterId(member.getMemberId());
				
			}else{
				member.setMemberId(genealogy.getCenterId());
				member.setUserId(genealogy.getCreatorId());
				member.setCreatorId(genealogy.getCreatorId());
				member = memberDao.get(session, member, member.getMemberId());
				if(member == null){
					logger.warn(String.format("center id %d of genealogy of user %d is error"
							, genealogy.getCenterId(), genealogy.getCreatorId()));
					tran.rollback();
					return ErrorCodeUtil.ERROR_NEED_CENTER;
				}
			}
			
			if(genealogyDao.insert(session, genealogy) == null){
				logger.warn("insert genealogy error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_GENEALOGY_FULL;
			}
			
			TGenealogyMap gmap = new TGenealogyMap();
			TGenealogyMapId mid = new TGenealogyMapId();
			mid.setUserId(genealogy.getCreatorId());
			mid.setGenealogyId(genealogy.getGenealogyId());
			gmap.setId(mid);
			gmap.setCreatorId(genealogy.getCreatorId());
			gmap.setCenterId(genealogy.getCenterId());
			gmap.setType(TypeUtil.GMAP_CREATE);
			if(genealogyMapDao.insert(session, gmap) == null){
				logger.warn("insert genealogy map error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_GENEALOGY_FULL;
			}
			
			TFamily family = new TFamily();
			family.setGenealogyCreatorId(genealogy.getCreatorId());
			family.setGenealogyId(genealogy.getGenealogyId());
			family.setChildCreatorId(member.getCreatorId());
			family.setChildId(member.getMemberId());
			family.setCreateTime(now);
			if(familyDao.insert(session, family) == null){
				logger.warn("insert family error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			
			tran.commit();
		} catch (Exception e) {
			// TODO: handle exception
			tran.rollback();
			logger.error(e.getMessage());
			return ErrorCodeUtil.ERROR_SYSTEM;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public int removeGenealogy(long genealogyId, long creatorId){
		TGenealogy genealogy = new TGenealogy();
		genealogy.setGenealogyId(genealogyId);
		genealogy.setCreatorId(creatorId);
		Session session = genealogyDao.getSession(genealogy);
		Transaction tran = session.beginTransaction();
		try {
			TGenealogyMap gmap = new TGenealogyMap();
			TGenealogyMapId gmid = new TGenealogyMapId();
			gmid.setUserId(creatorId);
			gmid.setGenealogyId(genealogyId);
			gmap.setId(gmid);
			gmap.setType(TypeUtil.GMAP_CREATE);
			int rows = genealogyMapDao.remove(session, gmap, true);
			if(rows <= 0){
				tran.rollback();
				logger.warn(String.format("user %d is not the creator of genealogy %d", genealogy.getCreatorId(), genealogy.getGenealogyId()));
				return ErrorCodeUtil.ERROR_GENEALOGY_INVALID;
			}
			logger.info(String.format("delete %d genealogy map", rows));
			
			TFamily family = new TFamily();
			family.setGenealogyId(genealogyId);
			family.setGenealogyCreatorId(creatorId);
			rows = familyDao.remove(session, family, true);
			logger.info(String.format("delete %d families of genealogy %d", rows, genealogy.getGenealogyId()));
			session.clear();
			if(!genealogyDao.delete(session, genealogy)){
				logger.warn("delete genealogy error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_GENEALOGY_INVALID;
			}
			
			tran.commit();
		} catch (Exception e) {
			// TODO: handle exception
			tran.rollback();
			logger.error(e.getMessage());
			return ErrorCodeUtil.ERROR_SYSTEM;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	public FUUser attachMemberUser(TMember member){
		TUser user = new TUser();
		user.setSurname(member.getSurname());
		user.setName(member.getName());
		user.setSexy(member.getSexy());
		
		TMemberExtend memberExt = new TMemberExtend();
		memberExt.setMemberId(member.getMemberId());
		memberExt.setName(TMemberExtend.NAME_PHONE);
		memberExt = memberExtDao.find(memberExt, true);
		if(memberExt != null){
			user.setPhone(memberExt.getValue());
		}
		
		user = userDao.find(user, true);
		if(user == null){
			logger.warn(String.format("can not find user matched with member %d", member.getMemberId()));
			return null;
		}
		member.setUserId(user.getUserId());
		if(!memberDao.update(member)){
			logger.warn(String.format("update member %d error", member.getMemberId()));
			return null;
		}
		
		FUUser uo = new FUUser();
		DataUtils.CopyBean(user, uo);
		TPartner partner = new TPartner();
		partner.setUserId(user.getUserId());
		partner.setType(TPartner.TYPE_DEFAULT);
		partner = partnerDao.find(partner, true);
		if(partner  == null){
			logger.warn(String.format("member %d has no partner", user.getUserId()));
		} else{
			uo.setPid(partner.getFuId());
			uo.setPartnerId(partner.getPartnerId());
		}
		return uo;
	}
	
	public int removeMember(long id, long creatorId){
		TMember member = new TMember();
		member.setMemberId(id);
		member.setCreatorId(creatorId);
		Session session = genealogyDao.getSession(member);
		Transaction tran = session.beginTransaction();
		try {
			TMemberExtend memberExt = new TMemberExtend();
			memberExt.setMemberId(id);
			int rows = memberExtDao.remove(session, memberExt, true);
			if(rows < 0){
				logger.warn("remove member extend error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			logger.info(String.format("delete %d member extend", rows));
			
			TMemberRight memberRight = new TMemberRight();
			memberRight.setMemberId(id);
			rows = memberRightDao.remove(session, memberRight, true);
			if(rows < 0){
				logger.warn("remove member right error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			logger.info(String.format("delete %d member right", rows));
			
			if(!memberDao.delete(session, member)){
				logger.warn("delete member error");
				tran.rollback();
				return ErrorCodeUtil.ERROR_NOT_EXIST;
			}
			
			tran.commit();
		} catch (Exception e) {
			// TODO: handle exception
			tran.rollback();
			logger.error(e.getMessage());
			return ErrorCodeUtil.ERROR_SYSTEM;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	/**
	 * 添加已有成员
	 * @param addMember : com.find.dto.FUMemberAdd
	 * @return
	 */
	public int addMember(FUMemberAdd addMember){
		if (addMember==null) {
			return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
		}
		if (addMember.getRootMemberId()==0 
				|| addMember.getRootMemberCreatorId()==0
				|| addMember.getAddType()==0
				|| addMember.getGenealogyId()==0
				|| addMember.getGenealogyCreatorId()==0) {
			logger.error("添加ERROR:参数错误");
			return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
		}
		
		if (addMember.getAddMember()==null && addMember.getAddMemberId()==0 && addMember.getAddMemberId()==0 ) {
			logger.error("添加ERROR:添加的成员不能为空");
			return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
		}
		TMember member = new TMember();
		member.setMemberId(addMember.getRootMemberId());
		member.setCreatorId(addMember.getRootMemberCreatorId());
		TMember rootMember = memberDao.find(member, true);
		if (rootMember==null) {
			logger.error("添加ERROR:主要关系成员不存在");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		TMember add_member = null;
		switch (addMember.getAddType()) {
		
		case 1:
			// TODO： 添加父亲
			logger.info("Add father>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			if (addMember.getFatherType()==null || addMember.getFatherType()==0) {
				logger.error("添加ERROR:参数错误");
				return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
			}
			
			add_member = InserterMember(addMember);
			if (add_member==null) {
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			
			TFamily f = new TFamily();
			f.setChildId(rootMember.getMemberId());
			f.setChildCreatorId(rootMember.getCreatorId());
			f.setGenealogyId(addMember.getGenealogyId());
			f.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
			if (addMember.getMotherId()!=null &&addMember.getMotherCreatorId()!=null && addMember.getMotherId()!= 0 && addMember.getMotherCreatorId()!=0) {
				f.setMotherId(addMember.getMotherId());
				f.setMotherCreatorId(addMember.getMotherCreatorId());
				f.setMotherType(addMember.getMotherType());
			}
			TFamily tempf = familyDao.find(f, true);
			if (tempf==null) {
				f.setFatherId(add_member.getMemberId());
				f.setFatherCreatorId(add_member.getCreatorId());
				f.setFatherType(addMember.getFatherType());
				return insertFamily(f);
			}else{
				f = tempf;
				if (f.getFatherId()!=null && f.getFatherType()==addMember.getFatherType()) {
					memberDao.delete(add_member);
					return ErrorCodeUtil.ERROR_REPEAT;
				}
				f.setFatherId(add_member.getMemberId());
				f.setFatherCreatorId(add_member.getCreatorId());
				f.setFatherType(addMember.getFatherType());
				return updateFamily(f);
			}
			
		case 2:
			// TODO： 添加母亲
			logger.info("Add mother>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (addMember.getMotherType()==null || addMember.getMotherType()==0) {
				logger.error("添加ERROR:参数错误");
				return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
			}
			add_member = InserterMember(addMember);
			if (add_member==null) {
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			
			TFamily mf = new TFamily();
			mf.setChildId(rootMember.getMemberId());
			mf.setChildCreatorId(rootMember.getCreatorId());
			mf.setGenealogyId(addMember.getGenealogyId());
			mf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
			if (addMember.getMotherId()!=null &&addMember.getMotherCreatorId()!=null && addMember.getMotherId()!= 0 && addMember.getMotherCreatorId()!=0) {
				mf.setMotherId(addMember.getMotherId());
				mf.setMotherCreatorId(addMember.getMotherCreatorId());
				mf.setMotherType(addMember.getMotherType());
			}
			TFamily tempmf = familyDao.find(mf, true);
			if (tempmf==null) {
				mf.setMotherId(add_member.getMemberId());
				mf.setMotherCreatorId(add_member.getCreatorId());
				mf.setMotherType(addMember.getMotherType());
				return insertFamily(mf);
			}else{
				mf = tempmf;
				if (mf.getMotherId()!=null && mf.getMotherType()==addMember.getMotherType()) {
					memberDao.delete(add_member);
					return ErrorCodeUtil.ERROR_REPEAT;
				}
				mf.setMotherId(add_member.getMemberId());
				mf.setMotherCreatorId(add_member.getCreatorId());
				mf.setMotherType(addMember.getMotherType());
				return updateFamily(mf);
			}
			
		case 3:
			// TODO： 添加配偶
			logger.info("Add spouse>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (addMember.getFatherType()==null && addMember.getMotherType()==null) {
				return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
			}
			add_member = InserterMember(addMember);
			if (add_member==null) {
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			
			if(addMember.getChilds()==null || addMember.getChilds().size()==0){
				TFamily sf = new TFamily();
				sf.setGenealogyId(addMember.getGenealogyId());
				sf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
				if (rootMember.getSexy()==1) {
					sf.setFatherId(rootMember.getMemberId());
					sf.setFatherCreatorId(rootMember.getCreatorId());
					sf.setMotherId(add_member.getMemberId());
					sf.setMotherCreatorId(add_member.getCreatorId());
				}else{
					sf.setFatherId(add_member.getMemberId());
					sf.setFatherCreatorId(add_member.getCreatorId());
					sf.setMotherId(rootMember.getMemberId());
					sf.setMotherCreatorId(rootMember.getCreatorId());
				}
				return insertFamily(sf);
			}else{
				if (addMember.getFatherType()==null && addMember.getMotherType()==null) {
					return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
				}
				if (addMember.getFatherType()!=null && addMember.getMotherType()!=null) {
					return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
				}
				for (TMember childmember: addMember.getChilds()) {
					member.setMemberId(childmember.getMemberId());
					member.setCreatorId(childmember.getCreatorId());
					TMember the_m = memberDao.find(member, true);
					if (the_m==null) {
						memberDao.delete(add_member);
						return ErrorCodeUtil.ERROR_NOT_EXIST;
					}
				}
				for (TMember childmember: addMember.getChilds()) {
					TFamily sf = new TFamily();
					sf.setGenealogyId(addMember.getGenealogyId());
					sf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
					sf.setChildId(childmember.getMemberId());
					sf.setChildCreatorId(childmember.getCreatorId());
					if (addMember.getMotherType()!=null) {
						sf.setFatherId(rootMember.getMemberId());
						sf.setFatherCreatorId(rootMember.getCreatorId());
						
						TFamily temp_sf = familyDao.find(sf, true);
						if (temp_sf==null) {

							memberDao.delete(add_member);
							return ErrorCodeUtil.ERROR_NOT_EXIST;
						}
						sf = temp_sf;
						
						sf.setMotherId(add_member.getMemberId());
						sf.setMotherCreatorId(add_member.getCreatorId());
						
						sf.setFatherType(addMember.getMotherType());
						sf.setMotherType(addMember.getMotherType());
					}else{

						sf.setMotherId(rootMember.getMemberId());
						sf.setMotherCreatorId(rootMember.getCreatorId());
						
						TFamily temp_sf = familyDao.find(sf, true);
						if (temp_sf==null) {

							memberDao.delete(add_member);
							return ErrorCodeUtil.ERROR_NOT_EXIST;
						}
						sf = temp_sf;
						sf.setFatherId(add_member.getMemberId());
						sf.setFatherCreatorId(add_member.getCreatorId());
						
						sf.setFatherType(addMember.getFatherType());
						sf.setMotherType(addMember.getFatherType());
					}
					int code = updateFamily(sf);
					if (code!=ErrorCodeUtil.ERROR_SUCCESS) {

						memberDao.delete(add_member);
						return code;
					}
				}
			}
			break;
		case 4:
			// TODO： 添加子女
			logger.info("Add child >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if (addMember.getFatherType()==null && addMember.getMotherType()==null) {
				return ErrorCodeUtil.ERROR_REQUEST_PARAMETER;
			}
			add_member = InserterMember(addMember);
			if (add_member==null) {
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			if (addMember.getFatherId()==null && addMember.getMotherId()==null) {
				TFamily cf = new TFamily();
				
				cf.setGenealogyId(addMember.getGenealogyId());
				cf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
				cf.setChildId(add_member.getMemberId());
				cf.setChildCreatorId(add_member.getCreatorId());
				if (addMember.getFatherType()!=null) {
					cf.setFatherId(rootMember.getMemberId());
					cf.setFatherCreatorId(rootMember.getCreatorId());
					cf.setFatherType(addMember.getFatherType());
				}else{
					cf.setMotherId(rootMember.getMemberId());
					cf.setMotherCreatorId(rootMember.getCreatorId());
					cf.setMotherType(addMember.getMotherType());
				}
				return insertFamily(cf);
			}else{
				TFamily cf = new TFamily();
				cf.setGenealogyId(addMember.getGenealogyId());
				cf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
				cf.setChildId(add_member.getMemberId());
				cf.setChildCreatorId(add_member.getCreatorId());
				if (addMember.getFatherId()!=null) {
					member.setMemberId(addMember.getFatherId());
					member.setCreatorId(addMember.getFatherCreatorId());
					TMember c_fmem = memberDao.find(member, true);
					if (c_fmem==null) {
						memberDao.delete(add_member);
						return ErrorCodeUtil.ERROR_NOT_EXIST;
					}
					cf.setFatherId(addMember.getFatherId());
					cf.setFatherCreatorId(addMember.getFatherCreatorId());
					cf.setFatherType(addMember.getFatherType());
					
					cf.setMotherId(rootMember.getMemberId());
					cf.setMotherCreatorId(rootMember.getCreatorId());
					cf.setMotherType(addMember.getMotherType());
				}else{
					member.setMemberId(addMember.getMotherId());
					member.setCreatorId(addMember.getMotherCreatorId());
					TMember c_mmem = memberDao.find(member, true);
					if (c_mmem==null) {
						memberDao.delete(add_member);
						return ErrorCodeUtil.ERROR_NOT_EXIST;
					}
					cf.setMotherId(addMember.getMotherId());
					cf.setMotherCreatorId(addMember.getMotherCreatorId());
					cf.setMotherType(addMember.getMotherType());
					
					cf.setFatherId(rootMember.getMemberId());
					cf.setFatherCreatorId(rootMember.getCreatorId());
					cf.setFatherType(addMember.getFatherType());
				}
				return insertFamily(cf);
			}
			
		case 5:
			// TODO： 添加兄弟姐妹
			logger.info("Add sibling>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			add_member = InserterMember(addMember);
			if (add_member==null) {
				return ErrorCodeUtil.ERROR_SYSTEM;
			}
			
			TFamily rootf  = new TFamily();
			rootf.setGenealogyId(addMember.getGenealogyId());
			rootf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
			rootf.setChildId(rootMember.getMemberId());
			rootf.setChildCreatorId(rootMember.getCreatorId());
			rootf = familyDao.find(rootf, true);
			if (rootf==null) {
				memberDao.delete(add_member);
				return ErrorCodeUtil.ERROR_NOT_EXIST;
			}
			TFamily cf = new TFamily();
			cf.setGenealogyId(addMember.getGenealogyId());
			cf.setGenealogyCreatorId(addMember.getGenealogyCreatorId());
			cf.setChildId(add_member.getMemberId());
			cf.setChildCreatorId(add_member.getCreatorId());
			if (rootf.getFatherId()!=null) {
				cf.setFatherId(rootf.getFatherId());
				cf.setFatherCreatorId(rootf.getFatherCreatorId());
				cf.setFatherType(rootf.getFatherType());
			}
			if (rootf.getMotherId()!=null) {
				cf.setMotherId(rootf.getMotherId());
				cf.setMotherCreatorId(rootf.getMotherCreatorId());
				cf.setMotherType(rootf.getMotherType());
			}
			return insertFamily(cf);

		default:
			break;
		}
		
		return ErrorCodeUtil.ERROR_SUCCESS;
	}
	
	private TMember InserterMember(FUMemberAdd memberAdd) {
		if (memberAdd!=null) {
			TMember addmember = memberAdd.getAddMember();
			if (addmember==null) {
				TMember member = new TMember();
				member.setMemberId(memberAdd.getAddMemberId());
				member.setCreatorId(memberAdd.getAddMemberCreatorId());
				addmember = memberDao.find(member, true);
			}
			if (addmember==null) {
				return null;
			}
			int code = insertMember(addmember);
			if (code==ErrorCodeUtil.ERROR_SUCCESS) {
				return addmember;
			}
			return null;
			
		}
		return null;
	}

	/**
	 * 修改时间轴
	 * 
	 * @param subject
	 *            subject实体
	 * @return 受影响的记录数
	 * 
	 * @author Hu Xiaobo
	 */
	public int updateSuject(TSubject subject) {
		if (!subjectDao.update(subject)) {
			logger.warn("update subject error");
			return ErrorCodeUtil.ERROR_NOT_EXIST;
		}
		return ErrorCodeUtil.ERROR_SUCCESS;
	}

	/**
	 * 获取某个成员的所有家庭成员信息
	 * 
	 * @param genealogyId
	 *            家谱编号
	 * @param genealogyCreatorId
	 *            家谱创建编号
	 * @param memberId
	 *            成员编号
	 * @return 家庭FUFamilyMember实体集合
	 */
	public FUFamilyMember getFamilyMember(long genealogyId, long genealogyCreatorId, long memberId) {
		TFamily family = new TFamily();
		family.setGenealogyId(genealogyId);
		family.setGenealogyCreatorId(genealogyCreatorId);
		//作为儿子查询父亲和母亲
		family.setChildId(memberId);
		TFamily familyByChildId = familyDao.find(family, true);
		FUFamilyMember familyMember = new FUFamilyMember();
		FUSearchPage page = new FUSearchPage();
		page.setPageSize(Integer.MAX_VALUE);
		if(familyByChildId!=null){
			Long fatherId = familyByChildId.getFatherId();
			Long fatherCreateId = familyByChildId.getFatherCreatorId();
			TFamily tfamily = new TFamily();
			tfamily.setGenealogyId(genealogyId);
			tfamily.setGenealogyCreatorId(genealogyCreatorId);
			if(fatherId!=null){
				familyMember.setFather(getMember(fatherId, fatherCreateId));
				tfamily.setFatherId(fatherId);
			}
			Long motherId = familyByChildId.getMotherId();
			Long motherCreatorId = familyByChildId.getMotherCreatorId();
			if(motherId!=null){
				familyMember.setMother(getMember(motherId, motherCreatorId));
				tfamily.setMotherId(motherId);
			}
			if(!(motherId==null&&fatherId==null)){
				//获取兄弟姐妹
				List<TFamily> familyByBrother = familyDao.search(family, true, null, false, page);
				if(!familyByBrother.isEmpty()){
					for (TFamily tFamily : familyByBrother) {
						Long childId = tFamily.getChildId();
						Long childCreatorId = tFamily.getChildCreatorId();
						//除开自己，添加自己的兄弟姐妹
						if (childId != memberId) {
							familyMember.getBrothers().add(getMember(childId, childCreatorId));
						}
					}
				}
			}
		}
		//作为父亲查询妻子和子女
		family.setChildId(null);
		family.setFatherId(memberId);
		List<TFamily> familyByFatherId = familyDao.search(family, true, null, false, page);
		if(!familyByFatherId.isEmpty()){
			for (TFamily tFamily : familyByFatherId) {
				Long motherId = tFamily.getMotherId();
				Long motherCreatorId = tFamily.getMotherCreatorId();
				if(motherId!=null){
					familyMember.setWife(getMember(motherId, motherCreatorId));
				}
				Long childId = tFamily.getChildId();
				Long childCreatorId = tFamily.getChildCreatorId();
				if(childId!=null){
					familyMember.getChildren().add(getMember(childId, childCreatorId));
				}
			}
		}
		//作为母亲查询丈夫和子女
		family.setFatherId(null);
		family.setMotherId(memberId);
		List<TFamily> familyByMotherId = familyDao.search(family, true, null, false, page);
		if(!familyByMotherId.isEmpty()){
			for (TFamily tFamily : familyByMotherId) {
				Long fatherId = tFamily.getFatherId();
				Long fatherCreatorId = tFamily.getFatherCreatorId();
				if(fatherId!=null){
					familyMember.setHusband(getMember(fatherId, fatherCreatorId));
				}
				Long childId = tFamily.getChildId();
				Long childCreatorId = tFamily.getChildCreatorId();
				if(childId!=null){
					familyMember.getChildren().add(getMember(childId, childCreatorId));
				}
			}
		}
		return familyMember;
	}

	public List<TFamily> getFamilies(TFamily family) {
		FUSearchPage page = new FUSearchPage();
		page.setPageSize(Integer.MAX_VALUE);
		return familyDao.search(family, true, null, false, page);
	}
	
}

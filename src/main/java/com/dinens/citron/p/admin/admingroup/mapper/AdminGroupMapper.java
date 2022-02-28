package com.dinens.citron.p.admin.admingroup.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.admingroup.domain.AdminGroupVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;

public interface AdminGroupMapper {
	public int selectAdminGroupListCount(@Param(value="adminGroupVO") AdminGroupVO adminGroupVO);
	public List<AdminGroupVO> selectAdminGroupList(@Param(value="adminGroupVO") AdminGroupVO adminGroupVO, @Param(value="pagingVO") PagingVO pagingVO);
	public List<Map<String, Object>> selectAdminGroupListExcel(@Param(value="adminGroupVO") AdminGroupVO adminGroupVO);

	public AdminGroupVO selectAdminGroupDetail(AdminGroupVO adminGroupVO);

	public int insertAdminGroup(AdminGroupVO adminGroupVO);
	public int updateAdminGroup(AdminGroupVO adminGroupVO);
	public int deleteAdminGroup(AdminGroupVO adminGroupVO);
}

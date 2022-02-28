package com.dinens.citron.p.admin.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;

public interface AdminMapper {
	
	public int selectAdminListCount(@Param(value="adminVO") AdminVO adminVO);
	public List<AdminVO> selectAdminList(@Param(value="adminVO") AdminVO adminVO, @Param(value="pagingVO") PagingVO pagingVO);
	public List<Map<String, Object>> selectAdminListExcel(@Param(value="adminVO") AdminVO adminVO);

	public AdminVO selectAdminDetail(AdminVO adminVO);

	public int insertAdmin(AdminVO adminVO);
	public int updateAdmin(AdminVO adminVO);
	public int updateAdminPassword(AdminVO adminVO);
	public int deleteAdmin(AdminVO adminVO);
}

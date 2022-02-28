<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<!-- 조회 조건 -->
<div class="row well search-group form-inline">
	<div class="form-group">
		<select id="inputState" class="form-control">
			<option>메뉴 3-1 ID</option>
			<option>메뉴 3-1 이름</option>
		</select> <input type="text" class="form-control" id="searchText"
			name="searchText" placeholder="검색어">
	</div>
	<div class="form-group">
		<label for="exampleInputName2">ID 유형</label> <select
			id="inputState" class="form-control">
			<option>전체</option>
			<option>일반 이메일</option>
			<option>페이스북</option>
			<option>네이버</option>
		</select>
	</div>
	<div class="form-group">
		<label for="exampleInputName2">메뉴 3-1 상태</label> <select
			id="inputState" class="form-control">
			<option>전체</option>
			<option>가입 대기</option>
			<option>가입 완료</option>
			<option>탈퇴</option>
		</select>
	</div>
	<div class="form-group"">
		<label for="exampleInputName2">등록일자</label> <input type="text"
			class="form-control datePicker" placeholder="From"> <input
			type="text" class="form-control datePicker" placeholder="To">
	</div>
	<a href="#" class="btn btn-primary" role="button">조회</a>
</div>

<!-- 조회 결과 -->
<!-- Total 건수 및 데이터 다운로드 -->
<div class="row">
	<div class="col-xs-6 table-total">
		<p>
			총 <span>12</span> 건
		</p>
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-success btn-xs">엑셀 다운로드</a>
	</div>
</div>
<!-- 데이터 목록 -->
<div class="row">
	<div class="table-responsive">
		<table class="table table-striped table-top-bordered table-hover">
			<colgroup>
				<col width="70" />
				<col width="" />
				<col width="150" />
				<col width="150" />
				<col width="150" />
				<col width="180" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>메뉴 3-1 ID</th>
					<th>ID 유형</th>
					<th>메뉴 3-1 이름</th>
					<th>메뉴 3-1 상태</th>
					<th>등록 시간</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>1</td>
					<td>gildong-hong@gmail.com</td>
					<td>일반 이메일</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>2</td>
					<td>gildong-hong@gmail.com</td>
					<td>페이스북</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>3</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>4</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>5</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>6</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>7</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>8</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>9</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
				<tr>
					<td>10</td>
					<td>gildong-hong@gmail.com</td>
					<td>네이버</td>
					<td>홍길동</td>
					<td>가입 대기</td>
					<td>2019-08-08 13:59:55</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<!-- 페이징 처리 -->
<div class="row">
	<div class="col-xs-1 perpage">
		<select id="inputState" class="form-control">
			<option selected>10</option>
			<option>15</option>
			<option>20</option>
			<option>50</option>
		</select>
	</div>
	<div class="col-xs-10">
		<nav aria-label="Page navigation" class="text-center">
			<ul class="pagination">
				<li><a href="#" aria-label="Previous"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<li><a href="#">1</a></li>
				<li class="active"><a href="#">2</a></li>
				<li><a href="#">3</a></li>
				<li><a href="#">4</a></li>
				<li><a href="#">5</a></li>
				<li><a href="#">6</a></li>
				<li><a href="#">7</a></li>
				<li><a href="#">8</a></li>
				<li><a href="#">9</a></li>
				<li><a href="#">10</a></li>
				<li><a href="#" aria-label="Next"> <span
						aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>
		</nav>
	</div>
</div>

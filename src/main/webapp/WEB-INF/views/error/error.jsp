<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="error_wrap">
	<div class="error">
		<div class="error_bg">
			<p class="l_logo"><img src="/images/error_bg.jpg" alt="" /></p>
			<div class="error_txt">
				<p class="tit">${error.code}</p>
				<p class="point">${error.status}</p>
				<p class="tt">${error.message}</p>
			</div>
			<div class="btn_group">
				<a href="/main/main" class="btn btn-default">Home</a>
				<a href="javascript:history.back();" class="btn btn-default">Back</a>
			</div>
		</div>
	</div>
</div>

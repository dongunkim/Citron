
/**
 * 
* jqGrid의 공통 설정에 대한 Wrapper
*  
*/

(function($){

	$.extend({
		jqGridWrapper: function (options) {
			
			var settings = $.extend({				
				mtype: "POST",				
				datatype: "json",
				postData: searchData,
				page: (isNull(searchData.nowPage) ? 1 : searchData.nowPage),
				autowidth: true,
				height: 350,
				rowNum: (isNull(searchData.rowNum) ? 15 : searchData.rowNum),
				rowList: [10, 15, 20, 30],				
				pager: "#jqGridPager",				
				rownumbers: true,
				caption: false,
				cmTemplate: { sortable: false },
				loadonce: false,
				styleUI : 'Bootstrap',
				colNames:  [],
				colModel: [],
				prmNames: { page: 'nowPage', rows: 'countPerPage' },			
				gridComplete: function() {					
				},
				loadError: function(jqXHR, textStatus, errorThrown) {
					jqGridLoadErrorHandler($(this), jqXHR, textStatus, errorThrown);
				},
				loadComplete: function(obj) {
					if( obj==undefined ) {
			   			return;
			   		}
					$("#totalCount").text(numberWithCommas(obj.pagingVO.totalCount));
					$("#nowPage").val($("#jqGrid").getGridParam('page'));
					$("#rowNum").val($("#jqGrid").getGridParam('rowNum'));					
				},
				loadError: function(xhr, status, error) {
					alert("Status : " + xhr.responseJSON.code + "("+ xhr.responseJSON.status + ")" +
							"\nMessage : " + xhr.responseJSON.message +
								"\n시스템 관리자에게 문의 하거나, 잠시후 다시 시도 바랍니다.")
					
					console.log("xhr : ");
					console.log(xhr);					
				},
				jsonReader: {
					root   : function(obj) {return obj.list;},
				    records: function(obj) {return obj.pagingVO.totalCount;},
				    page   : function(obj) {return obj.pagingVO.nowPage;},
				    total  : function(obj) {return obj.pagingVO.totalPage;},
				    repeatitems: false,
				    repeat: false
				},
				onSelectRow: function(rowid) {					
				},
				onPaging: function(pgButton) {					
					if (pgButton == 'next_jqGridPager')
						$("#nowPage").val($("#jqGrid").getGridParam('page') + 1);
					else if (pgButton == 'prev_jqGridPager')
						$("#nowPage").val($("#jqGrid").getGridParam('page') - 1);
					else if (pgButton == 'last_jqGridPager')
						$("#nowPage").val($("#jqGrid").getGridParam('lastpage'));
					else if (pgButton == 'first_jqGridPager')
						$("#nowPage").val(1);
					else if (pgButton == 'user') {
						var totalPage = $("#jqGrid").getGridParam('total');
						var userInput = Number($("#jqGridPager .ui-pg-input").val());					
						if (totalPage < userInput)
							$("#nowPage").val($("#jqGrid").getGridParam('lastpage'));
						else if (totalPage >= userInput && userInput > 0) {
							$("#nowPage").val(userInput);
						}					
					}					
				}
			}, options);
			
			$("#jqGrid").jqGrid(settings);
		},
		
		jqGridNoPagingWrapper: function (id,options) {
		
		var settings = $.extend({				
			mtype: "POST",				
			datatype: "json",
			postData: searchData,
			autowidth: true,
			caption: false,
			cmTemplate: { sortable: false },
			loadonce: false,
			styleUI : 'Bootstrap',
			colNames:  [],
			colModel: [],
			gridComplete: function() {					
			},
			loadError: function(jqXHR, textStatus, errorThrown) {
				jqGridLoadErrorHandler($(this), jqXHR, textStatus, errorThrown);
			},
			loadError: function(xhr, status, error) {
				alert("Status : " + xhr.responseJSON.code + "("+ xhr.responseJSON.status + ")" +
						"\nMessage : " + xhr.responseJSON.message +
							"\n시스템 관리자에게 문의 하거나, 잠시후 다시 시도 바랍니다.")
				
				console.log("xhr : ");
				console.log(xhr);					
			},
			jsonReader: {
				root   : function(obj) {return obj.list;},
			    repeatitems: false,
			    repeat: false
			},
			onSelectRow: function(rowid) {					
			},
			onPaging: function(pgButton) {
				if (pgButton == 'next_jqGridPager')
					$("#nowPage").val($("#jqGrid").getGridParam('page') + 1);
				else if (pgButton == 'prev_jqGridPager')
					$("#nowPage").val($("#jqGrid").getGridParam('page') - 1);
				else if (pgButton == 'last_jqGridPager')
					$("#nowPage").val($("#jqGrid").getGridParam('lastpage'));
				else if (pgButton == 'first_jqGridPager')
					$("#nowPage").val(1);
				else if (pgButton == 'user') {
					var totalPage = $("#jqGrid").getGridParam('total');
					var userInput = Number($("#jqGridPager .ui-pg-input").val());					
					if (totalPage < userInput)
						$("#nowPage").val($("#jqGrid").getGridParam('lastpage'));
					else if (totalPage >= userInput && userInput > 0) {
						$("#nowPage").val(userInput);
					}
				}
			}
		}, options);
		
		$(id).jqGrid(settings);
	}

	});
})(jQuery);
package com.mage.util;

import java.util.List;

/**
 * 分页工具类
 * @author Cushier
 * @param <T>
 *
 */
public class Page<T> {

	private Integer pageNum; // 当前页（一般情况都是前台传递；若前台未传递，后台默认第一页）
	private Integer PageSize; // 每页显示的数量（若前台传递，则以前台的值为准；若未传递则默认后台值）
	private Integer totalCount; // 查询数据的总数量（数据库查询，count()）
	
	private Integer totalPages; // 总页数（查询数据的总数量/每页显示的数量，向上取整）
	private Integer prePage; // 上一页（当前页-1；若当前页-1小于1，则上一页为1）
	private Integer nextPage; // 下一页（当前页+1；若当前页+1大于总页数，则下一页为页数）
	private Integer startNavPage; // 导航开始数（当前页-5；若当前页-5的值小于1，导航开始数为1，结束数为导航开始数+9）
	private Integer endNavPage; // 导航结束数（当前页+4；若当前页+4的值大于总页数，导航结束数为总页数，开始数为导航结束数-9）
	
	private List<T> datas; // 每页要显示的数据集合（数据根据当前页及每页显示数量查询到的集合）

	
	
	public Page(Integer pageNum, Integer pageSize, Integer totalCount) {
		super();
		this.pageNum = pageNum;
		this.PageSize = pageSize;
		this.totalCount = totalCount;
		
		// 总页数（查询数据的总数量/每页显示的数量，向上取整）
		totalPages = (int) Math.ceil(totalCount * 1.0 / pageSize);
		
		// 上一页（当前页-1；若当前页-1小于1，则上一页为1）
		prePage = pageNum - 1 < 1 ? 1 : pageNum - 1;
		// 下一页（当前页+1；若当前页+1大于总页数，则下一页为页数）
		nextPage = pageNum + 1 > totalPages ? totalPages : pageNum + 1;
		
		// 导航开始数（当前页-5；若当前页-5的值小于1，导航开始数为1，结束数为导航开始数+9）
		startNavPage = pageNum - 5;
		// 导航结束数（当前页+4；若当前页+4的值大于总页数，导航结束数为总页数，开始数为导航结束数-9）
		endNavPage = pageNum + 4;
		
		// 判断导航开始数是否小于1
		if(startNavPage < 1){
			startNavPage = 1;
			endNavPage = startNavPage + 9 > totalPages ? totalPages : startNavPage+9;
		}
		
		// 导航结束数大于总页数
		if(endNavPage > totalPages){
			endNavPage = totalPages;
			startNavPage = endNavPage - 9 < 1 ? 1 : endNavPage - 9;
		}
		
		
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return PageSize;
	}

	public void setPageSize(Integer pageSize) {
		PageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getPrePage() {
		return prePage;
	}

	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public Integer getStartNavPage() {
		return startNavPage;
	}

	public void setStartNavPage(Integer startNavPage) {
		this.startNavPage = startNavPage;
	}

	public Integer getEndNavPage() {
		return endNavPage;
	}

	public void setEndNavPage(Integer endNavPage) {
		this.endNavPage = endNavPage;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	
	
}

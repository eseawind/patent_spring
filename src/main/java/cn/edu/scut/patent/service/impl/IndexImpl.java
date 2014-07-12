package cn.edu.scut.patent.service.impl;

public interface IndexImpl {
	
	/**
	 * 数据库索引程序的执行
	 * 
	 * @throws Exception
	 */
	void doIndexFromDatabase() throws Exception;

	/**
	 * 逐个PDF索引程序的执行
	 * 
	 * @throws Exception
	 */
	void doIndexFromPDF() throws Exception;
	
	/**
	 * 数据库索引程序的执行前所需要的准备工作
	 */
	void doIndexPrework();
}

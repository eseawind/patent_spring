package cn.edu.scut.patent.dao;

import org.hibernate.Session;
import cn.edu.scut.patent.model.Triz;

public class TrizDao extends TotalDao {

	/**
	 * 检查表是否为空
	 * 
	 * @param session
	 */
	public static Boolean isEmpty(Session session) {
		return isEmpty(session, "TRIZ");
	}

	/**
	 * 清空表
	 * 
	 * @param session
	 */
	public static void cleanTable(Session session) {
		cleanTable(session, "TRIZ");
	}

	/**
	 * 保存单个TRIZ
	 * 
	 * @param session
	 * @param triz
	 */
	public void save(Session session, Triz triz) {
		if (find(session, triz.getTrizNum()) == null) {
			save(session, (Object) triz);
		}
	}

	/**
	 * 查询
	 * 
	 * @param session
	 * @param triz
	 * @return
	 */
	public Triz find(Session session, int trizNum) {
		session.beginTransaction();
		Triz triz = (Triz) session.get(Triz.class, trizNum);
		session.getTransaction().commit();
		return triz;
	}

	/**
	 * 填充所有TRIZ
	 * 
	 * @param session
	 */
	public void fillTriz(Session session) {

		Triz triz1 = new Triz();
		triz1.setTrizNum(1);
		triz1.setTrizText("分割原理");
		save(session, triz1);

		Triz triz2 = new Triz();
		triz2.setTrizNum(2);
		triz2.setTrizText("抽出原理");
		save(session, triz2);

		Triz triz3 = new Triz();
		triz3.setTrizNum(3);
		triz3.setTrizText("局部特性原理");
		save(session, triz3);

		Triz triz4 = new Triz();
		triz4.setTrizNum(4);
		triz4.setTrizText("不对称原理");
		save(session, triz4);

		Triz triz5 = new Triz();
		triz5.setTrizNum(5);
		triz5.setTrizText("组合原理");
		save(session, triz5);

		Triz triz6 = new Triz();
		triz6.setTrizNum(6);
		triz6.setTrizText("多用性原理");
		save(session, triz6);

		Triz triz7 = new Triz();
		triz7.setTrizNum(7);
		triz7.setTrizText("嵌套原理");
		save(session, triz7);

		Triz triz8 = new Triz();
		triz8.setTrizNum(8);
		triz8.setTrizText("反重力原理");
		save(session, triz8);

		Triz triz9 = new Triz();
		triz9.setTrizNum(9);
		triz9.setTrizText("预先反作用原理");
		save(session, triz9);

		Triz triz10 = new Triz();
		triz10.setTrizNum(10);
		triz10.setTrizText("预先作用原理");
		save(session, triz10);

		Triz triz11 = new Triz();
		triz11.setTrizNum(11);
		triz11.setTrizText("预先防范原理");
		save(session, triz11);

		Triz triz12 = new Triz();
		triz12.setTrizNum(12);
		triz12.setTrizText("等势原理");
		save(session, triz12);

		Triz triz13 = new Triz();
		triz13.setTrizNum(13);
		triz13.setTrizText("反向作用原理");
		save(session, triz13);

		Triz triz14 = new Triz();
		triz14.setTrizNum(14);
		triz14.setTrizText("曲面化原理");
		save(session, triz14);

		Triz triz15 = new Triz();
		triz15.setTrizNum(15);
		triz15.setTrizText("动态性原理");
		save(session, triz15);

		Triz triz16 = new Triz();
		triz16.setTrizNum(16);
		triz16.setTrizText("不足或过量作用原理");
		save(session, triz16);

		Triz triz17 = new Triz();
		triz17.setTrizNum(17);
		triz17.setTrizText("多维化原理");
		save(session, triz17);

		Triz triz18 = new Triz();
		triz18.setTrizNum(18);
		triz18.setTrizText("振动原理");
		save(session, triz18);

		Triz triz19 = new Triz();
		triz19.setTrizNum(19);
		triz19.setTrizText("周期性动作原理");
		save(session, triz19);

		Triz triz20 = new Triz();
		triz20.setTrizNum(20);
		triz20.setTrizText("有效持续作用原理");
		save(session, triz20);

		Triz triz21 = new Triz();
		triz21.setTrizNum(21);
		triz21.setTrizText("急速作用原理");
		save(session, triz21);

		Triz triz22 = new Triz();
		triz22.setTrizNum(22);
		triz22.setTrizText("变害为益原理");
		save(session, triz22);

		Triz triz23 = new Triz();
		triz23.setTrizNum(23);
		triz23.setTrizText("反馈原理");
		save(session, triz23);

		Triz triz24 = new Triz();
		triz24.setTrizNum(24);
		triz24.setTrizText("中介原理");
		save(session, triz24);

		Triz triz25 = new Triz();
		triz25.setTrizNum(25);
		triz25.setTrizText("自服务原理");
		save(session, triz25);

		Triz triz26 = new Triz();
		triz26.setTrizNum(26);
		triz26.setTrizText("复制原理");
		save(session, triz26);

		Triz triz27 = new Triz();
		triz27.setTrizNum(27);
		triz27.setTrizText("一次性用品替代原理");
		save(session, triz27);

		Triz triz28 = new Triz();
		triz28.setTrizNum(28);
		triz28.setTrizText("替换机械系统原理");
		save(session, triz28);

		Triz triz29 = new Triz();
		triz29.setTrizNum(29);
		triz29.setTrizText("气压或液压结构原理");
		save(session, triz29);

		Triz triz30 = new Triz();
		triz30.setTrizNum(30);
		triz30.setTrizText("柔性壳体或薄膜结构原理");
		save(session, triz30);

		Triz triz31 = new Triz();
		triz31.setTrizNum(31);
		triz31.setTrizText("多孔材料原理");
		save(session, triz31);

		Triz triz32 = new Triz();
		triz32.setTrizNum(32);
		triz32.setTrizText("变换颜色原理");
		save(session, triz32);

		Triz triz33 = new Triz();
		triz33.setTrizNum(33);
		triz33.setTrizText("同质原理");
		save(session, triz33);

		Triz triz34 = new Triz();
		triz34.setTrizNum(34);
		triz34.setTrizText("自弃与修复原理");
		save(session, triz34);

		Triz triz35 = new Triz();
		triz35.setTrizNum(35);
		triz35.setTrizText("状态和参数变化原理");
		save(session, triz35);

		Triz triz36 = new Triz();
		triz36.setTrizNum(36);
		triz36.setTrizText("相变原理");
		save(session, triz36);

		Triz triz37 = new Triz();
		triz37.setTrizNum(37);
		triz37.setTrizText("热膨胀原理");
		save(session, triz37);

		Triz triz38 = new Triz();
		triz38.setTrizNum(38);
		triz38.setTrizText("强氧化作用原理");
		save(session, triz38);

		Triz triz39 = new Triz();
		triz39.setTrizNum(39);
		triz39.setTrizText("惰性介质原理");
		save(session, triz39);

		Triz triz40 = new Triz();
		triz40.setTrizNum(40);
		triz40.setTrizText("复合材料原理");
		save(session, triz40);
	}
}

package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.model.Account;
import cn.edu.scut.patent.model.Setting;
import cn.edu.scut.patent.service.AccountService;
import cn.edu.scut.patent.service.SettingService;

@Controller
public class AccountController {

	/**
	 * 登录验证操作
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "login")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String email = request.getParameter("loginEmail");
		String password = request.getParameter("loginPassword");

		Account account = new AccountService().find(email);
		RequestDispatcher re;
		if (account != null) {
			if (account.getPassword().equals(password)) {
				request.getSession()
						.setAttribute("Account", account.getEmail());
				String accountType = account.getAccountType();
				if (accountType.equals("user")) {
					re = request.getRequestDispatcher("view/search.jsp");
				} else if (accountType.equals("classifier")) {
					re = request.getRequestDispatcher("view/search.jsp");
				} else {
					request.getSession().setAttribute("UNCHECKACCOUNT",
							getUncheckAccount().toString());
					re = request.getRequestDispatcher("view/administrator.jsp");
				}
				System.out.println(email + " 登录成功");
			} else {
				re = request.getRequestDispatcher("index.jsp");
				System.out.println(email + " 登录密码不正确");
			}
		} else {
			re = request.getRequestDispatcher("index.jsp");
			System.out.println("该账户不存在");
		}

		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 注册操作
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "register")
	public void register(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String email = request.getParameter("registerEmail");
		String accountType = request.getParameter("registerSelect");
		String username = request.getParameter("registerName");
		String department = request.getParameter("registerDepartment");
		String password = request.getParameter("registerPassord");

		Account account;
		if (department == null) {
			account = new Account(email, accountType, username, password);
		} else {
			account = new Account(email, accountType, username, department,
					password);
		}
		// 查看是否开启了自动审核
		Setting autopass = new SettingService().find("autopass");
		if (autopass.getFlag().equals("0")) {
			account.setPass("0");
		} else if (autopass.getFlag().equals("1")) {
			account.setPass("1");
		}
		new AccountService().save(account);

		RequestDispatcher re;
		request.getSession().setAttribute("Account", email);
		if (accountType.equals("user")) {
			re = request.getRequestDispatcher("view/search.jsp");
		} else if (accountType.equals("classifier")) {
			re = request.getRequestDispatcher("view/search.jsp");
		} else {
			request.getSession().setAttribute("UNCHECKACCOUNT",
					getUncheckAccount().toString());
			re = request.getRequestDispatcher("view/administrator.jsp");
		}
		System.out.println(email + " 注册成功");

		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 注销操作
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exit")
	public void exit(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out
				.println(request.getSession().getAttribute("Account") + " 退出");
		request.getSession().removeAttribute("Account");
		RequestDispatcher re = request.getRequestDispatcher("index.jsp");

		try {
			re.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新账户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateAccount")
	public void updateAccount(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String email = request.getParameter("updateEmail");
		String pass = request.getParameter("updatePass");

		Account account = new AccountService().find(email);
		account.setPass(pass);
		new AccountService().update(account);
		System.out.println(email + " 更新成功");
	}

	/**
	 * 获取自动审核功能的参数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getAutopass")
	public void getAutopass(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String result = "";
		// 检查自动审核是否拥有参数，如果没有，则添加上去
		Setting autopass = new SettingService().find("autopass");
		if (autopass == null) {
			Setting setting = new Setting();
			setting.setFunction("autopass");
			setting.setFlag("0");
			new SettingService().save(setting);
			System.out.println("自动审核没有参数，新建后默认为0");
			result = "0";
		} else {
			result = autopass.getFlag();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);

		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(jsonObj.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新自动审核功能
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "updateAutopass")
	public void updateAutopass(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String flag = request.getParameter("updateFlag");

		Setting setting = new SettingService().find("autopass");
		setting.setFlag(flag);
		new SettingService().update(setting);
		System.out.println("自动审核功能更新成功");
	}

	/**
	 * 获取没有审核的账户数据
	 * 
	 * @return
	 */
	public JSONArray getUncheckAccount() {
		List<Account> accountList = new AccountService().getUncheckAccount();
		JSONArray jsonArray = new JSONArray();
		for (Account account : accountList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Email", account.getEmail());
			jsonObj.put("AccountType", account.getAccountType());
			jsonObj.put("Username", account.getUsername());
			jsonObj.put("Department", account.getDepartment());
			jsonObj.put("Pass", account.getPass());
			jsonArray.put(jsonObj);
		}
		return jsonArray;
	}
}

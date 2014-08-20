package cn.edu.scut.patent.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.edu.scut.patent.model.Account;
import cn.edu.scut.patent.service.AccountService;

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
		new AccountService().save(account);

		RequestDispatcher re;
		request.getSession().setAttribute("Account", email);
		if (accountType.equals("user")) {
			re = request.getRequestDispatcher("view/search.jsp");
		} else if (accountType.equals("classifier")) {
			re = request.getRequestDispatcher("view/search.jsp");
		} else {
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
}

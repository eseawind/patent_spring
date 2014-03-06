package cn.edu.scut.patent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.edu.scut.patent.javabean.LoginForm;

@Controller
public class LoginController {
	@RequestMapping(value = "login")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response, LoginForm command) {
		String username = command.getUsername();
		ModelAndView mv = new ModelAndView("/index", "command",
				"LOGIN SUCCESS, " + username);
		return mv;
	}
}

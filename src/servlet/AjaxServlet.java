package servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2018/1/28 0028.
 */
public class AjaxServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("-----------init----------------");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决上传参数中文乱码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        //获取web.xml中的初始值
        System.out.println("-----config---------"+getServletConfig());
        ServletConfig servletConfig=getServletConfig();
        System.out.println("-----test-----"+servletConfig.getInitParameter("test"));
        System.out.println("-----test1-------"+servletConfig.getInitParameter("test1"));
        //普通get/post请求（post请求传送json字符串的时候，不能用此方法）
        System.out.println("-----name-------"+req.getParameter("username"));
        System.out.println("-----password-------"+req.getParameter("password"));

        //前端提交的数据是json格式的字符串数据时，需要以下方式接收数据
        ServletInputStream is = req.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        String line=null;
        while((line=br.readLine())!=null){
            System.out.println("------------------------------------------values-----------------------------");
            System.out.println(line); //{"username":"汤姆","password":"123"}
        }

        PrintWriter pw=resp.getWriter();
        pw.write("SUCCESS!!!!!");
        pw.close();
//        req.getRequestDispatcher("WEB-INF/views/success.html").forward(req,resp);
    }

    @Override
    public void destroy() {
        System.out.println("------------------destroy--------------");
    }


}

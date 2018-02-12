package servlet;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

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
//        ServletConfig servletConfig=getServletConfig();
//        //普通get/post请求（post请求传送json字符串的时候，不能用此方法）
//        System.out.println("-----name-------"+req.getParameter("username"));
//        System.out.println("-----password-------"+req.getParameter("password"));
//
//        //前端提交的数据是json格式的字符串数据时，需要以下方式接收数据
//        ServletInputStream is = req.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
//        String line=null;
//        while((line=br.readLine())!=null){
//            System.out.println("------------------------------------------values-----------------------------");
//            System.out.println(line); //{"username":"汤姆","password":"123"}
//        }
//
//        PrintWriter pw=resp.getWriter();
//        pw.write("SUCCESS!!!!!");
//        pw.close();
////        req.getRequestDispatcher("WEB-INF/views/success.html").forward(req,resp);
        BASE64Decoder decoder = new BASE64Decoder();
        BASE64Encoder encoder = new BASE64Encoder();
        String bb64 = "";
        String addr = "jdbc:oracle:thin:@192.168.169.200:1521:orcl";
        String user = "fsgadtfj";
        String psw = "fsgadtfj2017";
        String sql = "select t.test,t.img_file from TEST_9_30 t where t.test='a351c6f12a12484fb500caae1a215d01'";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(addr, user, psw);
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                if (!bb64.equals("")) {
                    bb64 = bb64 + "<SP>";
                }
                bb64 = bb64+ res.getString("test");
                Blob blob = res.getBlob("img_file");
                if (blob != null) {
                    InputStream in = blob.getBinaryStream();
                    long nLen = blob.length();
                    int nSize = (int) nLen;
                    byte[] data = new byte[nSize];
                    in.read(data);
                    in.close();
//                    bb64 = bb64 + ",img_file:" +new String(data,"utf8");
                    bb64 = bb64 + ";;data:image/png;base64," + encoder.encode(data);
                }
            }
            if (bb64.equals("")) {
                bb64 = "0,查无信息!";
            }
        } catch (SQLException e) {
            bb64 = "-5," + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            bb64 = "-5,err!";
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                bb64 = "-5,err!";
                e.printStackTrace();
            }
        }
        req.setAttribute("dataString", bb64);
        req.getRequestDispatcher("WEB-INF/views/result.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("------------------destroy--------------");
    }


}

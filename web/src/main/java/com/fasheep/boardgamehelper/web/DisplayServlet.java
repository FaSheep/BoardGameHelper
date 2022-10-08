package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/display.do")
public class DisplayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        super.doGet(request, response);
        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            response.setStatus(500);
            return;
        }
        Cookie[] cookies = request.getCookies();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
//        System.out.println("cookie l = " + cookies.length);
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (id.equals(c.getName())) {
                    writer.println(getHTML(c.getValue().replace("_", " "), "From cookie"));
                    writer.flush();
                    writer.close();
                    response.setStatus(200);
                    return;
                }
            }
        }
        if (RoomManager.getRoom(id) == null) {
            response.setStatus(500);
            writer.println("invalid id");
            writer.flush();
            writer.close();
            return;
        }

        response.setStatus(200);
        String message = RoomManager.getRoom(id).getText();

        Cookie cookie = new Cookie(id, message.replace(" ", "_"));
        cookie.setMaxAge(60 * 60 * 5);
        response.addCookie(cookie);


        writer.write(getHTML(message, RoomManager.getRoom(id).getPlayer()));
        writer.flush();
        writer.close();

    }

    private String getHTML(String text, String text2) {
        final String html = "<html><head><title>Role</title></head><body><h1 style=\"font-size:6em\">%s<br>%s</h1></body></html>";
        return String.format(html, text, text2);
    }
}

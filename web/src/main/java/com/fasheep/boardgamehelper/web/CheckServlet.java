package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "CheckServlet", value = "/check.do")
public class CheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAll(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAll(request, response);
    }

    private void doAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookie = request.getCookies();
        if (cookie != null) {
            for (Cookie c : cookie) {
                if (c.getName().equals("admin") && c.getValue().equals("admin")) {
                    response.setCharacterEncoding("utf-8");
                    PrintWriter writer = response.getWriter();
                    Set<String> keys = RoomManager.getKeySet();
                    int num;
                    if ((num = keys.size()) > 0) {
                        writer.println(num);
                        for (String s : keys) {
                            writer.println(s);
                        }
                    } else {
                        writer.println(0);
                    }
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}

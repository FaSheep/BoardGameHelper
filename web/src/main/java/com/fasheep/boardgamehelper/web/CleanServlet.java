package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CleanServlet", value = "/clean.do")
public class CleanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doAll(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doAll(request);
    }

    private static void doAll(HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        if (cookie != null) {
            for (Cookie c : cookie) {
                if (c.getName().equals("admin") && c.getValue().equals("admin")) {
                    RoomManager.clean();
                }
            }
        }
    }
}

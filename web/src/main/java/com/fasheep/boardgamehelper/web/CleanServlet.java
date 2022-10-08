package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CleanServlet", value = "/CleanServlet")
public class CleanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookie = request.getCookies();
        if (cookie != null) {
            if (cookie[0].getName().equals("fasheep") && cookie[0].getValue().equals("fashionlyy")) {
                RoomManager.clean();
            }
//            cookie[0].getValue();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookie = request.getCookies();
        if (cookie != null) {
            if (cookie[0].getName().equals("fasheep") && cookie[0].getValue().equals("fashionlyy")) {
                RoomManager.clean();
            }
        }
    }
}

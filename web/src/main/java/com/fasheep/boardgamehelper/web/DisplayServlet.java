package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.Room;
import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DisplayServlet", value = "/display.do")
public class DisplayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            response.setStatus(500);
            writer.print("invalid id");
            writer.flush();
            writer.close();
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (id.equals(c.getName())) {
                    response.setStatus(200);
                    writer.print(c.getValue().replace("_", " "));
                    writer.flush();
                    writer.close();
                    return;
                }
            }
        }
        Room room;
        if ((room = RoomManager.getRoom(id)) == null) {
            response.setStatus(500);
            writer.println("invalid id");
            writer.flush();
            writer.close();
            return;
        }
        String message = room.getPlayer();
        if (message.contains(";|,|=|\"|/|\\|?|@|:|(|)|[|]|<|>|{|}|\t|'")) {
            response.setStatus(500);
            writer.println("invalid role name");
            writer.flush();
            writer.close();
            return;
        }

        Cookie cookie = new Cookie(id, message.replace(" ", "_"));
        cookie.setMaxAge(60 * 60 * 5);
        response.addCookie(cookie);

        writer.write(message);
        writer.flush();
        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}

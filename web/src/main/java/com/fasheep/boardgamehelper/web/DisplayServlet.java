package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.Room;
import com.fasheep.boardgamehelper.core.RoomManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@WebServlet(name = "DisplayServlet", value = "/display.do")
public class DisplayServlet extends HttpServlet {
    private static String HTML;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/html/display.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((temp = reader.readLine()) != null) {
                stringBuilder.append(temp);
            }
            reader.close();
            HTML = stringBuilder.toString();
        } catch (NullPointerException | IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAll(request, response, HTML);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doAll(request, response, "%d");
    }

    private static void doAll(HttpServletRequest request, HttpServletResponse response, String format) throws IOException {
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
                    writer.print(String.format(format, c.getValue().replace("_", " ")));
                    writer.flush();
                    writer.close();
                    return;
                }
            }
        }
        Room room;
        if ((room = RoomManager.getRoom(id)) == null) {
            response.setStatus(500);
            writer.print("invalid id");
            writer.flush();
            writer.close();
            return;
        }
        String message = room.getPlayer();
        if (Pattern.compile("[;,=\"\\[\\]()/@:<>{}\n\r\t']").matcher(message).find()) {
            response.setStatus(500);
            writer.print("invalid role name");
            writer.flush();
            writer.close();
            return;
        }

        Cookie cookie = new Cookie(id, message.replace(" ", "_"));
        cookie.setMaxAge(60 * 60 * 5);
        response.addCookie(cookie);
        writer.print(String.format(format, message));
        writer.flush();
        writer.close();
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse response) {
//        super.doOptions(req, response);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
//        response.
    }
}

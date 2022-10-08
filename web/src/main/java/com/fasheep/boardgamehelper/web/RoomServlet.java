package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.Room;
import com.fasheep.boardgamehelper.core.RoomManager;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

@WebServlet("/add.do")
public class RoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        super.doGet(request, response);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();

        writer.println("<html>");
        writer.println("<head><title>no get</title></head>");
        writer.println("<body>");
        writer.println("<p>no get</p>");
        writer.println("</body>");
        writer.println("<html>");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        String id = request.getHeader("date");
        if (id == null || "".equals(id)) {
            writer.println("fail");
            System.out.println("fail:no id");
            response.setStatus(500);
            writer.flush();
            writer.close();
            return;
        }

        InputStream is = request.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String temp;
        StringBuilder stringBuilder = new StringBuilder();
        while ((temp = reader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        reader.close();

        System.out.println(stringBuilder);

        Room room = null;
        try {
            room = Room.getInstanceFromJson(stringBuilder.toString());
        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
            response.setStatus(500);
            writer.flush();
            writer.close();
            return;
        }
//        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

//        writer.println(stringBuilder);
        if (room == null) {
            writer.println("fail");
            response.setStatus(500);
            System.out.println("fail:room == null");
            writer.flush();
            writer.close();
            return;
        } else {
            writer.println("success");
            response.setStatus(200);
            room.rearrange();
            RoomManager.addRoom(id, room);
            System.out.println("success:" + id);
        }
        writer.flush();
        writer.close();
//        RoomManager.addRoom(String.valueOf(i++), Room.getInstanceFromJson(stringBuilder.toString()));
//        super.doPost(req, resp);
/*        PrintWriter writer = response.getWriter();
        String json;
        Gson gson = new Gson();
        Room room = null;
        try {
            room = Room.getInstanceFromJson(json);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        writer.flush();
        writer.close();*/
    }
}

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

@WebServlet(name = "RoomServlet", value = "/add.do")
public class RoomServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        String id = request.getHeader("date");
        if (id == null || "".equals(id)) {
            response.setStatus(500);
            writer.print("invalid id");
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

        Room room = null;
        try {
            room = Room.getInstanceFromJson(stringBuilder.toString());
        } catch (JsonSyntaxException e) {
            System.err.println(e.getLocalizedMessage());
        }

        if (room == null) {
            writer.print("json convert fail");
            response.setStatus(500);
            writer.flush();
            writer.close();
        } else {
            writer.print("success");
            writer.flush();
            writer.close();
            response.setStatus(200);
            room.rearrange();
            RoomManager.addRoom(id, room);
            System.out.printf("success:%s", id);
        }
    }
}

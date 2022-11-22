package com.fasheep.boardgamehelper.web;

import com.fasheep.boardgamehelper.core.Encoder;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet(name = "ShareServlet", value = "/share.do")
public class ShareServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        String id = request.getParameter("id");
        if (id == null || id.isBlank() || !id.matches("\\d{13}")) {
            response.setStatus(500);
            writer.print("invalid id");
            writer.flush();
            writer.close();
            return;
        }
        String url = "http://" + request.getHeader("host") + "/web/display.do?id=" + id;
        Encoder encoder = new Encoder(Encoder.EncoderType.UTF8);
        BitMatrix matrix = null;
        try {
            matrix = encoder.getMatrix(url);
        } catch (WriterException e) {
            System.err.println(e.getMessage());
        }
        BufferedImage image = null;
        if (matrix != null) {
            image = MatrixToImageWriter.toBufferedImage(matrix);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(image, "jpg", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        Base64.Encoder base64encoder = Base64.getEncoder();
        String png_base64 = base64encoder.encodeToString(bytes);//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        png_base64 = "data:image/jpg;base64," + png_base64;
//        System.out.println("值为：" + "data:image/jpg;base64," + png_base64);
        writer.println("<html><body>" +
                "<h1 style='text-align: center; margin: 5vw 0 1vw 0;'>" +
                url +
                "</h1>" +
                "<div style='display: flex; justify-content: center;'><img style='width: 70vmin' src='" + png_base64 + "'></div>" +
                "</body></html>");
        writer.flush();
        writer.close();


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/jpeg");
        response.addHeader("Access-Control-Allow-Origin", "*");
        InputStream is = request.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String temp;
        StringBuilder stringBuilder = new StringBuilder();
        while ((temp = reader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        reader.close();

        Encoder encoder = new Encoder(Encoder.EncoderType.UTF8);
        BitMatrix matrix = null;
        try {
            matrix = encoder.getMatrix(stringBuilder.toString());
        } catch (WriterException e) {
            System.err.println(e.getMessage());
        }
        OutputStream out = response.getOutputStream();
        if (matrix != null) {
            MatrixToImageWriter.writeToStream(matrix, "JPEG", out);
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse response) {
//        super.doOptions(req, response);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
//        response.
    }
}

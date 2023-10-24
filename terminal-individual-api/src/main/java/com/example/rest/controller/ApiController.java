package com.example.rest.controller;

import com.example.rest.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Controller
@RequestMapping("/api/connection")
public class ApiController {

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private QRCodeService qrCodeService;


    @GetMapping
    public String index(Model model) throws IOException {
        InetAddress myIP = null;
        try{
            myIP = InetAddress.getLocalHost();
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
        String IPAddress = myIP.getHostAddress();    // return ip address of a machine
        Integer port = serverProperties.getPort();
        model.addAttribute("text", "Api is working!");
        model.addAttribute("qrCodeContent", "/api/connection/generateQRCode?qrContent=" + "http://" + IPAddress + ":" + port);
        return "index";
    }

    @GetMapping("/generateQRCode")
    public void generateQRCode(String qrContent, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        byte[] qrCode = qrCodeService.generateQRCode(qrContent, 400, 400);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrCode);
    }
}

package com.example.rest.service;

public interface QRCodeService {
    byte[] generateQRCode(String qrContent, int width, int height);
}

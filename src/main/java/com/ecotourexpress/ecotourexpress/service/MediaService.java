package com.ecotourexpress.ecotourexpress.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {

    @Autowired
    private Cloudinary cloudinary;

    private static final List<String> EXTENSIONES_PERMITIDAS = Arrays.asList("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    //Sube una lista de imágenes a Cloudinary y devuelve sus URLs.
    @SuppressWarnings("rawtypes")
    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            validarArchivo(file);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            urls.add((String) uploadResult.get("url"));
        }
        return urls;
    }

    /**
     * Elimina múltiples imágenes de Cloudinary usando sus URLs.
     */
    public void deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        List<String> publicIds = imageUrls.stream()
                .map(this::obtenerPublicId)
                .collect(Collectors.toList());

        try {
            cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.err.println("Error al eliminar imágenes: " + e.getMessage());
        }
    }

    private void validarArchivo(MultipartFile file) {
        if (!EXTENSIONES_PERMITIDAS.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato no permitido. Solo JPG, PNG y WEBP.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo no puede superar los 5 MB.");
        }
    }

    private String obtenerPublicId(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
    }
}

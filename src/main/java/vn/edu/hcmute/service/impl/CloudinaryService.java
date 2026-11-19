package vn.edu.hcmute.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryService {
    public String uploadAvatar(Part file) throws IOException {
        if (file == null || file.getSize() == 0) return null;
        if (file.getSize() > 5L * 1024 * 1024) throw new IOException("Image must be 5 MB or smaller.");
        String mime = file.getContentType();
        if (!"image/jpeg".equals(mime) && !"image/png".equals(mime) && !"image/gif".equals(mime)) {
            throw new IOException("Only JPG, PNG or GIF images are allowed.");
        }
        try (InputStream stream = file.getInputStream()) {
            if (ImageIO.read(stream) == null) throw new IOException("Invalid image file.");
        }
        String url = System.getenv("CLOUDINARY_URL");
        if (url == null || !url.startsWith("cloudinary://")) {
            throw new IOException("CLOUDINARY_URL is not configured.");
        }
        Cloudinary cloudinary = new Cloudinary(url);
        byte[] bytes;
        try (InputStream stream = file.getInputStream()) { bytes = stream.readAllBytes(); }
        Map<?, ?> result = cloudinary.uploader().upload(bytes,
                ObjectUtils.asMap("folder", "javawebmvc/profiles", "resource_type", "image"));
        String secureUrl = String.valueOf(result.get("secure_url"));
        if (!secureUrl.startsWith("https://") || secureUrl.length() > 1000) {
            throw new IOException("Cloudinary returned an invalid image URL.");
        }
        return secureUrl;
    }
}

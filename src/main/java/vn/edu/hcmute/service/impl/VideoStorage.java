package vn.edu.hcmute.service.impl;

import jakarta.servlet.http.Part;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class VideoStorage {
    private static final Map<String,String> TYPES=Map.of(
            ".mp4","video/mp4", ".webm","video/webm", ".ogg","video/ogg");
    private static final long MAX_SIZE=100L*1024*1024;

    public Path directory() throws IOException {
        String env=System.getenv("JWM_VIDEO_DIR");
        Path dir=(env==null||env.isBlank() ?
                Path.of(System.getProperty("user.home"),"JavaWebMVC-JPA","videos") : Path.of(env))
                .toAbsolutePath().normalize();
        return Files.createDirectories(dir);
    }
    public String save(Part part) throws IOException {
        if(part==null||part.getSize()==0) throw new IOException("Please choose a video file.");
        if(part.getSize()>MAX_SIZE)throw new IOException("Video file must be under 100 MB.");
        String name=Optional.ofNullable(part.getSubmittedFileName()).orElse("").toLowerCase(Locale.ROOT);
        String extension=TYPES.keySet().stream().filter(name::endsWith).findFirst().orElse(null);
        if(extension==null || !TYPES.get(extension).equalsIgnoreCase(part.getContentType()))
            throw new IOException("Only MP4, WebM, and OGG videos are allowed.");
        String stored=UUID.randomUUID().toString()+extension;
        Path file=directory().resolve(stored);
        try(InputStream in=part.getInputStream()) {
            Files.copy(in,file,StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) { Files.deleteIfExists(file); throw e; }
        return stored;
    }
    public Path resolve(String name) throws IOException {
        if(name==null||!name.matches("[0-9a-f-]{36}\\.(mp4|webm|ogg)"))
            throw new FileNotFoundException("Invalid video file");
        Path dir=directory();
        Path file=dir.resolve(name).normalize();
        if(!file.startsWith(dir))throw new FileNotFoundException("Invalid video path");
        return file;
    }
    public void delete(String name) {
        try { Files.deleteIfExists(resolve(name)); } catch(IOException ignored) { /* Missing file is harmless. */ }
    }
    public String contentType(String name) {
        String ext=name.substring(name.lastIndexOf('.'));
        return TYPES.getOrDefault(ext,"application/octet-stream");
    }
}

package vn.edu.hcmute.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.dao.VideoDAO;
import vn.edu.hcmute.model.Video;
import vn.edu.hcmute.service.impl.VideoStorage;
import java.io.*;
import java.nio.file.*;

@WebServlet("/video/file")
public class VideoFileController extends HttpServlet {
    private final VideoDAO dao=new VideoDAO();
    private final VideoStorage storage=new VideoStorage();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        long id;
        try {id=Long.parseLong(req.getParameter("id"));}
        catch(Exception ex){resp.sendError(400);return;}
        Video video=dao.find(id);
        if(video==null){resp.sendError(404);return;}
        Path path;
        try {path=storage.resolve(video.getFileName());}
        catch(IOException ex){resp.sendError(404);return;}
        if(!Files.isRegularFile(path)){resp.sendError(404);return;}
        long size=Files.size(path),start=0,end=size-1;
        if(size==0){resp.sendError(404);return;}
        String range=req.getHeader("Range");
        boolean partial=false;
        if(range!=null) {
            try {
                if(!range.matches("bytes=[0-9]*-[0-9]*"))throw new NumberFormatException();
                String[] bounds=range.substring(6).split("-",-1);
                if(bounds[0].isBlank()) {
                    long suffix=Long.parseLong(bounds[1]);
                    if(suffix<=0)throw new NumberFormatException();
                    start=Math.max(0,size-suffix);
                } else {
                    start=Long.parseLong(bounds[0]);
                    if(!bounds[1].isBlank())end=Math.min(end,Long.parseLong(bounds[1]));
                }
                if(start>=size||end<start)throw new NumberFormatException();
                partial=true;
            } catch(NumberFormatException ex) {
                resp.setStatus(416);resp.setHeader("Content-Range","bytes */"+size);return;
            }
        }
        resp.setContentType(storage.contentType(video.getFileName()));
        resp.setHeader("Accept-Ranges","bytes");
        resp.setHeader("X-Content-Type-Options","nosniff");
        resp.setHeader("Content-Disposition","inline");
        resp.setContentLengthLong(end-start+1);
        if(partial){resp.setStatus(206);resp.setHeader("Content-Range","bytes "+start+"-"+end+"/"+size);}
        try(RandomAccessFile file=new RandomAccessFile(path.toFile(),"r");OutputStream out=resp.getOutputStream()) {
            file.seek(start);long remaining=end-start+1;
            byte[] buffer=new byte[64*1024];
            while(remaining>0){
                int n=file.read(buffer,0,(int)Math.min(buffer.length,remaining));
                if(n<0)break;
                out.write(buffer,0,n);remaining-=n;
            }
        }
    }
}

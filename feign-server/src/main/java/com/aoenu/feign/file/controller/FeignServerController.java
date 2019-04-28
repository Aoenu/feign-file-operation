package com.aoenu.feign.file.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/25
 */
@RestController
public class FeignServerController {

    /**
     * 文件（二进制数据）下载
     * @param fileType 文件类型
     * @return
     */
    @RequestMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(String fileType, HttpServletRequest request ){

        System.out.println(request.getParameter("fileType"));
        System.out.println("参数fileType: "+fileType);

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> entity = null;
        InputStream in=null;
        try {
            in=new FileInputStream(new File("d:/myImg/001.png"));

            byte[] bytes = new byte[in.available()];

            String imageName="001.png";

            //处理IE下载文件的中文名称乱码的问题
            String header = request.getHeader("User-Agent").toUpperCase();
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                imageName = URLEncoder.encode(imageName, "utf-8");
                imageName = imageName.replace("+", "%20");    //IE下载文件名空格变+号问题
            } else {
                imageName = new String(imageName.getBytes(), "iso-8859-1");
            }

            in.read(bytes);

            headers.add("Content-Disposition", "attachment;filename="+imageName);

            entity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(in!=null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entity;
    }

}

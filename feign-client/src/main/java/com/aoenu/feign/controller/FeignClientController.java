package com.aoenu.feign.controller;

import com.aoenu.feign.service.FileFeignService;
import com.aoenu.feign.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/25
 */
@RestController
public class FeignClientController {

    @Autowired
    private FileFeignService feignService;


    @ResponseBody
    @RequestMapping("/user_downloadFile")
    public Object userDownloadFile(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam("fileType") String fileType) {
        ResponseEntity<byte[]> entity = feignService.downloadFile(fileType);
        System.out.println(entity.getStatusCode());
        return entity;
    }

    
    @PostMapping(value = "/uploadNewFiles")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile multipartFile) {

        // 文件名
        String fileName = multipartFile.getOriginalFilename();
        // 文件类型
        String fileType = FileUtils.getFileType(fileName);
        //将相对路径转换成绝对路径
        String realPath = this.getClass().getResource("").getPath();
        //将file写入指定的路径
        File file = new File(realPath, fileName);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("11");
    }

}

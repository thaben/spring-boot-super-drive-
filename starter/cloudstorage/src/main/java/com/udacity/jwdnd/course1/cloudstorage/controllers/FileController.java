package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.udacity.jwdnd.course1.cloudstorage.entities.Files;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;

@Controller
@RequestMapping("/files")
public class FileController   implements HandlerExceptionResolver {

    private FilesService filesService;

    public FileController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @RequestParam(required = false, name = "fileid") Integer fileId) {

        Files file = filesService.getFileByFileId(fileId);
        byte[] fileData = file.getFiledata();
        String fileName = file.getFilename();
        String contentType = file.getContenttype();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                             .contentType(MediaType.parseMediaType(contentType))
                             .body(resource);
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("fileUpload") MultipartFile fileUpload,
            HttpSession httpSession
    ) throws IOException {

        Integer userId = (Integer) httpSession.getAttribute("userId");

        if (fileUpload.isEmpty()) {
            return "redirect:/result?isSuccess=" + false;
        }

        if (filesService.isFileNameTaken(fileUpload.getOriginalFilename(), userId).booleanValue()) {
            return "redirect:/result?isSuccess=" + false;
        }

        Boolean isSuccess = filesService.save(fileUpload, userId);

        return "redirect:/result?isSuccess=" + isSuccess;
    }

    @GetMapping("/delete")
    public String deleteFile(
            @RequestParam(required = true, name = "fileid") Integer fileId) {
        Boolean isSuccess = filesService.delete(fileId);
        return "redirect:/result?isSuccess=" + isSuccess;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        Map<String, Object> model = new HashMap<String, Object>();
        if (e instanceof MaxUploadSizeExceededException) {
            return new ModelAndView("redirect:/result?isSuccess=false", model);
        }
        return new ModelAndView("/upload", model);

    }
}
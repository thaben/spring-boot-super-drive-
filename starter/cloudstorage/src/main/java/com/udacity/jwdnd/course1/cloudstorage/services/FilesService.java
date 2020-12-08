package com.udacity.jwdnd.course1.cloudstorage.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.entities.Files;
import com.udacity.jwdnd.course1.cloudstorage.repository.FilesRepository;

@Service
public class FilesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesService.class);


    private FilesRepository filesRepository;

    public FilesService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }


    public List<Files> getAllFilesByUserId(Integer userid) {

        try {
            List<Files> files = filesRepository.findByUserId(userid);
            LOGGER.info("Files found {} for userid{}", files, userid);
            if (files == null) {
                return Collections.emptyList();
            }
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Boolean isFileNameTaken(String fileName, Integer userId) {
        return filesRepository.findByFileNameAndUserId(fileName, userId).size() > 0;
    }

    public Boolean save(MultipartFile fileUpload, Integer userId) throws IOException {
        Files file = new Files();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
            file.setUserid(userId);
        } catch (IOException e) {
            throw e;
        }

        try {
            return filesRepository.save(file) > 0;
        } catch (Exception e) {
            throw e;
        }

    }

    public Boolean delete(Integer fileId) {
        try {
            return filesRepository.delete(fileId) > 0;
        } catch (Exception e) {
            LOGGER.error("Error while deleting file with fileId {}", fileId);
            throw e;
        }

    }

    public Files getFileByFileId(Integer fileId) {
        try {
            return filesRepository.findBy(fileId);
        } catch (Exception e) {
            LOGGER.error("Error while fetching file with fileId {}", fileId);
            throw e;
        }
    }
}
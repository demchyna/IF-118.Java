package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialService {

    MaterialResponse findById(Integer id);
    MaterialResponse create(MaterialRequest materialRequest, MultipartFile file);
    Material getById(Integer id);
    DownloadFileResponse downloadById(Integer id);
    void delete(Integer id, Integer currentUserId);
    void setExpirationDate(LocalDateTime expirationDate, Integer materialId, List<Integer> groupIds);
}

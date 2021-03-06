package top.yeonon.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    String upload(MultipartFile file, String path, String remotePath, String avatarName);

    String uploadMulitFile(MultipartFile file, String path, String remotePath);
}

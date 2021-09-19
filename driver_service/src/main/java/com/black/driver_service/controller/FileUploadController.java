package com.black.driver_service.controller;

import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.time.ZoneId;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.black.driver_service.exception.ResourceNotFoundException;
//import com.black.driver_service.model.Driver;
import com.black.driver_service.model.DriverUpload;
import com.black.driver_service.model.FileInfo;
import com.black.driver_service.payload.Response;
//import com.black.driver_service.repository.DriverRepository;
import com.black.driver_service.repository.DriverUploadRepository;
import com.black.driver_service.service.FileStorageService;

@CrossOrigin(origins = "*", allowedHeaders = "*") //{"http://localhost:3000", "http://localhost:80"})
@RestController
@RequestMapping("")
public class FileUploadController {
	@Autowired
    private FileStorageService fileStorageService;
	
	@Autowired
	private DriverUploadRepository driverUploadRepository;

    @PostMapping("/uploadFile")
    public Response uploadFile(@RequestParam("id") long id, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+02:30"));
        String created = localDateTime.toString();
        String modified = created;

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName)
            .toUriString();
//        System.out.println(fileDownloadUri);
        DriverUpload driverUpload = new DriverUpload(id, fileName, "photo", created, modified);
        driverUploadRepository.save(driverUpload);
        
        return new Response(fileName, fileDownloadUri,
            file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List <Response> uploadMultipleFiles(@RequestParam("id") long id, @RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
            .stream()
            .map(file -> uploadFile(id, file))
            .collect(Collectors.toList());
    }
    
    // get driver by id rest API
    @GetMapping("/files/driver-id/{id}")
    public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable long id) {
        List<FileInfo> driverUploads = driverUploadRepository.findAllByDriverId(id).stream().map(path->{
                String filename = path.getName();
                String url = MvcUriComponentsBuilder
                             .fromMethodName(FileUploadController.class, "getFile", path.getName()).build().toString();

        return new FileInfo(filename, url);
      }).collect(Collectors.toList());
      return ResponseEntity.status(HttpStatus.OK).body(driverUploads);
    }
    
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        Resource file = fileStorageService.load(filename);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}

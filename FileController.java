package com.example.filecompressiondecompression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final CompressionService compressionService;

    @Autowired
    public FileController(CompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @PostMapping(value = "/compress")
    public ResponseEntity<byte[]> compressFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("compressorType") String compressorType) {
        try {
            byte[] compressedData = compressionService.compressFile(file.getBytes(), compressorType);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compressed." + compressorType);
            return ResponseEntity.ok().headers(headers).body(compressedData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to compress file: " + e.getMessage()).getBytes());
        }
    }


    @PostMapping(value = "/decompress")
    public ResponseEntity<byte[]> decompressFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("compressorType") String compressorType) throws IOException {
        byte[] decompressedData = compressionService.decompressFile(file.getBytes(), compressorType);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=decompressed.file");
        return ResponseEntity.ok().headers(headers).body(decompressedData);
    }
}
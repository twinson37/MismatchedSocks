package MismatchedSocks.socksAI.controller;

import MismatchedSocks.socksAI.domain.Img;
import MismatchedSocks.socksAI.domain.ImgNoMember;
import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.service.ImgService;
import MismatchedSocks.socksAI.service.ImgServiceNoSession;
import MismatchedSocks.socksAI.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImgApiController {

    private final ImgServiceNoSession imgService;
    private final MemberService memberService;


    @ApiOperation(value = "이미지 등록", notes = "이미지를 업로드 가능합니다.")
    @PostMapping(value = "/img-upload")
    public ResponseEntity<?> uploadImageToFileSystem(HttpServletRequest request, @RequestParam("image") MultipartFile file) throws IOException, InterruptedException {
        log.info("uploadImage request!!");

//        HttpSession session = request.getSession();
//        if (session == null) {
//            log.info("No Session");
//            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
//                    .body("login first");
//        }
//        if(session.getAttribute("loginMember")==null){
//            log.info("No Cookie");
//            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
//                    .body("login first");
//        }
//        Member member = (Member) session.getAttribute("loginMember");
        Long img_id = imgService.storeImg(file);
        String uploadImage = imgService.findOne(img_id).getName();
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);


    }

    @ApiOperation(value = "이미지 전체 조회", notes = "업로드 된 이미지를 조회 가능 합니다.")
    @GetMapping("/img")
    public Result images(){
        List<ImgNoMember> images = imgService.findImages();
        List<ImageDTONoMember> collect = images.stream()
                .map(m-> new ImageDTONoMember(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }
    @ApiOperation(value = "이미지 반환", notes = "탐색 이미지 반환.")
    @ResponseBody
    @PostMapping(
            value = "/image-response",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public  byte[] downloadImage() throws IOException {
        Path path = Path.of(detectedImagePath());
        System.out.println("path = " + path);
//        imgService.deleteFolder(String.valueOf(path));
        File f = new File(String.valueOf(path));
        if(f.isDirectory()){
            System.out.println("디렉터리거나 파일 없음");
            File file = new File(System.getProperty("user.home")+"/eunhy/404.png");
            InputStream imageStream = new FileInputStream(String.valueOf(file));

            return IOUtils.toByteArray(imageStream);
        }else{
            if(f.exists()) {
                System.out.println("파일 존재");
                InputStream imageStream = new FileInputStream(String.valueOf(path));
                return IOUtils.toByteArray(imageStream);

            } else {
                System.out.println("파일 없음");
                File file = new File(System.getProperty("user.home")+"/eunhy/404.png");
                InputStream imageStream = new FileInputStream(String.valueOf(file));

                return IOUtils.toByteArray(imageStream);

            }
        }

//        InputStream in = getClass().getResourceAsStream(imageStream);
    }
    @ApiOperation(value = "이미지 다운", notes = "탐색 이미지 다운로드.")
    @GetMapping("/image-download")
    public ResponseEntity<Resource> downloadAttach() throws IOException {
        Path path = Path.of(detectedImagePath());
        Path directoryPath = Path.of(System.getProperty("user.home"));

        UrlResource resource = new UrlResource("file:" +
                path);
        File file = resource.getFile();
//        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode("detected.jpeg",
                StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, "application/download")
                .header(HttpHeaders.CONTENT_LENGTH,file.length()+"")
                .body(resource);
    }

    @ApiOperation(value = "이미지 등록 후 바로 양말 탐색", notes = "제곧내")
    @PostMapping(value = "img-req-resp",
            produces = MediaType.IMAGE_JPEG_VALUE)

    public byte[] uploadImageDetect(HttpServletRequest request, @RequestParam("image") MultipartFile file) throws IOException, InterruptedException {

        imgService.storeImg(file);
        Path path = Path.of(detectedImagePath());

//        String path = Path.of(System.getProperty("user.home"))+"/runs/detect/exp/detected.jpeg";
        InputStream imageStream = new FileInputStream(String.valueOf(path));
        return IOUtils.toByteArray(imageStream);

    }

    private String detectedImagePath() {
        File directory = new File(System.getProperty("user.home")+"/runs/detect/exp/");
        String name = new String();

        if (directory.exists() && directory.isDirectory()) {
            // 디렉터리 내의 폴더 목록을 가져옵니다.
            File[] folders = directory.listFiles(File::isFile);

            // 각 폴더의 이름을 출력합니다.
            for (File folder : folders) {
                name = folder.getName();
                System.out.println(folder.getName());
            }
        } else {
            System.out.println("디렉터리가 존재하지 않거나 디렉터리가 아닙니다.");
        }
        return Path.of(System.getProperty("user.home"))+"/runs/detect/exp/"+name;
    }

    @Data
    @AllArgsConstructor
    public static class ImageDTO{
        private String user_id;
        private String name;
    }
    @Data
    @AllArgsConstructor
    public static class ImageDTONoMember{
        private String file_name;
    }

    @Data
    @AllArgsConstructor
    public static class Result<T>{
        private T data;
    }

//    @PostMapping(value = "/img")
//    public ResponseEntity<?> uploadImageToFileSystem(@RequestBody @Valid CreateImgRequest request) throws IOException {
//
//        Img img = new Img();
//        img.setMember(memberService.findOne(request.getUser_id()));
//
//        Long img_id = imgService.storeImg(request.getFile());
//        String uploadImage = imgService.findOne(img_id).getName();
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//
//
//    }

//    @Data
//    static class CreateImgRequest{
//        private Long user_id;
//        private MultipartFile file;
//    }

}

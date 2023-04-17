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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
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

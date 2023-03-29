package MismatchedSocks.socksAI.service;

import MismatchedSocks.socksAI.domain.Img;
import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.repository.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImgService {
    private final Path directoryPath = Path.of(System.getProperty("user.home"));

//    Path currentPath = Paths.get("");
//    String path = currentPath.toAbsolutePath().toString();
//    private String path1 = path;
    private final String fileDir = directoryPath+"/img/";
    public String getFullPath(String filename) {
        return fileDir + filename;
    }
    private final ImgRepository ImgRepository;
    private final MemberService memberService;

    public void saveItem(Img item){
        ImgRepository.save(item);
    }

//    public List<File> findFiles(){
//        return fileRepository.findAll();
//    }

    public  Img findOne(Long itemId){
        return ImgRepository.findOne(itemId);
    }
    @Transactional
    public Long storeImg(MultipartFile multipartFile, HttpSession session) throws IOException {

        File file = new File(String.valueOf(fileDir));
        System.out.println(fileDir);
        // 디렉토리 생성
        file.mkdirs();
//        try{
//            // 디렉토리 생성
//            Files.createDirectory(directoryPath);
//
//            System.out.println(directoryPath + " 디렉토리가 생성되었습니다.");
//
//        } catch (FileAlreadyExistsException e) {
//            System.out.println("디렉토리가 이미 존재합니다");
//        } catch (NoSuchFileException e) {
//            System.out.println("디렉토리 경로가 존재하지 않습니다");
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
        if (multipartFile.isEmpty()) {
            return null;
        }
        Img img = new Img();
        img.setName(multipartFile.getOriginalFilename());
        Member loginMember = (Member) session.getAttribute("loginMember");
        Member member = memberService.findOne(loginMember.getId());
//        System.out.println("!!!member = " + member.getUser_id());
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename,img);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        img.setUUID(storeFileName);
        img.setMember(member);

        ImgRepository.save(img);
        return img.getId();
    }
    private String createStoreFileName(String originalFilename,Img img) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}

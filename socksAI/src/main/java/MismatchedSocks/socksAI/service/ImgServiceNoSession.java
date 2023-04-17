package MismatchedSocks.socksAI.service;

import MismatchedSocks.socksAI.domain.Img;
import MismatchedSocks.socksAI.domain.ImgNoMember;
import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.repository.ImgRepository;
import MismatchedSocks.socksAI.repository.ImgRepositoryNoSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
@RequiredArgsConstructor
public class ImgServiceNoSession {


        private final Path directoryPath = Path.of(System.getProperty("user.home"));

        //    Path currentPath = Paths.get("");
//    String path = currentPath.toAbsolutePath().toString();
//    private String path1 = path;
        private final String fileDir = directoryPath+"/img/";
        public String getFullPath(String filename) {
            return fileDir + filename;
        }
        private final ImgRepositoryNoSession imgRepository;
        private final MemberService memberService;
        String storeFileName;

        public void saveItem(ImgNoMember img){
            imgRepository.save(img);
        }

//    public List<File> findFiles(){
//        return fileRepository.findAll();
//    }

        public  ImgNoMember findOne(Long itemId){
            return imgRepository.findOne(itemId);
        }
        @Transactional
        public Long storeImg(MultipartFile multipartFile) throws IOException, InterruptedException {

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
            ImgNoMember img = new ImgNoMember();
            img.setName(multipartFile.getOriginalFilename());
//        System.out.println("!!!member = " + member.getUser_id());
            String originalFilename = multipartFile.getOriginalFilename();
            storeFileName = createStoreFileName(originalFilename,img);
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
            img.setUUID(storeFileName);

            imgRepository.save(img);
            py_detect();
            return img.getId();
        }
        private String createStoreFileName(String originalFilename,ImgNoMember img) {
            String ext = extractExt(originalFilename);
            String uuid = UUID.randomUUID().toString();

            return uuid + "." + ext;
        }

        private String extractExt(String originalFilename) {
            int pos = originalFilename.lastIndexOf(".");
            return originalFilename.substring(pos + 1);
        }

        public List<ImgNoMember> findImages() {
            return imgRepository.findAll();
        }

//        private static PythonInterpreter interpreter;

        public void py_detect() throws IOException, InterruptedException {

            String dir = String.format("%s/MismatchedSocks/hub.py",directoryPath);
            System.out.println("dir = " + dir);
            ProcessBuilder pb = new ProcessBuilder("python3", dir,storeFileName);
            pb.directory(new File(String.valueOf(directoryPath)));
            Process p = pb.start();
            System.out.println("start");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            try{
                System.out.println("first try");

                String line="";
                while((line = br.readLine())!=null){
                    System.out.println("readline");

                    System.out.println(">>> " + line);

                }
            }catch(Exception e){
                System.out.println("Exception");

                e.printStackTrace();
            }finally {
                System.out.println("finally");

                try{
                    System.out.println("try");

                    if(br!=null){
                        System.out.println("br.close");

                        br.close();
                    }
                }catch(Exception e){
                    System.out.println("execption2");

                    e.printStackTrace();
                }

            }

        }

}

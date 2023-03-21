package MismatchedSocks.socksAI.controller;

import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    @ApiOperation(value = "회원가입", notes = "회원가입이 가능합니다")
    @PostMapping("/join")
    public ResponseEntity<String> saveMember(@Valid @ModelAttribute CreateMemberRequest request) {
        Member member = new Member();
        member.setUser_id(request.getUser_id());
        member.setPassword(request.getPassword());
        member.setAddress(request.getAddress());
        member.setName(request.getName());
        member.setPhonenumber((request.getPhonenumber()));

        memberService.join(member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(request.getUser_id());
    }

    @ApiOperation(value = "로그인", notes = "로그인이 가능합니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @ModelAttribute LoginForm form,
                                        BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body("error");
        }
        Member loginMember = memberService.login(form.getLoginId(),form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body("wrong password");
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//        log.info("member: {}",loginMember);
//        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        member.g
//        log.info("session: {}",session.getAttribute(SessionConst.LOGIN_MEMBER));
//        log.info(member.getUser_id());
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginMember.getUser_id());
    }

    @ApiOperation(value = "로그아웃", notes = "로그인 했을 시 가능합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("logout");
    }

    @ApiOperation(value = "멤버 전체 조회", notes = "가입된 모든 멤버를 조회 가능 합니다.")
    @GetMapping("/members")
    public Result members(){
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m-> new MemberDto(m.getUser_id()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @ApiOperation(value = "멤버 조회", notes = "회원의 아이디를 입력해 가입된 아이디인지 확인할 수 있습니다.")
    @GetMapping("/member")
    public MemberDto members(@ModelAttribute @Valid MemberDto member){
        Optional<Member> findMember = memberService.findByUserId(member.getUser_id());
        if(findMember.isEmpty()){
            return new MemberDto("NO USER");
        }
        return new MemberDto(findMember.get().getUser_id());

    }

    @Data
    @AllArgsConstructor
    public static class MemberDto{
        private String user_id;
    }

    @Data
    @AllArgsConstructor
    public static class Result<T>{
        private T data;
    }

    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private String user_id;
        @NotEmpty
        private String password;
//        @NotEmpty
//        private String id;
//        @NotEmpty
//        private String passwd;
        @NotEmpty
        private String name;
        @NotEmpty
        private String phonenumber;
        @NotEmpty
        private String address;
    }

    @Data
    public static class LoginForm {
        @NotEmpty
        private String loginId;
        @NotEmpty
        private String password;
    }

    public static class SessionConst {
        public static final String LOGIN_MEMBER = "loginMember";
    }
//    @Data
//    static class CreateMemberResponse {
//        private Long id;
//        public CreateMemberResponse(Long id) {
//            this.id = id;
//        }
//    }
}

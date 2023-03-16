package MismatchedSocks.socksAI.controller;

import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> saveMember(@Valid @ModelAttribute CreateMemberRequest request) {
        Member member = new Member();
        member.setUser_id(request.getUser_id());
        member.setPassword(request.getPassword());
        memberService.join(member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(request.getUser_id());
    }

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

    @PostMapping("/logout")
    public ResponseEntity<String> logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("logout");
    }

    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private String user_id;
        @NotEmpty
        private String password;
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

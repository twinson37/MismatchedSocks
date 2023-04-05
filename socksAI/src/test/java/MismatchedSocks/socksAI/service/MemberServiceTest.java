package MismatchedSocks.socksAI.service;

import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void join() throws Exception {
        //given
        Member member = new Member(2L,"memberA","password","memberid","0101223","haksa 4-gil");
        //when
        Long savedId = memberService.join(member);
        Member findMember = memberService.findOne(savedId);
        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }

    @Test(expected = AssertionError.class)
    public void quit() throws Exception{
        Member member = new Member(2L,"memberA","password","memberid","0101223","haksa 4-gil");
        //when
        Long savedId = memberService.join(member);
        memberService.quit(member);
        Member findMember = memberService.findOne(savedId);

        //then
        fail("회원이 없습니다.");

    }

//    @Test
//    public void findMembers() {
//
//    }

//    @Test
//    public void findOne() {
//    }

//    @Test
//    public void findByUserId() {
//    }

    @Test
    public void login() {
        Member member = new Member(2L,"memberA","password","memberid","0101223","haksa 4-gil");
        memberService.join(member);
        Member loginMember = memberService.login(member.getUser_id(),member.getPassword());
        Assertions.assertThat(member).isEqualTo(loginMember);

    }
}
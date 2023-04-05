package MismatchedSocks.socksAI;

import MismatchedSocks.socksAI.domain.Member;
import MismatchedSocks.socksAI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDb {
    private final InitService initService;
    @PostConstruct
    public void init() {
        initService.dbInit1();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("test", "test","test","test","test");
            em.persist(member);

        }

        private Member createMember(String user_id, String password,String name, String adress, String phonenumber) {
            Member member = new Member();
            member.setUser_id(user_id);
            member.setPassword(password);
            member.setAddress(name);
            member.setName(adress);
            member.setPhonenumber(adress);
            return member;
        }
    }

}

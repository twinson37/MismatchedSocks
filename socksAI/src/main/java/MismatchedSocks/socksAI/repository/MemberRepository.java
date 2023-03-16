package MismatchedSocks.socksAI.repository;

import MismatchedSocks.socksAI.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository @RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;
    public void save(Member member) {
        em.persist(member);
    }
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByName(String user_id) {
        return em.createQuery("select m from Member m where m.user_id = :user_id",
                Member.class)
                .setParameter("user_id", user_id)
                .getResultList();
    }

    public Optional<Member> findByLoginId(String user_id) {
        return findAll().stream()
                .filter(m -> m.getUser_id().equals(user_id))
                .findFirst();
    }
}

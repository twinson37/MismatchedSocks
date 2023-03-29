package MismatchedSocks.socksAI.repository;

import MismatchedSocks.socksAI.domain.Img;
import MismatchedSocks.socksAI.domain.ImgNoMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImgRepositoryNoSession {
    private final EntityManager em;

    public void save(ImgNoMember img){
        em.persist(img);
    }

    public ImgNoMember findOne(Long id){
        return em.find(ImgNoMember.class,id);
    }
    public List<ImgNoMember> findAll() {
        return em.createQuery("select i from Img i", ImgNoMember.class)
                .getResultList();
    }
}

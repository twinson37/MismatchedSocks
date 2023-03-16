package MismatchedSocks.socksAI.repository;

import MismatchedSocks.socksAI.domain.Img;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ImgRepository {
    private final EntityManager em;

    public void save(Img img){
        em.persist(img);
    }

    public Img findOne(Long id){
        return em.find(Img.class,id);
    }

//    public List<File> findALl(File file){
//
//        return em.createQuery("select o from File o join o.member m" +
//                        " where m.id = :id", File.class)
//
//                .setParameter("id",file.getMember().getId())
//                .setMaxResults(1000)// max 1000 gae
//                .getResultList();
//    }
}

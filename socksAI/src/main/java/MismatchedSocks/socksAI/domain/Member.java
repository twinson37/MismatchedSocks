package MismatchedSocks.socksAI.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String user_id;
    private String password;
    private String name;
    private String phonenumber;
    private String address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Img> img = new ArrayList<>();

    public Member() {
    }

    public Member(Long id, String user_id, String password, String name, String phonenumber, String address) {
        this.user_id = user_id;
        this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
    }

//    public Member(String user_id, String password) {
//        this.user_id = user_id;
//        this.password = password;
//    }
}

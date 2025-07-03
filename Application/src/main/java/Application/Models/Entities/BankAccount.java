package Application.Models.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bankaccounts")
public class BankAccount {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "balance", nullable = false)
    @Setter
    private Double balance;

    @Column(name = "userlogin", nullable = false)
    @Setter
    private String userLogin;

    @ManyToOne
    @Setter
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public BankAccount(User user) {
        this.balance = 0.0;
        this.userLogin = user.getLogin();
        this.user = user;
    }
}

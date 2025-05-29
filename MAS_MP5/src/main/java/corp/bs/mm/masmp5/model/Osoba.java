package corp.bs.mm.masmp5.model;

import corp.bs.mm.masmp5.enums.typMiejsca;
import corp.bs.mm.masmp5.enums.typOsoby;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long osobaId;

    @ElementCollection(targetClass = typOsoby.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "osoba_role", joinColumns = @JoinColumn(name = "osoba_id"))
    @Column(name = "rola")
    @UniqueElements(message = "role w osobie nie mogą się powtarzać")
    @NotEmpty(message = "Lista ról nie może być pusta")
    private List<typOsoby> role = new ArrayList<>();

    @NotBlank
    private String imie;

    @NotBlank
    private String nazwisko;

    @NotBlank
    @Email(message = "Nieprawidłowy adres e-mail")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Numer telefonu może zawierać tylko cyfry")
    private String telefon;
}

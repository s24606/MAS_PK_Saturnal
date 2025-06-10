package corp.bs.mm.masmp5.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Polaczenie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long polaczenieId;

    @NotBlank
    private String oznaczeniePolaczenia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kursujacy_id", nullable = false, updatable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Pociag pociagKursujacy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @OrderBy("planowanyCzasOdjazdu ASC")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Postoj> postoje = new ArrayList<>();

    @AssertTrue(message = "Do polaczenia moze byc przypisany jeden postoj bez planowanego czasu odjazdu")
    private boolean isJedenPostojKoncowy() {
        if (postoje==null) {
            return true;
        }else{
            int counter=0;
            for(Postoj p:postoje)
            {
                if(p.getPlanowanyCzasOdjazdu()==null)
                    counter++;
            }
            return counter<=1;
        }
    }

    @AssertTrue(message = "Do polaczenia moze byc przypisany jeden postoj bez planowanego czasu przyjazdu")
    private boolean isJedenPostojStartowy() {
        if (postoje==null) {
            return true;
        }else{
            int counter=0;
            for(Postoj p:postoje)
            {
                if(p.getPlanowanyCzasPrzyjazdu()==null)
                    counter++;
            }
            return counter<=1;
        }
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "polaczenie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BiletBezposredni> biletBezposrednie = new HashSet<>();
}

package br.com.jusnexo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClientRating.
 */
@Entity
@Table(name = "client_rating")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClientRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    private Client clientEvaluator;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientRating id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getScore() {
        return this.score;
    }

    public ClientRating score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDescription() {
        return this.description;
    }

    public ClientRating description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return this.client;
    }

    public ClientRating client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClientEvaluator() {
        return this.clientEvaluator;
    }

    public ClientRating clientEvaluator(Client client) {
        this.setClientEvaluator(client);
        return this;
    }

    public void setClientEvaluator(Client client) {
        this.clientEvaluator = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientRating)) {
            return false;
        }
        return id != null && id.equals(((ClientRating) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientRating{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

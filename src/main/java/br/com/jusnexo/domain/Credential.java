package br.com.jusnexo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Credential.
 */
@Entity
@Table(name = "credential")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Credential implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "credential")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credential id(Long id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public Credential password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public Credential username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client getClient() {
        return this.client;
    }

    public Credential client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        if (this.client != null) {
            this.client.setCredential(null);
        }
        if (client != null) {
            client.setCredential(this);
        }
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Credential)) {
            return false;
        }
        return id != null && id.equals(((Credential) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Credential{" +
            "id=" + getId() +
            ", password='" + getPassword() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }
}

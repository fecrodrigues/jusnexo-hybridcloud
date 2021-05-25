package br.com.jusnexo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AreaOfExpertise.
 */
@Entity
@Table(name = "area_of_expertise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AreaOfExpertise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "is_selected")
    private Boolean isSelected;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_area_of_expertise__client",
        joinColumns = @JoinColumn(name = "area_of_expertise_id"),
        inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    @JsonIgnoreProperties(
        value = { "credential", "clientRatings", "clientEvaluatorRatings", "areas", "chatReceivers", "chatSenders" },
        allowSetters = true
    )
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AreaOfExpertise id(Long id) {
        this.id = id;
        return this;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public AreaOfExpertise areaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Boolean getIsSelected() {
        return this.isSelected;
    }

    public AreaOfExpertise isSelected(Boolean isSelected) {
        this.isSelected = isSelected;
        return this;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public AreaOfExpertise clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public AreaOfExpertise addClient(Client client) {
        this.clients.add(client);
        client.getAreas().add(this);
        return this;
    }

    public AreaOfExpertise removeClient(Client client) {
        this.clients.remove(client);
        client.getAreas().remove(this);
        return this;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaOfExpertise)) {
            return false;
        }
        return id != null && id.equals(((AreaOfExpertise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaOfExpertise{" +
            "id=" + getId() +
            ", areaName='" + getAreaName() + "'" +
            ", isSelected='" + getIsSelected() + "'" +
            "}";
    }
}

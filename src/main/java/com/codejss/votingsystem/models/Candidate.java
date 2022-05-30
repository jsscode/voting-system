package com.codejss.votingsystem.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Table(name="candidates")
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    @Column(name = "political_party")
    private String politicalParty;
    private String photo;
    @Column(name = "political_flag")
    private String politicalFlag;

    @Column(name = "numbers_of_votes")
    private Long numbersOfVotes;

    @PrePersist
    private void prePersist(){
        numbersOfVotes = 0L;
    }

    public Candidate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(String politicalParty) {
        this.politicalParty = politicalParty;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPoliticalFlag() {
        return politicalFlag;
    }

    public void setPoliticalFlag(String politicalFlag) {
        this.politicalFlag = politicalFlag;
    }

    public Long getNumbersOfVotes() {
        return numbersOfVotes;
    }

    public void setNumbersOfVotes(Long numbersOfVotes) {
        this.numbersOfVotes = numbersOfVotes;
    }
}

package com.madadipouya.workforce.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.madadipouya.workforce.metadata.UniqueEmail;
import com.madadipouya.workforce.validator.group.Create;
import com.madadipouya.workforce.validator.group.Update;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee",
        indexes = {
                @Index(columnList = "uuid", name = "idx_employee_uuid"), // TODO uuid is indexing little bit dangerous
                @Index(columnList = "email", name = "idx_employee_email")},
        uniqueConstraints = @UniqueConstraint(name = "uc_employee_email", columnNames = {"email"}))
public class Employee extends IdentifiableEntity {
    @Column(name = "email", nullable = false)
    @NotBlank(groups = {Create.class, Update.class})
    @Email(groups = {Create.class, Update.class})
    @UniqueEmail(groups = Create.class)
    @Size(groups = {Create.class, Update.class}, max = 254)
    @ApiModelProperty(position = 2, value = "{Email address of employee", required = true, example = "example@example.com")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotBlank(groups = {Create.class, Update.class})
    @Size(groups = {Create.class, Update.class}, min = 2, max = 128)
    @ApiModelProperty(value = "First name of employee", required = true, example = "John")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(groups = {Create.class, Update.class})
    @Size(groups = {Create.class, Update.class}, min = 2, max = 128)
    @ApiModelProperty(position = 1, value = "Last name of employee", required = true, example = "Wick")
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @ApiModelProperty(position = 3, value = "Date of birth of employee", required = true, example = "1989-07-25")
    private LocalDate birthDate;

    @ElementCollection
    @CollectionTable(name = "hobby", joinColumns = @JoinColumn(name = "employee_uuid"))
    @Column(name = "hobby")
    @ApiModelProperty(position = 4, value = "List of hobbies", allowableValues = "Walking")
    private List<String> hobbies = new ArrayList<>();

    @Column(name = "last_updated_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @ApiModelProperty(hidden = true)
    private ZonedDateTime lastUpdatedDate;

    @Column(name = "creation_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @ApiModelProperty(hidden = true)
    private ZonedDateTime creationDate;

    @PrePersist
    public void onPersist() {
        this.lastUpdatedDate = ZonedDateTime.now();
        this.creationDate = ZonedDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdatedDate = ZonedDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @JsonIgnore
    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @JsonIgnore
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
}

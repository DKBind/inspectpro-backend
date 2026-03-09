package com.inspectpro.entities;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private long id;

    @Column(name = "first_name", columnDefinition = "varchar(50)")
    private String firstName;

    @Column(name = "middle_name", columnDefinition = "varchar(50)")
    private String middleName;

    @Column(name = "last_name", columnDefinition = "varchar(50)")
    private String lastName;

    @Column(name = "email", columnDefinition = "varchar(250)")
    private String email;

    @Column(name = "uuid", unique = true, columnDefinition = "char(36)")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;

    @Column(name = "gender", columnDefinition = "varchar(50)")
    private String gender;

    @Column(name = "web_fcm_token")
    private String webFcmToken;

    @Column(name = "is_new_hire", columnDefinition = "int Default '0'")
    private int isNewHire;

    @Column(name = "unique_identifier", columnDefinition = "varchar(255)")
    private String uniqueIdentifier;

    @Column(name = "image_url", columnDefinition = "varchar(255)")
    private String imageUrl;

    @Column(name = "bio", columnDefinition = "varchar(1000)")
    private String bio;

    @Column(name = "failed_attempt_count", columnDefinition = "int Default '0'")
    private int failedAttemptCount;

    @Column(name = "is_locked", columnDefinition = "int Default '0'")
    private int isLocked;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Column(name = "login_identifier", columnDefinition = "varchar(255)")
    private String loginIdentifier;

    @Column(name = "is_super_admin", columnDefinition = "int Default '0'")
    private int isSuperAdmin;

    @Column(name = "location", columnDefinition = "varchar(50)")
    private String location;

    @Column(name = "remark", columnDefinition = "varchar(1500)")
    private String remark;

    @Column(name = "is_flag", columnDefinition = " int DEFAULT '1'")
    private int isFlag = 1;

}

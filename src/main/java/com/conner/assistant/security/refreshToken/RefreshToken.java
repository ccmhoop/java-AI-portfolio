package com.conner.assistant.security.refreshToken;

import com.conner.assistant.applicationUser.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private ApplicationUser applicationUser;

    private String token;
    private Instant expiryDate;



}



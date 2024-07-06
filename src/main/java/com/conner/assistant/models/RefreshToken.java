package com.conner.assistant.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class RefreshToken {

        @Id
        @GeneratedValue
        private Long id;
        @Getter
        private String token;
        @Getter
        private Instant expiryDate;

        @OneToOne
        @Getter
        @JoinColumn(name = "user_id", referencedColumnName = "userId")
        private ApplicationUser applicationUser;

    }



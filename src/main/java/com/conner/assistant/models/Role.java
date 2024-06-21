package com.conner.assistant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Entity(name="roles")
public class Role implements GrantedAuthority {

   @Getter
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "role_id")
   private Long roleId;

   private String authority;

   public Role(){
       super();
   }

   public Role(String authority){
       this.authority = authority;
   }

    public Role(Long roleId,String authority){
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}

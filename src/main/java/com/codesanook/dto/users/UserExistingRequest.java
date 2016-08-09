package com.codesanook.dto.users;

public class UserExistingRequest {
   private long facebookAppScopeUserId;
   private String email;


   public long getFacebookAppScopeUserId() {
      return facebookAppScopeUserId;
   }

   public void setFacebookAppScopeUserId(long facebookAppScopeUserId) {
      this.facebookAppScopeUserId = facebookAppScopeUserId;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }
}

package com.codesanook.dto;

public class NotificationDto {
 private  int postId;
 private String postTitle;
 private String postAlias;

 public int getPostId() {
  return postId;
 }

 public void setPostId(int postId) {
  this.postId = postId;
 }

 public String getPostTitle() {
  return postTitle;
 }

 public void setPostTitle(String postTitle) {
  this.postTitle = postTitle;
 }

 public String getPostAlias() {
  return postAlias;
 }

 public void setPostAlias(String postAlias) {
  this.postAlias = postAlias;
 }
}

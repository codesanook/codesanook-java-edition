package com.codesanook.service;

import com.codesanook.dto.TagDto;
import com.codesanook.dto.emails.EmailDto;
import com.codesanook.dto.posts.*;
import com.codesanook.dto.users.UserDto;
import com.codesanook.emailvariable.NewComment;
import com.codesanook.model.*;
import com.codesanook.query.TagPostCount;
import com.codesanook.repository.PostRepository;
import com.codesanook.repository.UserRepository;
import com.codesanook.util.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.WebContext;

import java.util.*;

@Service
public class PostService {

    private Log log = LogFactory.getLog(PostService.class);
    private final int SHORT_INTRODUCTION_LENGTH = 250;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private FileService fileService;

    @Autowired
    private Environment env;
    private MailService mailService;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       FileService fileService,
                       MailService mailService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.mailService = mailService;
    }


    public Post getPostById(int postId) {
        return postRepository.getPostById(postId);
    }


    public PostDtoList getPosts(int pageIndex, int itemsPerPage, String sort,
                                PostStatus postStatus) {
        return getPosts(null, pageIndex, itemsPerPage, sort, postStatus);
    }


    public PostDtoList getPostsByType(PostType postType, int pageIndex, int itemsPerPage,
                                      String sort, PostStatus postStatus) {
        int firstItemIndex = (pageIndex * itemsPerPage);//0 -9, 10 - 9
        boolean isAscending = sort.trim().toUpperCase() == "ASC";//DESC

        List<Post> posts;
        int totalPostsCount;
        posts = postRepository.getPostsByType(postType.getId(), firstItemIndex,
                itemsPerPage, isAscending, postStatus);
        totalPostsCount = postRepository.countPost(postType);

        int totalPageCount = getTotalPageCount(totalPostsCount, itemsPerPage);
        PostDtoList response = new PostDtoList();
        List<PostDto> postItems = new ArrayList<>();
        boolean includeContent = false;
        for (Post post : posts) {
            PostDto postItem = createPostDto(post, includeContent);
            postItems.add(postItem);
        }

        response.setPosts(postItems);
        response.setItemsPerPage(itemsPerPage);
        response.setPageIndex(pageIndex);
        response.setTotalPagesCount(totalPageCount);
        response.setTotalItemsCount(totalPostsCount);

        return response;
    }


    public PostDtoList getPostsBySubtype(PostSubtype postSubtype, int pageIndex, int itemsPerPage,
                                         String sort, PostStatus postStatus) {
        int firstItemIndex = (pageIndex * itemsPerPage);//0 -9, 10 - 9
        boolean isAscending = sort.trim().toUpperCase() == "ASC";//DESC

        List<Post> posts;
        int totalPostsCount;
        posts = postRepository.getPostsBySubtype(postSubtype.getId(), firstItemIndex,
                itemsPerPage, isAscending, postStatus);
        totalPostsCount = postRepository.countPost(postSubtype);

        int totalPageCount = getTotalPageCount(totalPostsCount, itemsPerPage);
        PostDtoList response = new PostDtoList();
        List<PostDto> postItems = new ArrayList<>();
        boolean includeContent = false;
        for (Post post : posts) {
            PostDto postItem = createPostDto(post, includeContent);
            postItems.add(postItem);
        }

        response.setPosts(postItems);
        response.setItemsPerPage(itemsPerPage);
        response.setPageIndex(pageIndex);
        response.setTotalPagesCount(totalPageCount);
        response.setTotalItemsCount(totalPostsCount);

        return response;
    }


    public PostDtoList getPosts(Tag tag, int pageIndex, int itemsPerPage,
                                String sort, PostStatus postStatus) {
        int firstItemIndex = (pageIndex * itemsPerPage);//0 -9, 10 - 9
        boolean isAscending = sort.trim().toUpperCase() == "ASC";//DESC

        List<Post> posts;
        int postCount;
        if (tag == null) {
//            posts = postRepository.getAllPosts(firstItemIndex, itemsPerPage,
//                    isAscending, postStatus);
//            postCount = postRepository.countPost();
            posts = new ArrayList<>();
            postCount = posts.size();
        } else {
            posts = postRepository.getPostsByTag(tag, firstItemIndex,
                    itemsPerPage, isAscending, postStatus);
            postCount = postRepository.countPost(tag);
        }
        int totalPageCount = getTotalPageCount(postCount, itemsPerPage);
        PostDtoList response = new PostDtoList();
        List<PostDto> postItems = new ArrayList<>();
        boolean includeContent = false;
        for (Post post : posts) {
            PostDto postItem = createPostDto(post, includeContent);
            postItems.add(postItem);
        }

        response.setPosts(postItems);
        response.setItemsPerPage(itemsPerPage);
        response.setPageIndex(pageIndex);
        response.setTotalPagesCount(totalPageCount);
        response.setTotalItemsCount(postCount);

        return response;
    }


    private PostMetaDataDto createPostMetaDataDto(Post post) {
        PostMetaDataDto postItem = new PostMetaDataDto();
        postItem.setId(post.getId());
        postItem.setTitle(post.getTitle());
        postItem.setAlias(post.getAlias());
        return postItem;
    }

    private PostDto createPostDto(Post post, boolean includeContent) {
        PostDto postItem = new PostDto();
        postItem.setId(post.getId());
        postItem.setTitle(post.getTitle());
        postItem.setAlias(post.getAlias());
        postItem.setIsDeleted(post.isDeleted());
        postItem.setIsMultipartPost(post.isMultipartPost());
        postItem.setShortIntroduction(post.getShortIntroduction());

        if (includeContent) {
            postItem.setContent(post.getContent());
            postItem.setHtmlContent(post.getHtmlContent());
        }

        PostStatus postStatus = post.getPostStatus();
        PostStatusDto postStatusDto = new PostStatusDto();
        postStatusDto.setId(postStatus.getId());
        postStatusDto.setName(postStatus.getName());
        postItem.setPostStatus(postStatusDto);

        PostType postType = post.getPostType();
        PostTypeDto postTypeDto = new PostTypeDto();
        postTypeDto.setId(postType.getId());
        postTypeDto.setName(postType.getName());
        postItem.setPostType(postTypeDto);

        postItem.setPageViewCount(post.getPageViewCount());

        User user = post.getUser();
        UserDto userDto = createUserDto(user);
        String profileUrl = User.DEFAULT_PROFILE_URL;
        if (user.getProfileImage() != null) {
            profileUrl = fileService.getAbsoluteUrl(user.getProfileImage().getRelativePath());
        }
        userDto.setProfileImageUrl(profileUrl);
        postItem.setUser(userDto);

        postItem.setUtcCreateDate(post.getUtcCreateDate());
        postItem.setUtcLastUpdate(post.getUtcLastUpdate());
        postItem.setUtcReleaseDate(post.getUtcReleaseDate());

        postItem.setFeaturedImage(fileService.toUploadedFileDto(post.getFeaturedImage()));

        List<String> tags = new ArrayList<>();
        for (Tag tag : post.getTags()) {
            tags.add(tag.getName());
        }
        postItem.setTags(tags);


        List<UploadedFileDto> files = new ArrayList<>();
        for (UploadedFile uploadedFile : post.getUploadedFiles()) {
            files.add(fileService.toUploadedFileDto(uploadedFile));
        }
        postItem.setUploadedFiles(files);

        List<CommentDto> comments = createCommentList(post);
        postItem.setComments(comments);


        return postItem;
    }

    private UserDto createUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public List<CommentDto> createCommentList(Post post) {
        List<CommentDto> comments = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            if (comment.isDeleted()) continue;
            CommentDto commentResponse = createCommentDto(comment);
            comments.add(commentResponse);
        }
        return comments;
    }

    public CommentDto createCommentDto(Comment comment) {
        CommentDto commentResponse = new CommentDto();
        commentResponse.setHtmlContent(comment.getHtmlContent());
        commentResponse.setContent(comment.getContent());
        commentResponse.setId(comment.getId());
        commentResponse.setUserId(comment.getUser().getId());
        commentResponse.setUserName(comment.getUser().getName());
        commentResponse.setPostId(comment.getPost().getId());

        String profileUrl = User.DEFAULT_PROFILE_URL;
        if (comment.getUser().getProfileImage() != null) {
            profileUrl = fileService.getAbsoluteUrl(comment.getUser().getProfileImage().getRelativePath());
        }
        commentResponse.setUserProfileUrl(profileUrl);
        return commentResponse;
    }


    private int getTotalPageCount(int postCount, int itemsPerPage) {
        float pageCountFloating = (float) postCount / itemsPerPage;
        int totalPageCount = (int) Math.ceil(pageCountFloating);
        return totalPageCount;
    }


    public List<TagDto> getAllTags() {

//        List<TagPostCount> tags = postRepository.getAllTags();
//        List<com.codesanook.dto.TagDto> responses = new ArrayList<>();
//        for (Tag tag : tags) {
//            TagDto item = new TagDto();
//            item.setId(tag.getId());
//            item.setName(tag.getName());
//            //todo update performance
//
//            item.setPostCount(tag.getPosts().size());
//            responses.add(item);
//        }
//        return responses;
        List<TagPostCount> tags = postRepository.getTagPostCounts();
        List<com.codesanook.dto.TagDto> responses = new ArrayList<>();
        for (TagPostCount tag : tags) {
            TagDto item = new TagDto();
            item.setId(tag.getId());
            item.setName(tag.getName());
            item.setPostCount((int) tag.getPostCount());
            responses.add(item);
        }
        return responses;
    }


    public PostDto getPost(int postId) {

        Post post = postRepository.getPostById(postId);
        post.increasePageViewCount();
        boolean includeContent = true;
        PostDto response = createPostDto(post, includeContent);
        return response;
    }


    public int addPost(PostDto postDto) {
        Post post = new Post();
        User user = userRepository.getUserById(postDto.getUser().getId());
        post.setUser(user);

        post.setTitle(postDto.getTitle());
        setAlias(post, postDto);

        post.setContent(postDto.getContent());

        String htmlContent = addYoutubeIframe(postDto.getHtmlContent());
        String safeHtmlContent = cleanHtmlContent(htmlContent);
        post.setHtmlContent(safeHtmlContent);

        setShortIntroduction(post, postDto);
        DateTime now = DateTime.now(DateTimeZone.UTC);
        post.setUtcCreateDate(now);

        setUploadedFiles(post, postDto);
        setTags(post, postDto);

        setPostStatus(post, postDto);
        setPostType(post, postDto);
        setPostSubtype(post, postDto);

        post.validateRequiredProperty();

        postRepository.addPost(post);
        return post.getId();
    }

    private String addYoutubeIframe(String html) {

        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        Elements elements = body.select("div.youtube-preview");
        Elements iframes = body.select("iframe");
        iframes.remove();

        // rename all 'font'-tags to 'span'-tags, will also keep attributs etc.
        for (Element e : elements) {
            String youtubeId = null;
            for (String className : e.classNames()) {
                if (!className.equals("youtube-preview")) {
                    youtubeId = className;
                }
            }

            Attributes attrs = new Attributes();
            String src = "https://www.youtube.com/embed/" + youtubeId;
            String width = "560";
            String height = "315";

            attrs.put("src", src);
            attrs.put("width", width);
            attrs.put("height", height);

            attrs.put("frameborder", "0");
            attrs.put("allowfullscreen", "true");
            attrs.put("class", "embedded-youtube__iframe");
            Element iframe = new Element(org.jsoup.parser.Tag.valueOf("iframe"), "", attrs);

            Element container = new Element(org.jsoup.parser.Tag.valueOf("div"), "");
            container.addClass("embedded-youtube");
            container.appendChild(iframe);

            e.replaceWith(container);
        }

        return body.html();
    }


    private void setShortIntroduction(Post post, PostDto postDto) {
        if (StringUtils.isEmpty(postDto.getHtmlContent())) {
            throw new IllegalStateException("cannot set short introduction,  htmlContent is null or empty");
        }

        String html = addYoutubeIframe(postDto.getHtmlContent());
        String safeHtml = cleanHtmlContent(html);
        String contentWithoutHtmlTag = Jsoup.parse(safeHtml).text();
        contentWithoutHtmlTag = contentWithoutHtmlTag.length() > SHORT_INTRODUCTION_LENGTH ?
                contentWithoutHtmlTag.substring(0, SHORT_INTRODUCTION_LENGTH) : contentWithoutHtmlTag;
        post.setShortIntroduction(contentWithoutHtmlTag);
    }


    public String cleanHtmlContent(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent))
            throw new IllegalArgumentException("Cannot set clean null or empty htmlContent post");

        Whitelist whitelist = Whitelist.relaxed()
                .addTags("code", "iframe")
                .addAttributes(":all", "class")
                .addAttributes("iframe", "src", "width", "height", "frameborder", "allowfullscreen");
        String safe = Jsoup.clean(htmlContent, whitelist);
        return safe;
    }

    private String addClassToHtmlContent(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent))
            throw new IllegalArgumentException("Cannot set clean null or empty htmlContent post");

        Document document = Jsoup.parse(htmlContent);
        Elements pres = document.getElementsByTag("pre");
        if (pres != null) {
            for (Element pre : pres) {
                pre.attr("class", "prettyprint linenums");
            }
        }

        Elements img = document.getElementsByTag("img");
        if (pres != null) {
            for (Element pre : pres) {
                pre.attr("class", "prettyprint linenums");
            }
        }


        return document.html();
    }


    private void setAlias(Post post, PostDto postDto) {

        String title = postDto.getTitle();
        String alias = postDto.getAlias();
        String adjustedAlias;
        final String pattern = "[\\s/\\\\-]+";
        if (!StringUtils.isEmpty(alias)) {
            adjustedAlias = alias.trim().replaceAll(pattern, "-").toLowerCase();
        } else if (!StringUtils.isEmpty(title)) {
            adjustedAlias = title.trim().replaceAll(pattern, "-").toLowerCase();
        } else {
            throw new IllegalStateException("Empty title can not be set to set alias");
        }

        post.setAlias(adjustedAlias);
    }


    private void setPostType(Post post, PostDto postDto) {
        PostType postType = getPostTypeById(postDto.getPostType().getId());
        post.setPostType(postType);
    }

    private void setPostSubtype(Post post, PostDto postDto) {
        if (postDto.getPostSubtype() == null) return;
        if (postDto.getPostSubtype().getId() == 0) return;

        PostSubtype postSubtype = getPostSubtypeById(postDto.getPostSubtype().getId());
        post.setPostSubtype(postSubtype);
    }


    public PostType getPostTypeById(int postTypeId) {
        return postRepository.getPostTypeById(postTypeId);
    }


    public PostSubtype getPostSubtypeById(int postTypeId) {
        return postRepository.getPostSubtypeById(postTypeId);
    }


    public void setPostStatus(Post post, PostDto postDto) {
        PostStatus postStatus;
        PostStatusDto postStatusDto = postDto.getPostStatus();
        if (postStatusDto != null) {
            postStatus = getPostStatus(PostStatusEnum.fromId(postStatusDto.getId()));
        } else {
            postStatus = getPostStatus(PostStatusEnum.PUBLISHED);
        }
        post.setPostStatus(postStatus);
    }


    private void setTags(Post post, PostDto request) {
        List<String> tags = request.getTags();
        if (tags.isEmpty()) {
            return;
        }
        //remove duplicate
        HashSet<String> tagHash = new HashSet<>(tags);
        String[] tagArray = tagHash.toArray(new String[tagHash.size()]);

        for (String tagName : tagArray) {
            tagName = tagName.trim();
            Tag tag = postRepository.getTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                postRepository.addTag(tag);
            }
            post.addTagIfNotExist(tag);
        }
    }


    private void setUploadedFiles(Post post, PostDto request) {

        if (request.getFeaturedImage() != null) {
            UploadedFile featureImage = fileService.getUploadedFile(request.getFeaturedImage().getId());
            post.setFeaturedImage(featureImage);
        }

        for (UploadedFile uploadedFileRequest : request.getUploadedFiles()) {
            UploadedFile uploadedFile = fileService.getUploadedFile(uploadedFileRequest.getId());
            uploadedFile.setContextReferenceId(uploadedFileRequest.getContextReferenceId());
            log.info(String.format("uploadedFile.getContextReferenceId %d", uploadedFile.getContextReferenceId()));
            post.getUploadedFiles().add(uploadedFile);
        }
    }


    public Tag getTagByName(String tagName) {
        return postRepository.getTagByName(tagName);
    }


    public void addTag(Tag tag) {


        postRepository.addTag(tag);
    }


    public void updatePost(int postId, PostDto postDto) {

        Post post = postRepository.getPostById(postId);

        if (!StringUtils.isEmpty(postDto.getTitle())) post.setTitle(postDto.getTitle());
        updateAlias(post, postDto);

        if (!StringUtils.isEmpty(postDto.getContent())) {
            post.setContent(postDto.getContent());
        }

        if (!StringUtils.isEmpty(postDto.getHtmlContent())) {
            String htmlContent = addYoutubeIframe(postDto.getHtmlContent());
            log.debug("html \n" + htmlContent);
            String safeHtmlContent = cleanHtmlContent(htmlContent);
            post.setHtmlContent(safeHtmlContent);
        }

        if (!StringUtils.isEmpty(postDto.getHtmlContent())) {
            setShortIntroduction(post, postDto);
        }

        if (postDto.getUtcReleaseDate() != null) post.setUtcReleaseDate(postDto.getUtcReleaseDate());

        DateTime now = DateTime.now(DateTimeZone.UTC);
        post.setUtcLastUpdate(now);

        post.setIsMultipartPost(postDto.isMultipartPost());
        updateFeatureImage(post, postDto);
        updateUploadedFile(post, postDto);
        updateTag(post, postDto);

        post.validateRequiredProperty();
    }

    private void updateAlias(Post post, PostDto postDto) {

        String alias = postDto.getAlias();
        if (StringUtils.isEmpty(alias)) return;

        setAlias(post, postDto);
    }


    private void updateFeatureImage(Post post, PostDto request) {
        UploadedFile featuredImage = request.getFeaturedImage();
        if (featuredImage == null) {
            post.setFeaturedImage(null);
            return;
        }

        UploadedFile existingFeatureImage = post.getFeaturedImage();
        if (existingFeatureImage == null) {
            UploadedFile uploadedFile = fileService.getUploadedFile(featuredImage.getId());
            post.setFeaturedImage(uploadedFile);
            return;
        }

        if (existingFeatureImage.getId() != featuredImage.getId()) {
            UploadedFile uploadedFile = fileService.getUploadedFile(featuredImage.getId());
            post.setFeaturedImage(uploadedFile);
            return;
        }
    }


    private void updateUploadedFile(Post post, PostDto request) {
        List<Integer> existingFileIds = convertToFileIdList(post.getUploadedFiles());
        List<Integer> newFileIds = convertToFileIdList(request.getUploadedFiles());

        for (int fileId : existingFileIds) {
            boolean isFoundExistingId = newFileIds.indexOf(fileId) != -1;
            if (isFoundExistingId) continue;

            if (!isFoundExistingId) {
                //remove
                UploadedFile uploadedFile = fileService.getUploadedFile(fileId);
                uploadedFile.setContextReferenceId(0);
                post.getUploadedFiles().remove(uploadedFile);
            }
        }

        for (UploadedFile uploadedFile : request.getUploadedFiles()) {
            log.info(String.format("uploadedFile id %d, context id %d", uploadedFile.getId(),
                    uploadedFile.getContextReferenceId()));
            int fileId = uploadedFile.getId();
            boolean isFoundExistingId = existingFileIds.indexOf(fileId) != -1;
            if (isFoundExistingId) {
                UploadedFile existingFile = fileService.getUploadedFile(fileId);
                existingFile.setContextReferenceId(uploadedFile.getContextReferenceId());
            } else {
                //add
                UploadedFile newUploadedFile = fileService.getUploadedFile(fileId);
                newUploadedFile.setContextReferenceId(uploadedFile.getContextReferenceId());
                post.getUploadedFiles().add(newUploadedFile);
            }

        }

    }


    private <T extends UploadedFile> List<Integer> convertToFileIdList(List<T> uploadedFiles) {
        List<Integer> uploadedFileIdList = new ArrayList<>();
        if (uploadedFiles == null || uploadedFiles.size() == 0) return uploadedFileIdList;

        for (UploadedFile uploadedFile : uploadedFiles) {
            uploadedFileIdList.add(uploadedFile.getId());
        }
        return uploadedFileIdList;
    }


    private void updateTag(Post post, PostDto postDto) {
        for (String tagName : postDto.getTags()) {
            Tag tag = postRepository.getTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                postRepository.addTag(tag);
            }
            post.addTagIfNotExist(tag);
        }
        post.removeTagsIfMissing(postDto.getTags());
    }


    public PostStatus getPostStatus(PostStatusEnum postStatusEnum) {
        PostStatus postStatus = postRepository.getPostStatusById(postStatusEnum.getId());
        return postStatus;
    }


    public void removePost(Post post) {
        PostStatus postStatus = getPostStatus(PostStatusEnum.DELETED);
        post.setPostStatus(postStatus);
    }


    public PostType getPostTypeByName(String postTypeName) {
        return postRepository.getPostTypeByName(postTypeName);
    }


    public PostSubtype getPostSubtypeByName(String postSubtypeName) {
        return postRepository.getPostSubtypeByName(postSubtypeName);
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUserName(comment.getUser().getName());

        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setHtmlContent(comment.getHtmlContent());
        commentDto.setPostId(comment.getPost().getId());
        return commentDto;
    }

    public Comment getCommentById(int commentId) {
        Comment comment = postRepository.getCommentById(commentId);
        return comment;

    }

    private boolean notYourself(User you, User other) {
        return you.getId() != other.getId();
    }

    public int addComment(CommentDto commentDto, WebContext ctx) {
        Post post = postRepository.getPostById(commentDto.getPostId());
        User user = userRepository.getUserById(commentDto.getUserId());
        HashMap<Integer, User> receivers = new HashMap<>();

        User postCreator = post.getUser();
        if (notYourself(user, postCreator)) {
            receivers.put(postCreator.getId(), postCreator);
        }

        for (Comment existingComment : post.getComments()) {
            User commentator = existingComment.getUser();

            if (!receivers.containsKey(commentator.getId()) && notYourself(user, commentator)) {
                receivers.put(commentator.getId(), commentator);
            }
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(commentDto.getContent());
        String safeHtmlContent = cleanHtmlContent(commentDto.getHtmlContent());
        comment.setHtmlContent(safeHtmlContent);

        comment.setUserIp(commentDto.getUserIp());
        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        comment.setUtcCreateDate(utcNow);
        comment.validateRequriedProperties();

        post.addComment(comment);
        postRepository.addComment(comment);
        for (User receiver : receivers.values()) {
            sendNewComment(user, post, receiver, ctx);
        }
        return comment.getId();
    }

    private void sendNewComment(User commentator, Post post, User reciever, WebContext ctx) {
        try {
            String fromEmail = "admin@codesanook.com";
            String fromName = "codesanook.com";
            String toEmail = reciever.getEmail();
            String subject = String.format("%s has just commented on %s.",
                    commentator.getName(),
                    post.getTitle());
            String template = "email/new-comment";

            String domain = env.getProperty("domain");
            String postUrl = String.format("http://%s/post/details/%s/%d",
                    domain, post.getAlias(), post.getId());

            NewComment newComment = new NewComment();
            newComment.setReceiver(reciever.getName());
            newComment.setCommentator(commentator.getName());
            newComment.setPostTitle(post.getTitle());
            newComment.setPostUrl(postUrl);
            ctx.setVariable("newComment", newComment);

            EmailDto emailDto = new EmailDto();
            emailDto.setFromEmail(fromEmail);
            emailDto.setFromName(fromName);
            emailDto.setToEmail(toEmail);
            emailDto.setSubject(subject);
            emailDto.setTemplate(template);

            mailService.sendMail(emailDto, ctx);

        } catch (Exception ex) {
            log.error(ex);
        }
    }

    public int addMultipartPost(MultipartPostDto multipartPostDto, User user) {
        MultipartPost multipartPost = new MultipartPost();
        DateTime now = DateTime.now(DateTimeZone.UTC);
        multipartPost.setTitle(multipartPostDto.getTitle());
        multipartPost.setUtcCreateDate(now);
        multipartPost.setUser(user);
        postRepository.addMultipartPost(multipartPost);
        return multipartPost.getId();
    }

    public List<MultipartPostDto> searchMultipartPosts(String title) {

        List<MultipartPostDto> multipartDtoList = new ArrayList<>();
        List<MultipartPost> multipartPosts = postRepository.searchMultipartPostsByTile(title);

        for (MultipartPost multipartPost : multipartPosts) {
            MultipartPostDto dto = createMultipartPostDto(multipartPost, false);
            multipartDtoList.add(dto);
        }
        return multipartDtoList;
    }

    public MultipartPostDto getMultipartPostById(int multipartPostId) {
        MultipartPost multipartPost = postRepository.getMultipartPostById(multipartPostId);
        MultipartPostDto multipartPostDto = createMultipartPostDto(multipartPost, true);
        return multipartPostDto;
    }

    public MultipartPostDto getMultipartPostByPostId(int postId) {
        MultipartPost multipartPost = postRepository.getMultipartPostByPostId(postId);
        if (multipartPost == null) return null;

        MultipartPostDto multipartPostDto = createMultipartPostDto(multipartPost, true);
        return multipartPostDto;
    }


    private MultipartPostDto createMultipartPostDto(MultipartPost multipartPost, boolean includedMultipartPostItem) {

        MultipartPostDto dto = new MultipartPostDto();
        dto.setId(multipartPost.getId());
        dto.setTitle(multipartPost.getTitle());
        UserDto userDto = createUserDto(multipartPost.getUser());
        dto.setUser(userDto);
        dto.setUtcCreateDate(multipartPost.getUtcCreateDate());
        dto.setUtcLastUpdate(multipartPost.getUtcLastUpdate());

        if (includedMultipartPostItem) {
            List<MultipartPostItem> multipartPostItems = multipartPost.getMultipartPostItems();
            Collections.sort(multipartPostItems);
            List<MultipartPostItemDto> multipartPostItemDtoList = new ArrayList<>();
            for (MultipartPostItem multipartPostItem : multipartPostItems) {
                MultipartPostItemDto multipartPostItemDto = createMultipartPostItemDto(multipartPostItem);
                multipartPostItemDtoList.add(multipartPostItemDto);
            }
            dto.setMultipartPostItems(multipartPostItemDtoList);
        }

        return dto;
    }

    private MultipartPostItemDto createMultipartPostItemDto(MultipartPostItem multipartPostItem) {
        MultipartPostItemDto multipartPostItemDto = new MultipartPostItemDto();
        Post post = multipartPostItem.getPost();
        PostMetaDataDto postDto = createPostMetaDataDto(post);
        multipartPostItemDto.setPost(postDto);

        User user = multipartPostItem.getUser();
        UserDto userDto = createUserDto(user);
        multipartPostItemDto.setUser(userDto);

        multipartPostItemDto.setOrderIndex(multipartPostItem.getOrderIndex());
        return multipartPostItemDto;
    }


    public List<PostMetaDataDto> searchPost(String title) {
        List<PostMetaDataDto> postMetaDataDtos = new ArrayList<>();
        List<Post> posts = postRepository.searchPostByTitle(title);
        for (Post post : posts) {
            PostMetaDataDto postDto = createPostMetaDataDto(post);
            postMetaDataDtos.add(postDto);
        }
        return postMetaDataDtos;
    }


    public void updateMultipartPost(MultipartPostDto multipartPostDto, User user) {

        DateTime now = DateTime.now(DateTimeZone.UTC);
        MultipartPost multipartPost = postRepository.getMultipartPostById(
                multipartPostDto.getId());


        multipartPost.setTitle(multipartPostDto.getTitle());
        log.debug(String.format("multipartPost title %s", multipartPost.getTitle()));
        multipartPost.setUtcLastUpdate(now);

        List<MultipartPostItem> existingItems = multipartPost.getMultipartPostItems();
        List<Integer> existingPostIds = new ArrayList<>();
        for (MultipartPostItem item : existingItems) {
            existingPostIds.add(item.getPostId());
        }

        List<MultipartPostItemDto> newItems = multipartPostDto.getMultipartPostItems();
        List<Integer> newPostIds = new ArrayList<>();
        for (MultipartPostItemDto newItem : newItems) {
            newPostIds.add(newItem.getPost().getId());
        }

        List<Integer> toRemovePostIds = SetUtils.subtract(existingPostIds, newPostIds);
        List<Integer> toAddPostIds = SetUtils.subtract(newPostIds, existingPostIds);

        Map<Integer, MultipartPostItem> existingMapItems = new HashMap<>();
        for (MultipartPostItem item : existingItems) {
            existingMapItems.put(item.getPost().getId(), item);
        }

        //remove
        for (int postId : toRemovePostIds) {
            MultipartPostItem toRemoveItem = existingMapItems.get(postId);
            existingItems.remove(toRemoveItem);
            postRepository.removeMultipartPostItem(toRemoveItem);
        }

        //add
        for (int postId : toAddPostIds) {
            MultipartPostItem toAddItem = new MultipartPostItem();
            Post post = postRepository.getPostById(postId);
            toAddItem.setPost(post);
            toAddItem.setUser(user);
            toAddItem.setUtcCreateDate(now);
            toAddItem.setMultipartPost(multipartPost);
            existingItems.add(toAddItem);
        }

        Map<Integer, Integer> newOrderIndexMap = new HashMap<>();
        for (MultipartPostItemDto item : newItems) {
            newOrderIndexMap.put(item.getPost().getId(), item.getOrderIndex());
        }

        //update index
        for (MultipartPostItem item : existingItems) {
            int newOrderIndex = newOrderIndexMap.get(item.getPost().getId());
            item.setOrderIndex(newOrderIndex);
            item.setUtcLastUpdate(now);
        }
        log.debug(String.format("existingItems.size = %d", existingItems.size()));
    }

}

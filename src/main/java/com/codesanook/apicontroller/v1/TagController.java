package com.codesanook.apicontroller.v1;

import com.codesanook.dto.TagDto;
import com.codesanook.dto.tags.AddTagRequest;
import com.codesanook.dto.tags.UpdateTagNameRequest;
import com.codesanook.model.Tag;
import com.codesanook.repository.PostRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RequestMapping(value = "/api/tags")
@RestController
public class TagController {

    private PostRepository postRepository;

    @Autowired
    public TagController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //todo need to improve post count performance
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDto> getAllTags(@RequestParam String tagName) {

        List<Tag> tags;
        if (!StringUtils.isEmpty(tagName)) {
            tags = postRepository.searchTagByName(tagName);
        } else {
            tags = postRepository.getAllTags();
        }

        List<TagDto> responses = new ArrayList<>();
        for (Tag tag : tags) {
            TagDto item = new TagDto();
            item.setId(tag.getId());
            item.setName(tag.getName());
            item.setPostCount(tag.getPosts().size());
            responses.add(item);
        }
        return responses;
    }


    @RequestMapping(value = "", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int addTag(@RequestBody AddTagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.validateRequiredProperty();
        postRepository.addTag(tag);
        return tag.getId();
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateTag(@PathVariable int tagId,
                          @RequestBody UpdateTagNameRequest request) {
        Tag existingTag = postRepository.getTagById(tagId);
        existingTag.rename(request.getTagName());
    }
}



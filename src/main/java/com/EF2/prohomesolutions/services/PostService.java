package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.*;
import com.EF2.prohomesolutions.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentService commentService;

    public void createPost(Provider provider, MultipartFile file, String description) throws MyException {

        validate(provider, description);

        File image = fileService.saveImage(file);

        Post post = Post.builder()
                .description(description)
                .postDate(new Date())
                .image(image)
                .visible(true)
                .provider(provider)
                .providerType(provider.getProviderType())
                .build();
        postRepo.save(post);
    }

    private  void validate(Provider provider, String description) throws MyException {
        if (provider == null){
            throw new MyException("Session has expired.");
        }
        if (description.length() > 499){
            throw new MyException("The description has overpassed the limit of 500 characters.");
        }
    }

    public List<Post> getAll() {
        List<Post> posts = postRepo.findAll();
        posts.forEach(post -> post.setComments(commentService.orderByDateDesc(post.getComments())));
        return posts;
    }

    public Post getOne(String id) {
        return postRepo.getOne(id);
    }

    public Post addComment(Customer customer, String id, String comment) {
        Post post = getOne(id);
        commentService.create(customer,post,comment);
        return post;
    }


    public List<Post> getAllVisible() {
        List<Post> posts = postRepo.getAllVisible();
        posts.forEach(post -> post.setComments(commentService.orderByDateDesc(post.getComments())));
        return posts;
    }

    public void changeVisibility(String id) {
        Post post = getOne(id);
        post.setVisible(!post.getVisible());
        postRepo.save(post);
    }

    public Post commentChangeVisibility(String idPost, String idComment) {
        commentService.changeVisibility(idComment);
        return getOne(idPost);
    }

    public List<Post> orderByDateDesc(List<Post> posts){
        return posts.stream().sorted(Comparator.comparing(Post::getPostDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Post> orderByDateAsc(List<Post> posts){
        return posts.stream().sorted(Comparator.comparing(Post::getPostDate))
                .collect(Collectors.toList());
    }

}

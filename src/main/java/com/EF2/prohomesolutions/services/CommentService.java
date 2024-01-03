package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.models.Comment;
import com.EF2.prohomesolutions.models.Customer;
import com.EF2.prohomesolutions.models.Post;
import com.EF2.prohomesolutions.repositories.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private NotificationService notificationService;

    public void create(Customer customer, Post post, String description) {
        Comment comment = Comment.builder()
                .description(description)
                .post(post)
                .customer(customer)
                .date(new Date())
                .visible(true)
                .build();
        commentRepo.save(comment);
        String notiTitle = "New comment";
        String url = "/post/post-view/" + post.getId();
        notificationService.create(post.getProvider().getId(),
                post.getProvider().getUserType().name(), notiTitle, description, url, customer.getId());
    }

    public void changeVisibility(String idComment) {
        Comment comment = commentRepo.getOne(idComment);
        comment.setVisible(!comment.getVisible());
        commentRepo.save(comment);
    }

    public List<Comment> orderByDateDesc(List<Comment> comments){
        return comments.stream().sorted(Comparator.comparing(Comment::getDate).reversed())
                .collect(Collectors.toList());
    }

}

package com.EF2.prohomesolutions.controllers;

import com.EF2.prohomesolutions.enums.PostFilterOrderOptions;
import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.*;
import com.EF2.prohomesolutions.services.CommentService;
import com.EF2.prohomesolutions.services.NotificationService;
import com.EF2.prohomesolutions.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public String post(HttpSession session, MultipartFile file, @RequestParam String description, ModelMap model){
        Provider provider = (Provider) session.getAttribute("userSession");
        try {
            postService.createPost(provider, file, description);
        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:post/portal-post/DATE_DESC";
    }

    @GetMapping("/portal-post/{filterOrder}")
    public String portalPost(HttpSession session, @PathVariable String filterOrder, ModelMap model){
        String userRole = (String) session.getAttribute("userRole");
        List<Post> posts = null;
        if (userRole.equals("ADMIN")){
            posts = postService.getAll();
        }else{
            posts = postService.getAllVisible();
        }

        if (filterOrder.equals(PostFilterOrderOptions.DATE_DESC.name())){
            posts = postService.orderByDateDesc(posts);
        } else if (filterOrder.equals(PostFilterOrderOptions.DATE_ASC.name())) {
            posts = postService.orderByDateAsc(posts);
        }

        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        model.addAttribute("posts", posts);
        return "Portal_posts";
    }

    @GetMapping("/post-view/{id}")
    public String postView(HttpSession session, @PathVariable("id") String id, ModelMap model){
        Post post = postService.getOne(id);
        boolean fav = false;
        String userType = (String) session.getAttribute("userType");
        if (userType.equals("CUSTOMER")){
            Customer customer = (Customer) session.getAttribute("userSession");
            for (Customer c : post.getLikes()) {
                if (c.getId().equals(customer.getId())){
                    fav = true;
                    break;
                }
            }
        }
        model.addAttribute("fav", fav);
        post.setComments(
                post.getComments().stream().sorted(Comparator.comparing(Comment::getDate).reversed())
                        .collect(Collectors.toList())
        );
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        model.addAttribute("post", post);
        return "Post_view";
    }

    @PostMapping("/add-comment/{id}")
    public String addComment(HttpSession session, @PathVariable("id") String id, @RequestParam String comment, ModelMap model){
        Customer customer = (Customer)session.getAttribute("userSession");
        Post post = postService.addComment(customer, id, comment);
        post.setComments(commentService.orderByDateDesc(post.getComments()));
        model.addAttribute("post", post);
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Post_view";
    }

    @GetMapping("/change-visiblity/{id}")
    public String changeVisibility(HttpSession session, @PathVariable String id, ModelMap model){
        String userRole = (String) session.getAttribute("userRole");
        postService.changeVisibility(id);
        List<Post> post = null;
        if (userRole.equals("ADMIN")){
            post = postService.orderByDateDesc(postService.getAll());
        }else{
            post = postService.orderByDateDesc(postService.getAllVisible());
        }
        model.addAttribute("posts", post);
        return "Portal_posts";
    }

    @GetMapping("/comment/change-visiblity/{idPost}/{idComment}")
    public String commentChangeVisibility(HttpSession session, @PathVariable("idPost") String idPost,
                                          @PathVariable("idComment") String idComment, ModelMap model){
        Post post = postService.commentChangeVisibility( idPost, idComment);
        post.setComments(commentService.orderByDateDesc(post.getComments()));
        model.addAttribute("post", post);
        List<Notification> notifications = notificationService.getNotifications(session);
        model.put("notifications", notifications);
        return "Post_view";
    }

}

package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Post, String> {

    @Query("SELECT p FROM Post p WHERE p.visible = true")
    List<Post> getAllVisible();
}

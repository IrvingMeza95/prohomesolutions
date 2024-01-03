package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, String> {
}

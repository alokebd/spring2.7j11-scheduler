package com.ait.cornjob.repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ait.cornjob.dto.Comment;
import com.ait.cornjob.exception.NoSuchElementException;

@Component
public class CommentRepository {

	private Collection<Comment> comments = new ConcurrentLinkedQueue();
	
	public Comment findById(Integer id) {
        return comments.stream()
            .filter(n -> id.equals(n.getId()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Not found id: "+ id));
    }

    public List<Comment> findAll() {
        return comments.stream()
          // .filter(notification -> !notification.isSentOut())
            .collect(Collectors.toList());
    }

    public void save(Comment comment) {
    	comments.add(comment);
    }

}

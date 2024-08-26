package com.ait.cornjob.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ait.cornjob.dto.BaseDto;
import com.ait.cornjob.dto.Comment;
import com.ait.cornjob.repository.CommentRepository;


@Component
public class CommentScheduledTask {

	private static final Logger log = LoggerFactory
			.getLogger(CommentScheduledTask.class);

	// set this to false to disable this job; set it it true by
	@Value("${example.scheduledJob.enabled:false}")
	private boolean scheduledJobEnabled;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
	private CommentRepository commentRepository;
	public CommentScheduledTask (CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(fixedRate = 30000)  // every 30 seconds
	public void pullRandomComment() throws Exception {
		if (!scheduledJobEnabled) {
			return;
		}
		int min = 1;
		int max = 500;
		Random r = new Random();
		int id = r.nextInt((max - min) + 1) + min;
		
		
		// pull data from example REST API
		Comment comment = restTemplate.getForObject("http://jsonplaceholder.typicode.com/comments/" + id, Comment.class);
		log.info("Pulled comment #" + id + " at " + dateFormat.format(new Date()));
		log.info("pullRandomComment - comment: " + BaseDto.toJsonString( comment));
		
		commentRepository.save(comment);
	}

	//2nd schedule to fetch data in every 15 seconds
	@Scheduled(cron ="0/35 * * * * *")
	public void fetchCommentJob() {
		List<Comment> comments= commentRepository.findAll();
		
		comments.stream().forEach((comment) -> {
		    try {
		    	log.info("fetch comment #" + comment.getId() + " at " + dateFormat.format(new Date()));
				log.info("fetchCommentJob - comment: " + BaseDto.toJsonString(comment));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// examples of other CRON expressions
	// * "0 0 * * * *" = the top of every hour of every day.
	// * "*/10 * * * * *" = every ten seconds.
	// * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	// * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	// * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	// * "0 0 0 25 12 ?" = every Christmas Day at midnight

}

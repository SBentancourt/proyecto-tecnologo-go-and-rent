package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.UpdateReviewDTO;
import com.tecnologo.grupo3.goandrent.entities.Review;
import com.tecnologo.grupo3.goandrent.repositories.ReviewRepository;
import com.tecnologo.grupo3.goandrent.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(String description, int qualification) {
        return reviewRepository.save(new Review(qualification, description, new Date()));
    }

    @Override
    public void updateReview(UpdateReviewDTO reviewDTO) {
        Review review = reviewRepository.getById(reviewDTO.getReviewId());
        review.setCreatedAt(new Date());
        review.setDescription(reviewDTO.getDescription());
        review.setQualification(reviewDTO.getQualification());
        reviewRepository.save(review);
    }
}

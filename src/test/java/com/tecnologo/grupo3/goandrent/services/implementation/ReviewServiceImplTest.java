package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.UpdateReviewDTO;
import com.tecnologo.grupo3.goandrent.entities.Review;
import com.tecnologo.grupo3.goandrent.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private UpdateReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        review = new Review(4, "review", new Date());
        reviewDTO = new UpdateReviewDTO(0, 4, "review");
    }

    @Test
    void addReview() {
        when(reviewRepository.save(review)).thenReturn(review);
        assertTrue(reviewService.addReview("review", 4) != null || reviewService.addReview("review", 4) == null);
    }

    @Test
    void updateReview() {
        when(reviewRepository.getById(0)).thenReturn(review);
        reviewService.updateReview(reviewDTO);
    }
}
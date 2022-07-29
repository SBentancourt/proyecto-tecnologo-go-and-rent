package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.UpdateReviewDTO;
import com.tecnologo.grupo3.goandrent.entities.Review;

public interface ReviewService {
    Review addReview(String description, int qualification);
    void updateReview(UpdateReviewDTO reviewDTO);
}

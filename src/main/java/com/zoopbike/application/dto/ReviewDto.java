package com.zoopbike.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID reviewId;
    private String commentRealtedToBike;
    private String commentRealtedBikeProvider;
    private LocalDateTime time;
    private String applicationName;
    private LocalDateTime updatedCommentTime;

}


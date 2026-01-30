package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.RatingsCount;
import grupa1.jutjubic.dto.UserRating;
import grupa1.jutjubic.service.IActivityService;
import grupa1.jutjubic.service.IRatingService;
import grupa1.jutjubic.service.impl.RatingService;
import grupa1.jutjubic.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "api/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
public class RatingController {
    @Autowired
    private IRatingService ratingService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}/couts")
    public ResponseEntity<RatingsCount> getCounts(
            @PathVariable Long id
    ) {
        return  ResponseEntity.ok(ratingService.getCounts(id));
    }

    @GetMapping("/{id}/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserRating> getMyRating(
            @PathVariable Long id,
            Principal user
    ) {
        return ResponseEntity.ok(ratingService.findByVideoIdAndUserId(id, userService.findByUsername(user.getName()).getId()));
    }

    @PostMapping("{id}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserRating> like(
            @PathVariable Long id,
            Principal user
    ) {
        return ResponseEntity.ok(ratingService.like(id, userService.findByUsername(user.getName()).getId()));
    }

    @PostMapping("{id}/dislike")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserRating> dislike(
            @PathVariable Long id,
            Principal user
    ) {
        return ResponseEntity.ok(ratingService.dislike(id, userService.findByUsername(user.getName()).getId()));
    }
}

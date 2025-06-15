// TESTS FAIL
package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.ReviewNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.impl.ReviewServiceImpl;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.model.PosType;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private ReviewDataService reviewDataService;

    @Mock
    private PosService posService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Pos pos;
    private User author;
    private User approver;
    private Review approvedReview;
    private Review unapprovedReview;

    @BeforeEach
    void setUp() {
        // Pos mit allen @NonNull-Feldern
        pos = Pos.builder()
                .id(1L)
                .name("TestPos")
                .description("TestPos description")
                .type(PosType.CAFE) 
                .build();

        author = User.builder()
                .id(10L)
                .build();

        approver = User.builder()
                .id(20L)
                .build();

        approvedReview = Review.builder()
                .id(100L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(true)
                .approvalCount(2)
                .build();

        unapprovedReview = Review.builder()
                .id(101L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(false)
                .approvalCount(0)
                .build();
    }

    @Test
    void getAllReturnsAllReviews() {
        List<Review> fixtures = List.of(approvedReview, unapprovedReview);
        when(reviewDataService.getAll()).thenReturn(fixtures);

        List<Review> result = reviewService.getAll();

        verify(reviewDataService).getAll();
        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(fixtures);
    }

    @Test
    void getByIdSuccess() throws ReviewNotFoundException {
        when(reviewDataService.getById(approvedReview.getId()))
                .thenReturn(approvedReview);

        Review result = reviewService.getById(approvedReview.getId());

        verify(reviewDataService).getById(approvedReview.getId());
        assertThat(result).isEqualTo(approvedReview);
    }

    @Test
    void getByIdNotFoundThrows() throws ReviewNotFoundException {
        long missing = 999L;
        when(reviewDataService.getById(missing))
                .thenThrow(new ReviewNotFoundException("Review not found: " + missing));

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.getById(missing));
        verify(reviewDataService).getById(missing);
    }

    @Test
    void getApprovedByPosFiltersCorrectly() throws PosNotFoundException {
        when(posService.getById(pos.getId())).thenReturn(pos);
        when(reviewDataService.getByPos(pos))
                .thenReturn(List.of(approvedReview, unapprovedReview));

        List<Review> approvedOnly = reviewService.getApprovedByPos(pos);

        verify(posService).getById(pos.getId());
        verify(reviewDataService).getByPos(pos);
        assertThat(approvedOnly)
                .singleElement()
                .isEqualTo(approvedReview);
    }

    @Test
    void getApprovedByPosPosNotFoundThrows() throws PosNotFoundException {
        long missingPos = 55L;
        Pos dummy = Pos.builder()
                .id(missingPos)
                .name("dummy")
                .description("irrelevant")
                .type(PosType.CAFE) 
                .build();
        when(posService.getById(missingPos))
                .thenThrow(new PosNotFoundException("Pos not found: " + missingPos));

        assertThrows(PosNotFoundException.class,
                () -> reviewService.getApprovedByPos(dummy));
        verify(posService).getById(missingPos);
        verifyNoMoreInteractions(reviewDataService);
    }

    @Test
    void createSuccessInitializesFields() throws PosNotFoundException, UserNotFoundException {
        when(posService.getById(pos.getId())).thenReturn(pos);
        when(userService.getById(author.getId())).thenReturn(author);

        Review toCreate = Review.builder()
                .posId(pos.getId())
                .authorId(author.getId())
                .build();
        Review stored = Review.builder().id(500L).build();
        when(reviewDataService.create(any(Review.class))).thenReturn(stored);

        Review result = reviewService.create(toCreate);

        verify(posService).getById(pos.getId());
        verify(userService).getById(author.getId());
        verify(reviewDataService).create(toCreate);

        assertEquals(500L, result.getId());
        assertNotNull(toCreate.getCreatedAt(), "createdAt should be set");
        assertFalse(toCreate.getApproved(), "new reviews are not approved");
        assertEquals(0, toCreate.getApprovalCount(), "approvalCount starts at 0");
    }

    @Test
    void createThrowsPosNotFound() throws PosNotFoundException {
        long missingPos = 77L;
        when(posService.getById(missingPos))
                .thenThrow(new PosNotFoundException("Pos not found: " + missingPos));

        Review toCreate = Review.builder()
                .posId(missingPos)
                .authorId(author.getId())
                .build();

        assertThrows(PosNotFoundException.class,
                () -> reviewService.create(toCreate));
        verify(posService).getById(missingPos);
        verifyNoMoreInteractions(userService, reviewDataService);
    }

    @Test
    void createThrowsUserNotFound() throws PosNotFoundException, UserNotFoundException {
        when(posService.getById(pos.getId())).thenReturn(pos);
        long missingUser = 88L;
        when(userService.getById(missingUser))
                .thenThrow(new UserNotFoundException("User not found: " + missingUser));

        Review toCreate = Review.builder()
                .posId(pos.getId())
                .authorId(missingUser)
                .build();

        assertThrows(UserNotFoundException.class,
                () -> reviewService.create(toCreate));
        verify(posService).getById(pos.getId());
        verify(userService).getById(missingUser);
        verifyNoMoreInteractions(reviewDataService);
    }

    @Test
    void approveFirstApprovalOnlyIncrementsCount() throws ReviewNotFoundException, UserNotFoundException {
        Review existing = Review.builder()
                .id(200L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(false)
                .approvalCount(null)
                .build();

        when(reviewDataService.getById(existing.getId())).thenReturn(existing);
        when(userService.getById(approver.getId())).thenReturn(approver);

        Review after = Review.builder().id(200L).build();
        when(reviewDataService.approve(existing)).thenReturn(after);

        Review result = reviewService.approve(
                Review.builder().id(existing.getId()).build(),
                approver);

        verify(reviewDataService).getById(existing.getId());
        verify(userService).getById(approver.getId());
        verify(reviewDataService).approve(existing);

        assertSame(after, result);
        assertEquals(1, existing.getApprovalCount());
        assertFalse(existing.getApproved());
    }

    @Test
    void approveSecondApprovalMarksApproved() throws Exception {
        Review existing = Review.builder()
                .id(201L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(false)
                .approvalCount(1)
                .build();

        when(reviewDataService.getById(existing.getId())).thenReturn(existing);
        when(userService.getById(approver.getId())).thenReturn(approver);
        when(reviewDataService.approve(existing)).thenReturn(existing);

        Review result = reviewService.approve(
                Review.builder().id(existing.getId()).build(),
                approver);

        assertTrue(existing.getApproved(), "≥2 approvals sets approved=true");
        assertEquals(2, existing.getApprovalCount());
        assertSame(existing, result);
    }

    @Test
    void approveThrowsSelfApproval() throws ReviewNotFoundException {
        Review existing = Review.builder()
                .id(300L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(false)
                .approvalCount(0)
                .build();

        when(reviewDataService.getById(existing.getId())).thenReturn(existing);

        assertThrows(IllegalArgumentException.class,
                () -> reviewService.approve(
                        Review.builder().id(existing.getId()).build(),
                        author));
        verify(reviewDataService).getById(existing.getId());
        verify(userService, never()).getById(anyLong());
        verify(reviewDataService, never()).approve(any());
    }

    @Test
    void approveThrowsUserNotFound() throws ReviewNotFoundException, UserNotFoundException {
        Review existing = Review.builder()
                .id(400L)
                .posId(pos.getId())
                .authorId(author.getId())
                .createdAt(LocalDateTime.now())
                .approved(false)
                .approvalCount(0)
                .build();

        when(reviewDataService.getById(existing.getId())).thenReturn(existing);
        long missingUser = 99L;
        when(userService.getById(missingUser))
                .thenThrow(new UserNotFoundException("User not found: " + missingUser));

        assertThrows(UserNotFoundException.class,
                () -> reviewService.approve(
                        Review.builder().id(existing.getId()).build(),
                        User.builder().id(missingUser).build()));
        verify(reviewDataService).getById(existing.getId());
        verify(userService).getById(missingUser);
        verify(reviewDataService, never()).approve(any());
    }

    @Test
    void approveThrowsReviewNotFound() throws ReviewNotFoundException {
        long missingReview = 12345L;
        when(reviewDataService.getById(missingReview))
                .thenThrow(new ReviewNotFoundException("Review not found: " + missingReview));

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.approve(
                        Review.builder().id(missingReview).build(),
                        approver));
        verify(reviewDataService).getById(missingReview);
        verify(userService, never()).getById(anyLong());
    }
}

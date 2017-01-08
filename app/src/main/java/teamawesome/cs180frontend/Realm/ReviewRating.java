package teamawesome.cs180frontend.Realm;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReviewRating extends RealmObject{
    @PrimaryKey
    public int reviewId;
    public boolean val; //did the user upvote or downvote?

    public ReviewRating() {
    }

    public ReviewRating(int reviewId, boolean val) {
        this.reviewId = reviewId;
        this.val = val;
    }
}

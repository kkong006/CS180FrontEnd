package teamawesome.cs180frontend.API.Models.ReviewModel;

import android.content.Context;

import java.util.List;

//For handling a page of reviews.
//Not meant to be serializable since it's purely for EventBus broadcasting
public class ReviewPageBundle {
    //Because overlapping activities that exist listen for this event, a context is needed
    //so that the listener is able to distinguish who the event is for!
    private Context context;
    private List<ReviewBundle> reviews;

    public ReviewPageBundle(Context context, List<ReviewBundle> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ReviewBundle> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewBundle> reviews) {
        this.reviews = reviews;
    }

    /*public void add(int index, ReviewBundle review) {
        this.reviews.add(index, review);
    }*/
}

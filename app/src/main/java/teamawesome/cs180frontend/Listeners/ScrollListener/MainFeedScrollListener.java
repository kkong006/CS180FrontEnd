package teamawesome.cs180frontend.Listeners.ScrollListener;

import android.content.Context;
import android.widget.AbsListView;

import teamawesome.cs180frontend.API.RetrofitSingleton;
import teamawesome.cs180frontend.API.Services.Callbacks.GetReviewsCallback;
import teamawesome.cs180frontend.Adapters.MainFeedAdapter;
import teamawesome.cs180frontend.Misc.Utils;


public class MainFeedScrollListener implements AbsListView.OnScrollListener {
    private int offset = 0;
    private int lastSelected = 0;
    private boolean isLoading = false;

    private Context context;
    private MainFeedAdapter adapter;

    public MainFeedScrollListener(Context context, MainFeedAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        if ((lastVisibleIndex == (totalItemCount - 1))
                && totalItemCount != 0
                && adapter.getItem(lastVisibleIndex) == null) {
            if (!isLoading && lastSelected != lastVisibleIndex) {
                isLoading = true;
                lastSelected = lastVisibleIndex;

                RetrofitSingleton.getInstance()
                        .getMatchingService()
                        .reviews(Utils.getSchoolId(context),
                                null, null, null,
                                null, offset)
                        .enqueue(new GetReviewsCallback(context));
            }
        }
    }
}

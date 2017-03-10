package teamawesome.cs180frontend.Misc.Events;

//used to notify an activity that it should finish
//purpose: to chain kill activities because the standard way to do so has a small moment
//where it blinks white
public class FinishEvent<T> {  //Template to make it generic
    private int cnt;
    private T object;

    public FinishEvent(int cnt, T object) {
        this.cnt = cnt;
        this.object = object;
    }

    public int getCnt() { return cnt; }

    public void setCnt(int cnt) { this.cnt = cnt; }

    public T getObject() { return object; }

    public void setObject(T object) { this.object = object; }
}

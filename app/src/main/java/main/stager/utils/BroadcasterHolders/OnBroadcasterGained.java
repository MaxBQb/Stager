package main.stager.utils.BroadcasterHolders;

@FunctionalInterface
public interface OnBroadcasterGained<B> {
    void addListeners(B broadcaster);
}

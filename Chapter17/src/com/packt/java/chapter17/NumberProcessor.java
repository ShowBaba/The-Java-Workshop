package com.packt.java.chapter17;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Predicate;

public class NumberProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {

    private Flow.Subscription subscription;

    private Predicate<String> predicate = new Predicate<String>() {
        @Override
        public boolean test(String s) {
            try {
                Integer.valueOf(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);

    }

    @Override
    public void onNext(String item) {
        if (predicate.test(item)) {
            submit(item);
        }
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        close();
    }
}

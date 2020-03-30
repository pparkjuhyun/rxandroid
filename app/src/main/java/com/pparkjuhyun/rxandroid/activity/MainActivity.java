package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pparkjuhyun.rxandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_onnext) TextView mTvOnNext;
    @BindView(R.id.tv_onnext2) TextView mTvOnNext2;
    @BindView(R.id.tv_onnext3) TextView mTvOnNext3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 가장 무식한 방법 or 정통적인 방법
        Observer<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                mTvOnNext.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello World!");
                emitter.onComplete();
            }
        }).subscribe(observer);

        // 옵저버 & 에미터를 체이닝해서 합침. 람다표현식으로 ovrride 처리 단순화
        Observable.<String>create(s -> {
            s.onNext("Hello, world! 2");
            s.onComplete();
        }).subscribe(o -> mTvOnNext2.setText(o));

        Observable.just("Hello, world! 3")
                .subscribe(mTvOnNext3::setText);
    }
}

package com.pparkjuhyun.rxandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pparkjuhyun.rxandroid.R;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends RxAppCompatActivity {
    @BindView(R.id.tv_onnext) TextView mTvOnNext;
    @BindView(R.id.tv_onnext2) TextView mTvOnNext2;
    @BindView(R.id.tv_onnext3) TextView mTvOnNext3;

    private Disposable mDisposable;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

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
        })
//                .compose(bindToLifecycle())   RxLifecycle 을 이용해 생명주기와 동기화 시킬 수 있음
                .compose(bindUntilEvent(ActivityEvent.DESTROY)) //DESTROY 가 호출된 경우 메모리 해제하도록 설정
                .subscribe(o -> mTvOnNext2.setText(o));

        Observable.just("Hello, world! 3")
                .subscribe(mTvOnNext3::setText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //구독상태의 Observable 안드로이드의 Context를 복사해 가지고 있음
        //앱이 비정상 종료 될 경우 메모리 누수가 발생할 수 있기 때문에 onDestroy Override를 통해 명시적으로 구독해제를 해주는 것이 좋음
        if(!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}

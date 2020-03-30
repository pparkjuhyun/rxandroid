package com.pparkjuhyun.rxandroid.vo;

import android.graphics.drawable.Drawable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class RecyclerViewItem {
    Drawable image;
    String title;
}

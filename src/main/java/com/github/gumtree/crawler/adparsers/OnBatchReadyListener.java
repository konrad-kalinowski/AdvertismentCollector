package com.github.gumtree.crawler.adparsers;

import com.github.gumtree.crawler.model.Advertisement;

import java.util.List;

public interface OnBatchReadyListener {
    void onBatchReady(List<Advertisement> batch);
}

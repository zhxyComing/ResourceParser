package com.app.dixon.resourceparser.core.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dixon.xu on 2018/3/22.
 */

public abstract class FilterLruDiskCache extends LruDiskCache {

    public FilterLruDiskCache(File cacheDir, File reserveCacheDir, FileNameGenerator fileNameGenerator, long cacheMaxSize) throws IOException {
        super(cacheDir, reserveCacheDir, fileNameGenerator, cacheMaxSize, 0);
    }

    @Override
    public boolean save(String imageUri, Bitmap bitmap) throws IOException {
        if (filter(imageUri)) {
            return false;
        }
        return super.save(imageUri, bitmap);
    }

    @Override
    public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException {
        if (filter(imageUri)) {
            return false;
        }
        return super.save(imageUri, imageStream, listener);
    }


    @Override
    public File get(String imageUri) {
        if (filter(imageUri)) {
            return null;
        }
        return super.get(imageUri);
    }

    public abstract boolean filter(String imageUri);


}

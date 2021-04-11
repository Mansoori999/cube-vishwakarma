package com.vinners.trumanms.core.dowloader;

import java.io.File;

public interface DownloadSuccessListener {
    void onComplete(File file);
}

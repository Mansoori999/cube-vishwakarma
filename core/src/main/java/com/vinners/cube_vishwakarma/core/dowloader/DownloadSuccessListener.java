package com.vinners.cube_vishwakarma.core.dowloader;

import java.io.File;

public interface DownloadSuccessListener {
    void onComplete(File file);
}

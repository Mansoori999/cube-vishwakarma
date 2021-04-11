package com.vinners.cube_vishwakarma.core.dowloader;

import com.vinners.cube_vishwakarma.core.executors.AppExecutors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * Created by Himanshu on 5/1/2018.
 */

public final class FileDownloader {

    private String fileUrl = null;
    private Map<String, String> headers = null;

    /**
     * For differentiating Downloads between multiple downloads
     */
    private String downloadTag;

    private DownloadProgressListener downloadProgressListener;
    private DownloadSuccessListener downloadSuccessListener;
    private DownloadFailureListener downloadFailureListener;

    private AppExecutors appExecutors = AppExecutors.getInstance();
    private File targetFile;
    private long totalBytesWritten = 0;
    private long totalFileLength = 0;
    private long sixPercentOfTotalBytes = 0;

    public FileDownloader download(String fileUrl) {

        if (fileUrl == null)
            throw new IllegalArgumentException("File Url Cannot be Null");

        this.fileUrl = fileUrl;

        return this;
    }

    public FileDownloader addHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public FileDownloader addDownloadTag(String downloadTag) {
        this.downloadTag = downloadTag;
        return this;
    }

    public FileDownloader saveTo(File targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    public FileDownloader addProgressListener(DownloadProgressListener downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
        return this;
    }

    public FileDownloader addSuccessListener(DownloadSuccessListener downloadSuccessListener) {
        this.downloadSuccessListener = downloadSuccessListener;
        return this;
    }

    public FileDownloader addFailureListener(DownloadFailureListener downloadFailureListener) {
        this.downloadFailureListener = downloadFailureListener;
        return this;
    }

    private void downloadFileAndWriteToDisk() throws IOException {

        URL url = new URL(fileUrl);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();

        FileOutputStream fos = new FileOutputStream(targetFile.getPath());
        BufferedOutputStream out = new BufferedOutputStream(fos);

        try {
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[8192];

            int len = 0;
            totalFileLength = c.getContentLength();
            sixPercentOfTotalBytes = (long) (totalFileLength * 0.06);

            long lastByteCountReported = 0;

            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                totalBytesWritten += len;

                if (downloadProgressListener != null) {

                    if (lastByteCountReported == 0L)
                        lastByteCountReported = totalBytesWritten;

                    if (totalBytesWritten == totalFileLength
                            || totalBytesWritten - lastByteCountReported > 100 - 00 - 000) {
                        lastByteCountReported = totalBytesWritten;

                        appExecutors.mainThread().execute(() -> downloadProgressListener.onProgressUpdate((int) ((totalBytesWritten * 100) / totalFileLength)));
                    }
                }
            }

            out.flush();
        } finally {
            fos.getFD().sync();
            out.close();
            c.disconnect();
        }
    }

    public void start() {

        appExecutors.diskIO().execute(() -> {

            try {
                downloadFileAndWriteToDisk();

                if (downloadSuccessListener != null)
                    appExecutors.mainThread().execute(() -> downloadSuccessListener.onComplete(targetFile));
            } catch (IOException e) {

                if (downloadFailureListener != null)
                    appExecutors.mainThread().execute(() -> downloadFailureListener.onError(e));
            }

        });

    }


}

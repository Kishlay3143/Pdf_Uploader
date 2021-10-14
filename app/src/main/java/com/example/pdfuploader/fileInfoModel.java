package com.example.pdfuploader;

public class fileInfoModel {
    String fileName,fileUrl;

    public fileInfoModel() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public fileInfoModel(final String fileName, final String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
}

package com.cscc01.chatbot.backend.model;

public class QueryResult {

    private String filename;
    private String content;

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setFilename(String filename) {
        this.filename = filename;
    }

    public static class Builder {
        private String filename;
        private String content;

        public Builder() {
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public QueryResult build() {
            QueryResult queryResult = new QueryResult();
            queryResult.setContent(this.content);
            queryResult.setFilename(this.filename);
            return queryResult;
        }
    }
}

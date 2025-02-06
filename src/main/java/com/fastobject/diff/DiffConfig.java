package com.fastobject.diff;

import java.util.HashMap;
import java.util.Map;

public class DiffConfig {
    private Map<String, Map<String,FieldConfig>> classFields = new HashMap<>();

    public Map<String, Map<String, FieldConfig>> getClassFields() {
        return classFields;
    }

    static class FieldConfig {
        private String name;
        private String fullName;
        private String dateFormat;
        private boolean ignore;

        private DiffLogKeyConfig diffLogKey;

        public FieldConfig(String name, String fullName,String dateFormat, boolean ignore,DiffLogKeyConfig diffLogKey) {
            this.name = name;
            this.fullName = fullName;
            this.ignore = ignore;
            this.diffLogKey = diffLogKey;
            this.dateFormat =dateFormat;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public DiffLogKeyConfig getDiffLogKey() {
            return diffLogKey;
        }

        public void setDiffLogKey(DiffLogKeyConfig diffLogKey) {
            this.diffLogKey = diffLogKey;
        }

        public String getName() {
            return name;
        }

        public String getFullName() {
            return fullName;
        }

        public boolean isIgnore() {
            return ignore;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("FieldConfig{");
            sb.append("name='").append(name).append('\'');
            sb.append(", fullName='").append(fullName).append('\'');
            sb.append(", ignore=").append(ignore);
            sb.append(", diffLogKey=").append(diffLogKey);
            sb.append('}');
            return sb.toString();
        }
    }

    static class DiffLogKeyConfig{
        private String name;

        public DiffLogKeyConfig() {
        }

        public DiffLogKeyConfig(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("DiffLogKeyConfig{");
            sb.append("name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DiffConfig{");
        sb.append("classFields=").append(classFields);
        sb.append('}');
        return sb.toString();
    }
}

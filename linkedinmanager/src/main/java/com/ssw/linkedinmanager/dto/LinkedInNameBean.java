package com.ssw.linkedinmanager.dto;

import java.io.Serializable;

public class LinkedInNameBean implements Serializable {
    public static final long serialVersionUID = 12345678904354321L;

    private FirstNameBean firstName;
    private LastNameBean lastName;
    private String id;

    public FirstNameBean getFirstName() {
        return firstName;
    }

    public void setFirstName(FirstNameBean firstName) {
        this.firstName = firstName;
    }

    public LastNameBean getLastName() {
        return lastName;
    }

    public void setLastName(LastNameBean lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class FirstNameBean {

        private LocalizedBean localized;

        public LocalizedBean getLocalized() {
            return localized;
        }

        public void setLocalized(LocalizedBean localized) {
            this.localized = localized;
        }

        public static class LocalizedBean {

            private String en_US;

            public String getEn_US() {
                return en_US;
            }

            public void setEn_US(String en_US) {
                this.en_US = en_US;
            }
        }
    }

    public static class LastNameBean {

        private LocalizedBeanX localized;

        public LocalizedBeanX getLocalized() {
            return localized;
        }

        public void setLocalized(LocalizedBeanX localized) {
            this.localized = localized;
        }

        public static class LocalizedBeanX {

            private String en_US;

            public String getEn_US() {
                return en_US;
            }

            public void setEn_US(String en_US) {
                this.en_US = en_US;
            }
        }
    }
}

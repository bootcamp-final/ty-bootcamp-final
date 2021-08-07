package com.github.buyalsky.userservice.dto;

import com.github.buyalsky.userservice.entity.User;
import com.github.buyalsky.userservice.enums.Gender;

import java.util.Date;
import java.util.Objects;

public class UpdateUserDto {
    private final String fullName;
    private final Date birthDay;
    private final String address;
    private final String emailAddress;
    private final Gender gender;

    public UpdateUserDto(String fullName, Date birthDay, String address, String emailAddress, Gender gender) {
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.address = address;
        this.emailAddress = emailAddress;
        this.gender = gender;
    }

    public User mapToUser(User user) {
        if (this.fullName != null)
            user.setFullName(this.fullName);
        if (this.birthDay != null)
            user.setBirthday(this.birthDay);
        if (this.address != null)
            user.setAddress(this.address);
        if (this.emailAddress != null)
            user.setEmailAddress(this.emailAddress);
        if (this.gender != null)
            user.setGender(this.gender);

        return user;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getAddress() {
        return address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserDto that = (UpdateUserDto) o;
        return Objects.equals(fullName, that.fullName) && Objects.equals(birthDay, that.birthDay) && Objects.equals(address, that.address) && Objects.equals(emailAddress, that.emailAddress) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, birthDay, address, emailAddress, gender);
    }

    public static class Builder {
        private String fullName;
        private Date birthDay;
        private String address;
        private String emailAddress;
        private Gender gender;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder birthDay(Date birthDay) {
            this.birthDay = birthDay;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder emailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UpdateUserDto build() {
            return new UpdateUserDto(fullName, birthDay, address, emailAddress, gender);
        }
    }

}

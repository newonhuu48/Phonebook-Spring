package com.example.phonebook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactDTO {

    private long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private boolean isFavorite;
}

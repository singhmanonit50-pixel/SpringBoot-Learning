package com.example.journalApp.entity;


import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexOptions;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "user")

@Data
public class User {


    @Id
    private ObjectId id;
    @Indexed(unique = true)

    private String username;

    private String password;
    private LocalDateTime date;
}

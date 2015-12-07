package com.example

class Message {

    String feedName
    String text
    String createdAt
    String category
   
    static constraints = {
        feedName(size: 1..40, blank: false, unique: true)
        text(size: 0..256, nullable: false)
        createdAt(maxSize: 40, blank: false, nullable: false)
	category(maxSize: 40, blank: false, nullable: false)
    }
}

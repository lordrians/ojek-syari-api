package com.lordrians.ojeksyariapi

import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo
import org.springframework.stereotype.Component

@Component
class DatabaseComponent {

    private val databaseURL = "mongodb+srv://lordrians:mongoDb*()890@cluster0.4iodyi8.mongodb.net/?retryWrites=true&w=majority"
    val database: MongoClient = KMongo.createClient(databaseURL)

}
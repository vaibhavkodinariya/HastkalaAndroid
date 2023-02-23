package com.example.hastkala.model

class ProductDetails(
    var id: Int,
    var name: String,
    var details: String,
    var price: Int,
    var color: String,
    var material: String,
    var composition: String,
    var path: String,
    var images: Array<String>,
    var sizes: Array<ProductSize>
)
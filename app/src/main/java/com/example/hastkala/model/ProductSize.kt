package com.example.hastkala.model

data class ProductSize(var fixedSize: String, var sizeId: Int){
    override fun toString(): String {
        return fixedSize
    }
}

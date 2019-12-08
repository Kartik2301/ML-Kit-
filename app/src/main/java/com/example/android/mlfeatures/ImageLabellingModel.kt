package com.example.android.mlfeatures

class ImageLabellingModel(name: String, probability: Float) {

    var name: String
        internal set
    var probability: Float = 0F
        internal set

    init {
        this.name = name
        this.probability = probability
    }
}
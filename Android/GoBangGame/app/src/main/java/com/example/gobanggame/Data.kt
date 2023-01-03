package com.example.gobanggame

import android.graphics.Point

/**
 * Created by wjc on 2019/12/10.
 */
data class Data(val point: Point) {
    override fun equals(other: Any?): Boolean {
        return if (other is Data) {
            point.x == other.point.x && point.y == other.point.y
        } else {
            false
        }
    }
}

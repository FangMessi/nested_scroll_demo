package com.fang.scroll

/**
 * @author fangkw on 2020/10/23
 **/
object Utils {

    val colors = arrayListOf(
        "#FEAC5E",
        "#F1A07B",
        "#E39397",
        "#D586B4",
        "#C779D0",
        "#A88BCE",
        "#899DCC",
        "#6AAFCA",
        "#5BB8C9",
        "#4BC0C8",
        "#59C1C4",
        "#67C2BF",
        "#83C3B5",
        "#91C4B1",
        "#9FC4AC",
        "#ADC5A7",
        "#BBC5A2"
    )

    fun pickOneColor(index : Int) = colors[index % colors.size]
}
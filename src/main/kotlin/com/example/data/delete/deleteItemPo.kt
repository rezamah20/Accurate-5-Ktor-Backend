package com.example.data.delete

import com.example.data.update.updateAllDataPo
import com.example.data.update.updateItemPO
import com.example.models.*
import com.example.models.Constants.c
import com.example.models.Constants.db_name
import com.example.models.Constants.s
import java.sql.DriverManager
import java.sql.SQLException

fun deleteItemPo(showItempo: showitemPO):String {
    val poDataitemList = mutableListOf<itemPO>()
    try {
        c = DriverManager.getConnection(db_name, Constants.user, Constants.password)
        s = c?.createStatement()
    }catch (e: Exception){

    }

    try {
        val deletequery = "DELETE FROM PODET WHERE POID = ${showItempo.pomodel.POID}"
        c!!.prepareStatement(deletequery).executeUpdate()
       // s!!.executeQuery(deletequery)
    }catch (e: SQLException){
        println("SQL ERROR : $e")
    }

    try {
        updateItemPO(showItempo)
    }catch (e:SQLException){
        println("SQL ERROR 2: $e")
    }
    return "success delete item"
}
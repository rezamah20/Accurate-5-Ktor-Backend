package com.example.models

import java.sql.Connection
import java.sql.Driver
import java.sql.ResultSet
import java.sql.Statement

object Constants {

//      var db_name = "jdbc:firebirdsql:192.168.1.5/3051:D:/Backup Accurate/PT. SIP.GDB"
    var db_name = "jdbc:firebirdsql:localhost/3051:D:/Accurate/PT. xx.GDB"
    var db_name_ci = "jdbc:firebirdsql:localhost/3051:D:/Accurate/CV.xxx.GDB"

    //example : jdbc:firebirdsql:localhost/3051:D:/Accurate/PT. xxx.GDB
    //    var db_name = "jdbc:firebirdsql:localhost/3051:D:/PT. SIP.GDB"
//    const val databaseURL = "jdbc:firebirdsql:localhost/3050:D:/Backup Accurate/011123/ACCURATE/CV.CI.GDB"
    const val user = "sysdba"
    const val password = ""

    var d: Driver? = null
    var c: Connection? = null
    var s: Statement? = null
    var rs: ResultSet? = null
    // var cm:
}
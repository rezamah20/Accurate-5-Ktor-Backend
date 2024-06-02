package com.example.data

import com.example.models.Constants
import com.example.models.Constants.c
import com.example.models.Constants.d
import com.example.models.Constants.db_name
import com.example.models.Constants.db_name_ci
import com.example.models.Constants.password
import com.example.models.Constants.s
import com.example.models.Constants.user
import org.firebirdsql.jdbc.FBDriver
import java.sql.Driver
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


class DatabaseseConnector {
    private var connection = c
    private var statement= s
    fun connnecttoDB(codedb: String){
        try {
            var db_name_code = ""
            if (codedb == "sip"){
                db_name_code = db_name
            }else if (codedb == "ci"){
                db_name_code = db_name_ci
            }
            c = DriverManager.getConnection(db_name_code, user, password)
            s = c?.createStatement()

        }catch (e: Exception){
            println("errror connect $e")
        }
    }

    fun disconnect(){
        try {
            s?.close()
            c?.close()
        }catch (e:Exception){

        }
    }
}

fun testconnnectDB(codedb: String){
    try {
        var db_name_code = ""
        if (codedb == "sip"){
            db_name_code = db_name
        }else if (codedb == "ci"){
            db_name_code = db_name_ci
        }
        c = DriverManager.getConnection(db_name_code, user, password)
        s = c?.createStatement()

    }catch (e: Exception){
        println("errror connect $e")
    }
}

suspend fun connectDB() {
    try {

        val registrationAlternative = 1
        when (registrationAlternative) {
            1 ->         // This is the standard alternative and simply loads the driver class.
                try {
                    Class.forName("org.firebirdsql.jdbc.FBDriver")
                    //  context.res.setBodyText(
                    //      Json.encodeToString(
                    //        ApiResponse.Success(successMessage = "Driver JDBC Found")
                    //    )
                    //)
                } catch (e: ClassNotFoundException) {
                    // context.res.setBodyText(
                    //     Json.encodeToString(
                    //         ApiResponse.Error(errorMessage = "Firebird JCA-JDBC driver not found in class path : $e")
                    //    )
                    // )
                    println("Firebird JCA-JDBC driver not found in class path")
                    println(e.message)
                    return
                }

            2 ->         // There is a bug in some JDK 1.1 implementations, eg. with Microsoft
                try {
                    DriverManager.registerDriver(
                        Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance() as Driver
                    )
                    // context.res.setBodyText(
                    //   Json.encodeToString(
                    //        ApiResponse.Success(successMessage = "Driver Found in class path")
                    //    )
                    // )
                } catch (e: ClassNotFoundException) {
                    // context.res.setBodyText(
                    //      Json.encodeToString(
                    //          ApiResponse.Error(errorMessage = "Driver not found in class path : $e")
                    //       )
                    //  )
                    println("Driver not found in class path")
                    println(e.message)
                    return
                } catch (e: IllegalAccessException) {
                    // context.res.setBodyText(
                    //    Json.encodeToString(
                    //        ApiResponse.Error(errorMessage = "Unable to acces driver Constructor :$e")
                    //    )
                    // )
                    println("Unable to access driver constructor, this shouldn't happen!")
                    println(e.message)
                    return
                } catch (e: InstantiationException) {
                    //context.res.setBodyText(
                    //   Json.encodeToString(
                    //       ApiResponse.Error(errorMessage = "Unable to create an instance of driver class :$e")
                    //   )
                    //)
                    println("Unable to create an instance of driver class, this shouldn't happen!")
                    println(e.message)
                    return
                } catch (e: SQLException) {
                    // context.res.setBodyText(
                    //    Json.encodeToString(
                    //       ApiResponse.Error(errorMessage = "Driver manager failed to register :$e")
                    //   )
                    //)
                    println("Driver manager failed to register driver")
                    showSQLException(e)
                    return
                }

            3 -> {
                val sysProps = System.getProperties()
                val drivers = StringBuffer("org.firebirdsql.jdbc.FBDriver")
                val oldDrivers = sysProps.getProperty("jdbc.drivers")
                if (oldDrivers != null) drivers.append(":$oldDrivers")
                sysProps["jdbc.drivers"] = drivers.toString()
                System.setProperties(sysProps)
            }

            4 ->         // Advanced: This is a non-standard alternative, and is tied to
                d = FBDriver()
        }

        try {
            d = DriverManager.getDriver(db_name)
            // context.res.setBodyText(
            //     Json.encodeToString(
            //       ApiResponse.Success(successMessage =
            //     "Firebird JCA-JDBC driver version " +
            //          d?.getMajorVersion() +
            //          "." +
            //          d?.getMinorVersion() +
            //          " registered with driver manager.")
            //)
            // )
            println(
                "Firebird JCA-JDBC driver version " +
                        d?.getMajorVersion() +
                        "." +
                        d?.getMinorVersion() +
                        " registered with driver manager."
            )
        } catch (e: SQLException) {
            // JsonCode(context, Json.encodeToString(ApiResponse.Error(errorMessage = "Unable to find JCA-JDBC driver among the registred driver: $e")))
            println("Unable to find Firebird JCA-JDBC driver among the registered drivers.")
            showSQLException(e)
            return
        }

        val connectionAlternative = 1
        when (connectionAlternative) {
            1 ->         // This alternative is driver independent;
                try {
                    c = DriverManager.getConnection(db_name, user, password)
                    println("Connection established.")
                    //  JsonCode(context, Json.encodeToString(ApiResponse.Success(successMessage = "Connection established")))
                } catch (e: SQLException) {
                    e.printStackTrace()
                    //  JsonCode(context, Json.encodeToString(ApiResponse.Error(errorMessage = "Unable to Establish a connection through driver manager :$e")))
                    println("Unable to establish a connection through the driver manager.")
                    showSQLException(e)
                    return
                }

            2 ->         // If you're working with a particular driver d, which may or may not be registered,
                try {
                    val connectionProperties = Properties()
                    connectionProperties["user"] = user
                    connectionProperties["password"] = password
                    connectionProperties["lc_ctype"] = "WIN1251"
                    c = d?.connect(db_name, connectionProperties)
                    //   JsonCode(context, Json.encodeToString(ApiResponse.Success("Connection established")))
                    println("Connection established.")
                } catch (e: SQLException) {
                    e.printStackTrace()
                    //  JsonCode(context, Json.encodeToString(ApiResponse.Error("Unable to establish a connecion through driver")))
                    println("Unable to establish a connection through the driver.")
                    showSQLException(e)
                    return
                }
        }

        try {
            c!!.autoCommit = false
            //  JsonCode(context, Json.encodeToString(JsonCode(context, Json.encodeToString(ApiResponse.Success("Auto=comit is disable")))))
            println("Auto-commit is disabled.")
        } catch (e: SQLException) {
            // JsonCode(context, Json.encodeToString(
            //    JsonCode(context,
            //        Json.encodeToString(ApiResponse.Error(errorMessage = "Unable to disable autocommit")))
            //  ))
            println("Unable to disable autocommit.")
            showSQLException(e)
            return
        }

        try {
            val dbMetaData = c?.metaData

            //if (dbMetaData!!.supportsTransactions()) ApiResponse.Success(successMessage = "Transaction are supported") else ApiResponse.Error("Transaction are not supported")
            // println("Transactions are supported."); else println("Transactions are not supported.")

            val tables = dbMetaData!!.getTables(null, null, "%", arrayOf("VIEW"))
            while (tables.next()) {
                //   JsonCode(context, Json.encodeToString(ApiResponse.Success(successMessage = tables.getString("TABLE_NAME") + " is a view.")))
                println(tables.getString("TABLE_NAME") + " is a view.")
            }
            tables.close()
        } catch (e: SQLException) {
            //  JsonCode(context, Json.encodeToString(ApiResponse.Error("Unable to extract database meta data")))
            println("Unable to extract database meta data.")
            showSQLException(e)
        }

        //  try {
        //     rs = s!!.executeQuery("select full_name from employee where salary < 50000")
        //  } catch (e: SQLException) {
        //     println("Unable to submit a static SQL query.")
        //     showSQLException(e)
        //     return
        //  }

    }finally {

    }
}

private fun showSQLException(e: SQLException) {
    var next: SQLException? = e
    while (next != null) {
        println(next.message)
        println("Error Code: " + next.errorCode)
        println("SQL State: " + next.sqlState)
        next = next.nextException
    }
}
package com.example.routes

import com.example.data.delete.deleteItemPo
import com.example.data.insert.insertPO
import com.example.data.insert.insertPR
import com.example.data.insert.insertVendor
import com.example.data.select.*
import com.example.data.update.updateAllDataPo
import com.example.data.update.updateItemPO
import com.example.models.*
import com.example.utils.pdf.createpdf
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.IllegalStateException

fun Route.dataRouting() {
    route("/purchaseorder"){
        get {
            val selectpo = selectPOprepare()
            call.respond(selectpo)
        //    call.respondText(Json.encodeToString(selectpo))
        }
        get("/alldata/{codedb?}") {
            val codedb = call.parameters["codedb"]?.toString() ?: throw IllegalStateException("Database Belum dipilih")
            val showdata = showalldataPO(codedb)
            call.respond(showdata)
           // call.respondText(Json.encodeToString(showdata))
        }
        get("/{codedb?}/search") {
            val codedb = call.parameters["codedb"]?.toString() ?: throw IllegalStateException("Database Belum dipilih")
            val key = call.request.queryParameters["key"]
            val value = call.request.queryParameters["value"]
            val showearch = searchPO(codedb, "$key")
            println("search : $key")
            if (key != null) {
                call.respond(showearch)
            } else {
                call.respondText("Invalid URL format")
            }
        }
        get("/{codedb?}/{poid?}") {
            val codedb = call.parameters["codedb"] ?: throw IllegalStateException("DB Tidak Boleh Kosong")
            val poid = call.parameters["poid"]?.toInt() ?: throw IllegalStateException("Nomor PO ID dibutuhkan")
            val showitempo = showitemPO(codedb,poid)
            call.respond(showitempo)
        }
        get("/pono/{codedb?}/{date?}/{year?}") {
            val codedb = call.parameters["codedb"]?.toString() ?: throw IllegalStateException("DB Tidak Boleh Kosong")
            val date = call.parameters["date"]?.toInt() ?: throw IllegalStateException("Tanggal tidak boleh Kosong")
            val year = call.parameters["year"]?.toInt() ?: throw IllegalStateException("Tahun tidak boleh Kosong")

            val pono = selectnumberpo(bulanAngka = date, tahunAngka = year, codedb)
            call.respond(pono)
        }
        get("/{codedb?}/datatoko") {
            val codedb = call.parameters["codedb"] ?: throw IllegalStateException("DB Tidak Boleh Kosong")
            val datatoko = selectPOdatatoko(codedb)
            call.respond(datatoko)
        }
        get("/{codedb?}/dataitem") {
            val codedb = call.parameters["codedb"] ?: throw IllegalStateException("DB Tidak Boleh Kosong")
            val dataitem = selectPOdataitem(codedb)
            call.respond(dataitem)
        }
        post {
            val po = call.receive<intanPO>()
            val insert = insertPO(po)
            call.respondText(insert, status = HttpStatusCode.Created)
        }
        post("/updateitempo") {
            val poupdate = call.receive<showitemPO>()
            val updatepo = updateItemPO(poupdate)
            call.respondText(updatepo, status = HttpStatusCode.Created)
        }
        post("/updatelldatapo") {
            val poupdate = call.receive<showitemPO>()
            val updatepo = updateAllDataPo(poupdate)
            call.respondText(updatepo, status = HttpStatusCode.Created)
        }
        post("/deletelldatapo") {
            val deleteallitem = call.receive<showitemPO>()
            val updatepo = deleteItemPo(deleteallitem)
            call.respondText(updatepo, status = HttpStatusCode.Created)
        }
        get("/pdf") {
            val file = File("/ktor/src/main/resources/static/032_PO_MKI_XII_2023.pdf")
            call.respondFile(file)
        }
        get("/{codedb?}cpdf={poid?}") {
           // val file = File("/ktor/src/main/resources/static/032_PO_MKI_XII_2023.pdf")
            val codedb = call.parameters["codedb"] ?: throw IllegalStateException("DB Tidak Boleh Kosong")
            val poid = call.parameters["poid"]?.toInt() ?: throw IllegalStateException("Nomor PO ID dibutuhkan")
            val showitempo = showitemPO(codedb,poid)

            showitempo.let {
                createpdf(showitempo)
                val updatepo = createpdf(showitempo)
                val file = File(updatepo)
                call.respondFile(file)
               // call.respondText("Data Berhasil di buat")
            }
        //  call.respondFile(file)
        }
    }
    route("/purchasereq") {
        get("/firstdata"){
          //  val date = call.parameters["date"]?.toString() ?: throw IllegalStateException("PR Bulan dibutuhkan")
          //  val year = call.parameters["year"]?.toString() ?: throw IllegalStateException("PR tahun dibutuhkan")

          //  println("date = $date, year = $year")
            val prno = selectpreparePR()
            call.respond(prno)
        }
    }
    route("/purchasereq") {
        get("/firstdata/prno"){
            val date = call.parameters["date"]?.toString() ?: throw IllegalStateException("PR Bulan dibutuhkan")
            val year = call.parameters["year"]?.toString() ?: throw IllegalStateException("PR tahun dibutuhkan")

            println("date = $date, year = $year")
            val prno = getPRno(bulan = date, tahun = year)
            call.respond(prno)
        }
        get("/allpr") {
            val showprdata = showallPR()
            call.respond(showprdata)
        }
        post("/insertpr") {
            val pr = call.receive<insertPR>()
            println(pr)
            val insert = insertPR(pr)
            call.respondText(insert, status = HttpStatusCode.Created)
        }
    }
    route("/persondata"){
        post("/data") {
            val persdata = call.receive<persondata>()
            println(persdata)
            val insert = insertVendor(persdata)
            call.respondText(insert, status = HttpStatusCode.Created)
        }
    }
}

package com.knoldus.postgres.repository

import org.scalatest.concurrent.ScalaFutures
import com.knoldus.postgres.connection.H2DBComponent
import org.scalatest.FunSuite
import org.scalatest.time.Seconds
import org.scalatest.time.Millis
import org.scalatest.time.Span

class SubjectRepoTest extends FunSuite with SubjectImpl  with H2DBComponent with ScalaFutures{

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Add new subject info") {
    val response =  insert("Data Structures")

    whenReady(response) { subDet =>
      assert(subDet === 3)
    }
  }

  test("Delete subject info") {
    val response= deleteSub(1)

    whenReady(response) { subDet =>
      assert(subDet === 1)
    }

  }

  test("Update subject info"){
    val response= updateSub(1,"operating systems")

    whenReady(response) { subDet =>
      assert(subDet === 1)
    }
  }

  test("get all subjects"){
    val result=getAll
    whenReady(result){res=>
    assert(res===List(Subject("Java",1), Subject("Scala",2)))}
  }
}

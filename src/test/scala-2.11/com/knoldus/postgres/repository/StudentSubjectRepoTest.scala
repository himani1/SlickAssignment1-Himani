package com.knoldus.postgres.repository

import org.scalatest.concurrent.ScalaFutures
import com.knoldus.postgres.connection.H2DBComponent
import org.scalatest.FunSuite
import org.scalatest.time.Seconds
import org.scalatest.time.Millis
import org.scalatest.time.Span

class StudentSubjectRepoTest extends FunSuite with StudentSubjectImpl  with H2DBComponent with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Insert a relation") {
    val res=insert(2,2)

    whenReady(res){response=>
      assert(response===1)
    }
  }


  test("Delete existing relation") {
    val response= deleteSub(2)

    whenReady(response) { subDet =>
      assert(subDet === 1)
    }

  }

  test("get all subjects taken by a student"){
    val result=getAllSubjects(1)

    whenReady(result){ res=>
    assert(res===Vector(("Santosh","Java")))}
  }

  test("invalid student id to get subjects"){
    val result=getAllSubjects(7)

    whenReady(result){ res=>
      assert(res===Vector())}
  }

  test("get all students that have a given subject"){
    val result=getAllStudents(1)

    whenReady(result){ res=>
      assert(res===Vector(("Himani","Java"), ("Santosh","Java")))}
  }

  test("get all students for invalid subject id"){
    val result=getAllStudents(17)

    whenReady(result){ res=>
      assert(res===Vector())}
  }

}

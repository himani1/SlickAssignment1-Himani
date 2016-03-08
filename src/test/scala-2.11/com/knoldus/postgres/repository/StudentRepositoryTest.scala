package com.knoldus.postgres.repository

import org.scalatest.concurrent.ScalaFutures
import com.knoldus.postgres.connection.H2DBComponent
import org.scalatest.FunSuite
import org.scalatest.time.Seconds
import org.scalatest.time.Millis
import org.scalatest.time.Span

class StudentRepositoryTest  extends FunSuite with StudentImpl  with H2DBComponent with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  test("Add new student info") {
    val response =  insert("himani")

    whenReady(response) { studentDet =>
      assert(studentDet === 3)
    }
  }

  test("Delete student info") {
    val response= deleteStud(1)

    whenReady(response) { studentDet =>
      assert(studentDet === 1)
    }

  }

  test("Update student info"){
    val response= updateStud(1,"ram")

    whenReady(response) { studentDet =>
      assert(studentDet === 1)
    }
  }

  test("get all students"){
    val response=getAll
    whenReady(response) { res=>
    assert(res===List(Student("Santosh",1), Student("Himani",2)))
    }
  }

}

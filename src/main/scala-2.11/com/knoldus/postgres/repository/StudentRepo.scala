package com.knoldus.postgres.repository

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.knoldus.postgres.connection.DBComponent


case class Student(sname:String , sid:Int=0)

trait StudentRepo {this: DBComponent =>

  import driver.api._

  class StudentTable(tag:Tag) extends Table[Student](tag,"student"){
    val sname =column[String]("sname",O.SqlType("VARCHAR(100)"))
    val sid = column[Int]("sid",O.PrimaryKey,O.AutoInc)
    def * = (sname,sid) <> (Student.tupled ,Student.unapply)
  }

  val studentTable=TableQuery[StudentTable]

}

trait StudentImpl extends StudentRepo {this: DBComponent =>

  import driver.api._

  def insert(name:String)= db.run { studentTable.returning(studentTable.map(_.sid))+= Student(name) }
  def deleteStud(id: Int): Future[Int] = db.run { studentTable.filter(_.sid === id).delete }
  def updateStud(id:Int,name:String): Future[Int] = db.run { studentTable.filter(_.sid ===id).update(Student(name,id)) }
  def getAll: Future[List[Student]] = { db.run { studentTable.to[List].result}}
}



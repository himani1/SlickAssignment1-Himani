package com.knoldus.postgres.repository

import com.knoldus.postgres.connection.DBComponent
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class Subject(subname:String,subid:Int=0)

trait SubjectRepo {this: DBComponent =>

  import driver.api._

  class SubjectTable(tag:Tag) extends Table[Subject](tag,"subject"){
    val subname =column[String]("subname",O.SqlType("VARCHAR(100)"))
    val subid = column[Int]("subid",O.PrimaryKey,O.AutoInc)
    def * = (subname,subid) <> (Subject.tupled ,Subject.unapply)
  }
  val subjectTable=TableQuery[SubjectTable]
}

trait SubjectImpl extends SubjectRepo{this: DBComponent =>

  import driver.api._

  def insert(name:String)= db.run { subjectTable.returning(subjectTable.map(_.subid))+= Subject(name) }
  def deleteSub(id: Int): Future[Int] = db.run { subjectTable.filter(_.subid === id).delete }
  def updateSub(id:Int,name:String): Future[Int] = db.run { subjectTable.filter(_.subid ===id).update(Subject(name,id)) }
  def getAll: Future[List[Subject]] = { db.run { subjectTable.to[List].result}}
}

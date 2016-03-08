package com.knoldus.postgres.repository

import com.knoldus.postgres.connection.DBComponent
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


case class StudentSub(sid:Int,subid:Int)

trait StudentSubjectRepo extends StudentRepo with SubjectRepo {this: DBComponent =>

  import driver.api._

  class StudentSubTable(tag: Tag) extends Table[StudentSub](tag, "studentsub") {
    val sid = column[Int]("sid")
    val subid = column[Int]("subid")

    def student = foreignKey("student_id_fk", sid, studentTable)(_.sid)

    def subject = foreignKey("subject_id_fk", subid,subjectTable)(_.subid)

    def * = (sid, subid) <>(StudentSub.tupled, StudentSub.unapply)

  }

  val studentsubTable = TableQuery[StudentSubTable]

}

trait StudentSubjectImpl extends StudentSubjectRepo {this: DBComponent =>
  import driver.api._

  def insert(sid:Int,subid:Int):Future[Int]= db.run { studentsubTable += StudentSub(sid,subid) }

  def deleteSub(id: Int): Future[Int] = db.run {studentsubTable.filter(_.subid === id).delete }
  def updateStudSub(sid:Int,subid:Int): Future[Int] = db.run { studentsubTable.filter(_.sid ===sid).update(StudentSub(sid,subid)) }


  def getAllSubjects(sid:Int):Future[Seq[(String,String)]]={

   val monadicInnerJoin = for {
     stud <- studentTable if stud.sid === sid
     studSub <- studentsubTable if stud.sid===studSub.sid
     sub<- subjectTable if studSub.subid===sub.subid
   } yield (stud.sname,sub.subname)
    db.run(monadicInnerJoin.result)

  }


  def getAllStudents(subid:Int):Future[Seq[(String,String)]]={

    val monadicInnerJoin = for {
      sub <- subjectTable if sub.subid === subid
      studSub <- studentsubTable if sub.subid===studSub.subid
      stud<- studentTable if studSub.sid===stud.sid
    } yield (stud.sname,sub.subname)

    db.run(monadicInnerJoin.result)

  }
}



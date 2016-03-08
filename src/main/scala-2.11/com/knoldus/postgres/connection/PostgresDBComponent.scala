package com.knoldus.postgres.connection

import slick.driver.PostgresDriver


trait PostgresDBComponent {

  val driver = PostgresDriver

  import driver.api._

  val db: Database = MyPostgres.connectionPool
}


object MyPostgres {

  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("postgresql")

}

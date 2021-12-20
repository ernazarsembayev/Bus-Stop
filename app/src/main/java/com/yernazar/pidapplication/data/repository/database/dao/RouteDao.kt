package com.yernazar.pidapplication.data.repository.database.dao

import androidx.room.*
import com.yernazar.pidapplication.data.repository.model.RouteAndNextArrive
import org.jguniverse.pidapplicationgm.repo.model.Route

@Dao
interface RouteDao {

    @Query("SELECT * FROM Route WHERE uid LIKE :uid")
    fun getById(uid: String) : Route?

    @Query("SELECT * FROM Route WHERE longName LIKE '%' || :request || '%'")
    fun getByNameLike(request: String) : List<Route>?

//    @Query("SELECT * FROM route WHERE uid LIKE ( " +
//            "SELECT routeId FROM trip WHERE uid LIKE (SELECT tripId FROM Vehicle WHERE nextStopId LIKE :tripId)")
//    fun getByStopId(tripId: String) : List<RouteNextArrive>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(route: Route)

    @Update
    fun update(route: Route)

    @Delete
    fun delete(route: Route)

    @Query("delete from Route")
    fun deleteAllRoutes()



    @Query("select route.uid as uid,  route.longName as longName, route.shortName as shortName, route.type as type, route.url as url, route.isNight as isNight, vehicle.nextStopArrival as nextArrive from route " +
            "inner join trip on route.uid = trip.routeId " +
            "inner join vehicle on trip.uid = vehicle.tripId " +
            "where vehicle.nextStopId = :nextStopId")
    fun getRouteNextArrive(nextStopId: String) : List<RouteAndNextArrive>
}
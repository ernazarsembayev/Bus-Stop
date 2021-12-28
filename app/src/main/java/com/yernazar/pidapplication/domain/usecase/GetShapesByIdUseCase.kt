package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import com.yernazar.pidapplication.data.repository.model.ShapeOld

class GetShapesByIdUseCase(private val appRepository: AppRepository) {

    suspend fun execute(shapeId: String): List<ShapeOld> {

        return appRepository.getShapesById(shapeId)

    }

}
package com.yernazar.pidapplication.domain.usecase

import com.yernazar.pidapplication.domain.repository.AppRepository
import org.jguniverse.pidapplicationgm.repo.model.Shape

class GetShapesByIdUseCase(private val appRepository: AppRepository) {

    suspend fun execute(shapeId: String): List<Shape> {

        return appRepository.getShapesById(shapeId)

    }

}
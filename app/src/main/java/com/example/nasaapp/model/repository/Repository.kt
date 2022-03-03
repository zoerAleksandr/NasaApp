package com.example.nasaapp.model.repository

import com.example.nasaapp.model.PodDTO
import com.example.nasaapp.model.room.PodDAO
import com.example.nasaapp.model.room.PodEntity

class Repository(private val localDataSource: PodDAO) : LocalRepository {

    companion object {
        fun newInstance(localDataSource: PodDAO) = Repository(localDataSource)
    }

    override fun getAllPod(): List<PodDTO> {
        return convertorToListPod(localDataSource.all())
    }

    override fun addPod(podDTO: PodDTO) {
        localDataSource.insert(convertorToEntity(podDTO))
    }

    override fun removePod(podDTO: PodDTO) {
        localDataSource.delete(convertorToEntity(podDTO))
    }

    private fun convertorToListPod(entityList: List<PodEntity>): List<PodDTO> {
        return entityList.map {
            PodDTO(it.date, it.description, null, null, it.title, it.imageURL)
        }
    }

    private fun convertorToEntity(podDTO: PodDTO): PodEntity {
        return PodEntity(0, podDTO.title!!, podDTO.date!!, podDTO.explanation!!, podDTO.url!!)
    }

}
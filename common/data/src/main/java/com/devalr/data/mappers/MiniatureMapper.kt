package com.devalr.data.mappers

import com.devalr.data.database.miniature.MiniatureEntity
import com.devalr.domain.mappers.Mapper
import com.devalr.domain.model.MiniCompletionBo
import com.devalr.domain.model.MiniatureBo

class MiniatureMapper : Mapper<MiniatureEntity, MiniatureBo>() {

    override fun transform(data: MiniatureEntity): MiniatureBo = MiniatureBo(
        id = data.id,
        projectId = data.projectId,
        name = data.name,
        imageUri = data.imageUri,
        percentage = data.completionPercentage,
        completion = MiniCompletionBo(
            isAssembled = data.isAssembled,
            isPrimed = data.isPrimed,
            isBaseColored = data.isBaseColored,
            isDetailed = data.isDetailed,
            baseIsFinished = data.baseIsFinished
        )
    )

    override fun transformReverse(data: MiniatureBo): MiniatureEntity = MiniatureEntity(
        id = data.id,
        projectId = data.projectId,
        name = data.name,
        completionPercentage = data.percentage,
        imageUri = data.imageUri,
        isAssembled = data.completion.isAssembled,
        isPrimed = data.completion.isPrimed,
        isBaseColored = data.completion.isBaseColored,
        isDetailed = data.completion.isDetailed,
        baseIsFinished = data.completion.baseIsFinished
    )
}
package com.teamadn.partyfinder.features.favorites.domain.usecase
import com.teamadn.partyfinder.features.favorites.domain.repository.IFavoriteRepository

class GetFavoritesUseCase(private val repository: IFavoriteRepository) {
    operator fun invoke() = repository.getFavorites()
}